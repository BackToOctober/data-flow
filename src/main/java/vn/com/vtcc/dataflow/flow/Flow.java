package vn.com.vtcc.dataflow.flow;

import java.io.Serializable;

public interface Flow {
    public void run();

    public void close();
}
