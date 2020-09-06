package vn.com.vtcc.dataflow.flow.source;

public abstract class StreamIO<V> {

    public abstract V consume();

    public abstract void close();
}
