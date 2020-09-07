package vn.com.vtcc.dataflow.app;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.flow.source.StreamIO;
import vn.com.vtcc.dataflow.flow.source.io.FileStreamIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ProducerApplication<V> {

    private static Logger logger = LogManager.getLogger(ProducerApplication.class);

    private String configFile;
    private KafkaProducer<String, V> kafkaProducer;
    private StreamIO<V> streamIO;
    private volatile boolean isRunning;

    public ProducerApplication(String configFile) {
        this.configFile = configFile;
        this.initParameters();
    }

    public ProducerApplication<V> setSourceIO(StreamIO<V> streamIO) {
        this.streamIO = streamIO;
        return this;
    }

    public Properties initParameters() {
        Properties props = new Properties();
        props.put("", "");
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
        this.kafkaProducer = new KafkaProducer<String, V>(props);
        while (this.isRunning) {
            V value = this.streamIO.consume();
            ProducerRecord<String, V> record = new ProducerRecord<String, V>(null, value);
            this.kafkaProducer.send(record);
        }
    }

    public synchronized void close() {
        this.isRunning = false;
        this.kafkaProducer.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String configFile = args[0];
        String consumeFile = args[1];
        StreamIO streamIO = new FileStreamIO() {
            @Override
            public String consume() {
                String value = null;
                try {
                    value = this.getFileGenerator().getLine();
                    if (value == null) {
                        this.setFileGenerator(consumeFile);
                        value = this.getFileGenerator().getLine();
                    }
                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    this.close();
                }
                return value;
            }
        }.setFileGenerator(consumeFile);

        new ProducerApplication<String>(configFile).setSourceIO(streamIO).run();
    }
}
