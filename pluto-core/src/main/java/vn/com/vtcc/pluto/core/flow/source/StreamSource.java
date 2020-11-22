package vn.com.vtcc.pluto.core.flow.source;

import org.apache.log4j.Logger;
import vn.com.vtcc.pluto.core.flow.Pipe;
import vn.com.vtcc.pluto.core.flow.processor.Processor;
import vn.com.vtcc.pluto.core.flow.sink.Sink;

import java.util.ArrayList;
import java.util.List;

public class StreamSource<V> {

    private static Logger logger = Logger.getLogger(StreamSource.class);

    private Processor nextProcessor;
    private List<Sink<V>> sinks;
    private volatile boolean isRun;
    private StreamIO<V> streamIO;
    private Pipe pipe;

    public StreamSource() {
        this.sinks = new ArrayList<>();
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    public StreamSource setStreamIO(StreamIO streamIO) {
        this.streamIO = streamIO;
        return this;
    }

    public void setProcessor(Processor processor) {
        this.nextProcessor = processor;
    }

    public void setSink(Sink sink) {
        this.sinks.add(sink);
    }

    public void run() {
        this.isRun = true;
        if (this.streamIO == null) {
            throw new NullPointerException("not found stream io");
        }
        while (this.isRun) {
            V value = this.consume();
            if (this.sinks.size() > 0) {
                for (Sink<V> sink : this.sinks) {
                    try {
                        sink.put(value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.nextProcessor != null) {
                this.nextProcessor.process(value);
            }
        }
    }

    public synchronized void close() {
        if (this.isRun == true) {
            this.isRun = false;
        }
        if (this.streamIO != null) {
            this.streamIO.close();
        }
    }

    public V consume() {
        return this.streamIO.consume();
    }

}
