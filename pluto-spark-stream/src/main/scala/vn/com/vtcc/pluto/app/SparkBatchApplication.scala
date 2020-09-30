package vn.com.vtcc.pluto.app

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession, types}
import vn.com.vtcc.pluto.core.utils.HdfsUtils
import vn.com.vtcc.pluto.schema.OrmArticle

import scala.collection.mutable.ListBuffer

object SparkBatchApplication {

    val logger = LogManager.getLogger(SparkBatchApplication.getClass)

    val schema = types.StructType(
        Seq(
            StructField(name = "id", dataType = StringType, nullable = true),
            StructField(name = "url", dataType = StringType, nullable = true),
            StructField(name = "domain", dataType = StringType, nullable = true),
            StructField(name = "source_id", dataType = StringType, nullable = true),
            StructField(name = "first_crawl_time", dataType = TimestampType, nullable = true),
            StructField(name = "published_time", dataType = TimestampType, nullable = true),
            StructField(name = "created_time", dataType = TimestampType, nullable = true),
            StructField(name = "last_updated_time", dataType = TimestampType, nullable = true),
            StructField(name = "title", dataType = StringType, nullable = true),
            StructField(name = "summary", dataType = StringType, nullable = true),
            StructField(name = "content", dataType = StringType, nullable = true),
            StructField(name = "image_sources", dataType = ArrayType(StringType), nullable = true), StructField(name = "video_sources", dataType = ArrayType(StringType), nullable = true),
            StructField(name = "similar_master", dataType = StringType, nullable = true),
            StructField(name = "similar_group_id", dataType = StringType, nullable = true),
            StructField(name = "article_type", dataType = StringType, nullable = true),
            StructField(name = "post_id", dataType = StringType, nullable = true),
            StructField(name = "comment_id", dataType = StringType, nullable = true),
            StructField(name = "reply_id", dataType = StringType, nullable = true),
            StructField(name = "like_count", dataType = LongType, nullable = true),
            StructField(name = "share_count", dataType = LongType, nullable = true),
            StructField(name = "comment_count", dataType = LongType, nullable = true),
            StructField(name = "reply_count", dataType = LongType, nullable = true),
            StructField(name = "view_count", dataType = LongType, nullable = true),
            StructField(name = "author_id", dataType = StringType, nullable = true),
            StructField(name = "wall_id", dataType = StringType, nullable = true),
            StructField(name = "author_display_name", dataType = StringType, nullable = true),
            StructField(name = "author_year_of_birth", dataType = IntegerType, nullable = true),
            StructField(name = "author_gender", dataType = IntegerType, nullable = true),
            StructField(name = "wall_display_name", dataType = StringType, nullable = true),
            StructField(name = "location", dataType = StringType, nullable = true),
            StructField(name = "tags", dataType = ArrayType(IntegerType), nullable = true),
            StructField(name = "newspaper_name", dataType = StringType, nullable = true),
            StructField(name = "newspaper_index", dataType = StringType, nullable = true),
            StructField(name = "newspaper_page_index", dataType = StringType, nullable = true),
            StructField(name = "newspaper_page_count", dataType = IntegerType, nullable = true),
            StructField(name = "newspaper_title", dataType = StringType, nullable = true)
        )
    )

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

    def run(rootPath: String, parsingPath: String): Unit = {

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
            val checkingPath = minuteFolderPath.replace("crawler", "crawler_parsing")
            if (!checkingParsingIsDone(checkingPath, fs)) {
                parsingMinuteFolderPaths += minuteFolderPath
            }
        }

        val spark = SparkSession.builder().getOrCreate()
        LogManager.getLogger("kafka").setLevel(Level.WARN)
        for (path <- parsingMinuteFolderPaths) {
            logger.info("run -> " + path)
            process(spark, path, path.replace(rootPath, parsingPath))
        }
        fs.close()
    }

    def process(spark: SparkSession, inputPath: String, outputPath: String): Unit = {
        val rdd = spark.sparkContext.textFile(inputPath)

        val ormMapper = new ObjectMapper() with ScalaObjectMapper with Serializable

        val rdd2 = rdd.map(r => {
            val ormArticle = ormMapper.readValue[OrmArticle](r)
            println(ormMapper.writeValueAsString(ormArticle))
            Row(
                ormArticle.id,
                ormArticle.url,
                ormArticle.domain,
                ormArticle.sourceId,
                ormArticle.firstCrawledTime,
                ormArticle.publishedTime,
                ormArticle.createdTime,
                ormArticle.lastUpdatedTime,
                ormArticle.title,
                ormArticle.summary,
                ormArticle.content,
                ormArticle.imageSources,
                ormArticle.videoSources,
                ormArticle.similarMaster,
                ormArticle.similarGroupId,
                ormArticle.articleType,
                ormArticle.postId,
                ormArticle.commentId,
                ormArticle.replyId,
                ormArticle.likeCount,
                ormArticle.shareCount,
                ormArticle.commentCount,
                ormArticle.replyCount,
                ormArticle.viewCount,
                ormArticle.authorId,
                ormArticle.wallId,
                ormArticle.authorDisplayName,
                ormArticle.authorYearOfBirth,
                ormArticle.authorGender,
                ormArticle.wallDisplayName,
                ormArticle.location,
                ormArticle.tags,
                ormArticle.newspaperName,
                ormArticle.newspaperIndex,
                ormArticle.newspaperPageIndex,
                ormArticle.newspaperPageCount,
                ormArticle.newspaperTitle
            )
        })

        val df = spark.createDataFrame(rdd2, schema)
        df.repartition(1).write.parquet(outputPath)
    }

    def main(args: Array[String]): Unit = {
        val rootPath = args(0)
        val parsingPath = args(1)
        run(rootPath, parsingPath)
    }
}
