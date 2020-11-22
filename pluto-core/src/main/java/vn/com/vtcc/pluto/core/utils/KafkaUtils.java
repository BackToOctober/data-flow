package vn.com.vtcc.pluto.core.utils;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * provide some api to get some information of kafka topic
 */
public class KafkaUtils {

    private final Properties props;
    private final Properties kafkaProps;

    /**
     *
     * @param props: properties
     *             kafka.bootstrap.servers = 127.0.0.1:9092
     *             kafka.topic = test_123
     */
    public KafkaUtils(Properties props) {
        this.props = props;
        this.kafkaProps = new Properties();
        this.kafkaProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.props.getProperty("kafka.bootstrap.servers"));
    }

    public List<String> getAllTopics(int timeout) throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        List<String> list = adminClient.listTopics(new ListTopicsOptions().timeoutMs(timeout)).listings().get()
                .stream()
                .filter(r -> !r.isInternal())
                .map(TopicListing::name).collect(Collectors.toList());
        adminClient.close();
        return list;
    }

    public void describeTopic(String topicName, int timeout) throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName), new DescribeTopicsOptions().timeoutMs(timeout));
        TopicDescription description = describeTopicsResult.values().get(topicName).get();
        System.out.println(description);
        adminClient.close();
    }

    public List<String> getAllGroupID(int timeout) throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups(new ListConsumerGroupsOptions().timeoutMs(timeout));
        List<String> list = listConsumerGroupsResult.all().get()
                .stream()
                .map(ConsumerGroupListing::groupId).collect(Collectors.toList());
        adminClient.close();
        return list;
    }

    public Map<TopicPartition, Long> getOffsetGroupID(String groupID, int timeout) throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(this.kafkaProps);
        ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = adminClient.listConsumerGroupOffsets(groupID, new ListConsumerGroupOffsetsOptions().timeoutMs(timeout));
        Map<TopicPartition, OffsetAndMetadata> consumerGroupOffsets = listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().get();
        adminClient.close();

        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.props.getProperty("kafka.bootstrap.servers"));
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(kafkaProps);
        kafkaConsumer.subscribe(Collections.singletonList(this.props.getProperty("kafka.topic")));

        Map<TopicPartition, Long> topicEndOffsets = kafkaConsumer.endOffsets(consumerGroupOffsets.keySet());

        Map<TopicPartition, Long> consumerGroupLag = consumerGroupOffsets.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(
                        entry.getKey()
                        , topicEndOffsets.get(entry.getKey()) - entry.getValue().offset()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        kafkaConsumer.close();
        return consumerGroupLag;
    }
}
