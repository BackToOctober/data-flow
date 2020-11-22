package vn.com.vtcc.pluto.app

import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.hadoop.fs.{FileSystem, Path}
import vn.com.vtcc.pluto.core.utils.HdfsUtils

import scala.collection.mutable.ListBuffer

object FolderChecking {

    def listFiles(path: String, fs: FileSystem): ListBuffer[String] = {
        val files = new ListBuffer[String]();
        val statusList = fs.listStatus(new Path(path))
        for (i <- 0 until statusList.length) {
            files += statusList(i).getPath.toString
        }
        files
    }

    def checkingParsingIsDone(path: String, fs: FileSystem): Boolean = {
        if (HdfsUtils.exists(path, fs) && HdfsUtils.exists(path + "/_SUCCESS", fs)) {
            return true
        }
        false
    }

    def run(coreSitePath: String, hdfsSitePath: String): Unit = {
        val fs = HdfsUtils.builder().setCoreSite(coreSitePath).setHdfsSite(hdfsSitePath).init()
        val dateNow = new Date()

        val rootPath = "/user/linhnv52/crawler"
        val dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd")
        val dateStr = dateTimeFormat.format(dateNow)
        val path = Paths.get(rootPath, dateStr)

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

        println("parsing minute folder")
        for (path <- parsingMinuteFolderPaths) {
            println(path)
        }
    }

    def main(args: Array[String]): Unit = {
        val path = args(0)
        val coreSitePath = args(1)
        val hdfsSitePath = args(2)
        run(coreSitePath, hdfsSitePath)
    }
}
