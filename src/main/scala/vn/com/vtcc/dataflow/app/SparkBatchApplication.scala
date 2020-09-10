package vn.com.vtcc.dataflow.app

import java.nio.file.Paths
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.types.{ArrayType, IntegerType, LongType, StringType, StructField, TimestampType}
import org.apache.spark.sql.{Row, SparkSession, types}
import vn.com.vtcc.dataflow.schema.OrmArticle
import vn.com.vtcc.dataflow.utils.HdfsUtils

object SparkBatchApplication {

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

    def run(): Unit = {
        val fs = HdfsUtils.builder()
                        .setCoreSite("core-site.xml")
                        .setHdfsSite("hdfs-site.xml")
                        .init()
        val rootFolderPath = "/user/linhnv52/crawler"
        val subPath = "/2020/09/10/19/*"
        val path = Paths.get(rootFolderPath, subPath)
        val files = fs.listFiles(new Path(path.toString), false)
        println(files)

        val spark = SparkSession.builder().getOrCreate()
        process(spark, "", "")
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
        val inputPath = args(0)
        val outputPath = args(1)
        run()
    }
}
