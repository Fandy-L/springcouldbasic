package com.to8to.tbt.msc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Slf4j
@Configuration
public class ElasticsearchConfiguration
{
    @Value(value = "${es.clusterName}")
    private String clusterName;

    @Value(value = "${es.inetAddress}")
    private String inetAddress;

    private Map<String, Integer> hosts = new HashMap<>();

    @PostConstruct
    public void initialize()
    {
        initHosts();
    }

    @Bean
    public TransportClient getTransportClient()
    {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName)
                .build();
        TransportClient client = new TransportClient(settings);
        for (String ip : hosts.keySet())
        {
            client.addTransportAddress(new InetSocketTransportAddress(ip, hosts.get(ip)));
        }
        log.info("ElasticsearchConfiguration.getTransportClient clusterName:{} hosts:{}", clusterName, hosts);
        return client;
    }

    private void initHosts()
    {
        String[] addressList = inetAddress.split(" ");
        for (String address : addressList)
        {
            String[] hostInfo = address.split(":");
            hosts.put(hostInfo[0], Integer.valueOf(hostInfo[1]));
        }
    }
}
