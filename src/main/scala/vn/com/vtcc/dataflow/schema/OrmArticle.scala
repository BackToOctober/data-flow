package vn.com.vtcc.dataflow.schema

import java.io.IOException
import java.sql.Timestamp

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer, JsonSerializer, SerializerProvider}
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import java.text.SimpleDateFormat
import java.util.Date

class OrmCustomDateSerializer extends JsonSerializer[Date] {

    @throws[IOException]
    override def serialize(date: Date, jsonGenerator: JsonGenerator,
                           serializerProvider: SerializerProvider): Unit = {
        val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val formattedDate = dateFormat.format(date)
        jsonGenerator.writeString(formattedDate)
    }
}

class OrmCustomDateDeSerializer extends JsonDeserializer[Date] {

    @throws[IOException]
    override def deserialize(jsonParser: JsonParser,
                             deserializationContext: DeserializationContext): Date = {
        val dateString = jsonParser.getText
        try {
            val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            return dateFormat.parse(dateString)
        } catch {
            case ex: Exception =>
                ex.printStackTrace()
        }
        null
    }
}

class OrmCustomTimestampSerializer extends JsonSerializer[Timestamp] {

    @throws[IOException]
    override def serialize(timestamp: Timestamp, jsonGenerator: JsonGenerator,
                           serializerProvider: SerializerProvider): Unit = {
        val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val formattedDate = dateFormat.format(new Date(timestamp.getTime))
        jsonGenerator.writeString(formattedDate)
    }
}

class OrmCustomTimestampDeSerializer extends JsonDeserializer[Timestamp] {

    @throws[IOException]
    override def deserialize(jsonParser: JsonParser,
                             deserializationContext: DeserializationContext): Timestamp = {
        val dateString = jsonParser.getText
        try {
            val dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            return new Timestamp(dateFormat.parse(dateString).getTime)
        } catch {
            case ex: Exception =>
                ex.printStackTrace()
        }
        null
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OrmArticle() extends Serializable {
    @JsonProperty
    val id: String = null

    @JsonProperty
    val url: String = null

    @JsonProperty
    val domain: String = null

    @JsonProperty("source_id")
    val sourceId: String = null

    @JsonProperty("first_crawl_time")
    @JsonSerialize(using = classOf[OrmCustomTimestampSerializer])
    @JsonDeserialize(using = classOf[OrmCustomTimestampDeSerializer])
    val firstCrawledTime: Timestamp = null

    @JsonProperty("published_time")
    @JsonSerialize(using = classOf[OrmCustomTimestampSerializer])
    @JsonDeserialize(using = classOf[OrmCustomTimestampDeSerializer])
    val publishedTime: Timestamp = null

    @JsonProperty("created_time")
    @JsonSerialize(using = classOf[OrmCustomTimestampSerializer])
    @JsonDeserialize(using = classOf[OrmCustomTimestampDeSerializer])
    val createdTime: Timestamp = null

    @JsonProperty("last_updated_time")
    @JsonSerialize(using = classOf[OrmCustomTimestampSerializer])
    @JsonDeserialize(using = classOf[OrmCustomTimestampDeSerializer])
    val lastUpdatedTime: Timestamp = null

    @JsonProperty
    val title: String = null

    @JsonProperty
    val summary: String = null

    @JsonProperty
    val content: String = null

    @JsonProperty("image_sources")
    val imageSources: Array[String] = null

    @JsonProperty("video_sources")
    val videoSources: Array[String] = null

    @JsonProperty("similar_master")
    val similarMaster: String = null

    @JsonProperty("similar_group_id")
    val similarGroupId: String = null

    @JsonProperty("article_type")
    val articleType: String = null

    @JsonProperty("post_id")
    val postId: String = null

    @JsonProperty("comment_id")
    val commentId: String = null

    @JsonProperty("reply_id")
    val replyId: String = null

    @JsonProperty("like_count")
    val likeCount: java.lang.Long = null

    @JsonProperty("share_count")
    val shareCount: java.lang.Long = null

    @JsonProperty("comment_count")
    val commentCount: java.lang.Long = null

    @JsonProperty("reply_count")
    val replyCount: java.lang.Long = null

    @JsonProperty("view_count")
    val viewCount: java.lang.Long = null

    @JsonProperty("author_id")
    val authorId: String = null

    @JsonProperty("wall_id")
    val wallId: String = null

    @JsonProperty("author_display_name")
    val authorDisplayName: String = null

    @JsonProperty("author_year_of_birth")
    val authorYearOfBirth: Integer = null

    @JsonProperty("author_gender")
    val authorGender: Integer = null

    @JsonProperty("wall_display_name")
    val wallDisplayName: String = null

    @JsonProperty
    val location: String = null

    @JsonProperty
    val tags: Array[Integer] = null

    @JsonProperty("newspaper_name")
    val newspaperName: String = null

    @JsonProperty("newspaper_index")
    val newspaperIndex: String = null

    @JsonProperty("newspaper_page_index")
    val newspaperPageIndex: String = null

    @JsonProperty("newspaper_page_count")
    val newspaperPageCount: Integer = null

    @JsonProperty("newspaper_title")
    val newspaperTitle: String = null

}
