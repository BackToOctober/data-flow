package vn.com.vtcc.dataflow.test

import scala.collection.immutable

object Hello {

    class Test {
        var a : Object = _

        def Test1(): Test = {
            a = "aaaa"
            this
        }
    }

    def main(args: Array[String]): Unit = {
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
        var a = 10
        println(-a)
    }
}
