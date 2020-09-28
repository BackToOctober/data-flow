//package vn.com.vtcc.dataflow.monitor.prometheus;
//
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import spark.Service;
//
//public class ExporterServer {
//
//    private static final Logger logger = LogManager.getLogger(ExporterServer.class);
//
//    private static ExporterServer exporterServer;
//    private int port = 9275;
//    private Service service;
//
//    public static synchronized ExporterServer getInstance() {
//        if (exporterServer == null) {
//            exporterServer = new ExporterServer();
//        }
//        return exporterServer;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    public void run() {
//        logger.info(" >>> ExporterServer start at port = " + port);
//        this.service = Service.ignite().port(port).threadPool(10);
//
//        service.get("/metrics", (request, response) -> {
//            String result = getResponseResult(getStatMetrics());
//            response.type("text/plain; version=0.0.4; charset=utf-8");
//            response.header("Content-Encoding","gzip");
//            return result;
//        });
//
//        service.get("/hello", ((request, response) -> {
//            response.type("text/plain; version=0.0.4; charset=utf-8");
//            return "hello";
//        }));
//
//        service.awaitInitialization();
//    }
//}