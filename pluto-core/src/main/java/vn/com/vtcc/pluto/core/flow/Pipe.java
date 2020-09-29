package vn.com.vtcc.pluto.core.flow;

import vn.com.vtcc.pluto.core.flow.processor.Processor;
import vn.com.vtcc.pluto.core.flow.sink.Sink;
import vn.com.vtcc.pluto.core.flow.source.StreamSource;

import java.util.ArrayList;
import java.util.List;

public class Pipe {

    private StreamSource streamSource;
    private List<Processor> processors;
    private List<Sink> sinks;
    private List<Object> components;
    private Processor currentProcessor;
    private String pipeName;

    public Pipe(String pipeName) {
        this.pipeName = pipeName;
        this.processors = new ArrayList<>();
        this.sinks = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public String getPipeName() {
        return this.pipeName;
    }

    public void setPipeName(String pipeName) {
        this.pipeName = pipeName;
    }

    /**
     * run flow...
     */
    public void run() {
        for (Sink sink : sinks) {
            new Thread(sink).start();
        }
        this.streamSource.run();
    }

    /**
     * apply a source
     *
     * @param source: StreamSource
     * @return StreamFlow
     */
    public Pipe apply(StreamSource source) {
        source.setPipe(this);
        if (this.streamSource != null) {
            throw new IllegalStateException("one stream source is exists");
        }
        this.streamSource = source;
        this.components.add(source);
        return this;
    }

    /**
     * apply a processor
     *
     * @param processor: Processor
     * @return StreamFlow
     */
    public Pipe apply(Processor processor) {
        processor.setPipe(this);
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

    /**
     * apply a sink
     *
     * @param sink: Sink
     * @return StreamFlow
     */
    public Pipe apply(Sink sink) {
        sink.setPipe(this);
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
