package vn.com.vtcc.pluto.core.monitor.prometheus;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * collect all collector registry to one main registry
 */
public class MainRegistry {

    private static MainRegistry mainRegistry;

    private final Map<Long, CollectorRegistry> registryMap;

    private final Map<Long, List<MetricHandler>> metricHandlerMap;

    private MainRegistry() {
        this.registryMap = new HashMap<>();
        this.metricHandlerMap = new HashMap<>();
    }

    public synchronized static MainRegistry getInstance() {
        if (mainRegistry == null) {
            mainRegistry = new MainRegistry();
        }
        return mainRegistry;
    }

    /**
     * metric register the collector registry
     *
     * @param scrapeInterval: number of seconds to scrape
     */
    public synchronized void register(long scrapeInterval, Collector collector) {
        this.register(scrapeInterval, collector, null);
    }

    public synchronized void register(long scrapeInterval, Collector collector, MetricHandler handler) {
        if (registryMap.containsKey(scrapeInterval)) {
            registryMap.get(scrapeInterval).register(collector);
        } else {
            CollectorRegistry cr = new CollectorRegistry();
            registryMap.put(scrapeInterval, cr);
            cr.register(collector);
        }
        if (handler != null) {
            if (metricHandlerMap.containsKey(scrapeInterval)) {
                metricHandlerMap.get(scrapeInterval).add(handler);
            } else {
                List<MetricHandler> list = new ArrayList<>();
                metricHandlerMap.put(scrapeInterval, list);
                list.add(handler);
            }
        }
    }

    public Map<Long, CollectorRegistry> getRegistryMap() {
        return this.registryMap;
    }

    public Map<Long, List<MetricHandler>> getMetricHandlerMap() {
        return this.metricHandlerMap;
    }
}
