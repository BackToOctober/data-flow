package vn.com.vtcc.pluto;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.util.Collections;
import java.util.Properties;

public class ConsumerApp {

    private KafkaConsumer<byte[],  byte[]> kafkaConsumer;

    public void run() {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.101.9:9092");
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test_12345678");
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        this.kafkaConsumer = new KafkaConsumer<>(kafkaProps);
        this.kafkaConsumer.subscribe(Collections.singletonList("test_linhnv52_1"));
        while (true) {
            ConsumerRecords<byte[], byte[]> consumerRecords = this.kafkaConsumer.poll(1000);
            consumerRecords.forEach(consumerRecord -> {
                System.out.println(consumerRecord);
            });
        }
    }

    public static void main(String[] args) {
        new ConsumerApp().run();
    }
}
