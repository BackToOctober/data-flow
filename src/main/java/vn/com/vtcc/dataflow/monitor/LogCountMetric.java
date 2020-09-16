package vn.com.vtcc.dataflow.monitor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogCountMetric {

    private String dateFormat = "yyyy/MM/dd HH:mm:ss";
    private SimpleDateFormat sdf;

    @JsonProperty("time_stamp")
    private long timeStampLog;

    @JsonProperty("date")
    private String dateLog;

    @JsonProperty("metric_name")
    private String metricName;

    @JsonProperty("count")
    private long count;

    public LogCountMetric() {
        this.sdf = new SimpleDateFormat(dateFormat);
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.sdf = new SimpleDateFormat(dateFormat);
    }

    public LogCountMetric setTimeStamp(long timeStampLog) {
        this.timeStampLog = timeStampLog;
        this.dateLog = this.sdf.format(new Date(timeStampLog));
        return this;
    }

    public long setAndGet(long value) {
        this.count = value;
        return this.count;
    }

    public void reset() {
        this.count = 0;
    }

    public long increaseAndGet(long value) {
        this.count = this.count + value;
        return this.count;
    }

    public LogCountMetric increase(long value) {
        this.count = this.count + value;
        return this;
    }

}
