package com.tinet.vlink.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tinet.escore.utils.PropertiesLoader;

@Service
public class VlinkESFactory {

	private static JestClient client;

	/**
	 * ES 集群
	 * 
	 * @return
	 */
	public synchronized JestClient getClient() {
		if (client == null) {
			JestClientFactory factory = new JestClientFactory();
			Set<String> serverUris = new HashSet<String>();
			PropertiesLoader pl = new PropertiesLoader("conf/elasticsearch.properties","elasticsearch.properties");
			int esNodesCount = pl.getInteger("es.nodes.count");
			for(int i=1;i<esNodesCount+1;i++){
				String esNode=pl.getProperty("es.nodes."+i);
				serverUris.add(esNode);
			}
			factory.setHttpClientConfig(new HttpClientConfig.Builder(serverUris)
					.multiThreaded(true).connTimeout(50000).readTimeout(500000).build());
			client = factory.getObject();
		}
		return client;
	}

}