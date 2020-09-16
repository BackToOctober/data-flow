package vn.com.vtcc.dataflow.dataSource.elasticsearch;

import vn.com.vtcc.dataflow.monitor.LogCountMetric;

public class ESLogService {

    private ESConnectorFactory connector;
    private String esIndex;

    public ESLogService setConnector(ESConnectorFactory connector) {
        this.connector = connector;
        return this;
    }

    public ESLogService setESIndex(String esIndex) {
        this.esIndex = esIndex;
        return this;
    }

    public void sendLogMetric(LogCountMetric logMetric, String esIndex) {

    }
}
