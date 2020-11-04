package vn.com.vtcc.pluto.app

import java.io.IOException
import java.nio.file.Paths
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util
import java.util.{Date, Properties}

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession, types}
import org.apache.spark.sql.types.{StringType, StructField, StructType, TimestampType}
import org.apache.spark.streaming.Time
import org.apache.spark.streaming.kafka010.OffsetRange
import org.elasticsearch.spark.sparkRDDFunctions
import org.json.JSONObject
import org.xerial.snappy.Snappy
import vn.com.vtcc.pluto.core.monitor.LogCountMetricFactory
import vn.com.vtcc.pluto.core.storage.elasticSearch.ESUtils.ESIndexBuilder
import vn.com.vtcc.pluto.core.storage.elasticSearch.{ESConnectorFactory, ESUtils}
import vn.com.vtcc.pluto.core.utils.{FileUtils, JacksonMapper, StopWatch}
import vn.com.vtcc.pluto.schema.HtmlRecord
import vn.com.vtcc.pluto.flow.SparkStreamFlow

import scala.collection.immutable

object SparkStreamHtmlCrawlerApplication extends {

    var appProps: Properties = _
    var sparkStreamFlow: SparkStreamFlowImpl = _

    val schema: StructType = types.StructType(
        Seq(
            StructField(name = "url", dataType = StringType, nullable = true),
            StructField(name = "html", dataType = StringType, nullable = true),
            StructField(name = "content", dataType = StringType, nullable = true),
            StructField(name = "published_time", dataType = TimestampType, nullable = true),
            StructField(name = "created_time", dataType = TimestampType, nullable = true)
        )
    )

    class SparkStreamFlowImpl extends SparkStreamFlow[String, String] {

        LogManager.getLogger("kafka").setLevel(Level.WARN)
        LogManager.getLogger("org"). setLevel(Level.ERROR)
        LogManager.getLogger("akka").setLevel(Level.ERROR)

        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd/HH/")
        val fileTimeFormat = new SimpleDateFormat("mm_ss_")
        var connectorFactory : ESConnectorFactory = _
        var esConfig : Properties = _
        var esIndexMonitor : String = _
        var esIndexData : String = _
        var mappingEsIndex : util.Map[String, Object] = _

        def setESConfig(props: Properties): SparkStreamFlowImpl = {
            esConfig = props
            this
        }

        def setESIndexMonitor(esIndex: String): SparkStreamFlowImpl = {
            esIndexMonitor = esIndex
            this
        }

        def setESIndexData(esIndex: String): SparkStreamFlowImpl = {
            esIndexData = esIndex
            this
        }

        def setMappingEsIndex(map: util.Map[String, Object]): SparkStreamFlowImpl = {
            mappingEsIndex = map
            this
        }

        override def prepare(): Unit = {
            connectorFactory = new ESConnectorFactory(esConfig)
        }

