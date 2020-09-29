package vn.com.vtcc.pluto.app;

import org.apache.kafka.clients.admin.*;
import vn.com.vtcc.pluto.core.monitor.prometheus.ExporterServer;
import vn.com.vtcc.pluto.core.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * monitor kafka and expose that metric to prometheus
 */
public class KafkaTopicMonitor {

    private static KafkaTopicMonitor kafkaTopicMonitor;
    private Properties props;
    private Properties kafkaProps;
    private ExporterServer exporterServer;

    private KafkaTopicMonitor() {}

    public static KafkaTopicMonitor getInstance() {
        if (kafkaTopicMonitor == null) {
            kafkaTopicMonitor = new KafkaTopicMonitor();
        }
        return kafkaTopicMonitor;
    }

    public void init() {
        this.kafkaProps = new Properties();
        this.kafkaProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.props.getProperty("kafka.bootstrap.servers"));
        this.exporterServer = ExporterServer.getInstance();
        this.exporterServer.setPort(Integer.parseInt(this.props.getProperty("monitor.exporter-server.port", "9276")));
        this.initMetrics();
    }

    public void initMetrics() {

    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public List<String> getAllTopics() throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        List<TopicListing> list = new ArrayList<>(adminClient.listTopics().listings().get());
        List<String> result = new ArrayList<>();
        for (TopicListing topicListing : list) {
            if (!topicListing.isInternal()) {
                result.add(topicListing.name());
            }
        }
        adminClient.close();
        return result;
    }

    public void describeTopic(String topicName) throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName));
        TopicDescription description = describeTopicsResult.values().get(topicName).get();
        System.out.println(description);
        adminClient.close();
    }

    public void getAllGroupID(String topic) throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups(new ListConsumerGroupsOptions().timeoutMs(2000));
        List<ConsumerGroupListing> list = new ArrayList<>(listConsumerGroupsResult.all().get());
        System.out.println(list);
        for (ConsumerGroupListing consumerGroupListing : list) {
            System.out.println(consumerGroupListing);
        }
        adminClient.close();
    }

    public void getOffsetGroupID(String groupID) {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        adminClient.listConsumerGroupOffsets(groupID);
        adminClient.close();
    }

    public void run() {
        this.init();
    }

    public void close() {
        if (this.exporterServer != null) {
            this.exporterServer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        Properties props = FileUtils.readPropertiesFile(path);
        KafkaTopicMonitor monitor = KafkaTopicMonitor.getInstance();
        monitor.setProps(props);
        monitor.run();
    }
}
