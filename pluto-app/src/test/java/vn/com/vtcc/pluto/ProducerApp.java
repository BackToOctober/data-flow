package vn.com.vtcc.pluto;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerApp {

    private KafkaProducer<String, String> kafkaProducer;

    public void run() throws InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.101.9:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        this.kafkaProducer = new KafkaProducer(props);
        String topic = "test_linhnv52_1";
        while (true) {
            ProducerRecord<String, String> record = new ProducerRecord(topic, System.currentTimeMillis() + "");
            this.kafkaProducer.send(record);
            System.out.println("end");
            Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ProducerApp().run();
    }
}
