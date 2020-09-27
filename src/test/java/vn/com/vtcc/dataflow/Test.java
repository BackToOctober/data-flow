package vn.com.vtcc.dataflow;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void createDataTest() throws IOException {
        String line = "{\"_index\":\"orm_article_200907\",\"_type\":\"orm\",\"_id\":\"2579143403003130764\",\"_score\":1.0,\"_source\":{\"id\":\"2579143403003130764\",\"url\":\"https://www.otofun.net/posts/57081664\",\"domain\":\"otofun.net\",\"source_id\":4,\"first_crawled_time\":\"2020/09/07 00:08:29\",\"last_crawled_time\":\"2020/09/07 00:08:29\",\"published_time\":\"2020/09/07 00:06:00\",\"published_timestamp\":1599411960000,\"created_time\":\"2020/09/07 00:08:29\",\"last_updated_time\":\"2020/09/07 00:08:29\",\"title\":\"centaur271188\",\"summary\":null,\"content\":\"NQHU nói: cạnh tranh gì cụ, cuối tháng 10 năm ngoái được cho bắt chính, nó ôm gôn chính đội này từ đó đến nay rồi! có phải kiểu bắt trận đực trận cái đâu mà kêu cạnh tranh! Đợt đầu năm mới chuyển đến chấn thương, bắt đội U21s nên số trận ít Nhấn vào đây để mở rộng... Không trận đực trận cái thì cụ giải thích con số 15 trận/mùa như thế nào Filip Nguyễn 2 mùa gần đây bắt 26 và 33 trận cho Liberec (chỉ tính giải VĐQG), bắt chính nó là như thế. Bohemians 1905 vs. Teplice - 27 May 2020 - Soccerway Bohemians 1905 vs. Teplice - 27 May 2020 - Soccerway int.soccerway.com Bohemians 1905 vs. České Budějovice - 31 May 2020 - Soccerway Bohemians 1905 vs. České Budějovice - 31 May 2020 - Soccerway int.soccerway.com Opava vs. Bohemians 1905 - 3 June 2020 - Soccerway Opava vs. Bohemians 1905 - 3 June 2020 - Soccerway int.soccerway.com Bohemians 1905 vs. Sparta Praha - 6 June 2020 - Soccerway Bohemians 1905 vs. Sparta Praha - 6 June 2020 - Soccerway int.soccerway.com Slovácko vs. Bohemians 1905 - 10 June 2020 - Soccerway Slovácko vs. Bohemians 1905 - 10 June 2020 - Soccerway int.soccerway.com Bohemians 1905 vs. Jablonec - 14 June 2020 - Soccerway Bohemians 1905 vs. Jablonec - 14 June 2020 - Soccerway int.soccerway.com Đây là 6 trận đầu tiên của Bohemians 1905 sau đợt nghỉ dịch bệnh, Patrik Lê Giang chỉ bắt 2 trận thôi, 3 & 6. Vị trí thủ môn không mất thể lực mấy, ít bị thẻ luôn, nên ít được bắt chỉ có thể là vì anh chưa phải là giỏi nhất thôi\",\"image_sources\":null,\"video_sources\":null,\"similar_master\":1,\"similar_group_id\":\"2579143403003130764\",\"time_type\":3,\"removed_by_host\":0,\"version\":1,\"total_version\":1,\"different_percent\":0.0,\"time_last_changed\":null,\"classifications\":null,\"article_type\":null,\"post_id\":null,\"comment_id\":null,\"reply_id\":null,\"like_count\":0,\"unlike_count\":0,\"share_count\":0,\"comment_count\":0,\"reply_count\":0,\"view_count\":0,\"share_content\":null,\"author_id\":null,\"wall_id\":null,\"author_display_name\":null,\"author_year_of_birth\":0,\"author_gender\":0,\"wall_display_name\":null,\"location\":null,\"video_frames\":null,\"reach_count\":0.0,\"tags\":[],\"kpi_tags\":[1],\"mic_parse\":0}}";
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/text_test.txt")));
        int count = 1;
        while (count < 100001) {
            bw.write(count + "\t" + line + "\n");
            count++;
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
//        createDataTest();

        JSONObject jsonObject = new JSONObject("{\"test1\": {\"a2\":\"asdasd\"}}");
        System.out.println(jsonObject.toMap().get("test1"));
    }
}
