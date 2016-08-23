package com.tinet.escore.service;

import io.searchbox.client.JestResult;

public interface ElasticSearchService {

	/**
	 * 创建索引
	 * @param indexName      索引名称
	 * @param numOfShards    分片数
	 * @param numOfReplicas  副本数
	 * @return
	 */
	public JestResult createIndex(String indexName,int numOfShards,int numOfReplicas);
	
	/**
	 * 删除映射
	 * @param indexName 索引名称
	 * @param typeName 类型名称
	 * @return
	 * esasticSearch2.0中，mapping不再支持删除，官方文档中原话是:
	 * In 1.x it was possible to delete a type mapping, along with all of the documents of that type, 
	 * using the delete mapping API. This is no longer supported, because remnants of the fields in the type could remain in the index,
	 *  causing corruption later on.
		Instead, if you need to delete a type mapping, 
        you should reindex to a new index which does not contain the mapping. 
        If you just need to delete the documents that belong to that type, 
        then use the delete-by-query plugin instead
	 */
	//public JestResult deleteMapping(String indexName,String typeName);
	
	/**
	 * 删除索引
	 * @param indexName 索引名称
	 * @param typeName 类型名称
	 * @return
	 */
	public JestResult deleteIndex(String indexName,String typeName);
	
	
	/**
	 * 创建索引映射mapping
	 * @param indexName  索引名
	 * @param typeName   类型名
	 * @param jsonFile   json格式的映射文件（全路径）
	 * @return
	 */
	public JestResult createIndexMapping(String indexName, String typeName,String jsonFile);
	

	/**
	 * 创建索引模版
	 * @param templateName
	 * @param jsonFile
	 * @return
	 */
	public JestResult createIndexTemplate(String templateName,String jsonFile);
	
	/**
	 * 批量删除文档
	 * @param ids  id
	 * @param indexName
	 * @param typeName
	 * @return
	 */
	public int deleteDocuments(String[] ids, String indexName, String typeName);
	

}
