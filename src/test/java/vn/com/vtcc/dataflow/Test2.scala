package vn.com.vtcc.dataflow

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import vn.com.vtcc.dataflow.schema.OrmArticle

object T extends App {
    val mapper = new ObjectMapper() with ScalaObjectMapper
//    val c = mapper.readValue[OrmArticle]("{\"last_updated_time\": \"2020/09/10 22:11:23\"}")
//    println(c.lastUpdatedTime.getClass)
//    val d = mapper.writeValueAsString(c)
//    println(d)


    val test_json = "{\"id\":\"7979369435218015615\",\n\"url\":\"https://facebook.com/1772665042871182\",\n\"domain\":\"facebook.com\",\n\"source_id\":2,\n\"first_crawled_time\":\"2020/09/07 00:01:02\",\n\"last_crawled_time\":\"2020/09/07 00:01:02\",\n\"published_time\":\"2020/09/07 00:01:20\",\n\"published_timestamp\":1599411680000,\n\"created_time\":\"2020/09/07 00:01:32\",\n\"last_updated_time\":\"2020/09/07 00:01:32\",\n\"title\":\"Minh Tình\",\n\"summary\":null,\n\"content\":\"\\uD83D\\uDE22\\uD83D\\uDE22\\uD83D\\uDE22\\uD83D\\uDE22\\nSở thú trên toàn thế giới biện minh cho sự tồn tại của họ thông qua ba mục tiêu: bảo tồn, giáo dục và nghiên cứu. Nhưng đó là những gì sở thú muốn bạn tin tưởng. rằng điều họ đang làm là đúng; thực tế lại hoàn toàn khác. Xem thêm Video định dạng Mutex tại #Mutex #Ci #Hổtrắng #sởthú #bảovệđộngvật\",\n\"image_sources\":[\n],\n\"video_sources\":[\n\"https://scontent.fvte3-1.fna.fbcdn.net/v/t15.5256-10/75229845_106893174083130_1006521797656444928_n.jpg?_nc_cat=1&_nc_sid=f2c4d5&_nc_ohc=IZ6Y24Yv4n0AX9KeaOb&_nc_ht=scontent.fvte3-1.fna&oh=a6feb7af6d2dc9731321e52e0a8ccdd2&oe=5F7B6AFE\"\n],\n\"similar_master\":0,\n\"similar_group_id\":\"423456872516432324\",\n\"time_type\":3,\n\"removed_by_host\":0,\n\"version\":1,\n\"total_version\":1,\n\"different_percent\":0.0,\n\"time_last_changed\":null,\n\"classifications\":null,\n\"article_type\":\"fb_user_post\",\n\"post_id\":\"1772665042871182\",\n\"comment_id\":null,\n\"reply_id\":null,\n\"like_count\":0,\n\"unlike_count\":0,\n\"share_count\":0,\n\"comment_count\":0,\n\"reply_count\":0,\n\"view_count\":0,\n\"share_content\":null,\n\"author_id\":\"100003831095966\",\n\"wall_id\":\"100003831095966\",\n\"author_display_name\":\"Minh Tình\",\n\"author_year_of_birth\":-1,\n\"author_gender\":1,\n\"wall_display_name\":\"Minh Tình\",\n\"location\":\"Bà Rịa-Vũng Tàu\",\n\"video_frames\":null,\n\"reach_count\":0.0,\n\"tags\":[12\n],\n\"kpi_tags\":[\n1\n],\n\"mic_parse\":0}"
    val test_o = mapper.readValue[OrmArticle](test_json)
    println(mapper.writeValueAsString(test_o))
}
