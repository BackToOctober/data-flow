package vn.com.vtcc.pluto.app

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date, GregorianCalendar, Properties}

import org.apache.commons.lang3.time.DateUtils
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import vn.com.vtcc.pluto.core.utils.{FileUtils, IDGenerator}
import vn.com.vtcc.pluto.storageScala.MysqlConnectorFactory
import org.apache.spark.sql.functions._

import scala.collection.JavaConversions._

object SparkBatchWriteWarehouseApplication {

    var mysqlProps: Properties = _

    def run(date: Date): Unit = {
        val rootPath = "/user/linhnv52/crawler_parsing"
        val table = "vtcc_dw.fact_crawler_parsing_daily"
        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd")
        val conf = new SparkConf()
            .set("hive.exec.dynamic.partition.mode", "nonstrict")
            .set("spark.sql.sources.partitionOverwriteMode","dynamic")
            .set("hive.exec.dynamic.partition", "true")
            .set("spark.sql.warehouse.dir", "/apps/hive/warehouse")
            .set("spark.shuffle.service.enabled", "true")
            .set("spark.sql.catalogImplementation", "hive")

        val spark = SparkSession.builder()
            .config(conf)
            .getOrCreate()
        LogManager.getLogger("org"). setLevel(Level.ERROR)
        LogManager.getLogger("akka").setLevel(Level.ERROR)

        val inputPath = Paths.get(rootPath, dateTimeFormat.format(date), "*", "*").toString
        println(" >> input path = " + inputPath)

        val partitions = Seq("d_date")
        val df = spark.read.parquet(inputPath)
            .withColumn("d_date", from_unixtime(unix_timestamp(col("published_time")), "yyyyMMdd"))
        df.select("published_time", "d_date").show()
        df.write
            .partitionBy(partitions: _*)
            .mode(SaveMode.Append)
            .saveAsTable(table)
        sendMsg("fact_crawler_parsing_daily", dateTimeFormat.format(date), "success")
    }

    def runListDate(startDate: String, endDate:String): Unit = {
        val dateTimeFormat = new SimpleDateFormat("yyyyMMdd")
        val list = getDaysBetweenDates(dateTimeFormat.parse(startDate), dateTimeFormat.parse(endDate))
        println("list date: " + list)
        for (date <- list) {
            run(date)
        }
    }

    def getDaysBetweenDates(startDate: Date, endDate: Date): util.List[Date] = {
        val dates = new util.ArrayList[Date]
        val calendar = new GregorianCalendar()
        calendar.setTime(startDate)
        while (calendar.getTime.before(endDate)) {
            val result = calendar.getTime
            dates.add(result)
            calendar.add(Calendar.DATE, 1)
        }
        dates
    }

    def initMysqlProps(path: String): Unit = {
        mysqlProps = FileUtils.readPropertiesFile(path)
    }

    def sendMsg(job: String, date: String, msg: String): Unit = {
        val source = new MysqlConnectorFactory(mysqlProps)
        val conn = source.createConnect()
        val table = "spark_job_batch_tracking"
        // execute sql
        val statement = conn.createStatement()
        val id = IDGenerator.genID()
        val simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val dateNow = simpleDateFormat.format(new Date())
        statement.execute(s"insert into $table(id, job, date, state, created_time) values('$id', '$job', '$date', '$msg', STR_TO_DATE('$dateNow', '%Y/%m/%d/ %T')")
        conn.close()
    }

    def main(args: Array[String]): Unit = {
        val startDate = args(0)
        val endDate = args(1)
        val path = args(2)
        initMysqlProps(path)
        runListDate(startDate, endDate)
    }
}