        override def process(rdd: RDD[ConsumerRecord[String, String]], time: Time, offsetRanges: Array[OffsetRange]): Unit = {
            val stopwatch = StopWatch.mark()
            val client = connectorFactory.createConnect()
            val dateNow = new Date()
            val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
            val indexNow = esIndexData + "-" + simpleDateFormat.format(dateNow)

            try {
                if (!ESUtils.existsIndex(client, indexNow)) {
                    ESIndexBuilder.getBuilder
                        .setClient(client)
                        .setIndex(indexNow)
                        .setMapping(mappingEsIndex)
                        .setIndexNumberOfReplicas(2)
                        .setIndexNumberOfShards(2)
                        .create()
                }
                val objectMapper = new ObjectMapper()

                // 1. uncompress
                val rdd2 = rdd.map(r => {
                    val htmlRecord = objectMapper.readValue(r.value(), classOf[HtmlRecord])
                    val html = new String(Snappy.uncompress(htmlRecord.getHtml), "UTF-8")
                    val content = new String(Snappy.uncompress(htmlRecord.getContent), "UTF-8")
                    Map(
                        "url" -> htmlRecord.getUrl,
                        "html" -> html,
                        "content" -> content,
                        "published_time" -> htmlRecord.getPublished_time,
                        "created_time" -> htmlRecord.getCreated_time
                    )
                })

                // 2. push to elastic search
                val esType = "_doc"
                rdd2.saveToEs(s"$indexNow/$esType", Map(
                    "es.nodes" -> esConfig.getProperty("elasticsearch.host"),
                    "es.port" -> "9200"
                ))

                // 3. push to hdfs
                val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
                val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val rdd3 = rdd2.map(r => {
                    Row(
                        r("url"),
                        r("html"),
                        r("content"),
                        new Timestamp(dateFormat.parse(r("published_time")).getTime),
                        new Timestamp(dateFormat.parse(r("created_time")).getTime)
                    )
                })
                val df = spark.createDataFrame(rdd3, schema)
                val rootFolder = params("output.folder").toString
                val pathFormat = new SimpleDateFormat("yyyy/MM/dd/HH/")
                val fileFormat = new SimpleDateFormat("mm_ss_")

                val outputPath = Paths.get(rootFolder, pathFormat.format(dateNow), fileFormat.format(dateNow) + dateNow.getTime).toString
                println(" >> outputPath = " + outputPath)
                df.write.parquet(outputPath)

                // 4. send some metric
                try {
                    val timeStamp = System.currentTimeMillis()
                    val recordsCountAllMetric = LogCountMetricFactory.init().createMetric("crawler.html.stream.rdd.count")
                    recordsCountAllMetric
                        .setTimeStamp(timeStamp)
                        .setAndGet(rdd2.count())
                    ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(recordsCountAllMetric))

                    val delayMetric = LogCountMetricFactory.init().createMetric("crawler.html.stream.delay")
                    delayMetric
                        .setTimeStamp(timeStamp)
                        .setAndGet(stopwatch.getDelay)

                    ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(delayMetric))

                    val partitionCountAllMetric = LogCountMetricFactory
                        .init().createMetric("crawler.html.stream.partition.count.all")
                        .setTimeStamp(timeStamp)
                    for (offset <- offsetRanges) {
                        val topic = offset.topic
                        val partition = offset.partition
                        val fromOffset = offset.fromOffset
                        val untilOffset = offset.untilOffset
                        val partitionCountMetric = LogCountMetricFactory
                            .init().createMetric("crawler.html.stream.stream.partition_" + partition + ".count")
                        partitionCountMetric
                            .setTimeStamp(timeStamp)
                            .setAndGet(offset.count())
                        partitionCountAllMetric
                            .increase(offset.count())
                        ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(partitionCountMetric))
                    }
                    ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(partitionCountAllMetric))
                } catch {
                    case e: Exception => {
                        println("--> error")
                        e.printStackTrace()
                    }
                }
                client.close()
            } catch {
                case e: IOException => {
                    e.printStackTrace()
                }
            }
        }
    }

    def run(configPath: String): Unit = {
        appProps = FileUtils.readPropertiesFile(configPath)

        // 1. kafka config
        var kafkaConfig: Map[String, Object] = new immutable.HashMap[String, Object]()
        kafkaConfig = kafkaConfig.+("group.id" -> appProps.getProperty("kafka.group.id"))
            .+("enable.auto.commit" -> appProps.getProperty("kafka.enable.auto.commit", "true"))
            .+("bootstrap.servers" -> appProps.getProperty("kafka.bootstrap.servers"))
            .+("key.deserializer" -> classOf[StringDeserializer])
            .+("value.deserializer" -> classOf[StringDeserializer])
            .+("auto.offset.reset" -> appProps.getProperty("kafka.auto.offset.reset", "latest"))
            .+("request.timeout.ms" -> appProps.getProperty("kafka.request.timeout.ms","70000"))
            .+("session.timeout.ms" -> appProps.getProperty("kafka.session.timeout.ms", "60000"))

        // 2. elasticsearch config
        val clientProps = new Properties()
        clientProps.setProperty("elasticsearch.host", appProps.getProperty("elasticsearch.host"))
        clientProps.setProperty("elasticsearch.connection.request.timeout"
            , appProps.getProperty("elasticsearch.connection.request.timeout", "50000"))
        clientProps.setProperty("elasticsearch.connection.timeout"
            , appProps.getProperty("elasticsearch.connection.timeout", "50000"))
        clientProps.setProperty("elasticsearch.socket.timeout"
            , appProps.getProperty("elasticsearch.socket.timeout", "50000"))

        // 3. mapping index
        val mappingEsIndexJson = new JSONObject("{\n    \"properties\": {\n        \"url\": {\n            \"type\": \"keyword\"\n        },\n        \"html\": {\n            \"type\": \"text\",\n            \"index\": false\n        },\n        \"content\": {\n            \"type\": \"text\",\n            \"index\": false\n        },\n        \"published_time\": {\n            \"type\": \"date\",\n            \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n        },\n        \"created_time\": {\n            \"type\": \"date\",\n            \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n        }\n    }\n}")
        val mappingEsIndex = mappingEsIndexJson.toMap

        // 4. run
        sparkStreamFlow = new SparkStreamFlowImpl()
        sparkStreamFlow
            .setESConfig(clientProps)
            .setESIndexMonitor("stream_crawler_html_tracking")
            .setESIndexData(appProps.getProperty("es.index.data"))
            .setMappingEsIndex(mappingEsIndex)
        sparkStreamFlow
            .setAppName(appProps.getProperty("app.name"))
            .setMaster(appProps.getProperty("spark.core"))
            .setTopic(appProps.getProperty("kafka.topic"))
            .setParameter("output.folder", appProps.getProperty("output.folder"))
            .setKafkaConfig(kafkaConfig)
            .setDuration(Integer.parseInt(appProps.getProperty("spark.stream.duration")))
            .initStream()
            .run()
    }

    def main(args: Array[String]): Unit = {
        val configPath = args(0)
        run(configPath)
    }
}
