package vn.com.vtcc.pluto.app

import org.apache.hadoop.fs.{FileSystem, Path}
import vn.com.vtcc.pluto.core.utils.HdfsUtils

import scala.collection.mutable.ListBuffer

object PathExists {

    def listFiles(path: String, fs: FileSystem): ListBuffer[String] = {
        val files = new ListBuffer[String]();
        val statusList = fs.globStatus(new Path(path))
        for (i <- 0 until statusList.length) {
            files += statusList(i).getPath.toString
        }
        files
    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        val coreSitePath = args(1)
        val hdfsSitePath = args(2)
        val fs = HdfsUtils.builder().setCoreSite(coreSitePath).setHdfsSite(hdfsSitePath).init()

        println(path)
        println(fs.exists(new Path(path)))
        println(HdfsUtils.exists(path, fs))

        for (i <- listFiles(path, fs)) {
            println(i)
        }
    }
}
