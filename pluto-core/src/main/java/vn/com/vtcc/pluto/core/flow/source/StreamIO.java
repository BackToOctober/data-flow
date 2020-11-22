package vn.com.vtcc.pluto.core.flow.source;

public abstract class StreamIO<V> {

    public abstract V consume();

    public abstract void close();
}
