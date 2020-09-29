package vn.com.vtcc.pluto.core.monitor.prometheus;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spark.Service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * exporter server
 *
 * server default run at http://localhost:9275
 * this server is used to expose metric at the many endpoint, each endpoint has different scrape interval
 */
public class ExporterServer {

    private static final Logger logger = LogManager.getLogger(ExporterServer.class);

    private final static String PREFIX_PATH = "/metrics";
    private static ExporterServer exporterServer;
    private int port = 9275;
    private Service service;

    private final MainRegistry mainRegistry;
    private final Map<String, Long> pathMap;

    private ExporterServer() {
        this.mainRegistry = MainRegistry.getInstance();
        this.pathMap = new HashMap<>();
    }

    public static synchronized ExporterServer getInstance() {
        if (exporterServer == null) {
            exporterServer = new ExporterServer();
        }
        return exporterServer;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * write metric to client
     *
     * @param cr: CollectorRegistry
     * @return text of metrics
     */
    public String writeResponse(CollectorRegistry cr) {
        Enumeration<Collector.MetricFamilySamples> e = cr.metricFamilySamples();
        StringBuilder sb = new StringBuilder();
        while (e.hasMoreElements()) {
            Collector.MetricFamilySamples metric = e.nextElement();
            List<Collector.MetricFamilySamples.Sample> list = metric.samples;
            for (Collector.MetricFamilySamples.Sample sample : list) {
                sb.append(sample.name);
                if (sample.labelNames.size() > 0) {
                    sb.append("{");
                    for (int i=0; i<sample.labelNames.size(); i++) {
                        if (i != 0) {
                            sb.append(",");
                        }
                        sb.append(sample.labelNames.get(i)).append("=").append("\"").append(sample.labelValues.get(i)).append("\"");
                    }
                    sb.append("}");
                }
                sb.append(" ").append(sample.value).append("\n");
            }
        }
        return sb.toString();
    }

    public String getResponseResult(String uri) {
        long key = pathMap.get(uri);
        CollectorRegistry cr = mainRegistry.getRegistryMap().get(key);
        return writeResponse(cr);
    }

    public void makePath(String path) {
        this.service.get(path, ((request, response) -> {
            String result = this.getResponseResult(request.uri());
            response.type("text/plain; version=0.0.4; charset=utf-8");
            return result;
        }));
    }

    /**
     * run exporter server
     */
    public void run() {
        logger.info(" >>> ExporterServer start at port = " + this.port);
        this.service = Service.ignite().port(this.port).threadPool(10);
        for (Map.Entry<Long, CollectorRegistry> entry : mainRegistry.getRegistryMap().entrySet()) {
            String path = PREFIX_PATH + "/" + entry.getKey() + "s";
            makePath(path);
            this.pathMap.put(path, entry.getKey());
        }
        this.service.awaitInitialization();
    }

    /**
     * close exporter server
     */
    public void close() {
        if (this.service != null) {
            this.service.stop();
            this.service.awaitStop();
        }
    }
}