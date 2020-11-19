package vn.com.vtcc.pluto.app

import java.io.FileInputStream

import org.apache.hadoop.yarn.api.records.{ApplicationId, ApplicationReport}
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration
import vn.com.vtcc.pluto.core.utils.FileUtils
import org.apache.hadoop.yarn.api.records.YarnApplicationState
import org.apache.hadoop.yarn.client.api.impl.YarnClientImpl
import org.apache.log4j.LogManager


object YarnMonitor {

    val logger = LogManager.getLogger(YarnMonitor.getClass)

    def initYarnClient(yarnSitePath: String,
                       hdfsSitePath: String,
                       coreSitePath: String,
                       mapredSitePath: String): YarnClient = {
        val conf = new YarnConfiguration()
        conf.addResource(yarnSitePath)
        conf.addResource(new FileInputStream(yarnSitePath))
        val yarnClient = YarnClient.createYarnClient()
        yarnClient.init(conf)
        yarnClient
    }

    def monitorApp(yarnClient: YarnClient, appName: String): Unit = {
//        val appId = getAppIdByAppName(yarnClient, appName)
//        val report = yarnClient.getApplicationReport(appId)
//        val state = report.getFinalApplicationStatus
//
//        if (state == YarnApplicationState.RUNNING) {
//            println(s"app $appName - ${appId.getId} is running")
//        } else {
//            println(s"app $appName - ${appId.getId} is dead")
//        }
        import scala.collection.JavaConversions._
        yarnClient.start()
        val list = yarnClient.getApplications()
        for (app <- list) {
            val report = yarnClient.getApplicationReport(app.getApplicationId)
            val state = report.getYarnApplicationState
            if (app.getName.equals(appName)) {
                logger.info(s"app ${app.getName} - ${app.getApplicationId.getId}: ${state}")
            }
        }
        yarnClient.stop()
    }

    def run(configPath: String, appName: String): Unit = {
        val config = FileUtils.readPropertiesFile(configPath)
        val yarnSitePath = config.getProperty("hdfs.yarn_site")
        val hdfsSitePath = config.getProperty("hdfs.hdfs_site")
        val coreSitePath = config.getProperty("hdfs.core_site")
        val mapredSitePath = config.getProperty("hdfs.mapred_site")
        val yarnClient = initYarnClient(yarnSitePath, hdfsSitePath, coreSitePath, mapredSitePath)
        monitorApp(yarnClient, appName)
        yarnClient.close()
    }

    def main(args: Array[String]): Unit = {
        val configPath = args(0)
        val appName = args(1)
        run(configPath, appName)
    }
}
