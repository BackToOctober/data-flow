package vn.com.vtcc.pluto.app;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import vn.com.vtcc.pluto.core.flow.Flow;
import vn.com.vtcc.pluto.core.flow.ParallelStreamFlow;
import vn.com.vtcc.pluto.core.flow.Pipe;
import vn.com.vtcc.pluto.core.flow.processor.Processor;
import vn.com.vtcc.pluto.core.flow.processor.handler.StringHandler;
import vn.com.vtcc.pluto.core.flow.sink.Sink;
import vn.com.vtcc.pluto.core.flow.sink.io.FileIO;
import vn.com.vtcc.pluto.core.flow.source.StreamSource;
import vn.com.vtcc.pluto.core.flow.source.io.FileStreamIO;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SimpleApplication {

    private final Logger logger = LogManager.getLogger(SimpleApplication.class);

    private Flow flow;

    public void init() throws FileNotFoundException {
        List<Pipe> pipes = makePipes(2);
        this.flow = new ParallelStreamFlow(pipes);
    }

    public void run() throws FileNotFoundException {
        final Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread("shutdown") {
            @Override
            public void run() {
                close();
            }
        });

        this.init();
        this.flow.run();
    }

    public List<Pipe> makePipes(int number) throws FileNotFoundException {
        List<Pipe> pipes = new ArrayList<>();
        for (int i = 0; i<number; i++) {
            pipes.add(this.makePipe("test_" + i));
        }
        return pipes;
    }

    public Pipe makePipe(String pipeName) throws FileNotFoundException {
        String file1 = pipeName + "_test_linhnv52_1";
        String file2 = pipeName + "_test_linhnv52_2";
        String file3 = pipeName + "_test_linhnv52_3";
        Pipe pipe = new Pipe(pipeName);
        pipe.apply(new StreamSource<String>().setStreamIO(new FileStreamIO().setFileGenerator("data/data.jsonl")))
                .apply(new Sink<String>().setSinkIO(new FileIO(file1)))
                .apply(new Processor<String, String>().setHandler(new StringHandler()))
                .apply(new Sink<String>().setSinkIO(new FileIO(file2)))
                .apply(new Sink<String>().setSinkIO(new FileIO(file3)));
        return pipe;
    }

    public void close() {
        this.flow.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new SimpleApplication().run();
    }
}
