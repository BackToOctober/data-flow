package vn.com.vtcc.dataflow.utils;

public class StopWatch {

    private long time = System.currentTimeMillis();

    public static StopWatch mark() {
        return new StopWatch();
    }

    public long getDelay() {
        return System.currentTimeMillis() - time;
    }

    public String toString() {
        return "time run = " + (System.currentTimeMillis() - time) + " ms";
    }
}
