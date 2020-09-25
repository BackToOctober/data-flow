package vn.com.vtcc.dataflow.dataSource.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

public class ESConnectorFactory implements Serializable {

    private final String esHost;
    private final int esConnectionRequestTimeout;
    private final int esConnectTimeOut;
    private final int esSocketTimeOut;

    public ESConnectorFactory(Properties config) {
        this.esHost = config.getProperty("elasticsearch.host");
        this.esConnectionRequestTimeout = Integer.parseInt(
                config.getProperty("elasticsearch.connection.request.timeout", "50000"));
        this.esConnectTimeOut = Integer.parseInt(
                config.getProperty("elasticsearch.connection.request.timeout", "50000"));
        this.esSocketTimeOut = Integer.parseInt(
                config.getProperty("elasticsearch.socket.timeout", "50000"));
    }

    public RestHighLevelClient createConnect(){
        ArrayList<HttpHost> httpEsHost = new ArrayList<HttpHost>();
        String[] listHost = this.esHost.split(",");
        for(String h : listHost){
            String[] e = h.split(":");
            httpEsHost.add(new HttpHost(e[0], Integer.parseInt(e[1]), "http"));
        }
        return new RestHighLevelClient(RestClient.builder(httpEsHost.toArray(new HttpHost[0])).setRequestConfigCallback(
                new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(
                            RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder
                                .setConnectionRequestTimeout(esConnectionRequestTimeout)
                                .setConnectTimeout(esConnectTimeOut)
                                .setSocketTimeout(esSocketTimeOut);
                    }
                }));
    }
}
