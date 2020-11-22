package vn.com.vtcc.pluto.core.utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FileUtils {

    public static Properties readPropertiesFile(String filePath) throws IOException {
        Properties props = new Properties();
        props.load(new FileReader(filePath));
        return props;
    }

    public static JSONObject readJsonFile(String filePath) throws IOException {
        FileInputStream fis = null;
        String data = null;
        try {
            fis = new FileInputStream(filePath);
            data = IOUtils.toString(new FileInputStream(filePath), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        if (data != null) {
            return new JSONObject(data);
        }
        return null;
    }
}
