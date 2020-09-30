package vn.com.vtcc.pluto.app;

import io.prometheus.client.Gauge;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import vn.com.vtcc.pluto.core.monitor.prometheus.ExporterServer;
import vn.com.vtcc.pluto.core.monitor.prometheus.MainRegistry;
import vn.com.vtcc.pluto.core.monitor.prometheus.MetricHandler;
import vn.com.vtcc.pluto.core.utils.FileUtils;
import vn.com.vtcc.pluto.core.utils.KafkaUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * monitor kafka and expose that metric to prometheus
 */
public class KafkaTopicMonitor {

    private static KafkaTopicMonitor kafkaTopicMonitor;
    private Properties props;
    private ExporterServer exporterServer;
    private KafkaUtils kafkaUtils;
    private MainRegistry mainRegistry;

    private Gauge lagOffsetGroupIDKafkaMetric;

    private KafkaTopicMonitor() {}

    public static KafkaTopicMonitor getInstance() {
        if (kafkaTopicMonitor == null) {
            kafkaTopicMonitor = new KafkaTopicMonitor();
        }
        return kafkaTopicMonitor;
    }

    public void init() {
        this.kafkaUtils = new KafkaUtils(this.props);
        this.initMetrics();
        this.exporterServer = ExporterServer.getInstance();
        this.exporterServer.setPort(Integer.parseInt(this.props.getProperty("monitor.exporter-server.port", "9276")));
    }

    public void initMetrics() {
        this.mainRegistry = MainRegistry.getInstance();
        this.lagOffsetGroupIDKafkaMetric = Gauge.build()
                .name("lag_offset_groupId_kafka")
                .help("collect_lag_metric")
                .labelNames("topic", "groupId", "partition").create();
        this.mainRegistry.register(10, this.lagOffsetGroupIDKafkaMetric, new MetricHandler() {
            @Override
            public void handle() {
                try {
                    String topic = props.getProperty("kafka.topic");
                    String groupId = props.getProperty("kafka.groupId");
                    Map<TopicPartition, Long> lagOffsetMap = kafkaUtils.getOffsetGroupID(groupId);
                    for(Map.Entry<TopicPartition, Long> entry : lagOffsetMap.entrySet()) {
                        lagOffsetGroupIDKafkaMetric
                                .labels(topic, groupId, String.valueOf(entry.getKey().partition()))
                                .set(entry.getValue());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public void run() {
        LogManager.getLogger(AdminClientConfig.class).setLevel(Level.ERROR);
        LogManager.getLogger(ConsumerConfig.class).setLevel(Level.ERROR);
        final Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread("shutdown") {
            @Override
            public void run() {
                close();
            }
        });

        this.init();
        this.exporterServer.run();
    }

    public void close() {
        if (this.exporterServer != null) {
            this.exporterServer.close();
        }
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String path = args[0];
        Properties props = FileUtils.readPropertiesFile(path);
        KafkaTopicMonitor monitor = KafkaTopicMonitor.getInstance();
        monitor.setProps(props);
        monitor.run();
    }
}
