package vn.com.vtcc.dataflow.app

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.Time
import vn.com.vtcc.dataflow.flow.SparkStreamFlow

object SparkApplication {

    class SparkStreamFlowImpl extends SparkStreamFlow[String, String] {
        override def process(rdd: RDD[ConsumerRecord[String, String]], time: Time): Unit = {
            rdd.map(r => {
                // do something
            }).saveAsTextFile("/path/to/file")

        }
    }

    def main(args: Array[String]): Unit = {
        new SparkStreamFlowImpl().run()
    }
}
