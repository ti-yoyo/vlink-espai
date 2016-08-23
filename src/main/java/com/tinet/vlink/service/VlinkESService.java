/**
 * 
 */
package com.tinet.vlink.service;

import io.searchbox.client.JestResult;

import java.util.List;

import com.tinet.escore.service.ElasticSearchService;
import com.tinet.vlink.model.CdrIb;
import com.tinet.vlink.model.CdrOb;
import com.tinet.vlink.vo.CdrIbResponse;
import com.tinet.vlink.vo.CdrObResponse;
import com.tinet.vlink.vo.ConditionsIb;
import com.tinet.vlink.vo.ConditionsOb;

/**
 * @author wangguiyu
 *
 */
public interface VlinkESService extends ElasticSearchService {

	/**
	 * 批量添加文档
	 * @param lcs
	 * @param indexName
	 * @param typeName
	 */
	//public void indexDocuments(List<?> objs, String indexName,String typeName);

	/**
	 * 按天批量索引呼出数据
	 * @param cdrObs
	 */
	public boolean indexCdrObDocuments(List<CdrOb> cdrObs);

	/**
	 * 索引一条数据
	 * @param obj
	 * @param indexName
	 * @param typeName
	 * @return  201 索引文档成功    200 文档已存在
	 */
	//public int indexOneDocument(Object obj,String indexName,String typeName,String id);

	/**
	 * 呼出查询
	 * @param enterpriseId    企业编号
	 * @param startNo    起始页
	 * @param pageSize   每页大小
	 * @param startDate  YYYY-MM-dd
	 * @param endDate    YYYY-MM-dd
	 * @return
	 */
	public CdrObResponse searchCdrOb(String enterpriseId,int startNo,int pageSize,String startDate,String endDate,ConditionsOb c);

	/**
	 * 呼入查询
	 * @param enterpriseId    企业编号
	 * @param startNo    起始页
	 * @param pageSize   每页大小
	 * @param startDate  YYYY-MM-dd
	 * @param endDate    YYYY-MM-dd
	 * @return
	 */
	public CdrIbResponse searchCdrIb(String enterpriseId,int startNo,int pageSize,String startDate,String endDate,ConditionsIb c);

	/**
	 * 动态条件搜索
	 * @param c  条件
	 * @return
	 */
	//public  CdrObResponse searchCdrOb(Condition c);

	/**
	 * 指定日期范围批量删除索引,
	 * @param prefixIndexName  索引名称前缀
	 * @param startDate  起始日期(也删除此起始日的)
	 * @param endDate    截止日期(也删除此截止日的)
	 * @return
	 */
	public  JestResult deleteIndex(String prefixIndexName,String typeName,String startDate,String endDate);

	/**
	 * 按天批量索引呼入数据
	 * @param cdrIbs
	 */
	public boolean indexCdrIbDocuments(List<CdrIb> cdrIbs);

	/**
	 * 更新呼入详细记录
	 * @param enterpriseId
	 * @param mainUniqueId 主表相应的字段值
	 * @param uniqueId   要更新的子表cdr_ib_detail相应字段值
	 * @param sipCause   要更新的值
	 * @param startDate  对应主表cdr_ib的相应字段值,主叫用户进入系统的时间
	 * @param indexName
	 * @param typeName
	 * @return
	 */
	//public boolean updateCdrIbDetail(String enterpriseId,String mainUniqueId,String uniqueId,int sipCause, String startDate);

	/**
	 * 新增呼入详细记录
	 * @param enterpriseId
	 * @param mainUniqueId 主表相应的字段值
	 * @param cdrIbDetail  新增的detail
	 * @param startDate    对应主表cdr_ib的相应字段,主叫用户进入系统的时间
	 * @param indexName
	 * @param typeName
	 * @return
	 */
	//public boolean addCdrIbDetail(String enterpriseId,String mainUniqueId,CdrIbDetail cdrIbDetail, String startDate);

	/**
	 * 更新呼出详细记录
	 * @param enterpriseId
	 * @param mainUniqueId 主表相应的字段值
	 * @param uniqueId   要更新的子表cdr_ob_detail相应字段值
	 * @param sipCause   要更新的值
	 * @param startDate  对应主表cdr_ob的相应字段值，会话开始时间
	 * @param indexName
	 * @param typeName
	 * @return
	 */
	//public boolean updateCdrObDetail(String enterpriseId,String mainUniqueId,String uniqueId,int sipCause, String startDate);

	/**
	 * 
	 * @param enterpriseId
	 * @param mainUniqueId 主表相应的字段值
	 * @param sipCause     要更新的值
	 * @param startDate    对应主表cdr_ob的相应字段值，会话开始时间
	 * @param indexName
	 * @param typeName
	 * @return
	 */
	public boolean updateCdrOb(String enterpriseId,String mainUniqueId,int sipCause, String startDate);

	/**
	 * 新增呼出详细记录
	 * @param enterpriseId
	 * @param mainUniqueId 主表相应的字段值
	 * @param cdrIbDetail  新增的detail
	 * @param startDate    对应主表cdr_ob的相应字段值,会话开始时间
	 * @param indexName
	 * @param typeName
	 * @return
	 */
	//public boolean addCdrObDetail(String enterpriseId,String mainUniqueId,CdrObDetail cdrObDetail, String startDate);

	/**
	 * 批量查询呼入CdrIbDetail
	 * @param enterpriseId
	 * @param startNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 */
	//public CdrIbResponse searchCdrIbDetail(String enterpriseId,int startNo,int pageSize,String startDate,String endDate);

	/**
	 * 批量查询呼出CdrObDetail
	 * @param enterpriseId    企业编号
	 * @param startNo    起始页
	 * @param pageSize   每页大小
	 * @param startDate  YYYY-MM-dd
	 * @param endDate    YYYY-MM-dd
	 * @return
	 */
	//public CdrObResponse searchCdrObDetail(String enterpriseId,int startNo,int pageSize,String startDate,String endDate);

}
