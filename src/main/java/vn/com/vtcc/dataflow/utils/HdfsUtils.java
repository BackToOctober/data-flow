package vn.com.vtcc.dataflow.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HdfsUtils {

    private static final Logger logger = LogManager.getLogger(HdfsUtils.class);

    private static class FileSystemBuilder {
        private String coreSitePath;
        private String hdfsSitePath;

        public FileSystemBuilder setCoreSite(String coreSitePath) {
            this.coreSitePath = coreSitePath;
            return this;
        }

        public FileSystemBuilder setHdfsSite(String hdfsSitePath) {
            this.hdfsSitePath = hdfsSitePath;
            return this;
        }

        public FileSystem init() throws IOException {
            Configuration conf = new Configuration();
            conf.addResource(this.coreSitePath);
            conf.addResource(this.hdfsSitePath);
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            return FileSystem.get(conf);
        }
    }

    public static FileSystemBuilder builder() {
        return new HdfsUtils.FileSystemBuilder();
    }

    public static boolean exists(String path, FileSystem fs){
        try {
            return fs.exists(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createFolder(FileSystem fs, String path){
        try {
            return fs.mkdirs(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void safeMove(String from, String target, FileSystem fs){
        try {
            if(fs.exists(new Path(target))){
                fs.delete(new Path(target), true);
            }
            fs.rename(new Path(from), new Path(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
