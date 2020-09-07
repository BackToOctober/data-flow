package vn.com.vtcc.dataflow.flow

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import scala.collection.immutable.HashMap
import scala.collection.mutable.ArrayBuffer


abstract class SparkStreamFlow[K, V] extends Flow{

    var stream : InputDStream[ConsumerRecord[K, V]] = _
    var conf : SparkConf = _
    var ssc : StreamingContext = _
    var duration : Long = 10
    var kafkaParams : Map[String, Object] = new HashMap[String, Object]()
    var topics : ArrayBuffer[String] = _
    var autoCommit : java.lang.Boolean = _

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

    def setAutoCommit(value: java.lang.Boolean): SparkStreamFlow[K, V] = {
        kafkaParams += ("enable.auto.commit" -> value)
        this
    }

    def setParameter(key: String, value: String): SparkStreamFlow[K, V] = {
        kafkaParams += (key -> value)
        this
    }

    def setKafkaConfig(kafkaParams: Map[String, Object]): SparkStreamFlow[K, V] = {
        this.kafkaParams = this.kafkaParams ++ kafkaParams
        this
    }

    def initStream(): SparkStreamFlow[K, V] = {
        conf = new SparkConf()
        ssc = new StreamingContext(conf, Seconds(duration))
        stream = KafkaUtils.createDirectStream[K, V](
            ssc,
            PreferConsistent,
            Subscribe[K, V](this.topics, this.kafkaParams)
        )
        if (kafkaParams.getOrElse("enable.auto.commit", true: java.lang.Boolean) == true: java.lang.Boolean) {
            autoCommit = true: java.lang.Boolean
        } else {
            autoCommit = false: java.lang.Boolean
        }
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
            import spark.implicits._
            this.process(rdd, time)
            if (autoCommit == false) {
                stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
            }
        })
        ssc.start()
        ssc.awaitTermination()
    }

    def process(rdd: RDD[ConsumerRecord[K, V]], time: Time)

    def close(): Unit = {
        ssc.stop(true, true)
    }
}
