package vn.com.vtcc.pluto.core.flow.source.io;

import org.apache.log4j.Logger;
import vn.com.vtcc.pluto.core.flow.source.StreamIO;
import vn.com.vtcc.pluto.core.flow.source.io.utils.FileGenerator;

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
