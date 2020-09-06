package vn.com.vtcc.dataflow.flow.source.io;

import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.flow.source.StreamIO;
import vn.com.vtcc.dataflow.flow.source.StreamSource;

import java.io.File;

public class FileStreamIO extends StreamIO<String> {

    private static Logger logger = Logger.getLogger(FileStreamIO.class);

    private File file;
    private int count;

    public FileStreamIO() {
        this.count = 0;
    }

    @Override
    public String consume() {
        String value = String.valueOf(this.count);
        this.count++;
        logger.info("value consume = " + value);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return value;
    }

    @Override
    public void close() {

    }
}
