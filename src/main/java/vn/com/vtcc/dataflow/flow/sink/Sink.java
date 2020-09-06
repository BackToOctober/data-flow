package vn.com.vtcc.dataflow.flow.sink;

import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Sink<V> implements Runnable{

    private static Logger logger = Logger.getLogger(Sink.class);

    private BlockingQueue<V> dataQueue;
    private SinkIO<V> sinkIO;
    private volatile boolean isRun;

    public Sink() {
        this.dataQueue = new ArrayBlockingQueue<>(10000);
    }

    public Sink<V> setSinkIO(SinkIO sinkIO) {
        this.sinkIO = sinkIO;
        return this;
    }

    public void put(V value) throws InterruptedException {
        while (!this.dataQueue.offer(value)) {
            logger.warn("this queue is full");
            Thread.sleep(100);
        }
    }

    @Override
    public void run() {
        this.isRun = true;
        if (this.sinkIO == null) {
            throw new NullPointerException("not found sink io");
        }
        while (isRun) {
            try {
                V value = dataQueue.take();
                if (value != null) {
                    this.sinkIO.write(value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void close() {
        if (this.isRun == true) {
            this.isRun = false;
        }
        if (this.sinkIO != null) {
            this.sinkIO.close();
        }
    }
}
