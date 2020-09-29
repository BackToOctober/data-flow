package vn.com.vtcc.dataflow.monitor.prometheus;

import io.prometheus.client.Counter;

class ExporterServerTest {
    public static void run() {
        Counter c1 = Counter.build()
                .namespace("java")
                .name("test_1")
                .help("test")
                .labelNames("a1", "a2")
                .create();
        c1.labels("a1", "a2").inc(12.5);

        Counter c2 = Counter.build()
                .namespace("java")
                .name("test_2")
                .help("test")
                .labelNames("a1", "a2")
                .create();
        c2.labels("a1", "a2").inc(0.5);

        Counter c3 = Counter.build()
                .namespace("java")
                .name("test_3")
                .help("test")
                .labelNames("a1", "a2")
                .create();
        c3.labels("a1", "a2").inc(1.5);

        MainRegistry mainRegistry = MainRegistry.getInstance();
        mainRegistry.register(10, c1);
        mainRegistry.register(20, c2);
        mainRegistry.register(20, c3);

        ExporterServer server = ExporterServer.getInstance();
        server.setPort(9275);
        server.run();
    }

    public static void main(String[] args) {
        ExporterServerTest.run();
    }
}