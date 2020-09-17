package vn.com.vtcc.dataflow.monitor;

public class LogCountMetricFactory {

    public String dateFormat = "yyyy/MM/dd HH:mm:ss";

    public static LogCountMetricFactory init() {
        return new LogCountMetricFactory();
    }

    public LogCountMetricFactory setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public LogCountMetric createMetric(String metricName) {
        LogCountMetric logCountMetric = new LogCountMetric(metricName);
        logCountMetric.setDateFormat(this.dateFormat);
        return logCountMetric;
    }
}


