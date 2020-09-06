package vn.com.vtcc.dataflow.flow.processor;

import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.flow.sink.Sink;
import vn.com.vtcc.dataflow.flow.source.StreamSource;

import java.util.ArrayList;
import java.util.List;

public class Processor<V, R> {

    private static Logger logger = Logger.getLogger(Processor.class);

    private Processor nextProcessor;
    private List<Sink<R>> sinks;
    private Handler<V, R> handler;

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

    public void process(V value) {
        if (this.handler == null) {
            throw new NullPointerException("not found handler");
        }
        R result = this.handle(value);
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

    public R handle(V value) {
        return this.handler.handle(value);
    }
}
