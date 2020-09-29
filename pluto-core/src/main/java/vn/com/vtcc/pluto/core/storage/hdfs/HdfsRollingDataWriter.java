package vn.com.vtcc.pluto.core.storage.hdfs;

import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;

public class HdfsRollingDataWriter implements Writer {

    private static final Logger logger = LogManager.getLogger(HdfsRollingDataWriter.class);

    private final String subPathPattern;
    private final String parentFolderPath;
    private String fullOutputPath;
    private final FileSystem fileSystem;
    private HdfsWriter hdfsWriter;
    private final FileNamingSchedule schedule;
    public final Object lock = new Object();

    /**
     * full output path = parentFolderPath + subPathPattern + fileName
     *
     * @param timeReset : interval time before reset new path
     * @param parentFolderPath : parent folder path
     * @param subPathPattern : pattern of path
     * @param fileSystem : file system hdfs
     */
    public HdfsRollingDataWriter(long timeReset, String parentFolderPath, String subPathPattern, FileSystem fileSystem) {
        this.parentFolderPath = parentFolderPath;
        this.subPathPattern = subPathPattern;
        this.fileSystem = fileSystem;
        this.schedule = new FileNamingSchedule(timeReset, this);
    }

    public String getSubPathPattern() {
        return this.subPathPattern;
    }

    /**
     * open hdfs writer stream
     *
     * @throws IOException : IOException
     */
    public void openWriter() throws IOException {
        this.schedule.run();
        this.fullOutputPath = outputPathNaming();
        this.hdfsWriter = new HdfsWriter(fullOutputPath, fileSystem);
        this.hdfsWriter.openWriter();
    }

    public void close() throws IOException {
        synchronized (this.lock) {
            this.hdfsWriter.close();
            this.schedule.close();
        }
    }

    public void write(String data) throws IOException {
        synchronized (lock) {
            this.hdfsWriter.write(data + "\n");
        }
    }

    /**
     * reset file path to write to the new path
     *
     * @throws IOException : IOException
     */
    public void resetFilePath() throws IOException {
        synchronized (this.lock) {
            logger.debug("reset filePath");
            this.fullOutputPath = outputPathNaming();
            reOpenWriter();
        }
    }

    /**
     * reopen hdfs file writer stream
     *
     * @throws IOException : IOException
     */
    public void reOpenWriter() throws IOException {
        this.hdfsWriter.close();
        this.hdfsWriter = new HdfsWriter(fullOutputPath, fileSystem);
        this.hdfsWriter.openWriter();
    }

    /**
     * naming output path
     *
     * @return output path
     */
    public String outputPathNaming() {
        String output = Paths.get(parentFolderPath, schedule.naming()).toString();
        logger.info(output);
        return output;
    }
}
