package vn.com.vtcc.pluto.core.flow.sink;

public abstract class SinkIO<V> {
    public abstract void write(V value);

    public abstract void close();
}
