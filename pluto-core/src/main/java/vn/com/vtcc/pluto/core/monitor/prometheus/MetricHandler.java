package vn.com.vtcc.pluto.core.monitor.prometheus;

@FunctionalInterface
public interface MetricHandler {
    void handle();
}
