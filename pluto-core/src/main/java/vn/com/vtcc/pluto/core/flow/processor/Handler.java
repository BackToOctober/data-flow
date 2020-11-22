package vn.com.vtcc.pluto.core.flow.processor;

public abstract class Handler<V, R> {
    public abstract R handle(V value);
}
