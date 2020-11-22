package vn.com.vtcc.pluto.core.monitor.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.client.RestHighLevelClient;
import vn.com.vtcc.pluto.core.monitor.Metric;
import vn.com.vtcc.pluto.core.storage.elasticSearch.ESConnectorFactory;
import vn.com.vtcc.pluto.core.storage.elasticSearch.ESUtils;
import vn.com.vtcc.pluto.core.utils.JacksonMapper;

import java.io.IOException;

public class MonitorClient {

    private ESConnectorFactory esConnectorFactory;
    private RestHighLevelClient client;
    private String esIndex;

    public MonitorClient(ESConnectorFactory factory, String esIndex) {
        this.esConnectorFactory = factory;
        this.esIndex = esIndex;
    }

    public MonitorClient setESConnector(ESConnectorFactory esConnectorFactory) {
        this.esConnectorFactory = esConnectorFactory;
        return this;
    }

    public MonitorClient setESIndex(String esIndex) {
        this.esIndex = esIndex;
        return this;
    }

    public void reConnect() throws IOException {
        if (this.client != null) {
            this.client.close();
        }
        this.client = this.esConnectorFactory.createConnect();
    }

    public void Send(Metric msg) throws IOException {
        try {
            ESUtils.putData(this.client, this.esIndex ,JacksonMapper.parseToString(msg.getValue()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            this.reConnect();
            ESUtils.putData(this.client, this.esIndex ,JacksonMapper.parseToString(msg.getValue()));
        }
    }
}
