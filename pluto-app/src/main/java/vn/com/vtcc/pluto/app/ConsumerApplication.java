package vn.com.vtcc.pluto.app;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xerial.snappy.Snappy;
import vn.com.vtcc.pluto.core.flow.Flow;
import vn.com.vtcc.pluto.core.flow.ParallelStreamFlow;
import vn.com.vtcc.pluto.core.flow.Pipe;
import vn.com.vtcc.pluto.core.flow.processor.Handler;
import vn.com.vtcc.pluto.core.flow.processor.Processor;
import vn.com.vtcc.pluto.core.flow.sink.Sink;
import vn.com.vtcc.pluto.core.flow.sink.SinkIO;
import vn.com.vtcc.pluto.core.flow.sink.io.HdfsRollingDataIO;
import vn.com.vtcc.pluto.core.flow.source.StreamIO;
import vn.com.vtcc.pluto.core.flow.source.StreamSource;
import vn.com.vtcc.pluto.core.flow.source.io.KafkaStreamIO;
import vn.com.vtcc.pluto.core.monitor.prometheus.CustomGauge;
import vn.com.vtcc.pluto.core.utils.FileUtils;
import vn.com.vtcc.pluto.core.utils.HdfsUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * consumer application by kafka core
 */
public class ConsumerApplication {

    private final Logger logger = LogManager.getLogger(ConsumerApplication.class);

    private Flow flow;
    private final String name;
    private final int number;
    private final Map<String, AtomicLong> countSuccessfulRecord;
    private final Map<String, AtomicLong> countFailRecord;
    private final Timer timer;

    public ConsumerApplication(String appName, int number) {
        this.name = appName;
        this.number = number;
        this.countSuccessfulRecord = new HashMap<>();
        this.countFailRecord = new HashMap<>();
        this.timer = new Timer();
    }

    public List<Pipe> makePipes(Properties props) {
        List<Pipe> pipes = new ArrayList<>();
        for (int i = 0; i<this.number; i++) {
            String pipeName = this.name + "_part_" + i;
            pipes.add(makePipe(props, i, pipeName));
            this.countFailRecord.put(pipeName, new AtomicLong(0));
            this.countSuccessfulRecord.put(pipeName, new AtomicLong(0));
        }
        return pipes;
    }

    public Pipe makePipe(Properties props, int id, String pipeName) {
        String topic = props.getProperty("kafka.topic");
        Pipe pipe = new Pipe(pipeName);
        StreamIO<byte[]> kafkaStreamIO = this.getStreamSourceIO(props);
        Handler<byte[], String> handler = this.getHandler(topic, pipeName);
        SinkIO<String> hdfsRollingDataIO = null;
        try {
            hdfsRollingDataIO = this.getSinkIO(props, id);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        pipe.apply(new StreamSource().setStreamIO(kafkaStreamIO))
                .apply(new Processor().setHandler(handler))
                .apply(new Sink().setSinkIO(hdfsRollingDataIO));
        return pipe;
    }

    public void run(Properties props) {
        logger.info(" >>> start consumer " + this.name);

        final Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread("shutdown") {
            @Override
            public void run() {
                close();
            }
        });

        List<Pipe> pipes = makePipes(props);
        this.flow = new ParallelStreamFlow(pipes);
        this.flow.run();

        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pushMetrics(props);
            }
        }, 10000, 10000);
    }

    public void pushMetrics(Properties props) {
        PushGateway pushGateway = new PushGateway(props.getProperty("prometheus.gateway"));
        CollectorRegistry registry = new CollectorRegistry();
        final CustomGauge recordsParsingSuccessCount = CustomGauge.build()
                .name("records_parsing_success")
                .labelNames("topic", "thread")
                .help("count number of records parsed successfully")
                .register(registry);
        final CustomGauge recordsParsingFailureCount = CustomGauge.build()
                .name("records_parsing_failure")
                .labelNames("topic", "thread")
                .help("count number of records parsed unsuccessfully")
                .register(registry);
        this.countSuccessfulRecord.entrySet().stream().forEach(c -> {
            recordsParsingSuccessCount
                    .labels(props.getProperty("kafka.topic"), c.getKey())
                    .set(c.getValue().getAndSet(0));
        });
        this.countFailRecord.entrySet().stream().forEach(c -> {
            recordsParsingFailureCount
                    .labels(props.getProperty("kafka.topic"), c.getKey())
                    .set(c.getValue().getAndSet(0));
        });
        try {
            pushGateway.pushAdd(registry, "consumer_crawler_data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create stream io source
     *
     * @param props: props
     * @return stream io
     */
    public StreamIO<byte[]> getStreamSourceIO(Properties props) {
        StreamIO<byte[]> kafkaStreamIO = new KafkaStreamIO(props);
        return kafkaStreamIO;
    }

    /**
     * create handler
     *
     * @return handler
     */
    public Handler<byte[], String> getHandler(String topic, String threadName) {
        Handler<byte[], String> handler = new Handler<byte[], String>() {
            @Override
            public String handle(byte[] value) {
                try {
                    String str = new String(Snappy.uncompress(value), StandardCharsets.UTF_8);
                    countSuccessfulRecord.get(threadName).incrementAndGet();
                    return str;
                } catch (IOException e) {
                    e.printStackTrace();
                    countSuccessfulRecord.get(threadName).incrementAndGet();
                    return null;
                }
            }
        };
        return handler;
    }

    /**
     * create sink io
     *
     * @param props: props
     * @return sink io
     * @throws IOException: IOException
     */
    public SinkIO<String> getSinkIO(Properties props, int id) throws IOException {
        long timeReset = Long.parseLong(props.getProperty("hdfs.file.duration"));
        String parentFolderPath = props.getProperty("hdfs.folder.parent.path");
        String subPathPattern = props.getProperty("hdfs.path.pattern");
        String coreSite = props.getProperty("hdfs.hdfs_site");
        String hdfsSite = props.getProperty("hdfs.core_site");
        FileSystem fs = HdfsUtils.builder()
                .setHdfsSite(coreSite)
                .setCoreSite(hdfsSite)
                .init();
        SinkIO<String> hdfsRollingDataIO = new HdfsRollingDataIO(timeReset, parentFolderPath, subPathPattern, fs,
                true, String.valueOf(id));
        return hdfsRollingDataIO;
    }

    public void close() {
        if (this.flow != null) {
            this.flow.close();
        }
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        String name = args[1];
        int numberThread = Integer.parseInt(args[2]);
        Properties props = FileUtils.readPropertiesFile(path);
        new ConsumerApplication(name, numberThread).run(props);
    }
}