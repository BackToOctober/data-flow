package vn.com.vtcc.dataflow.flow;

import vn.com.vtcc.dataflow.flow.source.StreamSource;

import java.io.Serializable;

public interface Flow extends Serializable {
    public void run();
}
