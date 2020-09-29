package vn.com.vtcc.dataflow.app;

import vn.com.vtcc.dataflow.utils.FileUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * monitor kafka and expose that metric to prometheus
 */
public class KafkaTopicMonitor {

    private volatile boolean isRunning;

    private static KafkaTopicMonitor kafkaTopicMonitor;
    private Properties props;

    private KafkaTopicMonitor() {

    }

    public static KafkaTopicMonitor getInstance() {
        if (kafkaTopicMonitor == null) {
            kafkaTopicMonitor = new KafkaTopicMonitor();
        }
        return kafkaTopicMonitor;
    }

    public void init() {

    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public void run() {
        this.isRunning = true;
        this.init();
        while (isRunning) {

        }
    }

    public void close() {
        this.isRunning = false;
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        Properties props = FileUtils.readPropertiesFile(path);
        KafkaTopicMonitor monitor = KafkaTopicMonitor.getInstance();
        monitor.setProps(props);
        monitor.run();
    }
}
