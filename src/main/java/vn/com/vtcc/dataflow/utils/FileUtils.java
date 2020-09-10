package vn.com.vtcc.dataflow.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FileUtils {

    public static Properties readPropertiesFile(String filePath) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader(filePath));
        return props;
    }
}
