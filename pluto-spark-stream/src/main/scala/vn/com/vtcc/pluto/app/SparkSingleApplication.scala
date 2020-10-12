package vn.com.vtcc.pluto.app

import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import vn.com.vtcc.pluto.core.utils.HdfsUtils

import scala.collection.mutable.ListBuffer

object SparkSingleApplication {

    def run(inputPath: String, outputPath: String): Unit = {
        val spark = SparkSession.builder().getOrCreate()
        SparkBatchApplication.process(spark, inputPath, outputPath)
    }

    def run_23h(path: String): Unit = {
        val fs = HdfsUtils.builder().setCoreSite("core-site.xml").setHdfsSite("hdfs-site.xml").init()
        val files = new ListBuffer[String]()
        val statusList = fs.listStatus(new Path(path))
        for (i <- 0 until statusList.length) {
            files += statusList(i).getPath.toString
        }
        for (file <- files) {
            println(file)
            run(file, file.replace("crawler", "crawler_parsing"))
        }

    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        run_23h(path)
    }
}