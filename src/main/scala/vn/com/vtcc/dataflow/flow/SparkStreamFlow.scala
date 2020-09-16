package vn.com.vtcc.dataflow.flow

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import scala.collection.immutable.HashMap
import scala.collection.mutable.ArrayBuffer


abstract class SparkStreamFlow[K, V] extends Flow {

    var logger : Logger = LogManager.getLogger(SparkStreamFlow.super.getClass)
    var stream : InputDStream[ConsumerRecord[K, V]] = _
    var conf : SparkConf = _
    var ssc : StreamingContext = _
    var duration : Long = 10
    var params : Map[String, Object] = new HashMap[String, Object]()
    var kafkaParams : Map[String, Object] = new HashMap[String, Object]()
    var topics : ArrayBuffer[String] = new ArrayBuffer[String]()
    var autoCommit : java.lang.Boolean = _
    var appName : String = "spark_stream_flow"
    var master : String = "local[*]"

    def setDuration(duration: Long): SparkStreamFlow[K, V] = {
        this.duration = duration
        this
    }

    def setBootstrap(bootstrap: String): SparkStreamFlow[K, V] = {
        kafkaParams += ("bootstrap.servers" -> bootstrap)
        this
    }

    def setTopic(topic: String): SparkStreamFlow[K, V] = {
        this.topics += (topic)
        this
    }

    def setGroupId(groupId: String): SparkStreamFlow[K, V] = {
        kafkaParams += ("group.id" -> groupId)
        this
    }

    def setAutoCommit(value: String): SparkStreamFlow[K, V] = {
        kafkaParams += ("enable.auto.commit" -> value)
        this
    }

    def setKafkaParameter(key: String, value: Object): SparkStreamFlow[K, V] = {
        kafkaParams += (key -> value)
        this
    }

    def setKafkaConfig(kafkaParams: Map[String, Object]): SparkStreamFlow[K, V] = {
        this.kafkaParams = this.kafkaParams ++ kafkaParams
        this
    }

    def setParameter(key: String, value: Object):  SparkStreamFlow[K, V] = {
        this.params += (key -> value)
        this
    }

    def setParameters(params: Map[String, Object]): SparkStreamFlow[K, V] = {
        this.params = this.params ++ params
        this
    }

    def setAppName(appName: String): SparkStreamFlow[K, V] = {
        this.appName = appName
        this
    }

    def setMaster(master: String): SparkStreamFlow[K, V] = {
        this.master = master
        this
    }

    // [INFO]:TODO:prepare some params
    def prepare()

    def initStream(): SparkStreamFlow[K, V] = {
        conf = new SparkConf().setAppName(appName).setMaster(master)
        ssc = new StreamingContext(conf, Seconds(duration))
        println(this.kafkaParams)
        stream = KafkaUtils.createDirectStream[K, V](
            ssc,
            PreferConsistent,
            Subscribe[K, V](this.topics, this.kafkaParams)
        )
        this.prepare()
        if (kafkaParams.getOrElse("enable.auto.commit", "true") == "true") {
            autoCommit = true: java.lang.Boolean
        } else {
            autoCommit = false: java.lang.Boolean
        }
        logger.info("auto.commit = " + autoCommit)
        this
    }

    override def run(): Unit = {
        saveData()
    }

    def saveData(): Unit = {
        stream.foreachRDD((rdd, time) => {
            val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
            val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()
            spark.sparkContext.setLogLevel("ERROR")
            this.process(rdd, time)
            println("auto.commit = " + autoCommit)
            if (autoCommit == false) {
                println("--> auto commit = " )
                for (offset <- offsetRanges) {
                    println(offset)
                }
                stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
            }
            this.offsetsProcess(offsetRanges)
        })
        ssc.start()
        ssc.awaitTermination()
    }

    def process(rdd: RDD[ConsumerRecord[K, V]], time: Time)

    def offsetsProcess(offsetRanges:  Array[OffsetRange])

    def close(): Unit = {
        ssc.stop(true, true)
    }
}
