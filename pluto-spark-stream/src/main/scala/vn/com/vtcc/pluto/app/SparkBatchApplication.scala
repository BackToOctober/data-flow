package vn.com.vtcc.pluto.app

import java.io.IOException
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date, Properties}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.PushGateway
import org.apache.commons.lang3.time.DateUtils
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.log4j.{Level, LogManager, Logger}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession, types}
import vn.com.vtcc.pluto.core.monitor.prometheus.CustomGauge
import vn.com.vtcc.pluto.core.utils.{FileUtils, HdfsUtils, StopWatch}
import vn.com.vtcc.pluto.schema.{OrmArticle2, VideoFrame}

import scala.collection.mutable.ListBuffer
import collection.JavaConversions._


/**
 * batch crawler parsing daily job
 */
object SparkBatchApplication {

    val logger: Logger = LogManager.getLogger(SparkBatchApplication.getClass)

    val schema: StructType = types.StructType(
        Seq(
            StructField(name = "id", dataType = StringType, nullable = true),
            StructField(name = "url", dataType = StringType, nullable = true),
            StructField(name = "domain", dataType = StringType, nullable = true),
            StructField(name = "source_id", dataType = IntegerType, nullable = true),
            StructField(name = "first_crawled_time", dataType = TimestampType, nullable = true),
                StructField(name = "last_crawled_time", dataType = TimestampType, nullable = true),
            StructField(name = "published_time", dataType = TimestampType, nullable = true),
                StructField(name = "published_timestamp", dataType = LongType, nullable = true),
            StructField(name = "created_time", dataType = TimestampType, nullable = true),
            StructField(name = "last_updated_time", dataType = TimestampType, nullable = true),
            StructField(name = "title", dataType = StringType, nullable = true),
            StructField(name = "summary", dataType = StringType, nullable = true),
            StructField(name = "content", dataType = StringType, nullable = true),
            StructField(name = "image_sources", dataType = ArrayType(StringType), nullable = true),
            StructField(name = "video_sources", dataType = ArrayType(StringType), nullable = true),
            StructField(name = "similar_master", dataType = IntegerType, nullable = true),
            StructField(name = "similar_group_id", dataType = StringType, nullable = true),
                StructField(name = "time_type", dataType = IntegerType, nullable = true),
                StructField(name = "removed_by_host", dataType = IntegerType, nullable = true),
                StructField(name = "version", dataType = IntegerType, nullable = true),
                StructField(name = "total_version", dataType = IntegerType, nullable = true),
                StructField(name = "different_percent", dataType = DoubleType, nullable = true),
                StructField(name = "time_last_changed", dataType = TimestampType, nullable = true),
                StructField(name = "classifications", dataType = ArrayType(IntegerType), nullable = true),
            StructField(name = "article_type", dataType = StringType, nullable = true),
            StructField(name = "post_id", dataType = StringType, nullable = true),
            StructField(name = "comment_id", dataType = StringType, nullable = true),
            StructField(name = "reply_id", dataType = StringType, nullable = true),
            StructField(name = "like_count", dataType = LongType, nullable = true),
                StructField(name = "unlike_count", dataType = LongType, nullable = true),
            StructField(name = "share_count", dataType = LongType, nullable = true),
            StructField(name = "comment_count", dataType = LongType, nullable = true),
            StructField(name = "reply_count", dataType = LongType, nullable = true),
            StructField(name = "view_count", dataType = LongType, nullable = true),
                StructField(name = "share_content", dataType = ArrayType(StringType), nullable = true),
            StructField(name = "author_id", dataType = StringType, nullable = true),
            StructField(name = "wall_id", dataType = StringType, nullable = true),
            StructField(name = "author_display_name", dataType = StringType, nullable = true),
            StructField(name = "author_year_of_birth", dataType = IntegerType, nullable = true),
            StructField(name = "author_gender", dataType = IntegerType, nullable = true),
            StructField(name = "wall_display_name", dataType = StringType, nullable = true),
            StructField(name = "location", dataType = StringType, nullable = true),
                StructField(name = "video_frames", dataType = ArrayType(StringType), nullable = true),
                StructField(name = "reach_count", dataType = DoubleType, nullable = true),
            StructField(name = "tags", dataType = ArrayType(IntegerType), nullable = true),
                StructField(name = "kpi_tags", dataType = ArrayType(IntegerType), nullable = true),
                StructField(name = "mic_parse", dataType = IntegerType, nullable = true)
        )
    )

