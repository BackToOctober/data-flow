package vn.com.vtcc.dataflow.flow;

public class SerialStreamFlow implements Flow {

    private final Pipe pipe;

    public SerialStreamFlow(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void run() {
        this.pipe.run();
    }

    @Override
    public void close() {
        this.pipe.close();
    }
}
