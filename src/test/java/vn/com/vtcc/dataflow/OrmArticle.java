//package vn.com.vtcc.dataflow;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.experimental.Accessors;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import viettel.vtcc.reputa.orm.parser.define.Constants;
//import viettel.vtcc.reputa.orm.parser.utils.jackson.OrmCustomDateDeSerializer;
//import viettel.vtcc.reputa.orm.parser.utils.jackson.OrmCustomDateSerializer;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//@Data
//@Accessors(chain = true)
//@AllArgsConstructor
//@Builder
//@JsonIgnoreProperties(value = {"valid", "serialVersionUID", "organizationId", "spamCheckOrganizations", "matchedOrgs"}, ignoreUnknown = true)
//@Slf4j
//public class OrmArticle implements Serializable {
//
//    private static final long serialVersionUID = -1977392124202755430L;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.ID)
//    private String id;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.URL)
//    @Builder.Default
//    private String url;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.DOMAIN)
//    @Builder.Default
//    private String domain;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.SOURCE_ID)
//    private int sourceId;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.FIRST_CRAWLED_TIME)
//    @JsonSerialize(using = OrmCustomDateSerializer.class)
//    @JsonDeserialize(using = OrmCustomDateDeSerializer.class)
//    @Builder.Default
//    private Date firstCrawledTime = new Date();
//
//    @JsonProperty(Constants.OrmArticle.JsonField.LAST_CRAWLED_TIME)
//    @JsonSerialize(using = OrmCustomDateSerializer.class)
//    @JsonDeserialize(using = OrmCustomDateDeSerializer.class)
//    @Builder.Default
//    private Date lastCrawledTime = new Date();
//
//    @JsonProperty(Constants.OrmArticle.JsonField.PUBLISHED_TIME)
//    @JsonSerialize(using = OrmCustomDateSerializer.class)
//    @JsonDeserialize(using = OrmCustomDateDeSerializer.class)
//    @Builder.Default
//    private Date publishedTime;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.PUBLISHED_TIMESTAMP)
//    private long publishedTimestamp;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.CREATED_TIME)
//    @JsonSerialize(using = OrmCustomDateSerializer.class)
//    @JsonDeserialize(using = OrmCustomDateDeSerializer.class)
//    @Builder.Default
//    private Date createdTime = new Date();
//
//    @JsonProperty(Constants.OrmArticle.JsonField.LAST_UPDATED_TIME)
//    @JsonSerialize(using = OrmCustomDateSerializer.class)
//    @JsonDeserialize(using = OrmCustomDateDeSerializer.class)
//    @Builder.Default
//    private Date lastUpdatedTime = new Date();
//
//    @JsonProperty(Constants.OrmArticle.JsonField.TITLE)
//    private String title;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.SUMMARY)
//    private String summary;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.CONTENT)
//    private String content;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.IMAGE_SOURCES)
//    private List<String> imageSources;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.VIDEO_SOURCES)
//    private List<String> videoSources;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.IS_SIMILAR_MASTER)
//    @Builder.Default
//    private int similarMaster;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.SIMILAR_GROUP_ID)
//    @Builder.Default
//    private String similarGroupId = "";
//
////    @JsonProperty(Constants.OrmArticle.JsonField.SPAM_LEVEL)
////    @Builder.Default
////    private int spamLevel = 0;
////
////    @JsonProperty(Constants.OrmArticle.JsonField.SENTIMENT)
////    @Builder.Default
////    private int sentiment;
////
////    @JsonProperty(Constants.OrmArticle.JsonField.SENTIMENT_SCORE)
////    @Builder.Default
////    private double sentimentScore;
////
////    @JsonProperty(Constants.OrmArticle.JsonField.PAGE_CATEGORY_ID)
////    private int pageCategoryId;
////
////    @JsonProperty(Constants.OrmArticle.JsonField.CATEGORIES)
////    private List<String> categories;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.TIME_TYPE)
//    private int timeType;
//
////    @JsonProperty(Constants.OrmArticle.JsonField.TOPICS)
////    @Builder.Default
////    private List<Integer> topics = new LinkedList<>();
//
//    @JsonProperty(Constants.OrmArticle.JsonField.REMOVED_BY_HOST)
//    private int removedByHost;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.VERSION)
//    @Builder.Default
//    private int version = 1;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.TOTAL_VERSION)
//    @Builder.Default
//    private int totalVersion = 1;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.DIFFERENT_PERCENT)
//    private double differentPercent;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.TIME_LAST_CHANGED)
//    @JsonSerialize(using = OrmCustomDateSerializer.class)
//    @JsonDeserialize(using = OrmCustomDateDeSerializer.class)
//    @Builder.Default
//    private Date timeLastChanged;
//
//    //    @JsonProperty(Constants.OrmArticle.JsonField.CLASSIFICATION_LEVEL)
////    private List<Integer> classificationLevel;
////
//    @JsonProperty(Constants.OrmArticle.JsonField.CLASSIFICATIONS)
//    @Builder.Default
//    private List<Integer> classifications = new ArrayList<>();
//
//    @JsonProperty(Constants.OrmArticle.JsonField.ARTICLE_TYPE)
//    @Builder.Default
//    private String articleType;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.POST_ID)
//    @Builder.Default
//    private String postId;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.COMMENT_ID)
//    private String commentId = null;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.REPLY_ID)
//    private String replyId;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.LIKE_COUNT)
//    @Builder.Default
//    private long likeCount;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.UNLIKE_COUNT)
//    @Builder.Default
//    private long unlikeCount;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.SHARE_COUNT)
//    @Builder.Default
//    private long shareCount;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.COMMENT_COUNT)
//    @Builder.Default
//    private long commentCount;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.REPLY_COUNT)
//    @Builder.Default
//    private long replyCount;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.VIEW_COUNT)
//    @Builder.Default
//    private long viewCount;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.SHARE_CONTENT)
//    private List<String> shareContent;
//
////    @JsonProperty(Constants.OrmArticle.JsonField.SHARED_URL_DOMAINS)
////    private List<String> sharedUrlDomains;
////
////    @JsonProperty(Constants.OrmArticle.JsonField.SHARED_URL_ORM_INDEX)
////    private List<String> sharedUrlOrmIndex;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_ID)
//    private String authorId;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.WALL_ID)
//    private String wallId;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_DISPLAY_NAME)
//    private String authorDisplayName;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_YEAR_OF_BIRTH)
//    @Builder.Default
//    private int authorYearOfBirth;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_GENDER)
//    @Builder.Default
//    private int authorGender;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.WALL_DISPLAY_NAME)
//    private String wallDisplayName;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.LOCATION)
//    private String location;
//
//    @JsonProperty(Constants.OrmArticle.JsonField.VIDEO_FRAMES)
//    private List<VideoFrame> videoFrames;
//
//    //    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_NAME)
////    private String newspaperName;
////    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_INDEX)
////    private List<Long> newspaperIndex;
////    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_PAGE_INDEX)
////    private int newspaperPageIndex;
////    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_PAGE_COUNT)
////    private int newspaperPageCount;
////    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_TITLE)
////    private String newspaperTitle;
////
//    @JsonProperty(Constants.OrmArticle.JsonField.REACH_COUNT)
//    private double reachCount;
//
//    @JsonProperty(value = "tags")
//    @Builder.Default
//    private Collection<Integer> tags = new ArrayList<>();
//
//    @JsonProperty(value = "kpi_tags")
//    private Collection<Integer> kpiTags = new ArrayList<>();
//
//    @JsonProperty(value = "mic_parse")
//    private int micParse;
//
//    public OrmArticle() {
//        Date date = new Date();
//        this.firstCrawledTime = date;
//        this.lastUpdatedTime = date;
//        this.createdTime = date;
//        this.similarMaster = 1;
//        this.version = 1;
//        this.totalVersion = 1;
////        this.classifications = new ArrayList<>();
//        this.tags = new ArrayList<>();
//        this.kpiTags = new ArrayList<>();
//    }
//
//    public boolean hasId() {
//        return StringUtils.isNotEmpty(this.id);
//    }
//
//    public boolean hasUrl() {
//        return this.url != null && StringUtils.isNotEmpty(this.url.trim());
//    }
//
//    public boolean hasDomain() {
//        return this.domain != null && StringUtils.isNotEmpty(this.domain.trim());
//    }
//
//    public boolean hasPublishedTime() {
//        return this.publishedTime != null;
//    }
//
//    public boolean hasContent() {
//        return this.content != null && StringUtils.isNotEmpty(this.content.trim());
//    }
//
//    public boolean isValid() {
//        if (!hasId()) {
//            log.warn("Missing `id` property, url: {}", this.url);
//            return false;
//        }
//        if (!hasUrl()) {
//            log.warn("Missing `url` property");
//            return false;
//        }
//        if (!hasDomain()) {
//            log.warn("Missing `domain` property, url: {}", this.url);
//            return false;
//        }
//        if (!hasPublishedTime()) {
//            log.warn("Missing `publishedTime` property, url: {}", this.url);
//            return false;
//        }
//        if (!hasContent()) {
//            log.warn("Missing `content` property, url: {}", this.url);
//            return false;
//        }
//        return true;
//    }
//
//    public static void main(String[] args) throws JsonProcessingException {
////        ESRepository.getInstance().initializeConnection();
//        OrmArticle article = OrmArticle.builder()
//                .id("123")
//                .url("https://www.facebook.com/123")
//                .domain("facebook.com")
//                .sourceId(2)
//                .publishedTime(new Date())
//                .publishedTimestamp(new Date().getTime())
//                .content("abc test")
//                .build();
//        System.out.println(new ObjectMapper().writeValueAsString(article));
////        ESRepository.getInstance().insertDoc(article, "orm_article_220423", "orm", "123");
//    }
//}