    var gatewayProps: Properties = _

    var pathParsingCount: Int = 0
    var pathParsingSuccessCount: Int = 0
    var timeProcessing: Long = 0

    def checkingParsingIsDone(path: String, fs: FileSystem): Boolean = {
        if (HdfsUtils.exists(path, fs) && HdfsUtils.exists(path + "/_SUCCESS", fs)) {
            return true
        }
        fs.delete(new Path(path), true)
        false
    }

    def listFiles(path: String, fs: FileSystem): ListBuffer[String] = {
        val files = new ListBuffer[String]()
        val statusList = fs.listStatus(new Path(path))
        for (i <- 0 until statusList.length) {
            files += statusList(i).getPath.toString
        }
        files
    }

    /**
     * run...
     *  list all file need parse
     *  run each file as a parsing job
     *
     * param is pass from main method
     *
     * @param configPath :  path to gateway prometheus config file
     * @param rootPath   : path to file need parse
     * @param parsingPath: path to file saving data
     */
    def run(configPath: String, rootPath: String, parsingPath: String): Unit = {
        gatewayProps = FileUtils.readPropertiesFile(configPath)

        // 1. check 0-22h
        val fs = HdfsUtils.builder().setCoreSite("core-site.xml").setHdfsSite("hdfs-site.xml").init()
        val dateNow = new Date()
        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd")
        val subPath = dateTimeFormat.format(dateNow)
        val path = Paths.get(rootPath, subPath)
        val hourTimeFormat = new SimpleDateFormat("HH")

        val hourNow = hourTimeFormat.format(dateNow)
        val hourFolderPaths = listFiles(path.toString, fs)
        val checkingHourFolderPaths = new ListBuffer[String]()

        for (hourFolderPath <- hourFolderPaths) {
            val segmentPath = hourFolderPath.split("/")
            if (segmentPath(segmentPath.length - 1).toInt < hourNow.toInt) {
                checkingHourFolderPaths += hourFolderPath
            }
        }

        val minuteFolderPaths = new ListBuffer[String]()

        for (checkingHourFolderPath <- checkingHourFolderPaths) {
            minuteFolderPaths ++= listFiles(checkingHourFolderPath, fs)
        }

        val parsingMinuteFolderPaths = new ListBuffer[String]()
        for (minuteFolderPath <- minuteFolderPaths) {
            val checkingPath = minuteFolderPath.replace(rootPath, parsingPath)
            if (!checkingParsingIsDone(checkingPath, fs)) {
                parsingMinuteFolderPaths += minuteFolderPath
            }
        }

        // 2. check 23h
        if (hourNow.toInt == 0) {
            val yesterday = DateUtils.addDays(dateNow, -1)
            val subPathYesterday = dateTimeFormat.format(yesterday)
            val pathYesterday = Paths.get(rootPath, subPathYesterday, "23")
            val minuteFolderPathsYesterday = listFiles(pathYesterday.toString, fs)
            for (minuteFolderPathYesterday <- minuteFolderPathsYesterday) {
                val checkingPathYesterday = minuteFolderPathYesterday.replace(rootPath, parsingPath)
                if (!checkingParsingIsDone(checkingPathYesterday, fs)) {
                    parsingMinuteFolderPaths += minuteFolderPathYesterday
                }
            }
        }

        // 3. run...
        val spark = SparkSession.builder().getOrCreate()
        LogManager.getLogger("org"). setLevel(Level.ERROR)
        LogManager.getLogger("akka").setLevel(Level.ERROR)

        pathParsingCount = parsingMinuteFolderPaths.size
        val watch = StopWatch.mark()
        for (path <- parsingMinuteFolderPaths) {
            logger.info("run -> " + path)
            process(spark, path, path.replace(rootPath, parsingPath))
            pathParsingSuccessCount += 1
        }
        timeProcessing = watch.getDelay / 1000
        fs.close()
        pushMetrics()
    }

