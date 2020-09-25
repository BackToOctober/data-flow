package vn.com.vtcc.dataflow.test

import java.nio.file.Paths
import java.sql
import java.text.SimpleDateFormat
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.spark.sql.types.{DateType, StringType, StructField, TimestampType}
import org.apache.spark.sql.{Row, SparkSession, types}
import org.apache.spark.{SPARK_BRANCH, SparkConf, SparkContext, TaskContext}
import org.codehaus.jackson.annotate.JsonProperty
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
//import vn.com.vtcc.dataflow.schema.OrmArticle

import scala.collection.{immutable, mutable}

object Hello {

    class Test {
        var a : Object = _

        def Test1(): Test = {
            a = "aaaa"
            this
        }
    }


    case class H20(@JsonProperty("color") color: String,@JsonProperty("type") type_car: String)

//        @JsonProperty("color")
//        var color : String = _
//
//        @JsonProperty("type")
//        var type_car : String = _
//
//        def this(color: String, type_car: String) {
//            this()
//            this.color = color
//            this.type_car = type_car
//        }

//
    def hi(): Unit = {
        val conf = new SparkConf().setAppName("test").setMaster("local[*]")
        val spark = new SparkContext(conf)
        val session = SparkSession.builder().config(conf).getOrCreate()
        import session.implicits._

        val mapper = new ObjectMapper();

        val s = Seq((1,2,3), (4,5,6))

        val df = s.toDF("col_1", "col_2", "col_3")

        df.toJavaRDD.saveAsTextFile("output/test2")

//        val s2 = Seq((7,8,9), (10,11,12))
//        val df2 = s2.toDF("col_1", "col_2", "col_3")
//
//        df2.toJavaRDD.saveAsTextFile("output/test1")

    }

    def main(args: Array[String]): Unit = {
        hi()
//        var test = List("1", "2","3","4")
//        print("".mkString)
//
//        var test1 = immutable.HashMap[String, String]()
//        test1 += ("a" -> "b")
//        var test2 = immutable.HashMap[String, String]()
//        test2 += ("a" -> "d")
//        println(test1)
//        println(test2)
//        test1 =  test1.++(test2)
//        println(test1)
//
//        var a = 10
//        println(-a)

//        var test2 = new mutable.HashMap[String, Object]()
//        test2.put("a", "a")
//
//        println(test2)
//        val test3 = new Test3()
//        println(test3.a)

//        val t1 = System.currentTimeMillis()
//        val b = new Date(t1)


//        val year = b.getYear
//        val month = b.getMonthOfYear
//        val day = b.getDayOfMonth

//        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd/HH/mm/")
//        println(b)
//        println(dateTimeFormat.format(b))
//
//        import java.nio.file.Path
//        import java.nio.file.Paths
//        val path = Paths.get("foo/asd/aa", "bar", "baz.txt")
//        println(path.toString)


//        val objectMapper = new ObjectMapper()
//        val json = "{\n  \"id\": \"aaa\",\n  \"url\": \"aaaa\",\n  \"domain\": \"aa\",\n  \"source_id\": \"bb\"\n}"
//        val t = objectMapper.readValue(json.toString, classOf[OrmArticle])
//        println(t)
    }
}
