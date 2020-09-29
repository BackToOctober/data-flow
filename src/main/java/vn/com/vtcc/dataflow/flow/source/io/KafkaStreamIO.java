package vn.com.vtcc.dataflow.flow.source.io;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.flow.source.StreamIO;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.*;

public class KafkaStreamIO extends StreamIO<byte[]> {

    private static final Logger logger = Logger.getLogger(KafkaStreamIO.class);

    private final KafkaConsumer<byte[],  byte[]> kafkaConsumer;
    private volatile boolean isRunning;
    private final long timeout;
    private final BlockingQueue<byte[]> queue;
    private final String topic;
    private ExecutorService executorService;

    public KafkaStreamIO(Properties props) {
        this.topic = props.getProperty("kafka.topic");
        this.timeout = Integer.parseInt(props.getProperty("kafka.timeout", "1000"));
        Properties kafkaProps = initKafkaProperties(props);
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProps);
        this.queue = new ArrayBlockingQueue<>(20000);
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                runThread();
            }
        });
        this.executorService.shutdown();
    }

    public Properties initKafkaProperties(Properties props) {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getProperty("kafka.bootstrap.servers"));
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, props.getProperty("kafka.groupId"));
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return kafkaProps;
    }

    private void runThread() {
        this.isRunning = true;
        this.kafkaConsumer.subscribe(Collections.singletonList(this.topic));
        while (this.isRunning) {
            ConsumerRecords<byte[], byte[]> consumerRecords = this.kafkaConsumer.poll(this.timeout);
            consumerRecords.forEach(consumerRecord -> {
                while (!this.queue.offer(consumerRecord.value())) {
                    try {
                        Thread.sleep(100);
                        logger.warn("kafka queue is full !!!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            kafkaConsumer.commitAsync();
        }
    }

    @Override
    public byte[] consume() {
        try {
            return this.queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void close() {
        this.isRunning = false;
        if (this.kafkaConsumer != null) {
            this.kafkaConsumer.wakeup();
            this.kafkaConsumer.close();
        }
    }
}