    /**
     * parsing data
     *
     * @param spark: spark session
     * @param inputPath: path to file need parse
     * @param outputPath: path to file saving data
     */
    def process(spark: SparkSession, inputPath: String, outputPath: String): Unit = {
        val rdd = spark.sparkContext.textFile(inputPath)

        val ormMapper = new ObjectMapper() with ScalaObjectMapper with Serializable

        val rdd2 = rdd.map(r => {
            val ormArticle = ormMapper.readValue[OrmArticle2](r)
            val videoFrames = ormArticle.videoFrames
            var videoFramesParse: Array[String] = null
            if (videoFrames != null) {
                videoFramesParse = new Array[String](videoFrames.length)
                var c = 0
                for (videoFrame <- videoFrames) {
                    if (videoFrame != null) {
                        videoFramesParse(c) = ormMapper.writeValueAsString(videoFrame)
                    }
                    c += 1
                }
            }
            Row(
                ormArticle.id,
                ormArticle.url,
                ormArticle.domain,
                ormArticle.sourceId,
                ormArticle.firstCrawledTime,
                    ormArticle.lastCrawledTime,
                ormArticle.publishedTime,
                    ormArticle.publishedTimestamp,
                ormArticle.createdTime,
                ormArticle.lastUpdatedTime,
                ormArticle.title,
                ormArticle.summary,
                ormArticle.content,
                ormArticle.imageSources,
                ormArticle.videoSources,
                ormArticle.similarMaster,
                ormArticle.similarGroupId,
                    ormArticle.timeType,
                    ormArticle.removedByHost,
                    ormArticle.version,
                    ormArticle.totalVersion,
                    ormArticle.differentPercent,
                    ormArticle.timeLastChanged,
                    ormArticle.classifications,
                ormArticle.articleType,
                ormArticle.postId,
                ormArticle.commentId,
                ormArticle.replyId,
                ormArticle.likeCount,
                    ormArticle.unlikeCount,
                ormArticle.shareCount,
                ormArticle.commentCount,
                ormArticle.replyCount,
                ormArticle.viewCount,
                    ormArticle.shareContent,
                ormArticle.authorId,
                ormArticle.wallId,
                ormArticle.authorDisplayName,
                ormArticle.authorYearOfBirth,
                ormArticle.authorGender,
                ormArticle.wallDisplayName,
                ormArticle.location,
                    videoFramesParse,
                    ormArticle.reachCount,
                ormArticle.tags,
                    ormArticle.kpiTags,
                    ormArticle.micParse
            )
        })

        val df = spark.createDataFrame(rdd2, schema)
        df.repartition(1).write.parquet(outputPath)
    }

    /**
     * push metric to push gateway, that expose a api scraped by prometheus
     */
    def pushMetrics(): Unit = {
        val pushGateway = new PushGateway(gatewayProps.getProperty("prometheus.gateway"))
        val registry = new CollectorRegistry
        val pathParsingCountMetric = CustomGauge.build.name("path_parsing_count")
            .help("count number of path need parse parse")
            .register(registry)
        val pathParsingSuccessCountMetric = CustomGauge.build.name("path_parsing_success_count")
            .help("count number of path parsing success")
            .register(registry)
        val timeProcessingMetric = CustomGauge.build.name("time_processing")
            .help("measure processing time")
            .register(registry)
        pathParsingCountMetric.set(pathParsingCount)
        pathParsingSuccessCountMetric.set(pathParsingSuccessCount)
        timeProcessingMetric.set(timeProcessing)
        try pushGateway.pushAdd(registry, "batch_crawler_parsing")
        catch {
            case e: IOException =>
                e.printStackTrace()
        }
    }

    /**
     * configPath: path to gateway prometheus config file
     * rootPath: path to file need parse
     * parsingPath: path to file saving data
     *      ex: spark-submit --master local[*] pluto-spark-stream-1.0.0.jar  push-gateway.properties  /user/linhnv52/crawler /user/linhnv52/crawler_parsing
     *
     * @param args: configPath rootPath parsingPath
     */
    def main(args: Array[String]): Unit = {
        val configPath = args(0)
        val rootPath = args(1)
        val parsingPath = args(2)
        run(configPath, rootPath, parsingPath)
    }
}
