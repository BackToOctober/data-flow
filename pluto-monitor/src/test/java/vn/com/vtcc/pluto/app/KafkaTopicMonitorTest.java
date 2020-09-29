package vn.com.vtcc.pluto.app;

import vn.com.vtcc.pluto.core.utils.FileUtils;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaTopicMonitorTest {

    public static void getAllGroupID(Properties props) throws ExecutionException, InterruptedException {
        KafkaTopicMonitor monitor = KafkaTopicMonitor.getInstance();
        monitor.setProps(props);
        monitor.init();
        monitor.getAllGroupID("test_linhnv52_1");
    }

    public static void describeTopic(Properties props) throws ExecutionException, InterruptedException {
        KafkaTopicMonitor monitor = KafkaTopicMonitor.getInstance();
        monitor.setProps(props);
        monitor.init();
        List<String> list = monitor.getAllTopics();
        System.out.println(list);
        monitor.describeTopic("test_linhnv52_1");
    }

    public static void getAllTopics(Properties props) throws ExecutionException, InterruptedException {
        KafkaTopicMonitor monitor = KafkaTopicMonitor.getInstance();
        monitor.setProps(props);
        monitor.init();
        System.out.println(monitor.getAllTopics());
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        String path = args[0];
        Properties props = FileUtils.readPropertiesFile(path);
        getAllGroupID(props);
    }
}