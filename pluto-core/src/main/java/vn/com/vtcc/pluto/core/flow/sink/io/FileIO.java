package vn.com.vtcc.pluto.core.flow.sink.io;

import org.apache.log4j.Logger;
import vn.com.vtcc.pluto.core.flow.sink.SinkIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO extends SinkIO<String> {

    private static Logger logger = Logger.getLogger(FileIO.class);

    private File file;
    private BufferedWriter bufferedWriter;
    
    public FileIO(String fileName) {
        this.file = new File(fileName);
        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(this.file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void write(String value) {
        try {
            this.bufferedWriter.write(value + "\n");
            this.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            this.bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            this.flush();
            this.bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
