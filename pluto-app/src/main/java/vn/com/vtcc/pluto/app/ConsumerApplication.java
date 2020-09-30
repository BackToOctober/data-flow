package vn.com.vtcc.pluto.app;

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
import vn.com.vtcc.pluto.core.utils.FileUtils;
import vn.com.vtcc.pluto.core.utils.HdfsUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConsumerApplication {

    private final Logger logger = LogManager.getLogger(ConsumerApplication.class);

    private Flow flow;
    private final String name;
    private final int number;

    public ConsumerApplication(String appName, int number) {
        this.name = appName;
        this.number = number;
    }

    public List<Pipe> makePipes(Properties props) {
        List<Pipe> pipes = new ArrayList<>();
        for (int i = 0; i<this.number; i++) {
            String pipeName = this.name + "_part_" + i;
            pipes.add(makePipe(props, pipeName));
        }
        return pipes;
    }

    public Pipe makePipe(Properties props, String pipeName) {
        Pipe pipe = new Pipe(pipeName);
        StreamIO<byte[]> kafkaStreamIO = this.getStreamSourceIO(props);
        Handler<byte[], String> handler = this.getHandler();
        SinkIO<String> hdfsRollingDataIO = null;
        try {
            hdfsRollingDataIO = this.getSinkIO(props);
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
        if (this.flow != null) {
            this.flow.close();
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
