package vn.com.vtcc.dataflow.storage.writer;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HdfsWriter implements Writer {

    private final Logger logger = LogManager.getLogger(HdfsWriter.class);

    private final String outputPath;
    private final FileSystem fileSystem;
    private FSDataOutputStream outputStreamWriter;

    public HdfsWriter(String outputPath, FileSystem fileSystem) {
        this.outputPath = outputPath;
        this.fileSystem = fileSystem;
    }

    public void openWriter() throws IOException {
        Path path = new Path(outputPath);
        if (fileSystem.exists(path)) {
            this.outputStreamWriter = fileSystem.append(path);
            logger.info("HdfsWriter append path get FSDataOutputStream successfully: " + path.toString());
        } else {
            this.outputStreamWriter = fileSystem.create(path);
            logger.info("HdfsWriter create new path successfully: " + path.toString());
        }
    }

    public void close() throws IOException {
        this.outputStreamWriter.flush();
        this.outputStreamWriter.hflush();
        this.outputStreamWriter.close();
    }

    public void write(String data) throws IOException {
        this.outputStreamWriter.write((data).getBytes());
    }
}
