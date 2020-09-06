package vn.com.vtcc.dataflow.flow;

import vn.com.vtcc.dataflow.flow.processor.Processor;
import vn.com.vtcc.dataflow.flow.sink.Sink;
import vn.com.vtcc.dataflow.flow.source.StreamSource;

import java.util.ArrayList;

public class StreamFlow implements Flow{

    private StreamSource streamSource;
    private ArrayList<Processor> processors;
    private ArrayList<Sink> sinks;
    private ArrayList<Object> components;
    private Processor currentProcessor;

    public StreamFlow() {
        this.processors = new ArrayList<>();
        this.sinks = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public void run() {
        for (Sink sink : sinks) {
            new Thread(sink).start();
        }
        this.streamSource.run();
    }

    public StreamFlow apply(StreamSource source) {
        if (this.streamSource != null) {
            throw new IllegalStateException("one stream source is exists");
        }
        this.streamSource = source;
        this.components.add(source);
        return this;
    }

    public StreamFlow apply(Processor processor) {
        if (currentProcessor == null) {
            this.streamSource.setProcessor(processor);
        } else {
            this.currentProcessor.setNextProcessor(processor);
        }
        this.processors.add(processor);
        this.currentProcessor = processor;
        this.components.add(processor);
        return this;
    }

    public StreamFlow apply(Sink sink) {
        if (this.streamSource == null) {
            throw new NullPointerException("not found any stream source");
        }
        if (this.currentProcessor == null) {
            this.streamSource.setSink(sink);
        } else {
            this.currentProcessor.setSink(sink);
        }
        this.sinks.add(sink);
        this.components.add(sink);
        return this;
    }

    public void close() {
        for (Sink sink : sinks) {
            sink.close();
        }
        this.streamSource.close();
    }

    public String drawFlow() {
        return "flow";
    }
}
