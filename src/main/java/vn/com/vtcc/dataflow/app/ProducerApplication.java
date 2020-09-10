package vn.com.vtcc.dataflow.app;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.flow.source.StreamIO;
import vn.com.vtcc.dataflow.flow.source.io.FileStreamIO;
import vn.com.vtcc.dataflow.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ProducerApplication<V> {

    private static Logger logger = LogManager.getLogger(ProducerApplication.class);

    private String configFilePath;
    private KafkaProducer<String, V> kafkaProducer;
    private StreamIO<V> streamIO;
    private volatile boolean isRunning;
    private Properties appProps;

    public ProducerApplication(String configFilePath) throws IOException {
        this.configFilePath = configFilePath;
        this.appProps = FileUtils.readPropertiesFile(this.configFilePath);
    }

    public ProducerApplication<V> setSourceIO(StreamIO<V> streamIO) {
        this.streamIO = streamIO;
        return this;
    }

    public Properties initParameters() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appProps.getProperty("kafka.bootstrap.servers"));
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    public void run() {
        final Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread("shutdown") {
            @Override
            public void run() {
                close();
            }
        });

        Properties props = this.initParameters();
        this.isRunning = true;
        this.kafkaProducer = new KafkaProducer(props);
        String topic = this.appProps.getProperty("kafka.topic");
        while (this.isRunning) {
            V value = this.streamIO.consume();
            if (value == null) {
                this.close();
            } else {
                ProducerRecord<String, V> record = new ProducerRecord(topic, value);
                this.kafkaProducer.send(record);
            }
        }
    }

    public synchronized void close() {
        this.isRunning = false;
        if (this.kafkaProducer != null) {
            this.kafkaProducer.close();
        }
        if (this.streamIO != null) {
            this.streamIO.close();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String configFilePath = args[0];
        String consumeFilePath = args[1];
        StreamIO streamIO = new FileStreamIO() {
            @Override
            public String consume() {
                String value = null;
                try {
                    value = this.getFileGenerator().getLine();
                    if (value == null) {
                        logger.info("reset new file");
                        this.close();
//                        this.setFileGenerator(consumeFilePath);
//                        value = this.getFileGenerator().getLine();
                    }
//                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                catch (InterruptedException e) {
//                    e.printStackTrace();
//                    this.close();
//                }
                return value;
            }
        }.setFileGenerator(consumeFilePath);

        try {
            new ProducerApplication<String>(configFilePath).setSourceIO(streamIO).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
