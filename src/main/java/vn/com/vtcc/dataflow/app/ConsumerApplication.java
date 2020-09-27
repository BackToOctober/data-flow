package vn.com.vtcc.dataflow.app;

import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xerial.snappy.Snappy;
import vn.com.vtcc.dataflow.flow.StreamFlow;
import vn.com.vtcc.dataflow.flow.processor.Handler;
import vn.com.vtcc.dataflow.flow.processor.Processor;
import vn.com.vtcc.dataflow.flow.sink.Sink;
import vn.com.vtcc.dataflow.flow.sink.SinkIO;
import vn.com.vtcc.dataflow.flow.sink.io.HdfsRollingDataIO;
import vn.com.vtcc.dataflow.flow.source.StreamIO;
import vn.com.vtcc.dataflow.flow.source.StreamSource;
import vn.com.vtcc.dataflow.flow.source.io.KafkaStreamIO;
import vn.com.vtcc.dataflow.utils.FileUtils;
import vn.com.vtcc.dataflow.utils.HdfsUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConsumerApplication {

    private Logger logger = LogManager.getLogger(ConsumerApplication.class);

    private final StreamFlow streamFlow;
    private String name;

    public ConsumerApplication(String appName) {
        this.name = appName;
        this.streamFlow = new StreamFlow();
    }

    public void run(Properties props) {
        logger.info("======> START consumer " + this.name);
        StreamIO<byte[]> kafkaStreamIO = this.getStreamSourceIO(props);
        Handler<byte[], String> handler = this.getHandler();
        SinkIO<String> hdfsRollingDataIO = null;
        try {
            hdfsRollingDataIO = this.getSinkIO(props);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.streamFlow.apply(new StreamSource().setStreamIO(kafkaStreamIO))
                    .apply(new Processor().setHandler(handler))
                    .apply(new Sink().setSinkIO(hdfsRollingDataIO));
        this.streamFlow.run();
    }

    public StreamIO<byte[]> getStreamSourceIO(Properties props) {
        StreamIO<byte[]> kafkaStreamIO = new KafkaStreamIO(props);
        return kafkaStreamIO;
    }

    public Handler<byte[], String> getHandler() {
        Handler<byte[], String> handler = new Handler<byte[], String>() {
            @Override
            public String handle(byte[] value) {
                try {
                    return new String(Snappy.uncompress(value), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        return handler;
    }

    public SinkIO<String> getSinkIO(Properties props) throws IOException {
        long timeReset = Long.parseLong(props.getProperty("hdfs.file.duration"));
        String parentFolderPath = props.getProperty("hdfs.folder.parent.path");
        String subPathPattern = props.getProperty("hdfs.path.pattern");
        String coreSite = props.getProperty("hdfs.hdfs_site");
        String hdfsSite = props.getProperty("hdfs.core_site");
        FileSystem fs = HdfsUtils.builder()
                .setHdfsSite(coreSite)
                .setCoreSite(hdfsSite)
                .init();
        SinkIO<String> hdfsRollingDataIO = new HdfsRollingDataIO(timeReset, parentFolderPath, subPathPattern, fs);
        return hdfsRollingDataIO;
    }

    public void close() {
        if (this.streamFlow != null) {
            this.streamFlow.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        String name = args[1];
        Properties props = FileUtils.readPropertiesFile(path);
        new ConsumerApplication(name).run(props);
    }
}
