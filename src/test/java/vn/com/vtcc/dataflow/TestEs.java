package vn.com.vtcc.dataflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import vn.com.vtcc.dataflow.dataSource.elasticsearch.ESConnectorFactory;
import vn.com.vtcc.dataflow.dataSource.elasticsearch.ESUtils;
import vn.com.vtcc.dataflow.monitor.LogCountMetric;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class TestEs {

    public static String printJson(Object value) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String r = objectMapper.writeValueAsString(value);
        System.out.println(r);
        return r;
    }

    public static LogCountMetric createMetric() {
        LogCountMetric metric = new LogCountMetric();
        metric.setTimeStamp(System.currentTimeMillis()).increaseAndGet(10);
        metric.setAndGet(new Random().nextInt(20));
        return metric;
    }

    public static void main(String[] args) throws IOException {
        String esIndex = "test_log";
        Properties properties = new Properties();
        properties.setProperty("elasticsearch.host", "127.0.0.1:9200");
        ESConnectorFactory esConnectorFactory = new ESConnectorFactory(properties);
        RestHighLevelClient client = esConnectorFactory.createConnect();

//        ESUtils.deleteIndex(client, esIndex);

        if (!ESUtils.existsIndex(client, esIndex)) {
            System.out.println("create index");
            boolean isDone = ESUtils.ESIndexBuilder
                    .getBuilder()
                    .setClient(client)
                    .setIndex(esIndex)
                    .setIndexNumberOfReplicas(3)
                    .setIndexNumberOfShards(5)
                    .create();

            if (isDone) {
                System.out.println("create index successfully");
            }
        }

        ESUtils.putData(client, esIndex, printJson(createMetric()));


        client.close();
    }
}
