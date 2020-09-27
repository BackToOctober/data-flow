package vn.com.vtcc.dataflow.app


import java.util.Properties

import vn.com.vtcc.dataflow.app.SparkStreamApplication.appProps
import vn.com.vtcc.dataflow.storage.elasticSearch.{ESConnectorFactory, ESUtils}
import vn.com.vtcc.dataflow.utils.FileUtils

object IndexCRUD {
    def main(args: Array[String]): Unit = {
        val configPath = args(0)
        val cmd = args(1)
        val index = args(2)

        appProps = FileUtils.readPropertiesFile(configPath)

        val monitorConfig = new Properties()
        monitorConfig.setProperty("elasticsearch.host", appProps.getProperty("elasticsearch.host"))
        monitorConfig.setProperty("elasticsearch.connection.request.timeout"
            , appProps.getProperty("elasticsearch.connection.request.timeout", "50000"))
        monitorConfig.setProperty("elasticsearch.connection.request.timeout"
            , appProps.getProperty("elasticsearch.connection.request.timeout", "50000"))
        monitorConfig.setProperty("elasticsearch.socket.timeout"
            , appProps.getProperty("elasticsearch.socket.timeout", "50000"))

        val connectorFactory = new ESConnectorFactory(monitorConfig)
        val client = connectorFactory.createConnect()

        if (cmd.equals("delete")) {
            ESUtils.deleteIndex(client, index)
        } else if (cmd.equals("create")) {
            val jsonMappingPath = args(3)
            val jsonObject = FileUtils.readJsonFile(jsonMappingPath)

            println(jsonObject)

            ESUtils.ESIndexBuilder
                .getBuilder
                .setClient(client)
                .setIndex(index)
                .setIndexNumberOfReplicas(2)
                .setIndexNumberOfShards(1)
                .setMapping(jsonObject.toMap)
                .create()
        }
        client.close()
    }
}
