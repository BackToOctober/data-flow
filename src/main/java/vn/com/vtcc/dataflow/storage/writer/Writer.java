package vn.com.vtcc.dataflow.storage.writer;

import java.io.IOException;

public interface Writer {

    public void openWriter() throws IOException;

    public void close() throws IOException;

    public void write(String data) throws IOException;
}
