package vn.com.vtcc.dataflow.app;

import vn.com.vtcc.dataflow.flow.StreamFlow;
import vn.com.vtcc.dataflow.flow.processor.Processor;
import vn.com.vtcc.dataflow.flow.processor.handler.StringHandler;
import vn.com.vtcc.dataflow.flow.sink.Sink;
import vn.com.vtcc.dataflow.flow.sink.io.FileIO;
import vn.com.vtcc.dataflow.flow.source.StreamSource;
import vn.com.vtcc.dataflow.flow.source.io.FileStreamIO;

public class Application {

    private StreamFlow flow;

    public Application() {
        this.flow = new StreamFlow();
    }

    public void run() {
        String file1 = "test_linhnv52_1";
        String file2 = "test_linhnv52_2";
        String file3 = "test_linhnv52_3";
        this.flow.apply(new StreamSource<String>().setStreamIO(new FileStreamIO()))
            .apply(new Sink<String>().setSinkIO(new FileIO(file1)))
            .apply(new Processor<String, String>().setHandler(new StringHandler()))
            .apply(new Sink<String>().setSinkIO(new FileIO(file2)))
            .apply(new Sink<String>().setSinkIO(new FileIO(file3)));
        this.flow.run();
    }

    public void close() {
        this.flow.close();
    }

    public static void main(String[] args) {
        new Application().run();
    }
}
