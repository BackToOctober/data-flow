package vn.com.vtcc.dataflow;

import java.io.IOException;
import io.prometheus.client.Counter;

public class TestPrometheus {

    public static void main(String[] args) throws IOException {
        Counter counter = Counter.build().namespace("java").name("my_counter").help("This is my counter").register();

    }
}
