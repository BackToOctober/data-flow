package vn.com.vtcc.dataflow.flow.sink;

public abstract class SinkIO<V> {
    public abstract void write(V value);

    public abstract void close();
}
