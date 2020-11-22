package vn.com.vtcc.pluto.core.flow;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelStreamFlow implements Flow{

    private List<Pipe> pipes;
    private ExecutorService executorService;

    public ParallelStreamFlow(List<Pipe> pipes) {
        this.pipes = pipes;
        this.executorService =  Executors.newFixedThreadPool(this.pipes.size());
    }

    @Override
    public void run() {
        for (Pipe pipe : pipes) {
            this.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    pipe.run();
                }
            });
        }
        executorService.shutdown();
    }

    @Override
    public void close() {
        if (pipes.size() > 0) {
            for (Pipe pipe : this.pipes) {
                pipe.close();
            }
        }
    }
}
