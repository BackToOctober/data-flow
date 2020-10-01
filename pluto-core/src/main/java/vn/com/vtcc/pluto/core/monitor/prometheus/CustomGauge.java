package vn.com.vtcc.pluto.core.monitor.prometheus;

import io.prometheus.client.*;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CustomGauge extends SimpleCollector<CustomGauge.Child> implements Collector.Describable {
    CustomGauge(CustomGauge.Builder b) {
        super(b);
    }

    public static class Builder extends SimpleCollector.Builder<CustomGauge.Builder, CustomGauge> {
        @Override
        public CustomGauge create() {
            return new CustomGauge(this);
        }
    }

    /**
     *  Return a Builder to allow configuration of a new Gauge. Ensures required fields are provided.
     *
     *  @param name The name of the metric
     *  @param help The help string of the metric
     */
    public static CustomGauge.Builder build(String name, String help) {
        return new CustomGauge.Builder().name(name).help(help);
    }

    /**
     *  Return a Builder to allow configuration of a new Gauge.
     */
    public static CustomGauge.Builder build() {
        return new CustomGauge.Builder();
    }

    @Override
    protected CustomGauge.Child newChild() {
        return new CustomGauge.Child();
    }

    /**
     * Represents an event being timed.
     */
    public static class Timer implements Closeable {
        private final CustomGauge.Child child;
        private final long start;
        private Timer(CustomGauge.Child child) {
            this.child = child;
            start = CustomGauge.Child.timeProvider.nanoTime();
        }
        /**
         * Set the amount of time in seconds since {@link Gauge.Child#startTimer} was called.
         * @return Measured duration in seconds since {@link Gauge.Child#startTimer} was called.
         */
        public double setDuration() {
            double elapsed = (CustomGauge.Child.timeProvider.nanoTime() - start) / NANOSECONDS_PER_SECOND;
            child.set(elapsed);
            return elapsed;
        }

        /**
         * Equivalent to calling {@link #setDuration()}.
         */
        @Override
        public void close() {
            setDuration();
        }
    }

    /**
     * The value of a single Gauge.
     * <p>
     * <em>Warning:</em> References to a Child become invalid after using
     * {@link SimpleCollector#remove} or {@link SimpleCollector#clear},
     */
    private final Object lockNoLable = new Object();

    public static class Child {

        private final DoubleAdder value = new DoubleAdder();

        static CustomGauge.TimeProvider timeProvider = new CustomGauge.TimeProvider();

        /**
         * Increment the gauge by 1.
         */
        public void inc() {
            inc(1);
        }
        /**
         * Increment the gauge by the given amount.
         */
        public void inc(double amt) {
            value.add(amt);
        }
        /**
         * Decrement the gauge by 1.
         */
        public void dec() {
            dec(1);
        }
        /**
         * Decrement the gauge by the given amount.
         */
        public void dec(double amt) {
            value.add(-amt);
        }
        /**
         * Set the gauge to the given value.
         */
        public void set(double val) {
            value.set(val);
        }
        /**
         * Set the gauge to the current unixtime.
         */
        public void setToCurrentTime() {
            set(timeProvider.currentTimeMillis() / MILLISECONDS_PER_SECOND);
        }
        /**
         * Start a timer to track a duration.
         * <p>
         * Call {@link Gauge.Timer#setDuration} at the end of what you want to measure the duration of.
         * <p>
         * This is primarily useful for tracking the durations of major steps of batch jobs,
         * which are then pushed to a PushGateway.
         * For tracking other durations/latencies you should usually use a {@link Summary}.
         */
        public CustomGauge.Timer startTimer() {
            return new CustomGauge.Timer(this);
        }

        /**
         * Executes runnable code (e.g. a Java 8 Lambda) and observes a duration of how long it took to run.
         *
         * @param timeable Code that is being timed
         * @return Measured duration in seconds for timeable to complete.
         */
        public double setToTime(Runnable timeable){
            CustomGauge.Timer timer = startTimer();

            double elapsed;
            try {
                timeable.run();
            } finally {
                elapsed = timer.setDuration();
            }

            return elapsed;
        }

        /**
         * Executes callable code (e.g. a Java 8 Lambda) and observes a duration of how long it took to run.
         *
         * @param timeable Code that is being timed
         * @return Result returned by callable.
         */
        public <E> E setToTime(Callable<E> timeable){
            CustomGauge.Timer timer = startTimer();

            try {
                return timeable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                timer.setDuration();
            }
        }

        /**
         * Get the value of the gauge.
         */
        public double get() {
            return value.sum();
        }
    }

    // Convenience methods.
    /**
     * Increment the gauge with no labels by 1.
     */
    public void inc() {
        inc(1);
    }
    /**
     * Increment the gauge with no labels by the given amount.
     */
    public void inc(double amt) {
        synchronized (lockNoLable) {
            noLabelsChild.inc(amt);
        }
    }
    /**
     * Decrement the gauge with no labels by 1.
     */
    public void dec() {
        dec(1);
    }
    /**
     * Decrement the gauge with no labels by the given amount.
     */
    public void dec(double amt) {
        synchronized (lockNoLable) {
            noLabelsChild.dec(amt);
        }
    }
    /**
     * Set the gauge with no labels to the given value.
     */
    public void set(double val) {
        synchronized (lockNoLable) {
            noLabelsChild.set(val);
        }
    }

    public double getAndSet(double val) {
        synchronized (lockNoLable) {
            double v = this.get();
            this.noLabelsChild.set(val);
            return v;
        }
    }

    /**
     * Set the gauge with no labels to the current unixtime.
     */
    public void setToCurrentTime() {
        noLabelsChild.setToCurrentTime();
    }
    /**
     * Start a timer to track a duration, for the gauge with no labels.
     * <p>
     * This is primarily useful for tracking the durations of major steps of batch jobs,
     * which are then pushed to a PushGateway.
     * For tracking other durations/latencies you should usually use a {@link Summary}.
     * <p>
     * Call {@link Gauge.Timer#setDuration} at the end of what you want to measure the duration of.
     */
    public CustomGauge.Timer startTimer() {
        return noLabelsChild.startTimer();
    }

    /**
     * Executes runnable code (e.g. a Java 8 Lambda) and observes a duration of how long it took to run.
     *
     * @param timeable Code that is being timed
     * @return Measured duration in seconds for timeable to complete.
     */
    public double setToTime(Runnable timeable){
        return noLabelsChild.setToTime(timeable);
    }

    /**
     * Executes callable code (e.g. a Java 8 Lambda) and observes a duration of how long it took to run.
     *
     * @param timeable Code that is being timed
     * @return Result returned by callable.
     */
    public <E> E setToTime(Callable<E> timeable){
        return noLabelsChild.setToTime(timeable);
    }

    /**
     * Get the value of the gauge.
     */
    public double get() {
        return noLabelsChild.get();
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>(children.size());
        for(Map.Entry<List<String>, CustomGauge.Child> c: children.entrySet()) {
            samples.add(new MetricFamilySamples.Sample(fullname, labelNames, c.getKey(), c.getValue().get()));
        }
        return familySamplesList(Type.GAUGE, samples);
    }

    @Override
    public List<MetricFamilySamples> describe() {
        return Collections.<MetricFamilySamples>singletonList(new GaugeMetricFamily(fullname, help, labelNames));
    }

    static class TimeProvider {
        long currentTimeMillis() {
            return System.currentTimeMillis();
        }
        long nanoTime() {
            return System.nanoTime();
        }
    }
}
