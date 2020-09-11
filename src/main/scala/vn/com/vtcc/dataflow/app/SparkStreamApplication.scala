package vn.com.vtcc.dataflow.app

import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.apache.log4j.{Level, LogManager, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Time
import vn.com.vtcc.dataflow.flow.SparkStreamFlow
import vn.com.vtcc.dataflow.utils.FileUtils
import java.nio.file.Paths

import org.xerial.snappy.{Snappy, SnappyFramedInputStream}

import scala.collection.immutable

object SparkStreamApplication {

    var appProps: Properties = _
    var sparkStreamFlow: SparkStreamFlow[Array[Byte], Array[Byte]] = _

    class SparkStreamFlowImpl extends SparkStreamFlow[Array[Byte], Array[Byte]] with Serializable {
        LogManager.getLogger("kafka").setLevel(Level.WARN)
        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd/HH/mm")

        override def process(rdd: RDD[ConsumerRecord[Array[Byte], Array[Byte]]], time: Time): Unit = {
            val rootFolder = this.params.get("output.folder").get.toString
            val subPath = dateTimeFormat.format(new Date(time.milliseconds))
            val path = Paths.get(rootFolder, subPath)
            val rdd2 = rdd.map(r => {
                val byteText = r.value()
                new String(Snappy.uncompress(byteText), "UTF-8")
            })
            rdd2.saveAsTextFile(path.toString)
        }
    }

    def run(configPath: String): Unit = {
        appProps = FileUtils.readPropertiesFile(configPath)
        var kafkaConfig: Map[String, Object] = new immutable.HashMap[String, Object]()
        kafkaConfig = kafkaConfig.+("group.id" -> appProps.getProperty("kafka.group.id"))
            .+("enable.auto.commit" -> "false")
            .+("bootstrap.servers" -> appProps.getProperty("kafka.bootstrap.servers"))
            .+("key.deserializer" -> classOf[ByteArrayDeserializer])
            .+("value.deserializer" -> classOf[ByteArrayDeserializer])
            .+("auto.offset.reset" -> "earliest")
            .+("request.timeout.ms" -> "70000")
            .+("session.timeout.ms" -> "60000")

        val runtime = Runtime.getRuntime
        runtime.addShutdownHook(new Thread("shutdown") {
            override def run(): Unit = {
                close()
            }
        })

        sparkStreamFlow = new SparkStreamFlowImpl()
            .setAppName(appProps.getProperty("app.name"))
            .setMaster(appProps.getProperty("spark.core"))
            .setTopic(appProps.getProperty("kafka.topic"))
            .setParameter("output.folder", appProps.getProperty("output.folder"))
            .setKafkaConfig(kafkaConfig)
            .setDuration(Integer.parseInt(appProps.getProperty("spark.stream.duration")))
            .initStream()
        sparkStreamFlow.run()
    }

    def close(): Unit = {
        if (sparkStreamFlow != null) {
            sparkStreamFlow.close()
        }
    }

    def main(args: Array[String]): Unit = {
        val configPath = args(0)
        run(configPath)
    }
}
