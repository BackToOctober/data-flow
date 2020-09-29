package vn.com.vtcc.pluto.core.monitor.prometheus;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * collect all collector registry to one main registry
 */
public class MainRegistry {

    private static MainRegistry mainRegistry;

    private final Map<Long, CollectorRegistry> registryMap;

    private MainRegistry() {
        this.registryMap = new HashMap<>();
    }

    public static MainRegistry getInstance() {
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
        if (registryMap.containsKey(scrapeInterval)) {
            registryMap.get(scrapeInterval).register(collector);
        } else {
            CollectorRegistry cr = new CollectorRegistry();
            registryMap.put(scrapeInterval, cr);
            cr.register(collector);
        }
    }

    public Map<Long, CollectorRegistry> getRegistryMap() {
        return this.registryMap;
    }
}
