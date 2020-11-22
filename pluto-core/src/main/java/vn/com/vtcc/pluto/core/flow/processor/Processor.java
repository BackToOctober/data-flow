package vn.com.vtcc.pluto.core.flow.processor;

import org.apache.log4j.Logger;
import vn.com.vtcc.pluto.core.flow.Pipe;
import vn.com.vtcc.pluto.core.flow.sink.Sink;

import java.util.ArrayList;
import java.util.List;

public class Processor<V, R> {

    private static Logger logger = Logger.getLogger(Processor.class);

    private Processor nextProcessor;
    private List<Sink<R>> sinks;
    private Handler<V, R> handler;
    private Pipe pipe;

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    public Pipe getPipe() {
        return pipe;
    }

    public Processor setHandler(Handler handler) {
        this.sinks = new ArrayList<>();
        this.handler = handler;
        return this;
    }

    public void setNextProcessor(Processor processor) {
        this.nextProcessor = processor;
    }

    public void setSink(Sink sink) {
        this.sinks.add(sink);
    }

    /**
     * process data
     * send to downstream if result is not null, otherwise return and exit method
     *
     * @param value: input value
     */
    public void process(V value) {
        if (this.handler == null) {
            throw new NullPointerException("not found handler");
        }
        R result = this.handle(value);
        if (result != null) {
            if (this.sinks.size() > 0) {
                for (Sink<R> sink : this.sinks) {
                    try {
                        sink.put(result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.nextProcessor != null) {
                this.nextProcessor.process(result);
            }
        }

    }

    public R handle(V value) {
        return this.handler.handle(value);
    }
}
