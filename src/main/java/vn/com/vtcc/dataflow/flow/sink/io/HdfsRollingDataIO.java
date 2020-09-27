package vn.com.vtcc.dataflow.flow.sink.io;

import org.apache.hadoop.fs.FileSystem;
import vn.com.vtcc.dataflow.flow.sink.SinkIO;
import vn.com.vtcc.dataflow.storage.hdfs.HdfsRollingDataWriter;

import java.io.IOException;

public class HdfsRollingDataIO extends SinkIO<String> {

    private HdfsRollingDataWriter hdfsRollingDataWriter;

    public HdfsRollingDataIO(long timeReset, String parentFolderPath, String subPathPattern, FileSystem fileSystem) throws IOException {
        this.hdfsRollingDataWriter = new HdfsRollingDataWriter(timeReset, parentFolderPath, subPathPattern, fileSystem);
        this.hdfsRollingDataWriter.openWriter();
    }

    @Override
    public void write(String value) {
        try {
            this.hdfsRollingDataWriter.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.hdfsRollingDataWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
