package vn.com.vtcc.dataflow.flow.source.io;

import org.apache.log4j.Logger;
import vn.com.vtcc.dataflow.flow.source.StreamIO;
import vn.com.vtcc.dataflow.flow.source.StreamSource;
import vn.com.vtcc.dataflow.flow.source.io.utils.FileGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileStreamIO extends StreamIO<String> {

    private static Logger logger = Logger.getLogger(FileStreamIO.class);

    private FileGenerator fileGenerator;

    public FileStreamIO setFileGenerator(String fileName) throws FileNotFoundException {
        this.fileGenerator = new FileGenerator(fileName);
        return this;
    }

    public FileGenerator getFileGenerator() {
        return this.fileGenerator;
    }

    @Override
    public String consume() {
        String value = null;
        try {
            value = this.fileGenerator.getLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public void close() {
        try {
            this.fileGenerator.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
