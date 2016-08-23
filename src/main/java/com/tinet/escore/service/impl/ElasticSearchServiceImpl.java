package com.tinet.escore.service.impl;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Delete;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.template.PutTemplate;

import java.io.IOException;

import org.elasticsearch.common.settings.Settings;

import com.tinet.escore.service.ElasticSearchService;
import com.tinet.escore.utils.ElasticSearchUtils;
import com.tinet.vlink.service.VlinkESFactory;


public class ElasticSearchServiceImpl implements ElasticSearchService {

	private JestClient client;

	public ElasticSearchServiceImpl() {

	}
	
	public ElasticSearchServiceImpl(VlinkESFactory vlinkFactory) {
		super();
		this.client = vlinkFactory.getClient();
	}

	public JestClient getClient(){
		return this.client;
	}

	/**
	 * 创建索引
	 * 
	 * @param indexName
	 *            索引名称
	 * @param numOfShards
	 *            分片数
	 * @param numOfReplicas
	 *            副本数
	 * @return
	 */
	public JestResult createIndex(String indexName, int numOfShards,
			int numOfReplicas) {
		Settings.Builder settingsBuilder = Settings.settingsBuilder();
		settingsBuilder.put("number_of_shards", numOfShards);
		settingsBuilder.put("number_of_replicas", numOfReplicas);
		JestResult jr = null;
		try {
			jr = client.execute(new CreateIndex.Builder(indexName).settings(
					settingsBuilder.build().getAsMap()).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jr;
	}

	/**
	 * 创建索引
	 * 
	 * @param indexName
	 *            索引模版名称
	 * @return
	 */
	public JestResult createIndexTemplate(String templateName, String jsonFile) {
		JestResult jr = null;
		try {
			jr = client.execute(new PutTemplate.Builder(templateName,
					ElasticSearchUtils.getJson(jsonFile)).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jr;
	}

	/**
	 * 删除索引
	 * 
	 * @param indexName
	 * @return
	 */
	public JestResult deleteIndex(String indexName, String typeName) {
		JestResult jr = null;
		try {
			// if(typeName!=null&&!typeName.isEmpty()){
			jr = client.execute(new DeleteIndex.Builder(indexName).type(
					typeName).build());
			// }else{
			// jr = clients.execute(new DeleteIndex.Builder(indexName).build());
			// System.out.println("deleteIndex------result:"+jr.getJsonString());
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jr;
	}

	/**
	 * 创建索引映射mapping
	 * 
	 * @param indexName
	 * @return
	 */
	public JestResult createIndexMapping(String indexName, String typeName,
			String jsonFile) {
		JestResult jr = null;
		try {
			PutMapping putMapping = new PutMapping.Builder(indexName, typeName,ElasticSearchUtils.getJson(jsonFile)).build();
			jr = client.execute(putMapping);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jr;
	}

	public int deleteDocuments(String[] ids, String indexName, String typeName) {
		/*
		 * Bulk bulk = new Bulk.Builder() .defaultIndex(indexName)
		 * .defaultType(typeName) .addAction(new
		 * Delete.Builder("1").index("twitter").type("tweet").build()) .build();
		 * clients.execute(bulk);
		 */
		// -------------------------------
		BulkResult br=null;
		int result=0;
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		for (int i = 0; i < ids.length; i++) {
			Delete delete = new Delete.Builder(ids[i]).index(indexName).type(typeName).build();
			bulkBuilder.addAction(delete);
		}
		try {
			br=client.execute(bulkBuilder.build());
			result=br.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// client.shutdownClient();
		}
		return result;
	}

	// -------------------------------------------------------------------------------------

	/**
	 * 删除映射
	 */
	/*
	 * @Override public JestResult deleteMapping(String indexName, String
	 * typeName) { JestResult jr=null; DeleteMapping deleteMapping = new
	 * DeleteMapping.Builder( indexName, typeName ).build(); try {
	 * jr=clients.execute(deleteMapping); } catch (IOException e) {
	 * e.printStackTrace(); } return jr; }
	 */
	
	public static void main(String[] args) {

	}

}
