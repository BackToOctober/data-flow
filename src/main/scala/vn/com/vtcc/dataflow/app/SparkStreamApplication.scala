package vn.com.vtcc.dataflow.app

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Time
import vn.com.vtcc.dataflow.flow.SparkStreamFlow
import vn.com.vtcc.dataflow.utils.{FileUtils, JacksonMapper, StopWatch}
import java.nio.file.Paths

import org.apache.spark.streaming.kafka010.OffsetRange
import org.xerial.snappy.Snappy
import vn.com.vtcc.dataflow.storage.elasticSearch.{ESConnectorFactory, ESUtils}
import vn.com.vtcc.dataflow.monitor.LogCountMetricFactory

import scala.collection.immutable

object SparkStreamApplication {

    var appProps: Properties = _
    var sparkStreamFlow: SparkStreamFlowImpl = _

    class SparkStreamFlowImpl extends SparkStreamFlow[Array[Byte], Array[Byte]] with Serializable {
        LogManager.getLogger("kafka").setLevel(Level.WARN)
        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd/HH/")
        val fileTimeFormat = new SimpleDateFormat("mm_ss_")
        var connectorFactory : ESConnectorFactory = _
        var monitorConfig : Properties = _
        var esIndexMonitor : String = _

        def setMonitorConfig(props: Properties): SparkStreamFlowImpl = {
            monitorConfig = props
            this
        }

        def setESIndexMonitor(esIndex: String): SparkStreamFlowImpl = {
            esIndexMonitor = esIndex
            this
        }

        override def prepare(): Unit = {
            connectorFactory = new ESConnectorFactory(monitorConfig)
        }

        override def process(rdd: RDD[ConsumerRecord[Array[Byte], Array[Byte]]], time: Time
                             , offsetRanges: Array[OffsetRange]): Unit = {
            val stopwatch = StopWatch.mark()
            val rootFolder = this.params.get("output.folder").get.toString
            val subPath = dateTimeFormat.format(new Date(time.milliseconds)) + fileTimeFormat.format(new Date(time.milliseconds)) + time.milliseconds
            val path = Paths.get(rootFolder, subPath)
            val rdd2 = rdd.map(r => {
                val byteText = r.value()
                new String(Snappy.uncompress(byteText), "UTF-8")
            })
            rdd2.saveAsTextFile(path.toString)

            // [INFO]: send some metric
            try {
                val timeStamp = System.currentTimeMillis()
                val client = connectorFactory.createConnect()
                val recordsCountAllMetric = LogCountMetricFactory.init().createMetric("stream.rdd.count")
                recordsCountAllMetric
                    .setTimeStamp(timeStamp)
                    .setAndGet(rdd2.count())
                ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(recordsCountAllMetric))

                val delayMetric = LogCountMetricFactory.init().createMetric("stream.delay")
                delayMetric
                    .setTimeStamp(timeStamp)
                    .setAndGet(stopwatch.getDelay)

                ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(delayMetric))

                val partitionCountAllMetric = LogCountMetricFactory
                    .init().createMetric("stream.partition.count.all")
                    .setTimeStamp(timeStamp)
                for (offset <- offsetRanges) {
                    val topic = offset.topic
                    val partition = offset.partition
                    val fromOffset = offset.fromOffset
                    val untilOffset = offset.untilOffset
                    val partitionCountMetric = LogCountMetricFactory
                        .init().createMetric("stream.partition_" + partition + ".count")
                    partitionCountMetric
                        .setTimeStamp(timeStamp)
                        .setAndGet(offset.count())
                    partitionCountAllMetric
                        .increase(offset.count())
                    ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(partitionCountMetric))
                }
                ESUtils.putData(client, esIndexMonitor, JacksonMapper.parseToString(partitionCountAllMetric))
                client.close()
            } catch {
                case e: Exception => {
                    println("--> error")
                    e.printStackTrace()
                }
            }

        }
    }

    def run(configPath: String): Unit = {
        appProps = FileUtils.readPropertiesFile(configPath)
        var kafkaConfig: Map[String, Object] = new immutable.HashMap[String, Object]()
        kafkaConfig = kafkaConfig.+("group.id" -> appProps.getProperty("kafka.group.id"))
            .+("enable.auto.commit" -> appProps.getProperty("kafka.enable.auto.commit", "true"))
            .+("bootstrap.servers" -> appProps.getProperty("kafka.bootstrap.servers"))
            .+("key.deserializer" -> classOf[ByteArrayDeserializer])
            .+("value.deserializer" -> classOf[ByteArrayDeserializer])
            .+("auto.offset.reset" -> appProps.getProperty("kafka.auto.offset.reset", "latest"))
            .+("request.timeout.ms" -> appProps.getProperty("kafka.request.timeout.ms","70000"))
            .+("session.timeout.ms" -> appProps.getProperty("kafka.session.timeout.ms", "60000"))

        //        val runtime = Runtime.getRuntime
        //        runtime.addShutdownHook(new Thread("shutdown") {
        //            override def run(): Unit = {
        //                close()
        //            }
        //        })

        val clientProps = new Properties()
        clientProps.setProperty("elasticsearch.host", appProps.getProperty("elasticsearch.host"))
        clientProps.setProperty("elasticsearch.connection.request.timeout"
            , appProps.getProperty("elasticsearch.connection.request.timeout", "50000"))
        clientProps.setProperty("elasticsearch.connection.timeout"
            , appProps.getProperty("elasticsearch.connection.timeout", "50000"))
        clientProps.setProperty("elasticsearch.socket.timeout"
            , appProps.getProperty("elasticsearch.socket.timeout", "50000"))

        sparkStreamFlow = new SparkStreamFlowImpl()
        sparkStreamFlow
            .setMonitorConfig(clientProps)
            .setESIndexMonitor("stream_crawler_tracking")
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

    //    def close(): Unit = {
    //        if (sparkStreamFlow != null) {
    //            sparkStreamFlow.close()
    //        }
    //    }

    def main(args: Array[String]): Unit = {
        val configPath = args(0)
        run(configPath)
    }
}
