package vn.com.vtcc.dataflow.flow.processor;

public abstract class Handler<V, R> {
    public abstract R handle(V value);
}
