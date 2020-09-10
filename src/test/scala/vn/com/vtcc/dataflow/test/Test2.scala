//package vn.com.vtcc.dataflow.test
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties
//import com.fasterxml.jackson.annotation.JsonProperty
//import com.fasterxml.jackson.core.JsonProcessingException
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize
//import com.fasterxml.jackson.databind.annotation.JsonSerialize
//import lombok.AllArgsConstructor
//import lombok.Builder
//import lombok.Data
//import lombok.experimental.Accessors
//import lombok.extern.slf4j.Slf4j
//import org.apache.commons.lang3.StringUtils
//import java.io.Serializable
//import java.util
//import java.util.{ArrayList, Collection, Date, List}
//
//
//@Data
//@Accessors(chain = true)
//@AllArgsConstructor
//@Builder
//@JsonIgnoreProperties(value = Array(Array("valid", "serialVersionUID", "organizationId", "spamCheckOrganizations", "matchedOrgs")), ignoreUnknown = true)
//@Slf4j
//@SerialVersionUID(-1977392124202755430L)
//object OrmArticle {
//    private def $default$firstCrawledTime:
//
//    =
//    {
//        return new Date
//    }
//
//    private def $default$lastCrawledTime:
//
//    =
//    {
//        return new Date
//    }
//
//    private def $default$createdTime:
//
//    =
//    {
//        return new Date
//    }
//
//    private def $default$lastUpdatedTime:
//
//    =
//    {
//        return new Date
//    }
//
//    private def $default$similarGroupId:
//
//    =
//    {
//        return ""
//    }
//
//    private def $default$version:
//
//    =
//    {
//        return 1
//    }
//
//    private def $default$totalVersion:
//
//    =
//    {
//        return 1
//    }
//
//    private def $default$classifications: = {
//        return new util.ArrayList[AnyRef]
//    }
//
//    private def $default$tags:
//
//    =
//    {
//        return new util.ArrayList[AnyRef]
//    }
//
//    def builder:
//
//    =
//    {
//        return new Nothing
//    }
//    private val log = org.slf4j.LoggerFactory.getLogger(classOf[OrmArticle])
//    class private[dataflow
//    ] () {
//        def id(id: String):
//
//        =
//        {
//            this.id = id
//            return this
//        }
//
//        def url(url: String):
//
//        =
//        {
//            this.url$value = url
//            this.url$set = true
//            return this
//        }
//
//        def domain(domain: String):
//
//        =
//        {
//            this.domain$value = domain
//            this.domain$set = true
//            return this
//        }
//
//        def sourceId(sourceId: Int):
//
//        =
//        {
//            this.sourceId = sourceId
//            return this
//        }
//
//        def firstCrawledTime(firstCrawledTime: Date):
//
//        =
//        {
//            this.firstCrawledTime$value = firstCrawledTime
//            this.firstCrawledTime$set = true
//            return this
//        }
//
//        def lastCrawledTime(lastCrawledTime: Date):
//
//        =
//        {
//            this.lastCrawledTime$value = lastCrawledTime
//            this.lastCrawledTime$set = true
//            return this
//        }
//
//        def publishedTime(publishedTime: Date):
//
//        =
//        {
//            this.publishedTime$value = publishedTime
//            this.publishedTime$set = true
//            return this
//        }
//
//        def publishedTimestamp(publishedTimestamp: Long):
//
//        =
//        {
//            this.publishedTimestamp = publishedTimestamp
//            return this
//        }
//
//        def createdTime(createdTime: Date):
//
//        =
//        {
//            this.createdTime$value = createdTime
//            this.createdTime$set = true
//            return this
//        }
//
//        def lastUpdatedTime(lastUpdatedTime: Date):
//
//        =
//        {
//            this.lastUpdatedTime$value = lastUpdatedTime
//            this.lastUpdatedTime$set = true
//            return this
//        }
//
//        def title(title: String):
//
//        =
//        {
//            this.title = title
//            return this
//        }
//
//        def summary(summary: String):
//
//        =
//        {
//            this.summary = summary
//            return this
//        }
//
//        def content(content: String):
//
//        =
//        {
//            this.content = content
//            return this
//        }
//
//        def imageSources(imageSources: util.List[String]):
//
//        =
//        {
//            this.imageSources = imageSources
//            return this
//        }
//
//        def videoSources(videoSources: util.List[String]):
//
//        =
//        {
//            this.videoSources = videoSources
//            return this
//        }
//
//        def similarMaster(similarMaster: Int):
//
//        =
//        {
//            this.similarMaster$value = similarMaster
//            this.similarMaster$set = true
//            return this
//        }
//
//        def similarGroupId(similarGroupId: String):
//
//        =
//        {
//            this.similarGroupId$value = similarGroupId
//            this.similarGroupId$set = true
//            return this
//        }
//
//        def timeType(timeType: Int):
//
//        =
//        {
//            this.timeType = timeType
//            return this
//        }
//
//        def removedByHost(removedByHost: Int):
//
//        =
//        {
//            this.removedByHost = removedByHost
//            return this
//        }
//
//        def version(version: Int):
//
//        =
//        {
//            this.version$value = version
//            this.version$set = true
//            return this
//        }
//
//        def totalVersion(totalVersion: Int):
//
//        =
//        {
//            this.totalVersion$value = totalVersion
//            this.totalVersion$set = true
//            return this
//        }
//
//        def differentPercent(differentPercent: Double):
//
//        =
//        {
//            this.differentPercent = differentPercent
//            return this
//        }
//
//        def timeLastChanged(timeLastChanged: Date):
//
//        =
//        {
//            this.timeLastChanged$value = timeLastChanged
//            this.timeLastChanged$set = true
//            return this
//        }
//
//        def classifications(classifications: util.List[Integer]):
//
//        =
//        {
//            this.classifications$value = classifications
//            this.classifications$set = true
//            return this
//        }
//
//        def articleType(articleType: String):
//
//        =
//        {
//            this.articleType$value = articleType
//            this.articleType$set = true
//            return this
//        }
//
//        def postId(postId: String):
//
//        =
//        {
//            this.postId$value = postId
//            this.postId$set = true
//            return this
//        }
//
//        def commentId(commentId: String):
//
//        =
//        {
//            this.commentId = commentId
//            return this
//        }
//
//        def replyId(replyId: String):
//
//        =
//        {
//            this.replyId = replyId
//            return this
//        }
//
//        def likeCount(likeCount: Long):
//
//        =
//        {
//            this.likeCount$value = likeCount
//            this.likeCount$set = true
//            return this
//        }
//
//        def unlikeCount(unlikeCount: Long):
//
//        =
//        {
//            this.unlikeCount$value = unlikeCount
//            this.unlikeCount$set = true
//            return this
//        }
//
//        def shareCount(shareCount: Long):
//
//        =
//        {
//            this.shareCount$value = shareCount
//            this.shareCount$set = true
//            return this
//        }
//
//        def commentCount(commentCount: Long):
//
//        =
//        {
//            this.commentCount$value = commentCount
//            this.commentCount$set = true
//            return this
//        }
//
//        def replyCount(replyCount: Long):
//
//        =
//        {
//            this.replyCount$value = replyCount
//            this.replyCount$set = true
//            return this
//        }
//
//        def viewCount(viewCount: Long):
//
//        =
//        {
//            this.viewCount$value = viewCount
//            this.viewCount$set = true
//            return this
//        }
//
//        def shareContent(shareContent: util.List[String]):
//
//        =
//        {
//            this.shareContent = shareContent
//            return this
//        }
//
//        def authorId(authorId: String):
//
//        =
//        {
//            this.authorId = authorId
//            return this
//        }
//
//        def wallId(wallId: String):
//
//        =
//        {
//            this.wallId = wallId
//            return this
//        }
//
//        def authorDisplayName(authorDisplayName: String):
//
//        =
//        {
//            this.authorDisplayName = authorDisplayName
//            return this
//        }
//
//        def authorYearOfBirth(authorYearOfBirth: Int):
//
//        =
//        {
//            this.authorYearOfBirth$value = authorYearOfBirth
//            this.authorYearOfBirth$set = true
//            return this
//        }
//
//        def authorGender(authorGender: Int):
//
//        =
//        {
//            this.authorGender$value = authorGender
//            this.authorGender$set = true
//            return this
//        }
//
//        def wallDisplayName(wallDisplayName: String):
//
//        =
//        {
//            this.wallDisplayName = wallDisplayName
//            return this
//        }
//
//        def location(location: String):
//
//        =
//        {
//            this.location = location
//            return this
//        }
//
//        def videoFrames(videoFrames: util.List[Nothing]):
//
//        =
//        {
//            this.videoFrames = videoFrames
//            return this
//        }
//
//        def reachCount(reachCount: Double):
//
//        =
//        {
//            this.reachCount = reachCount
//            return this
//        }
//
//        def tags(tags: util.Collection[Integer]):
//
//        =
//        {
//            this.tags$value = tags
//            this.tags$set = true
//            return this
//        }
//
//        def kpiTags(kpiTags: util.Collection[Integer]):
//
//        =
//        {
//            this.kpiTags = kpiTags
//            return this
//        }
//
//        def micParse(micParse: Int):
//
//        =
//        {
//            this.micParse = micParse
//            return this
//        }
//
//        def build:
//
//        =
//        {
//            var url$value = this.url$value
//            if (!this.url$set) url$value = OrmArticle.$default$url
//            var domain$value = this.domain$value
//            if (!this.domain$set) domain$value = OrmArticle.$default$domain
//            var firstCrawledTime$value = this.firstCrawledTime$value
//            if (!this.firstCrawledTime$set) firstCrawledTime$value = OrmArticle.$default$firstCrawledTime
//            var lastCrawledTime$value = this.lastCrawledTime$value
//            if (!this.lastCrawledTime$set) lastCrawledTime$value = OrmArticle.$default$lastCrawledTime
//            var publishedTime$value = this.publishedTime$value
//            if (!this.publishedTime$set) publishedTime$value = OrmArticle.$default$publishedTime
//            var createdTime$value = this.createdTime$value
//            if (!this.createdTime$set) createdTime$value = OrmArticle.$default$createdTime
//            var lastUpdatedTime$value = this.lastUpdatedTime$value
//            if (!this.lastUpdatedTime$set) lastUpdatedTime$value = OrmArticle.$default$lastUpdatedTime
//            var similarMaster$value = this.similarMaster$value
//            if (!this.similarMaster$set) similarMaster$value = OrmArticle.$default$similarMaster
//            var similarGroupId$value = this.similarGroupId$value
//            if (!this.similarGroupId$set) similarGroupId$value = OrmArticle.$default$similarGroupId
//            var version$value = this.version$value
//            if (!this.version$set) version$value = OrmArticle.$default$version
//            var totalVersion$value = this.totalVersion$value
//            if (!this.totalVersion$set) totalVersion$value = OrmArticle.$default$totalVersion
//            var timeLastChanged$value = this.timeLastChanged$value
//            if (!this.timeLastChanged$set) timeLastChanged$value = OrmArticle.$default$timeLastChanged
//            var classifications$value = this.classifications$value
//            if (!this.classifications$set) classifications$value = OrmArticle.$default$classifications
//            var articleType$value = this.articleType$value
//            if (!this.articleType$set) articleType$value = OrmArticle.$default$articleType
//            var postId$value = this.postId$value
//            if (!this.postId$set) postId$value = OrmArticle.$default$postId
//            var likeCount$value = this.likeCount$value
//            if (!this.likeCount$set) likeCount$value = OrmArticle.$default$likeCount
//            var unlikeCount$value = this.unlikeCount$value
//            if (!this.unlikeCount$set) unlikeCount$value = OrmArticle.$default$unlikeCount
//            var shareCount$value = this.shareCount$value
//            if (!this.shareCount$set) shareCount$value = OrmArticle.$default$shareCount
//            var commentCount$value = this.commentCount$value
//            if (!this.commentCount$set) commentCount$value = OrmArticle.$default$commentCount
//            var replyCount$value = this.replyCount$value
//            if (!this.replyCount$set) replyCount$value = OrmArticle.$default$replyCount
//            var viewCount$value = this.viewCount$value
//            if (!this.viewCount$set) viewCount$value = OrmArticle.$default$viewCount
//            var authorYearOfBirth$value = this.authorYearOfBirth$value
//            if (!this.authorYearOfBirth$set) authorYearOfBirth$value = OrmArticle.$default$authorYearOfBirth
//            var authorGender$value = this.authorGender$value
//            if (!this.authorGender$set) authorGender$value = OrmArticle.$default$authorGender
//            var tags$value = this.tags$value
//            if (!this.tags$set) tags$value = OrmArticle.$default$tags
//            return new OrmArticle(id, url$value, domain$value, sourceId, firstCrawledTime$value, lastCrawledTime$value, publishedTime$value, publishedTimestamp, createdTime$value, lastUpdatedTime$value, title, summary, content, imageSources, videoSources, similarMaster$value, similarGroupId$value, timeType, removedByHost, version$value, totalVersion$value, differentPercent, timeLastChanged$value, classifications$value, articleType$value, postId$value, commentId, replyId, likeCount$value, unlikeCount$value, shareCount$value, commentCount$value, replyCount$value, viewCount$value, shareContent, authorId, wallId, authorDisplayName, authorYearOfBirth$value, authorGender$value, wallDisplayName, location, videoFrames, reachCount, tags$value, kpiTags, micParse)
//        }
//
//        override def toString:
//
//        =
//        {
//            return "OrmArticle.OrmArticleBuilder(id=" + this.id + ", url$value=" + this.url$value + ", domain$value=" + this.domain$value + ", sourceId=" + this.sourceId + ", firstCrawledTime$value=" + this.firstCrawledTime$value + ", lastCrawledTime$value=" + this.lastCrawledTime$value + ", publishedTime$value=" + this.publishedTime$value + ", publishedTimestamp=" + this.publishedTimestamp + ", createdTime$value=" + this.createdTime$value + ", lastUpdatedTime$value=" + this.lastUpdatedTime$value + ", title=" + this.title + ", summary=" + this.summary + ", content=" + this.content + ", imageSources=" + this.imageSources + ", videoSources=" + this.videoSources + ", similarMaster$value=" + this.similarMaster$value + ", similarGroupId$value=" + this.similarGroupId$value + ", timeType=" + this.timeType + ", removedByHost=" + this.removedByHost + ", version$value=" + this.version$value + ", totalVersion$value=" + this.totalVersion$value + ", differentPercent=" + this.differentPercent + ", timeLastChanged$value=" + this.timeLastChanged$value + ", classifications$value=" + this.classifications$value + ", articleType$value=" + this.articleType$value + ", postId$value=" + this.postId$value + ", commentId=" + this.commentId + ", replyId=" + this.replyId + ", likeCount$value=" + this.likeCount$value + ", unlikeCount$value=" + this.unlikeCount$value + ", shareCount$value=" + this.shareCount$value + ", commentCount$value=" + this.commentCount$value + ", replyCount$value=" + this.replyCount$value + ", viewCount$value=" + this.viewCount$value + ", shareContent=" + this.shareContent + ", authorId=" + this.authorId + ", wallId=" + this.wallId + ", authorDisplayName=" + this.authorDisplayName + ", authorYearOfBirth$value=" + this.authorYearOfBirth$value + ", authorGender$value=" + this.authorGender$value + ", wallDisplayName=" + this.wallDisplayName + ", location=" + this.location + ", videoFrames=" + this.videoFrames + ", reachCount=" + this.reachCount + ", tags$value=" + this.tags$value + ", kpiTags=" + this.kpiTags + ", micParse=" + this.micParse + ")"
//        }
//        private val id = null
//        private val url$value = null
//        private val url$set = false
//        private val domain$value = null
//        private val domain$set = false
//        private val sourceId = 0
//        private val firstCrawledTime$value = null
//        private val firstCrawledTime$set = false
//        private val lastCrawledTime$value = null
//        private val lastCrawledTime$set = false
//        private val publishedTime$value = null
//        private val publishedTime$set = false
//        private val publishedTimestamp = 0L
//        private val createdTime$value = null
//        private val createdTime$set = false
//        private val lastUpdatedTime$value = null
//        private val lastUpdatedTime$set = false
//        private val title = null
//        private val summary = null
//        private val content = null
//        private val imageSources = null
//        private val videoSources = null
//        private val similarMaster$value = 0
//        private val similarMaster$set = false
//        private val similarGroupId$value = null
//        private val similarGroupId$set = false
//        private val timeType = 0
//        private val removedByHost = 0
//        private val version$value = 0
//        private val version$set = false
//        private val totalVersion$value = 0
//        private val totalVersion$set = false
//        private val differentPercent = .0
//        private val timeLastChanged$value = null
//        private val timeLastChanged$set = false
//        private val classifications$value = null
//        private val classifications$set = false
//        private val articleType$value = null
//        private val articleType$set = false
//        private val postId$value = null
//        private val postId$set = false
//        private val commentId = null
//        private val replyId = null
//        private val likeCount$value = 0L
//        private val likeCount$set = false
//        private val unlikeCount$value = 0L
//        private val unlikeCount$set = false
//        private val shareCount$value = 0L
//        private val shareCount$set = false
//        private val commentCount$value = 0L
//        private val commentCount$set = false
//        private val replyCount$value = 0L
//        private val replyCount$set = false
//        private val viewCount$value = 0L
//        private val viewCount$set = false
//        private val shareContent = null
//        private val authorId = null
//        private val wallId = null
//        private val authorDisplayName = null
//        private val authorYearOfBirth$value = 0
//        private val authorYearOfBirth$set = false
//        private val authorGender$value = 0
//        private val authorGender$set = false
//        private val wallDisplayName = null
//        private val location = null
//        private val videoFrames = null
//        private val reachCount = .0
//        private val tags$value = null
//        private val tags$set = false
//        private val kpiTags = null
//        private val micParse = 0
//    }
//
//    //    @JsonProperty(Constants.OrmArticle.JsonField.SPAM_LEVEL)
//    //    @Builder.Default
//    //    private int spamLevel = 0;
//    //
//    //    @JsonProperty(Constants.OrmArticle.JsonField.SENTIMENT)
//    //    private int sentiment;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.SENTIMENT_SCORE)
//    //    private double sentimentScore;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.PAGE_CATEGORY_ID)
//    //    private int pageCategoryId;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.CATEGORIES)
//    //    private List<String> categories;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.TOPICS)
//    //    private List<Integer> topics = new LinkedList<>();
//    //    @JsonProperty(Constants.OrmArticle.JsonField.SHARED_URL_DOMAINS)
//    //    private List<String> sharedUrlDomains;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.SHARED_URL_ORM_INDEX)
//    //    private List<String> sharedUrlOrmIndex;
//    @throws[JsonProcessingException]
//    def main(args: Array[String]): Unit = { //        ESRepository.getInstance().initializeConnection();
//        val article = OrmArticle.builder.id("123").url("https://www.facebook.com/123").domain("facebook.com").sourceId(2).publishedTime(new Date).publishedTimestamp(new Date().getTime).content("abc test").build
//        System.out.println(new ObjectMapper().writeValueAsString(article))
//        //        ESRepository.getInstance().insertDoc(article, "orm_article_220423", "orm", "123");
//    }
//}
//
//@Data
//@Accessors(chain = true)
//@AllArgsConstructor
//@Builder
//@JsonIgnoreProperties(value = Array(Array("valid", "serialVersionUID", "organizationId", "spamCheckOrganizations", "matchedOrgs")), ignoreUnknown = true)
//@Slf4j
//@SerialVersionUID(-1977392124202755430L)
//class OrmArticle() extends Serializable {
//    val date = new Date
//    this.firstCrawledTime = date
//    this.lastUpdatedTime = date
//    this.createdTime = date
//    this.similarMaster = 1
//    this.version = 1
//    this.totalVersion = 1
//    //        this.classifications = new ArrayList<>();
//    this.tags = new util.ArrayList[Integer]
//    this.kpiTags = new util.ArrayList[Integer]
//
//    def this(id: String, url: String, domain: String, sourceId: Int, firstCrawledTime: Date, lastCrawledTime: Date, publishedTime: Date, publishedTimestamp: Long, createdTime: Date, lastUpdatedTime: Date, title: String, summary: String, content: String, imageSources: util.List[String], videoSources: util.List[String], similarMaster: Int, similarGroupId: String, timeType: Int, removedByHost: Int, version: Int, totalVersion: Int, differentPercent: Double, timeLastChanged: Date, classifications: util.List[Integer], articleType: String, postId: String, commentId: String, replyId: String, likeCount: Long, unlikeCount: Long, shareCount: Long, commentCount: Long, replyCount: Long, viewCount: Long, shareContent: util.List[String], authorId: String, wallId: String, authorDisplayName: String, authorYearOfBirth: Int, authorGender: Int, wallDisplayName: String, location: String, videoFrames: util.List[Nothing], reachCount: Double, tags: util.Collection[Integer], kpiTags: util.Collection[Integer], micParse: Int) {
//        this()
//        this.id = id
//        this.url = url
//        this.domain = domain
//        this.sourceId = sourceId
//        this.firstCrawledTime = firstCrawledTime
//        this.lastCrawledTime = lastCrawledTime
//        this.publishedTime = publishedTime
//        this.publishedTimestamp = publishedTimestamp
//        this.createdTime = createdTime
//        this.lastUpdatedTime = lastUpdatedTime
//        this.title = title
//        this.summary = summary
//        this.content = content
//        this.imageSources = imageSources
//        this.videoSources = videoSources
//        this.similarMaster = similarMaster
//        this.similarGroupId = similarGroupId
//        this.timeType = timeType
//        this.removedByHost = removedByHost
//        this.version = version
//        this.totalVersion = totalVersion
//        this.differentPercent = differentPercent
//        this.timeLastChanged = timeLastChanged
//        this.classifications = classifications
//        this.articleType = articleType
//        this.postId = postId
//        this.commentId = commentId
//        this.replyId = replyId
//        this.likeCount = likeCount
//        this.unlikeCount = unlikeCount
//        this.shareCount = shareCount
//        this.commentCount = commentCount
//        this.replyCount = replyCount
//        this.viewCount = viewCount
//        this.shareContent = shareContent
//        this.authorId = authorId
//        this.wallId = wallId
//        this.authorDisplayName = authorDisplayName
//        this.authorYearOfBirth = authorYearOfBirth
//        this.authorGender = authorGender
//        this.wallDisplayName = wallDisplayName
//        this.location = location
//        this.videoFrames = videoFrames
//        this.reachCount = reachCount
//        this.tags = tags
//        this.kpiTags = kpiTags
//        this.micParse = micParse
//    }
//
//    def getId:
//
//    =
//    {
//        return this.id
//    }
//
//    def getUrl:
//
//    =
//    {
//        return this.url
//    }
//
//    def getDomain:
//
//    =
//    {
//        return this.domain
//    }
//
//    def getSourceId:
//
//    =
//    {
//        return this.sourceId
//    }
//
//    def getFirstCrawledTime:
//
//    =
//    {
//        return this.firstCrawledTime
//    }
//
//    def getLastCrawledTime:
//
//    =
//    {
//        return this.lastCrawledTime
//    }
//
//    def getPublishedTime:
//
//    =
//    {
//        return this.publishedTime
//    }
//
//    def getPublishedTimestamp:
//
//    =
//    {
//        return this.publishedTimestamp
//    }
//
//    def getCreatedTime:
//
//    =
//    {
//        return this.createdTime
//    }
//
//    def getLastUpdatedTime:
//
//    =
//    {
//        return this.lastUpdatedTime
//    }
//
//    def getTitle:
//
//    =
//    {
//        return this.title
//    }
//
//    def getSummary:
//
//    =
//    {
//        return this.summary
//    }
//
//    def getContent:
//
//    =
//    {
//        return this.content
//    }
//
//    def getImageSources:
//
//    =
//    {
//        return this.imageSources
//    }
//
//    def getVideoSources:
//
//    =
//    {
//        return this.videoSources
//    }
//
//    def getSimilarMaster:
//
//    =
//    {
//        return this.similarMaster
//    }
//
//    def getSimilarGroupId:
//
//    =
//    {
//        return this.similarGroupId
//    }
//
//    def getTimeType:
//
//    =
//    {
//        return this.timeType
//    }
//
//    def getRemovedByHost:
//
//    =
//    {
//        return this.removedByHost
//    }
//
//    def getVersion:
//
//    =
//    {
//        return this.version
//    }
//
//    def getTotalVersion:
//
//    =
//    {
//        return this.totalVersion
//    }
//
//    def getDifferentPercent:
//
//    =
//    {
//        return this.differentPercent
//    }
//
//    def getTimeLastChanged:
//
//    =
//    {
//        return this.timeLastChanged
//    }
//
//    def getClassifications:
//
//    =
//    {
//        return this.classifications
//    }
//
//    def getArticleType:
//
//    =
//    {
//        return this.articleType
//    }
//
//    def getPostId:
//
//    =
//    {
//        return this.postId
//    }
//
//    def getCommentId:
//
//    =
//    {
//        return this.commentId
//    }
//
//    def getReplyId:
//
//    =
//    {
//        return this.replyId
//    }
//
//    def getLikeCount:
//
//    =
//    {
//        return this.likeCount
//    }
//
//    def getUnlikeCount:
//
//    =
//    {
//        return this.unlikeCount
//    }
//
//    def getShareCount:
//
//    =
//    {
//        return this.shareCount
//    }
//
//    def getCommentCount:
//
//    =
//    {
//        return this.commentCount
//    }
//
//    def getReplyCount:
//
//    =
//    {
//        return this.replyCount
//    }
//
//    def getViewCount:
//
//    =
//    {
//        return this.viewCount
//    }
//
//    def getShareContent:
//
//    =
//    {
//        return this.shareContent
//    }
//
//    def getAuthorId:
//
//    =
//    {
//        return this.authorId
//    }
//
//    def getWallId:
//
//    =
//    {
//        return this.wallId
//    }
//
//    def getAuthorDisplayName:
//
//    =
//    {
//        return this.authorDisplayName
//    }
//
//    def getAuthorYearOfBirth:
//
//    =
//    {
//        return this.authorYearOfBirth
//    }
//
//    def getAuthorGender:
//
//    =
//    {
//        return this.authorGender
//    }
//
//    def getWallDisplayName:
//
//    =
//    {
//        return this.wallDisplayName
//    }
//
//    def getLocation:
//
//    =
//    {
//        return this.location
//    }
//
//    def getVideoFrames:
//
//    =
//    {
//        return this.videoFrames
//    }
//
//    def getReachCount:
//
//    =
//    {
//        return this.reachCount
//    }
//
//    def getTags:
//
//    =
//    {
//        return this.tags
//    }
//
//    def getKpiTags:
//
//    =
//    {
//        return this.kpiTags
//    }
//
//    def getMicParse:
//
//    =
//    {
//        return this.micParse
//    }
//
//    def setId(id: String):
//
//    =
//    {
//        this.id = id
//        return this
//    }
//
//    def setUrl(url: String):
//
//    =
//    {
//        this.url = url
//        return this
//    }
//
//    def setDomain(domain: String):
//
//    =
//    {
//        this.domain = domain
//        return this
//    }
//
//    def setSourceId(sourceId: Int):
//
//    =
//    {
//        this.sourceId = sourceId
//        return this
//    }
//
//    def setFirstCrawledTime(firstCrawledTime: Date):
//
//    =
//    {
//        this.firstCrawledTime = firstCrawledTime
//        return this
//    }
//
//    def setLastCrawledTime(lastCrawledTime: Date):
//
//    =
//    {
//        this.lastCrawledTime = lastCrawledTime
//        return this
//    }
//
//    def setPublishedTime(publishedTime: Date):
//
//    =
//    {
//        this.publishedTime = publishedTime
//        return this
//    }
//
//    def setPublishedTimestamp(publishedTimestamp: Long):
//
//    =
//    {
//        this.publishedTimestamp = publishedTimestamp
//        return this
//    }
//
//    def setCreatedTime(createdTime: Date):
//
//    =
//    {
//        this.createdTime = createdTime
//        return this
//    }
//
//    def setLastUpdatedTime(lastUpdatedTime: Date):
//
//    =
//    {
//        this.lastUpdatedTime = lastUpdatedTime
//        return this
//    }
//
//    def setTitle(title: String):
//
//    =
//    {
//        this.title = title
//        return this
//    }
//
//    def setSummary(summary: String):
//
//    =
//    {
//        this.summary = summary
//        return this
//    }
//
//    def setContent(content: String):
//
//    =
//    {
//        this.content = content
//        return this
//    }
//
//    def setImageSources(imageSources: util.List[String]):
//
//    =
//    {
//        this.imageSources = imageSources
//        return this
//    }
//
//    def setVideoSources(videoSources: util.List[String]):
//
//    =
//    {
//        this.videoSources = videoSources
//        return this
//    }
//
//    def setSimilarMaster(similarMaster: Int):
//
//    =
//    {
//        this.similarMaster = similarMaster
//        return this
//    }
//
//    def setSimilarGroupId(similarGroupId: String):
//
//    =
//    {
//        this.similarGroupId = similarGroupId
//        return this
//    }
//
//    def setTimeType(timeType: Int):
//
//    =
//    {
//        this.timeType = timeType
//        return this
//    }
//
//    def setRemovedByHost(removedByHost: Int):
//
//    =
//    {
//        this.removedByHost = removedByHost
//        return this
//    }
//
//    def setVersion(version: Int):
//
//    =
//    {
//        this.version = version
//        return this
//    }
//
//    def setTotalVersion(totalVersion: Int):
//
//    =
//    {
//        this.totalVersion = totalVersion
//        return this
//    }
//
//    def setDifferentPercent(differentPercent: Double):
//
//    =
//    {
//        this.differentPercent = differentPercent
//        return this
//    }
//
//    def setTimeLastChanged(timeLastChanged: Date):
//
//    =
//    {
//        this.timeLastChanged = timeLastChanged
//        return this
//    }
//
//    def setClassifications(classifications: util.List[Integer]):
//
//    =
//    {
//        this.classifications = classifications
//        return this
//    }
//
//    def setArticleType(articleType: String):
//
//    =
//    {
//        this.articleType = articleType
//        return this
//    }
//
//    def setPostId(postId: String):
//
//    =
//    {
//        this.postId = postId
//        return this
//    }
//
//    def setCommentId(commentId: String):
//
//    =
//    {
//        this.commentId = commentId
//        return this
//    }
//
//    def setReplyId(replyId: String):
//
//    =
//    {
//        this.replyId = replyId
//        return this
//    }
//
//    def setLikeCount(likeCount: Long):
//
//    =
//    {
//        this.likeCount = likeCount
//        return this
//    }
//
//    def setUnlikeCount(unlikeCount: Long):
//
//    =
//    {
//        this.unlikeCount = unlikeCount
//        return this
//    }
//
//    def setShareCount(shareCount: Long):
//
//    =
//    {
//        this.shareCount = shareCount
//        return this
//    }
//
//    def setCommentCount(commentCount: Long):
//
//    =
//    {
//        this.commentCount = commentCount
//        return this
//    }
//
//    def setReplyCount(replyCount: Long):
//
//    =
//    {
//        this.replyCount = replyCount
//        return this
//    }
//
//    def setViewCount(viewCount: Long):
//
//    =
//    {
//        this.viewCount = viewCount
//        return this
//    }
//
//    def setShareContent(shareContent: util.List[String]):
//
//    =
//    {
//        this.shareContent = shareContent
//        return this
//    }
//
//    def setAuthorId(authorId: String):
//
//    =
//    {
//        this.authorId = authorId
//        return this
//    }
//
//    def setWallId(wallId: String):
//
//    =
//    {
//        this.wallId = wallId
//        return this
//    }
//
//    def setAuthorDisplayName(authorDisplayName: String):
//
//    =
//    {
//        this.authorDisplayName = authorDisplayName
//        return this
//    }
//
//    def setAuthorYearOfBirth(authorYearOfBirth: Int):
//
//    =
//    {
//        this.authorYearOfBirth = authorYearOfBirth
//        return this
//    }
//
//    def setAuthorGender(authorGender: Int):
//
//    =
//    {
//        this.authorGender = authorGender
//        return this
//    }
//
//    def setWallDisplayName(wallDisplayName: String):
//
//    =
//    {
//        this.wallDisplayName = wallDisplayName
//        return this
//    }
//
//    def setLocation(location: String):
//
//    =
//    {
//        this.location = location
//        return this
//    }
//
//    def setVideoFrames(videoFrames: util.List[Nothing]):
//
//    =
//    {
//        this.videoFrames = videoFrames
//        return this
//    }
//
//    def setReachCount(reachCount: Double):
//
//    =
//    {
//        this.reachCount = reachCount
//        return this
//    }
//
//    def setTags(tags: util.Collection[Integer]):
//
//    =
//    {
//        this.tags = tags
//        return this
//    }
//
//    def setKpiTags(kpiTags: util.Collection[Integer]):
//
//    =
//    {
//        this.kpiTags = kpiTags
//        return this
//    }
//
//    def setMicParse(micParse: Int):
//
//    =
//    {
//        this.micParse = micParse
//        return this
//    }
//
//    override def equals(o: Any):
//
//    =
//    {
//        if (o eq this) return true
//        if (!o.isInstanceOf[OrmArticle]) return false
//        val other = o.asInstanceOf[OrmArticle]
//        if (!other.canEqual(this.asInstanceOf[Any])) return false
//        val this$id = this.getId
//        val other$id = other.getId
//        if (if (this$id == null) other$id != null
//        else !(this$id == other$id)) return false
//        val this$url = this.getUrl
//        val other$url = other.getUrl
//        if (if (this$url == null) other$url != null
//        else !(this$url == other$url)) return false
//        val this$domain = this.getDomain
//        val other$domain = other.getDomain
//        if (if (this$domain == null) other$domain != null
//        else !(this$domain == other$domain)) return false
//        if (this.getSourceId != other.getSourceId) return false
//        val this$firstCrawledTime = this.getFirstCrawledTime
//        val other$firstCrawledTime = other.getFirstCrawledTime
//        if (if (this$firstCrawledTime == null) other$firstCrawledTime != null
//        else !(this$firstCrawledTime == other$firstCrawledTime)) return false
//        val this$lastCrawledTime = this.getLastCrawledTime
//        val other$lastCrawledTime = other.getLastCrawledTime
//        if (if (this$lastCrawledTime == null) other$lastCrawledTime != null
//        else !(this$lastCrawledTime == other$lastCrawledTime)) return false
//        val this$publishedTime = this.getPublishedTime
//        val other$publishedTime = other.getPublishedTime
//        if (if (this$publishedTime == null) other$publishedTime != null
//        else !(this$publishedTime == other$publishedTime)) return false
//        if (this.getPublishedTimestamp != other.getPublishedTimestamp) return false
//        val this$createdTime = this.getCreatedTime
//        val other$createdTime = other.getCreatedTime
//        if (if (this$createdTime == null) other$createdTime != null
//        else !(this$createdTime == other$createdTime)) return false
//        val this$lastUpdatedTime = this.getLastUpdatedTime
//        val other$lastUpdatedTime = other.getLastUpdatedTime
//        if (if (this$lastUpdatedTime == null) other$lastUpdatedTime != null
//        else !(this$lastUpdatedTime == other$lastUpdatedTime)) return false
//        val this$title = this.getTitle
//        val other$title = other.getTitle
//        if (if (this$title == null) other$title != null
//        else !(this$title == other$title)) return false
//        val this$summary = this.getSummary
//        val other$summary = other.getSummary
//        if (if (this$summary == null) other$summary != null
//        else !(this$summary == other$summary)) return false
//        val this$content = this.getContent
//        val other$content = other.getContent
//        if (if (this$content == null) other$content != null
//        else !(this$content == other$content)) return false
//        val this$imageSources = this.getImageSources
//        val other$imageSources = other.getImageSources
//        if (if (this$imageSources == null) other$imageSources != null
//        else !(this$imageSources == other$imageSources)) return false
//        val this$videoSources = this.getVideoSources
//        val other$videoSources = other.getVideoSources
//        if (if (this$videoSources == null) other$videoSources != null
//        else !(this$videoSources == other$videoSources)) return false
//        if (this.getSimilarMaster != other.getSimilarMaster) return false
//        val this$similarGroupId = this.getSimilarGroupId
//        val other$similarGroupId = other.getSimilarGroupId
//        if (if (this$similarGroupId == null) other$similarGroupId != null
//        else !(this$similarGroupId == other$similarGroupId)) return false
//        if (this.getTimeType != other.getTimeType) return false
//        if (this.getRemovedByHost != other.getRemovedByHost) return false
//        if (this.getVersion != other.getVersion) return false
//        if (this.getTotalVersion != other.getTotalVersion) return false
//        if (java.lang.Double.compare(this.getDifferentPercent, other.getDifferentPercent) != 0) return false
//        val this$timeLastChanged = this.getTimeLastChanged
//        val other$timeLastChanged = other.getTimeLastChanged
//        if (if (this$timeLastChanged == null) other$timeLastChanged != null
//        else !(this$timeLastChanged == other$timeLastChanged)) return false
//        val this$classifications = this.getClassifications
//        val other$classifications = other.getClassifications
//        if (if (this$classifications == null) other$classifications != null
//        else !(this$classifications == other$classifications)) return false
//        val this$articleType = this.getArticleType
//        val other$articleType = other.getArticleType
//        if (if (this$articleType == null) other$articleType != null
//        else !(this$articleType == other$articleType)) return false
//        val this$postId = this.getPostId
//        val other$postId = other.getPostId
//        if (if (this$postId == null) other$postId != null
//        else !(this$postId == other$postId)) return false
//        val this$commentId = this.getCommentId
//        val other$commentId = other.getCommentId
//        if (if (this$commentId == null) other$commentId != null
//        else !(this$commentId == other$commentId)) return false
//        val this$replyId = this.getReplyId
//        val other$replyId = other.getReplyId
//        if (if (this$replyId == null) other$replyId != null
//        else !(this$replyId == other$replyId)) return false
//        if (this.getLikeCount != other.getLikeCount) return false
//        if (this.getUnlikeCount != other.getUnlikeCount) return false
//        if (this.getShareCount != other.getShareCount) return false
//        if (this.getCommentCount != other.getCommentCount) return false
//        if (this.getReplyCount != other.getReplyCount) return false
//        if (this.getViewCount != other.getViewCount) return false
//        val this$shareContent = this.getShareContent
//        val other$shareContent = other.getShareContent
//        if (if (this$shareContent == null) other$shareContent != null
//        else !(this$shareContent == other$shareContent)) return false
//        val this$authorId = this.getAuthorId
//        val other$authorId = other.getAuthorId
//        if (if (this$authorId == null) other$authorId != null
//        else !(this$authorId == other$authorId)) return false
//        val this$wallId = this.getWallId
//        val other$wallId = other.getWallId
//        if (if (this$wallId == null) other$wallId != null
//        else !(this$wallId == other$wallId)) return false
//        val this$authorDisplayName = this.getAuthorDisplayName
//        val other$authorDisplayName = other.getAuthorDisplayName
//        if (if (this$authorDisplayName == null) other$authorDisplayName != null
//        else !(this$authorDisplayName == other$authorDisplayName)) return false
//        if (this.getAuthorYearOfBirth != other.getAuthorYearOfBirth) return false
//        if (this.getAuthorGender != other.getAuthorGender) return false
//        val this$wallDisplayName = this.getWallDisplayName
//        val other$wallDisplayName = other.getWallDisplayName
//        if (if (this$wallDisplayName == null) other$wallDisplayName != null
//        else !(this$wallDisplayName == other$wallDisplayName)) return false
//        val this$location = this.getLocation
//        val other$location = other.getLocation
//        if (if (this$location == null) other$location != null
//        else !(this$location == other$location)) return false
//        val this$videoFrames = this.getVideoFrames
//        val other$videoFrames = other.getVideoFrames
//        if (if (this$videoFrames == null) other$videoFrames != null
//        else !(this$videoFrames == other$videoFrames)) return false
//        if (java.lang.Double.compare(this.getReachCount, other.getReachCount) != 0) return false
//        val this$tags = this.getTags
//        val other$tags = other.getTags
//        if (if (this$tags == null) other$tags != null
//        else !(this$tags == other$tags)) return false
//        val this$kpiTags = this.getKpiTags
//        val other$kpiTags = other.getKpiTags
//        if (if (this$kpiTags == null) other$kpiTags != null
//        else !(this$kpiTags == other$kpiTags)) return false
//        if (this.getMicParse != other.getMicParse) return false
//        return true
//    }
//
//    protected def canEqual(other: Any):
//
//    =
//    {
//        return other.isInstanceOf[OrmArticle]
//    }
//
//    override def hashCode:
//
//    =
//    {
//        val PRIME = 59
//        var result = 1
//        val $id = this.getId
//        result = result * PRIME + (if ($id == null) 43
//        else $id.hashCode)
//        val $url = this.getUrl
//        result = result * PRIME + (if ($url == null) 43
//        else $url.hashCode)
//        val $domain = this.getDomain
//        result = result * PRIME + (if ($domain == null) 43
//        else $domain.hashCode)
//        result = result * PRIME + this.getSourceId
//        val $firstCrawledTime = this.getFirstCrawledTime
//        result = result * PRIME + (if ($firstCrawledTime == null) 43
//        else $firstCrawledTime.hashCode)
//        val $lastCrawledTime = this.getLastCrawledTime
//        result = result * PRIME + (if ($lastCrawledTime == null) 43
//        else $lastCrawledTime.hashCode)
//        val $publishedTime = this.getPublishedTime
//        result = result * PRIME + (if ($publishedTime == null) 43
//        else $publishedTime.hashCode)
//        val $publishedTimestamp = this.getPublishedTimestamp
//        result = result * PRIME + ($publishedTimestamp >>> 32 ^ $publishedTimestamp).toInt
//        val $createdTime = this.getCreatedTime
//        result = result * PRIME + (if ($createdTime == null) 43
//        else $createdTime.hashCode)
//        val $lastUpdatedTime = this.getLastUpdatedTime
//        result = result * PRIME + (if ($lastUpdatedTime == null) 43
//        else $lastUpdatedTime.hashCode)
//        val $title = this.getTitle
//        result = result * PRIME + (if ($title == null) 43
//        else $title.hashCode)
//        val $summary = this.getSummary
//        result = result * PRIME + (if ($summary == null) 43
//        else $summary.hashCode)
//        val $content = this.getContent
//        result = result * PRIME + (if ($content == null) 43
//        else $content.hashCode)
//        val $imageSources = this.getImageSources
//        result = result * PRIME + (if ($imageSources == null) 43
//        else $imageSources.hashCode)
//        val $videoSources = this.getVideoSources
//        result = result * PRIME + (if ($videoSources == null) 43
//        else $videoSources.hashCode)
//        result = result * PRIME + this.getSimilarMaster
//        val $similarGroupId = this.getSimilarGroupId
//        result = result * PRIME + (if ($similarGroupId == null) 43
//        else $similarGroupId.hashCode)
//        result = result * PRIME + this.getTimeType
//        result = result * PRIME + this.getRemovedByHost
//        result = result * PRIME + this.getVersion
//        result = result * PRIME + this.getTotalVersion
//        val $differentPercent = java.lang.Double.doubleToLongBits(this.getDifferentPercent)
//        result = result * PRIME + ($differentPercent >>> 32 ^ $differentPercent).toInt
//        val $timeLastChanged = this.getTimeLastChanged
//        result = result * PRIME + (if ($timeLastChanged == null) 43
//        else $timeLastChanged.hashCode)
//        val $classifications = this.getClassifications
//        result = result * PRIME + (if ($classifications == null) 43
//        else $classifications.hashCode)
//        val $articleType = this.getArticleType
//        result = result * PRIME + (if ($articleType == null) 43
//        else $articleType.hashCode)
//        val $postId = this.getPostId
//        result = result * PRIME + (if ($postId == null) 43
//        else $postId.hashCode)
//        val $commentId = this.getCommentId
//        result = result * PRIME + (if ($commentId == null) 43
//        else $commentId.hashCode)
//        val $replyId = this.getReplyId
//        result = result * PRIME + (if ($replyId == null) 43
//        else $replyId.hashCode)
//        val $likeCount = this.getLikeCount
//        result = result * PRIME + ($likeCount >>> 32 ^ $likeCount).toInt
//        val $unlikeCount = this.getUnlikeCount
//        result = result * PRIME + ($unlikeCount >>> 32 ^ $unlikeCount).toInt
//        val $shareCount = this.getShareCount
//        result = result * PRIME + ($shareCount >>> 32 ^ $shareCount).toInt
//        val $commentCount = this.getCommentCount
//        result = result * PRIME + ($commentCount >>> 32 ^ $commentCount).toInt
//        val $replyCount = this.getReplyCount
//        result = result * PRIME + ($replyCount >>> 32 ^ $replyCount).toInt
//        val $viewCount = this.getViewCount
//        result = result * PRIME + ($viewCount >>> 32 ^ $viewCount).toInt
//        val $shareContent = this.getShareContent
//        result = result * PRIME + (if ($shareContent == null) 43
//        else $shareContent.hashCode)
//        val $authorId = this.getAuthorId
//        result = result * PRIME + (if ($authorId == null) 43
//        else $authorId.hashCode)
//        val $wallId = this.getWallId
//        result = result * PRIME + (if ($wallId == null) 43
//        else $wallId.hashCode)
//        val $authorDisplayName = this.getAuthorDisplayName
//        result = result * PRIME + (if ($authorDisplayName == null) 43
//        else $authorDisplayName.hashCode)
//        result = result * PRIME + this.getAuthorYearOfBirth
//        result = result * PRIME + this.getAuthorGender
//        val $wallDisplayName = this.getWallDisplayName
//        result = result * PRIME + (if ($wallDisplayName == null) 43
//        else $wallDisplayName.hashCode)
//        val $location = this.getLocation
//        result = result * PRIME + (if ($location == null) 43
//        else $location.hashCode)
//        val $videoFrames = this.getVideoFrames
//        result = result * PRIME + (if ($videoFrames == null) 43
//        else $videoFrames.hashCode)
//        val $reachCount = java.lang.Double.doubleToLongBits(this.getReachCount)
//        result = result * PRIME + ($reachCount >>> 32 ^ $reachCount).toInt
//        val $tags = this.getTags
//        result = result * PRIME + (if ($tags == null) 43
//        else $tags.hashCode)
//        val $kpiTags = this.getKpiTags
//        result = result * PRIME + (if ($kpiTags == null) 43
//        else $kpiTags.hashCode)
//        result = result * PRIME + this.getMicParse
//        return result
//    }
//
//    override def toString:
//
//    =
//    {
//        return "OrmArticle(id=" + this.getId + ", url=" + this.getUrl + ", domain=" + this.getDomain + ", sourceId=" + this.getSourceId + ", firstCrawledTime=" + this.getFirstCrawledTime + ", lastCrawledTime=" + this.getLastCrawledTime + ", publishedTime=" + this.getPublishedTime + ", publishedTimestamp=" + this.getPublishedTimestamp + ", createdTime=" + this.getCreatedTime + ", lastUpdatedTime=" + this.getLastUpdatedTime + ", title=" + this.getTitle + ", summary=" + this.getSummary + ", content=" + this.getContent + ", imageSources=" + this.getImageSources + ", videoSources=" + this.getVideoSources + ", similarMaster=" + this.getSimilarMaster + ", similarGroupId=" + this.getSimilarGroupId + ", timeType=" + this.getTimeType + ", removedByHost=" + this.getRemovedByHost + ", version=" + this.getVersion + ", totalVersion=" + this.getTotalVersion + ", differentPercent=" + this.getDifferentPercent + ", timeLastChanged=" + this.getTimeLastChanged + ", classifications=" + this.getClassifications + ", articleType=" + this.getArticleType + ", postId=" + this.getPostId + ", commentId=" + this.getCommentId + ", replyId=" + this.getReplyId + ", likeCount=" + this.getLikeCount + ", unlikeCount=" + this.getUnlikeCount + ", shareCount=" + this.getShareCount + ", commentCount=" + this.getCommentCount + ", replyCount=" + this.getReplyCount + ", viewCount=" + this.getViewCount + ", shareContent=" + this.getShareContent + ", authorId=" + this.getAuthorId + ", wallId=" + this.getWallId + ", authorDisplayName=" + this.getAuthorDisplayName + ", authorYearOfBirth=" + this.getAuthorYearOfBirth + ", authorGender=" + this.getAuthorGender + ", wallDisplayName=" + this.getWallDisplayName + ", location=" + this.getLocation + ", videoFrames=" + this.getVideoFrames + ", reachCount=" + this.getReachCount + ", tags=" + this.getTags + ", kpiTags=" + this.getKpiTags + ", micParse=" + this.getMicParse + ")"
//    }
//    @JsonProperty(Constants.OrmArticle.JsonField.ID) private val id = null
//    @JsonProperty(Constants.OrmArticle.JsonField.URL)
//    @Builder.Default private val url = null
//    @JsonProperty(Constants.OrmArticle.JsonField.DOMAIN)
//    @Builder.Default private val domain = null
//    @JsonProperty(Constants.OrmArticle.JsonField.SOURCE_ID) private val sourceId = 0
//    @JsonProperty(Constants.OrmArticle.JsonField.FIRST_CRAWLED_TIME)
//    @JsonSerialize(using = classOf[Nothing])
//    @JsonDeserialize(using = classOf[Nothing])
//    @Builder.Default private var firstCrawledTime = new Date
//    @JsonProperty(Constants.OrmArticle.JsonField.LAST_CRAWLED_TIME)
//    @JsonSerialize(using = classOf[Nothing])
//    @JsonDeserialize(using = classOf[Nothing])
//    @Builder.Default private val lastCrawledTime = new Date
//    @JsonProperty(Constants.OrmArticle.JsonField.PUBLISHED_TIME)
//    @JsonSerialize(using = classOf[Nothing])
//    @JsonDeserialize(using = classOf[Nothing])
//    @Builder.Default private val publishedTime = null
//    @JsonProperty(Constants.OrmArticle.JsonField.PUBLISHED_TIMESTAMP) private val publishedTimestamp = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.CREATED_TIME)
//    @JsonSerialize(using = classOf[Nothing])
//    @JsonDeserialize(using = classOf[Nothing])
//    @Builder.Default private var createdTime = new Date
//    @JsonProperty(Constants.OrmArticle.JsonField.LAST_UPDATED_TIME)
//    @JsonSerialize(using = classOf[Nothing])
//    @JsonDeserialize(using = classOf[Nothing])
//    @Builder.Default private var lastUpdatedTime = new Date
//    @JsonProperty(Constants.OrmArticle.JsonField.TITLE) private val title = null
//    @JsonProperty(Constants.OrmArticle.JsonField.SUMMARY) private val summary = null
//    @JsonProperty(Constants.OrmArticle.JsonField.CONTENT) private val content = null
//    @JsonProperty(Constants.OrmArticle.JsonField.IMAGE_SOURCES) private val imageSources = null
//    @JsonProperty(Constants.OrmArticle.JsonField.VIDEO_SOURCES) private val videoSources = null
//    @JsonProperty(Constants.OrmArticle.JsonField.IS_SIMILAR_MASTER)
//    @Builder.Default private var similarMaster = 0
//    @JsonProperty(Constants.OrmArticle.JsonField.SIMILAR_GROUP_ID)
//    @Builder.Default private val similarGroupId = ""
//    @JsonProperty(Constants.OrmArticle.JsonField.TIME_TYPE) private val timeType = 0
//    @JsonProperty(Constants.OrmArticle.JsonField.REMOVED_BY_HOST) private val removedByHost = 0
//    @JsonProperty(Constants.OrmArticle.JsonField.VERSION)
//    @Builder.Default private var version = 1
//    @JsonProperty(Constants.OrmArticle.JsonField.TOTAL_VERSION)
//    @Builder.Default private var totalVersion = 1
//    @JsonProperty(Constants.OrmArticle.JsonField.DIFFERENT_PERCENT) private val differentPercent = .0
//    @JsonProperty(Constants.OrmArticle.JsonField.TIME_LAST_CHANGED)
//    @JsonSerialize(using = classOf[Nothing])
//    @JsonDeserialize(using = classOf[Nothing])
//    @Builder.Default private val timeLastChanged = null
//    //    @JsonProperty(Constants.OrmArticle.JsonField.CLASSIFICATION_LEVEL)
//    //    private List<Integer> classificationLevel;
//    @JsonProperty(Constants.OrmArticle.JsonField.CLASSIFICATIONS)
//    @Builder.Default private val classifications = new util.ArrayList[Integer]
//    @JsonProperty(Constants.OrmArticle.JsonField.ARTICLE_TYPE)
//    @Builder.Default private val articleType = null
//    @JsonProperty(Constants.OrmArticle.JsonField.POST_ID)
//    @Builder.Default private val postId = null
//    @JsonProperty(Constants.OrmArticle.JsonField.COMMENT_ID) private val commentId = null
//    @JsonProperty(Constants.OrmArticle.JsonField.REPLY_ID) private val replyId = null
//    @JsonProperty(Constants.OrmArticle.JsonField.LIKE_COUNT)
//    @Builder.Default private val likeCount = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.UNLIKE_COUNT)
//    @Builder.Default private val unlikeCount = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.SHARE_COUNT)
//    @Builder.Default private val shareCount = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.COMMENT_COUNT)
//    @Builder.Default private val commentCount = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.REPLY_COUNT)
//    @Builder.Default private val replyCount = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.VIEW_COUNT)
//    @Builder.Default private val viewCount = 0L
//    @JsonProperty(Constants.OrmArticle.JsonField.SHARE_CONTENT) private val shareContent = null
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_ID) private val authorId = null
//    @JsonProperty(Constants.OrmArticle.JsonField.WALL_ID) private val wallId = null
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_DISPLAY_NAME) private val authorDisplayName = null
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_YEAR_OF_BIRTH)
//    @Builder.Default private val authorYearOfBirth = 0
//    @JsonProperty(Constants.OrmArticle.JsonField.AUTHOR_GENDER)
//    @Builder.Default private val authorGender = 0
//    @JsonProperty(Constants.OrmArticle.JsonField.WALL_DISPLAY_NAME) private val wallDisplayName = null
//    @JsonProperty(Constants.OrmArticle.JsonField.LOCATION) private val location = null
//    @JsonProperty(Constants.OrmArticle.JsonField.VIDEO_FRAMES) private val videoFrames = null
//    //    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_NAME)
//    //    private String newspaperName;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_INDEX)
//    //    private List<Long> newspaperIndex;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_PAGE_INDEX)
//    //    private int newspaperPageIndex;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_PAGE_COUNT)
//    //    private int newspaperPageCount;
//    //    @JsonProperty(Constants.OrmArticle.JsonField.NEWSPAPER_TITLE)
//    //    private String newspaperTitle;
//    @JsonProperty(Constants.OrmArticle.JsonField.REACH_COUNT) private val reachCount = .0
//    @JsonProperty(value = "tags")
//    @Builder.Default private var tags = new util.ArrayList[Integer]
//    @JsonProperty(value = "kpi_tags") private var kpiTags = new util.ArrayList[Integer]
//    @JsonProperty(value = "mic_parse") private val micParse = 0
//
//    def hasId: Boolean = StringUtils.isNotEmpty(this.id)
//
//    def hasUrl: Boolean = this.url != null && StringUtils.isNotEmpty(this.url.trim)
//
//    def hasDomain: Boolean = this.domain != null && StringUtils.isNotEmpty(this.domain.trim)
//
//    def hasPublishedTime: Boolean = this.publishedTime != null
//
//    def hasContent: Boolean = this.content != null && StringUtils.isNotEmpty(this.content.trim)
//
//    def isValid: Boolean = {
//        if (!hasId) {
//            OrmArticle.log.warn("Missing `id` property, url: {}", this.url)
//            return false
//        }
//        if (!hasUrl) {
//            OrmArticle.log.warn("Missing `url` property")
//            return false
//        }
//        if (!hasDomain) {
//            OrmArticle.log.warn("Missing `domain` property, url: {}", this.url)
//            return false
//        }
//        if (!hasPublishedTime) {
//            OrmArticle.log.warn("Missing `publishedTime` property, url: {}", this.url)
//            return false
//        }
//        if (!hasContent) {
//            OrmArticle.log.warn("Missing `content` property, url: {}", this.url)
//            return false
//        }
//        true
//    }
//}