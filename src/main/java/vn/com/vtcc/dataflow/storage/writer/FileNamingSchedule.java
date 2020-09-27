package vn.com.vtcc.dataflow.storage.writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.utils.IDGenerator;
import vn.com.vtcc.dataflow.utils.TimeUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class FileNamingSchedule {

    private static final Logger logger = LogManager.getLogger(FileNamingSchedule.class);

    private final Timer timer;
    private final long intervalTimeReset;
    private long lastMarkerTime;
    private final HdfsRollingDataWriter hdfsRollingDataWriter;
    private String customName;
    private boolean isAutoGenFileName;

    public FileNamingSchedule(long timeReset, HdfsRollingDataWriter writer) {
        this.intervalTimeReset = timeReset;
        this.hdfsRollingDataWriter = writer;
        this.timer = new Timer("file_naming_schedule");
        this.isAutoGenFileName = true;
    }

    public void setGenFileName(boolean value) {
        this.isAutoGenFileName = value;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    /**
     * run schedule job tracking time to reset file name
     */
    public void run() {
        this.lastMarkerTime = System.currentTimeMillis();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    task();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000L, 1000L);
    }

    /**
     * task in timer
     *
     * @throws IOException : IOException
     */
    public void task() throws IOException {
        long t = System.currentTimeMillis();
        if (checkFilePathReset(t)) {
            resetFilePath(t);
        }
    }

    /**
     * call method of HdfsRollingDataWriter to reset file path
     *
     * @param markerTime : last marker time used to compute interval time when it is time up
     * @throws IOException : IOException
     */
    public void resetFilePath(long markerTime) throws IOException {
        this.lastMarkerTime = markerTime;
        this.hdfsRollingDataWriter.resetFilePath();
    }

    /**
     * check condition to reset file Path
     *
     * @return true if file is need reset, false otherwise
     */
    public boolean checkFilePathReset(long time) {
        return (time - this.lastMarkerTime) > this.intervalTimeReset;
    }

    /**
     * name the file path name
     *
     * @param time : current time
     * @return new output file path
     */
    public String naming(long time) {
        String patternPath = TimeUtils.formatPattern(this.hdfsRollingDataWriter.getSubPathPattern(), time);
        if (this.isAutoGenFileName) {
            if (this.customName != null) {
                return Paths.get(patternPath, this.customName + "_" + IDGenerator.genID()).toString();
            }
            return Paths.get(patternPath, IDGenerator.genID()).toString();
        }
        return Paths.get(patternPath, this.customName).toString();
    }

    /**
     * name the file path name
     *
     * @return new output file path
     */
    public String naming() {
        return naming(this.lastMarkerTime);
    }

    public void close() {
        this.timer.cancel();
    }
}