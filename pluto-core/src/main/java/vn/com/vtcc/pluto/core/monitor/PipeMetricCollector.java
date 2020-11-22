package vn.com.vtcc.pluto.core.monitor;

import vn.com.vtcc.pluto.core.flow.Pipe;
import vn.com.vtcc.pluto.core.flow.source.StreamSource;

public class PipeMetricCollector {

    private Pipe pipe;

    public PipeMetricCollector(Pipe pipe) {
        this.pipe = pipe;
    }

    public void getAllMetrics() {
        StreamSource streamSource = this.pipe.getStreamSource();
    }
}
