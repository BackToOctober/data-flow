package vn.com.vtcc.dataflow.flow.source.io.utils;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileGenerator {

    private String fileName;
    private BufferedReader in;
    private Lock lock;

    public FileGenerator(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.in = new BufferedReader(new FileReader(new File(fileName)));
        this.lock = new ReentrantLock();
    }

    public String getLine() throws IOException {
        this.lock.lock();
        String line = this.in.readLine();
        if (line == null) {
            this.in.close();
        }
        this.lock.unlock();
        return line;
    }

    public void close() throws IOException {
        this.lock.lock();
        this.in.close();
        this.lock.unlock();
    }
}
