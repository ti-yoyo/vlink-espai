/**
 * 
 */
package com.tinet.vlink.service.impl;

import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.Update;
import io.searchbox.core.search.aggregation.SumAggregation;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tinet.escore.service.impl.ElasticSearchServiceImpl;
import com.tinet.escore.utils.DateUtil;
import com.tinet.vlink.model.CdrIb;
import com.tinet.vlink.model.CdrOb;
import com.tinet.vlink.service.VlinkESFactory;
import com.tinet.vlink.service.VlinkESService;
import com.tinet.vlink.utils.VlinkUtils;
import com.tinet.vlink.vo.CdrIbResponse;
import com.tinet.vlink.vo.CdrObResponse;
import com.tinet.vlink.vo.ConditionsIb;
import com.tinet.vlink.vo.ConditionsOb;

/**
 * @author wangguiyu
 *
 */
@Service("vlinkESService")
public class VlinkESServiceImpl extends ElasticSearchServiceImpl implements VlinkESService {

	public VlinkESServiceImpl() {
		
	}

	@Autowired
	public VlinkESServiceImpl(@Qualifier("vlinkESFactory")VlinkESFactory vlinkESFactory) {
		super(vlinkESFactory);
	}

	/**
	 * 批量索引文档至一个索引
	 * 
	 * @param lcs
	 * @param indexName
	 * @param typeName
	 */
/*	public void indexDocuments(List<?> objs, String indexName,String typeName) {
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		for (int i = 0; i < objs.size(); i++) {
			Object obj = objs.get(i);
			Index index = null;
			if (obj instanceof CdrOb) {
				String id = ((CdrOb) obj).getId().toString();
				index = new Index.Builder(obj).index(indexName).type(typeName).id(id).build();// 自增id
			} else {
				index = new Index.Builder(obj).index(indexName).type(typeName)
						.build();
			}
			bulkBuilder.addAction(index);
		}
		try {
			this.getClient().execute(bulkBuilder.build());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// client.shutdownClient();
		}
	}*/

	/**
	 * 按天批量索引文档
	 * 
	 * @param lcs
	 */
	public boolean indexCdrObDocuments(List<CdrOb> cdrobs) {
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		for (int i = 0; i < cdrobs.size(); i++) {
			CdrOb cdrob = cdrobs.get(i);
			Index index = null;
			String id = cdrob.getUniqueId();
			long startTime = cdrob.getStartTime();
			//cdrob.setEnterpriseId(enterpriseId);
			if(cdrob.getStatus()!=null&&cdrob.getBillDuration()!=null&&cdrob.getBridgeDuration()!=null){
				cdrob.setBillDuration2(VlinkUtils.calculateBillDuration(cdrob.getStatus(),cdrob.getBillDuration(),VlinkUtils.CALL_TYPE_OB));
				cdrob.setBridgeDuration2(VlinkUtils.calculateBridgeDuration(cdrob.getStatus(),cdrob.getBridgeDuration(),VlinkUtils.CALL_TYPE_OB));
			}else{
				cdrob.setBillDuration2(0);
				cdrob.setBridgeDuration2(0);
			}
			String date = DateUtil.format(String.valueOf(startTime),DateUtil.FMT_DATE_YYYY_MM_DD);
			index = new Index.Builder(cdrob).index("vlink-cdrob"+"-" + date).type("cdr").id(id).build();// 自增id
			bulkBuilder.addAction(index);
		}
		BulkResult br = null;
		try {
			br=this.getClient().execute(bulkBuilder.build());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// client.shutdownClient();
		}	
		return br==null?false:br.isSucceeded();
	}

	/**
	 * 按天批量索引文档
	 * 
	 * @param lcs
	 * @param typeName
	 */
	public boolean indexCdrIbDocuments(List<CdrIb> cdribs) {
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		for (int i = 0; i < cdribs.size(); i++) {
			CdrIb cdrib = cdribs.get(i);
			Index index = null;
			String id = cdrib.getUniqueId();
			long startTime = cdrib.getStartTime();
			if(cdrib.getStatus()!=null&&cdrib.getBillDuration()!=null&&cdrib.getBridgeDuration()!=null){
				cdrib.setBillDuration2(VlinkUtils.calculateBillDuration(cdrib.getStatus(),cdrib.getBillDuration(),VlinkUtils.CALL_TYPE_IB));
				cdrib.setBridgeDuration2(VlinkUtils.calculateBridgeDuration(cdrib.getStatus(),cdrib.getBridgeDuration(),VlinkUtils.CALL_TYPE_IB));
			}else{
				cdrib.setBillDuration2(0);
				cdrib.setBridgeDuration2(0);
			}
			
			String date = DateUtil.format(String.valueOf(startTime),DateUtil.FMT_DATE_YYYY_MM_DD);
			index = new Index.Builder(cdrib).index("vlink-cdrib"+"-" + date).type("cdr").id(id).build();// 自增id
			bulkBuilder.addAction(index);
		}
		BulkResult br = null;
		try {
			br=this.getClient().execute(bulkBuilder.build());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// client.shutdownClient();
		}
		
		return br==null?false:br.isSucceeded();
	}

	/**
	 * 索引一条数据
	 * 
	 * @param obj
	 * @param indexName
	 * @param typeName
	 * @param id
	 *            自增id
	 */
/*	public int indexOneDocument(Object obj, String indexName, String typeName,String id) {
		Index index = new Index.Builder(obj).index(indexName).type(typeName).id(id).build();
		DocumentResult dr = null;
		int responseCode = 0;
		try {
			dr = this.getClient().execute(index);
			// String isCreated=dr.getJsonObject().get("created").toString();
			responseCode = dr.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseCode; // responseCode =201 创建成功,此时isCreated=true;
		// boolean b=dr.isSucceeded();
	}*/

	/**
	 * 
	 * @param startNo    起始页
	 * @param pageSize   每页大小
	 * @param startDate  YYYY-MM-dd
	 * @param endDate    YYYY-MM-dd
	 * @param enterpriseId 企业编号
	 * @return
	 */
	public CdrObResponse searchCdrOb(String enterpriseId,int startNo,int pageSize,String startDate,String endDate,ConditionsOb c) {
		int iStartDate=DateUtil.getTimeStemp(startDate,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
/*		Date dd=null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd=DateUtil.format(dd,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
		int iEndDate=DateUtil.getTimeStemp(sd,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);*/
		int iEndDate=DateUtil.getTimeStemp(endDate,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
		String query = null;
		CdrObResponse  cdrvos = new CdrObResponse();
		 query ="{"
			  +"\"query\": {"
			   +" \"bool\": {"
			   +"   \"must\": ["
			   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},"
			   +"    {"
			   +"      \"range\": {"
			   +"        \"startTime\": {"
			   +"           \"gte\":"+iStartDate+","
			   +"           \"lt\":"+iEndDate
			   +"         }"
			   +"       }"
			   +"     }";
			   //------------------------
		 	   String temp=",";
			   String uniqueId= c.getUniqueId();
			   String requestUniqueId = c.getRequestUniqueId();
			   String filterRetry=c.getFilterRetry();
			   Integer status=c.getStatus();  
			   if(uniqueId!=null&&!"".equals(uniqueId)){//如果uniqueId不为空，则其余参数无效
				   temp=temp+"{\"term\":{\"uniqueId\":\""+uniqueId+"\"}},";
			   }else if(requestUniqueId!=null&&!"".equals(requestUniqueId)){//如果requestUniqueId不为空，则其余参数无效
				   temp=temp+"{\"term\":{\"requestUniqueId\":\""+requestUniqueId+"\"}},";
			   }else{
				   Integer callType=c.getCallType();
				   String title=c.getTitle();
				   String titleValue=c.getTitleValue();
				   //Integer mark = c.getMark();
				   String province = c.getProvince();//
				   String city = c.getCity();//
				   
				   String userField = c.getUserField();
				   
				   if(userField!=null&&!"".equals(userField)){ 
					   temp=temp+"{\"term\":{\"userField\":\""+userField+"\"}},";
				   }
				   
				   //为空表示全部 2.webcall
				   if(callType!=null){
					   if(callType == 2){
						   temp=temp+"{\"term\":{\"callType\":\""+callType+"\"}},";
					   }
				   }
				   
				   //callee_number 呼转号码,customer_number客户号码,clid外显号码
				   if(title!=null&&!"".equals(title)&&titleValue!=null&&!"".equals(titleValue)){
					  if(title.equals("customer_number")){
						   temp=temp+"{\"term\":{\"customerNumber\":\""+titleValue+"\"}},";
					   }else if(title.equals("clid")){
						   temp=temp+"{\"term\":{\"clid\":\""+titleValue+"\"}},";
					   }else if(title.equals("callee_number")){
						   temp=temp+"{\"term\":{\"calleeNumber\":\""+titleValue+"\"}},";
					   }
				   }
				   
				   //1.留言2.AMD 4按键 8这次呼叫属于试呼叫
/*				   if(mark!=null){				   
					   if(mark != 0){
						   temp=temp+"{\"term\":{\"mark\":\""+mark+"\"}},";
					   }
				   }*/
				   
				   //为空表示全部，21:webcall未接通 22:webcall已接通 23:webcall已呼转24:webcall呼转已桥接
				   if(filterRetry!=null&&"false".equals(filterRetry)){//不过滤
					   if(status!=null&&(status>20 && status<25)){			
							   temp=temp+"{\"term\":{\"status\":\""+status+"\"}},";					   
					   }
				   }
				   
				   if(filterRetry!=null&&"true".equals(filterRetry)){//过虑
					   if(status!=null&&status == 21){//webcall未接通
						   temp=temp+"{\"term\":{\"status\":\""+status+"\"}},";		
						   
					   }else if(status!=null&&status >=22 ){//webcall已接通
						   temp=temp+"{\"term\":{\"status\":\""+status+"\"}},";		
					   }
					   
				   }
				   
				   if(province!=null&&!"".equals(province)){
					   temp=temp+"{\"wildcard\":{\"customerProvince\":\""+"*"+province+"*"+"\"}},"; 
				   }
				   
				   if(city!=null&&!"".equals(city)){
					   temp=temp+"{\"wildcard\":{\"customerCity\":\""+"*"+city+"*"+"\"}},"; 
				   } 
			   }
			   temp=temp.substring(0, temp.length()-1);//删去最后一个逗号
			   //-----------------------------------------------
			   query=query+temp   
			   +"   ],"
			   +"   \"must_not\": [";
			   
			   //----------------------是否过滤重试记录,  true:过滤 false:不过滤 默认为false
			   temp="";
			   if(filterRetry!=null&&"true".equals(filterRetry)){
				   if(status!=null&&status == 21){//webcall未接通
					   temp=temp+"{\"term\":{\"mark\":\"8\"}}";
				   }
				   
				   if(status == null){
					   temp = temp + 
					   "{"
							+"\"bool\": {"
							+"	\"must\": ["
							+"		{"
							+"			\"term\": {"
							+"				\"status\": 21"
							+"			}"
							+"		},"
							//+"	],"
							//+"	\"must_not\": ["
							+"		{"
							+"			\"term\": {"
							+"				\"mark\": 8"
							+"			}"
							+"		}"
							+"	]"
							+"}"
							+"}"
			/*				+ ", {"
							+"\"bool\": {"
							+"	\"must\": ["
							+"		{"
							+"			\"range\": {"
							+"				\"status\": {"
							+"					\"gte\": 22"
							+"				}"
							+"			}"
							+"		}"
							+"	]"
							+"}"
							+"}"*/
							;
					   
				   }
				   
			   }
			   
			   //-----------------------------------------------
			   
			 query=query+temp
			   + "],"
			   +"   \"should\": ["
			//-------------------------------------------------
	/*		   temp = "";
			   if(filterRetry!=null&&"true".equals(filterRetry)){//过滤

			   };
			 
			   query=query+temp*/
		    //---------------------------------------------------
			   + "]"
			   +" }"
			   +"},"
			   +" \"from\":"+startNo+","
			   +"\"size\":"+pageSize+","
			   +"\"sort\": ["
			   +" {"
			   +"   \"startTime\": \"desc\""
			   +" }"
			   +"],"
			   +"\"aggs\": {"
			   +" \"billDurations\": {"
			   +"   \"sum\": {"
			   +"\"field\": \"billDuration2\""
			   +"   }"
			   +" },"
			   +" \"bridgeDurations\": {"
			   +"   \"sum\": {"
			   +"\"field\": \"bridgeDuration2\""
			   +"   }"
			   +" }"
			   +"}"
			   +"}";
		    Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrob","alias",startDate,endDate);
		    //-------------------------
		    //IndicesExists ie=new IndicesExists.Builder(indexs).ignoreUnavailable(true).build();
		    
		    //-------------------------
		    
		    
			Search search = new Search.Builder(query).addIndex(indexs).ignoreUnavailable(true).build();
			//Search search = new Search.Builder(query).build();
			try {
				SearchResult result = this.getClient().execute(search);
				if (result == null) {
					System.out.println("null!");
				} else {
			        //long s = System.currentTimeMillis();
					List<SearchResult.Hit<CdrOb, Void>> hits = result.getHits(CdrOb.class);
					int totalnum=result.getTotal();
					SumAggregation saBillD=result.getAggregations().getSumAggregation("billDurations");
					SumAggregation saBidgeD=result.getAggregations().getSumAggregation("bridgeDurations");
					
					Double   dBillDurations=saBillD==null?0:saBillD.getSum();
					Double   dBidgeDurations=saBidgeD==null?0:saBidgeD.getSum();
					
					
				    cdrvos.setPageSize(pageSize);
					cdrvos.setStartNo(startNo);
					cdrvos.setTotalNum(totalnum);
					
					cdrvos.setBillDurationsTotal(Math.round(dBillDurations));
					cdrvos.setBridgeDurationsTotal(Math.round(dBidgeDurations));
					
					cdrvos.setEnterpriseId(enterpriseId);
					cdrvos.setStartDate(startDate);
					cdrvos.setEndDate(endDate);
					
					//long e = System.currentTimeMillis();
				    //System.out.print("e-s="+(e-s));
					//int size = hits.size();
					//System.out.println("\n size----" + size+"\n"+result.getJsonString()+"--totalnum--"+totalnum+"\n");
					List<CdrOb> cdrs = new ArrayList<CdrOb>();
					for(Hit<CdrOb, Void> cdr:hits){
						//System.out.println(cdr.source.toString());
						cdrs.add(cdr.source);
					}
					cdrvos.setCdrs(cdrs);//*****
				}
				
			}catch (SocketException e){
				e.printStackTrace();
			}
			catch (IOException e) {

				e.printStackTrace();
			} 
		
		return cdrvos;
	}

	/**
	 * 动态条件搜索
	 * @param c
	 * @return
	 */
/*	public  CdrObResponse searchCdrOb(Condition c){
		List<Map<String, Object>> ms=c.getConditions();
	    //StringBuilder sb = new StringBuilder();
		String templatedQuery = "";
		templatedQuery="{\"file\": \"vlink_cdr_ob_search_template\","+
		"\"params\": {";
		for(Map<String, Object> m:ms){
			
			if(m.get("name").equals("appId")){//企业id
				templatedQuery=templatedQuery+getJsonString("enterpriseId",m.get("value"));
			}else if(m.get("name").equals("startTime")){
				templatedQuery=templatedQuery+getJsonString("startTime",m.get("start"),m.get("end"));
			}
			
		}
		
		templatedQuery=templatedQuery.substring(0, templatedQuery.length()-1);//去掉最后 一个逗号
		templatedQuery=templatedQuery+"}"+"}";
		
		
		CdrObResponse  cdrvos = new CdrObResponse();
		Search search = new Search.TemplateBuilder(templatedQuery).build();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
		        //long s = System.currentTimeMillis();
				List<SearchResult.Hit<CdrOb, Void>> hits = result.getHits(CdrOb.class);
				int totalnum=result.getTotal();
				Double   dBillDurations=result.getAggregations().getSumAggregation("billDurations").getSum();
				Double   dBidgeDurations=result.getAggregations().getSumAggregation("bridgeDurations").getSum();
				
			    //cdrvos.setPageSize(pageSize);
				//cdrvos.setStartNo(startNo);
				cdrvos.setTotalNum(totalnum);
				
				cdrvos.setBillDurationsTotal(Math.round(dBillDurations));
				cdrvos.setBridgeDurationsTotal(Math.round(dBidgeDurations));
				
				List<CdrOb> cdrs = new ArrayList<CdrOb>();
				for(Hit<CdrOb, Void> cdr:hits){
					//System.out.println(cdr.source.toString());
					cdrs.add(cdr.source);
				}
				cdrvos.setCdrs(cdrs);//*****
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return cdrvos;
	}*/

	//----------------------------------------------------------------
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
/*	private String getJsonString(String name,Object value){
		return "\""+name+"\":"+"\""+value.toString()+"\""+",";
	}*/

	/**
	 * 
	 * @param name
	 * @param start
	 * @param end
	 * @return
	 */
/*	private String getJsonString(String name,Object start,Object end){
		String ret="";
		ret = "\""+name+"\":{\"start\":"+start.toString();
		if(end!=null){
			ret = ret+","+"\"end\":"+end.toString();
		}
	    ret=ret+"},";
	    return ret;
	}*/

	@Override
	public JestResult deleteIndex(String prefixIndexName,String typeName,String startDate,
			String endDate) {
		JestResult jr = null;
		try {
			 Set<String> indexs = VlinkUtils.generateIndexs(prefixIndexName,"alias",startDate,endDate);
			 for(String indexName:indexs){
				 jr = this.getClient().execute(new IndicesExists.Builder(indexName).ignoreUnavailable(true).build());
				 if(jr.getResponseCode()==200)
					 jr = this.getClient().execute(new DeleteIndex.Builder(indexName).type(typeName).build());
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jr;
	}

	@Override
	public CdrIbResponse searchCdrIb(String enterpriseId, int startNo,
			int pageSize, String startDate, String endDate,ConditionsIb c) {
		int iStartDate=DateUtil.getTimeStemp(startDate,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
/*		Date dd=null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd=DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
		int iEndDate=DateUtil.getTimeStemp(sd,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);*/
		int iEndDate=DateUtil.getTimeStemp(endDate,DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
		String query = null;
		CdrIbResponse  cdrvos = new CdrIbResponse();
		 query ="{"
			  +"\"query\": {"
			   +" \"bool\": {"
			   +"   \"must\": ["
			   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},"
			   +"    {"
			   +"      \"range\": {"
			   +"        \"startTime\": {"
			   +"           \"gte\":"+iStartDate+","
			   +"           \"lt\":"+iEndDate
			   +"         }"
			   +"       }"
			   +"     }";
			   //------------------------
		 	   String temp=",";
			   String uniqueId= c.getUniqueId();
			   if(uniqueId!=null&&!"".equals(uniqueId)){//如果uniqueId不为空，则其余参数无效
				   temp=temp+"{\"term\":{\"uniqueId\":\""+uniqueId+"\"}},";
			   }else{
				   String answerType=c.getAnswerType();
				   String title=c.getTitle();
				   String titleValue=c.getTitleValue();
				   Integer mark = c.getMark();
				   String province = c.getProvince();
				   String city = c.getCity();
				   String userField = c.getUserFiled();
				   
				   //answerType:telBridged呼转接听,telCalled已呼转未接听 ,systemAnswer系统接听,systemNoAnswer系统未接听
				   if(answerType!=null&&!"".equals(answerType)){
					   int status = 0;
					   if(answerType.equals("telBridged")){
						   status = 1;
					   }else if(answerType.equals("telCalled")){
						   status = 2;
					   }else if(answerType.equals("systemAnswer")){
						   status = 3;
					   }else if(answerType.equals("systemNoAnswer")){
						   status = 4;  
					   }
					   if(status!=0){
						   if(status == 4){
							   temp=temp+"{\"range\":{\"status\":{\"gte\":"+status+"}}},";
							   
						   }else{
							   temp=temp+"{\"term\":{\"status\":\""+status+"\"}},";
						   }
						   
					   }
				   }
				   //tsno电话组，customer_number客户电话，hotline热线号码，callee_number呼转号码
				   if(title!=null&&!"".equals(title)&&titleValue!=null&&!"".equals(titleValue)){
					   if(title.equals("tsno")){
						   temp=temp+"{\"wildcard\":{\"telsetName\":\""+"*"+titleValue+"*"+"\"}},"; 
					   }else if(title.equals("customer_number")){
						   temp=temp+"{\"term\":{\"customerNumber\":\""+titleValue+"\"}},";
					   }else if(title.equals("hotline")){
						   temp=temp+"{\"term\":{\"hotline\":\""+titleValue+"\"}},";
					   }else if(title.equals("callee_number")){
						   temp=temp+"{\"term\":{\"calleeNumber\":\""+titleValue+"\"}},";
					   }
				   }
				   
				   //mark 1.留言
				   if(mark!=null&&!"".equals(mark)){
					   if(mark == 1){
						   temp=temp+"{\"term\":{\"mark\":\""+mark+"\"}},";
					   }
				   }
				   
				   if(userField!=null&&!"".equals(userField)){ 
						   temp=temp+"{\"term\":{\"userField\":\""+userField+"\"}},";
				   }
				   
				   if(province!=null&&!"".equals(province)){
					   temp=temp+"{\"wildcard\":{\"customerProvince\":\""+"*"+province+"*"+"\"}},"; 
				   }
				   
				   if(city!=null&&!"".equals(city)){
					   temp=temp+"{\"wildcard\":{\"customerCity\":\""+"*"+city+"*"+"\"}},"; 
				   } 
			   }
			   
			   temp=temp.substring(0, temp.length()-1);//删去最后一个逗号
			   //------------------------
			   
			query=query+temp   
			   +"   ],"
			   +"   \"must_not\": [],"
			   +"   \"should\": []"
			   +" }"
			   +"},"
			   +" \"from\":"+startNo+","
			   +"\"size\":"+pageSize+","
			   +"\"sort\": ["
			   +" {"
			   +"   \"startTime\": \"desc\""
			   +" }"
			   +"],"
			   +"\"aggs\": {"
			   +" \"billDurations\": {"
			   +"   \"sum\": {"
			   +"\"field\": \"billDuration2\""
			   +"   }"
			   +" },"
			   +" \"bridgeDurations\": {"
			   +"   \"sum\": {"
			   +"\"field\": \"bridgeDuration2\""
			   +"   }"
			   +" }"
			   +"}"
			   +"}";
		    Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrib","alias",startDate,endDate);
		    //-------------------------
		    //IndicesExists ie=new IndicesExists.Builder(indexs).ignoreUnavailable(true).build();
		    //-------------------------
		    
			Search search = new Search.Builder(query).addIndex(indexs).ignoreUnavailable(true).build();
			//Search search = new Search.Builder(query).build();
			try {
				SearchResult result = this.getClient().execute(search);
				if (result == null) {
					System.out.println("null!");
				} else {
					List<SearchResult.Hit<CdrIb, Void>> hits = result.getHits(CdrIb.class);
					int totalnum=result.getTotal();
					SumAggregation saBillD=result.getAggregations().getSumAggregation("billDurations");
					SumAggregation saBidgeD=result.getAggregations().getSumAggregation("bridgeDurations");
					
					Double   dBillDurations=saBillD==null?0:saBillD.getSum();
					Double   dBidgeDurations=saBidgeD==null?0:saBidgeD.getSum();
					
				    cdrvos.setPageSize(pageSize);
					cdrvos.setStartNo(startNo);
					cdrvos.setTotalNum(totalnum);
					
					cdrvos.setBillDurationsTotal(Math.round(dBillDurations));
					cdrvos.setBridgeDurationsTotal(Math.round(dBidgeDurations));
					
					cdrvos.setEnterpriseId(enterpriseId);
					cdrvos.setStartDate(startDate);
					cdrvos.setEndDate(endDate);
					
					List<CdrIb> cdrs = new ArrayList<CdrIb>();
					for(Hit<CdrIb, Void> cdr:hits){
						cdrs.add(cdr.source);
					}
					cdrvos.setCdrs(cdrs);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		
		return cdrvos;	
	}

	/**
	 * 查询一条CdrIbDetail文档
	 * @param mainUniqueId   对应主表的uniqueId
	 * @param uniqueId       要修改的detail表uniqueId
	 * @param startTime  对应主表的startTime   YYYY-MM-dd
	 * @param indexName  
	 * @param typeName
	 * @param flag  0:新增  1:更新
	 * @return
	 */
/*	private CdrIb getCdrIbDetail(String enterpriseId,String mainUniqueId,String uniqueId,String startDate, String indexName,String typeName,int flag){
		String query="";
		if(flag==1){//更新
			query="{"+
			 "\"query\": {"+
			 " \"bool\": {"+
			 "   \"must\": ["+
			   "     {"+
			         " \"match\": {"+
			         "   \"enterpriseId\": \""+enterpriseId+"\""+
			         " }"+
			       " },"+
			        "{"+
			         " \"match\": {"+
			         "   \"uniqueId\": \""+mainUniqueId+"\""+
			         " }"+
			        "},"+
			        "{"+
			        "  \"nested\": {"+
			         "   \"path\": \"details\","+
			         "   \"query\": {"+
			          "    \"bool\": {"+
			          "      \"must\": ["+
			          "        {"+
			          "          \"match\": {"+
			          "             \"details.uniqueId\": \""+uniqueId+"\""+
			          "          }"+
			          "        }"+
			          "      ]"+
			          "    }"+
			          "  }"+
			          "}"+
			        "}"+
			      "]"+
			   " }"+
			 " }"+
			"}";
		}else if(flag==0){//新增
			query="{"+
				  "\"query\": {"+
				  "  \"bool\": {"+
				  "    \"must\": ["+
				  "      {"+
				  "        \"match\": {"+
				  "          \"enterpriseId\": \""+enterpriseId+"\""+
				  "        }"+
				  "      },"+
				  "      {"+
				  "        \"match\": {"+
				  "          \"uniqueId\": \""+mainUniqueId+"\""+
				  "        }"+
				  "      }"+
				  "    ]"+
				  "  }"+
				  "}"+
				"}";
		}
		String indexFullName=indexName+"-"+startDate+"-"+"alias";
		
		Search search = new Search.Builder(query).addIndex(indexFullName).addType(typeName).ignoreUnavailable(true).build();
		CdrIb cdr = new CdrIb();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				//System.out.println("null!");
			} else {
				List<SearchResult.Hit<CdrIb, Void>> hits = result.getHits(CdrIb.class);
				for(Hit<CdrIb, Void> cdr0:hits){
					cdr=cdr0.source;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return cdr;
	}*/

	/**
	 * 更新呼入详细记录  sipCause
	 */
/*	@Override
	public boolean updateCdrIbDetail(String enterpriseId,String mainUniqueId,String uniqueId,int sipCause, String startDate) {
		List<CdrIb> cdribs = new ArrayList<CdrIb>();
		//先查询出所有details
		CdrIb ci=getCdrIbDetail(enterpriseId,mainUniqueId,null,startDate,"vlink-cdrib","cdr",0);
		if(ci.getDetails()==null) return false;
		for(CdrIbDetail cid:ci.getDetails()){
			if(uniqueId.equals(cid.getUniqueId())){
				cid.setSipCause(sipCause);//变更指定uniqueId的sipCause
			}
		}
		cdribs.add(ci);
		boolean ret=this.indexCdrIbDocuments(enterpriseId, cdribs);
		return ret;
	}*/

	/**
	 * 新增呼入详细记录
	 */
/*	@Override
	public boolean addCdrIbDetail(String enterpriseId,String mainUniqueId,CdrIbDetail cdrIbDetail, String startDate) {
		List<CdrIb> cdribs = new ArrayList<CdrIb>();
		CdrIb ci=getCdrIbDetail(enterpriseId,mainUniqueId,null,startDate,"vlink-cdrib","cdr",0);
		if(ci.getUniqueId() == null){//此时先写子表cdr_ib_detail，再写主表cdr-ib
			ci.setUniqueId(mainUniqueId);
			ci.setStartTime((long)DateUtil.getTimeStemp(startDate));
			ci.setEnterpriseId(enterpriseId);
		}
		if(ci.getDetails()!=null)
		ci.getDetails().add(cdrIbDetail);
		cdribs.add(ci);
		boolean ret=this.indexCdrIbDocuments(enterpriseId, cdribs);
		return ret;
	}*/

/*	@Override
	public CdrIbResponse searchCdrIbDetail(String enterpriseId, int startNo,
			int pageSize, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CdrObResponse searchCdrObDetail(String enterpriseId, int startNo,
			int pageSize, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}*/

	/**
	 * 查询一条CdrObDetail文档
	 * @param mainUniqueId   对应主表的uniqueId
	 * @param uniqueId       要修改的detail表uniqueId
	 * @param startTime  对应主表的startTime   YYYY-MM-dd
	 * @param indexName  
	 * @param typeName
	 * @param flag  0:新增  1:更新
	 * @return
	 */
/*	private CdrOb getCdrObDetail(String enterpriseId,String mainUniqueId,String uniqueId,String startDate, String indexName,String typeName,int flag){
		String query="";
		if(flag==1){//更新
			query="{"+
			 "\"query\": {"+
			 " \"bool\": {"+
			 "   \"must\": ["+
			   "     {"+
			         " \"match\": {"+
			         "   \"enterpriseId\": \""+enterpriseId+"\""+
			         " }"+
			       " },"+
			        "{"+
			         " \"match\": {"+
			         "   \"uniqueId\": \""+mainUniqueId+"\""+
			         " }"+
			        "},"+
			        "{"+
			        "  \"nested\": {"+
			         "   \"path\": \"details\","+
			         "   \"query\": {"+
			          "    \"bool\": {"+
			          "      \"must\": ["+
			          "        {"+
			          "          \"match\": {"+
			          "             \"details.uniqueId\": \""+uniqueId+"\""+
			          "          }"+
			          "        }"+
			          "      ]"+
			          "    }"+
			          "  }"+
			          "}"+
			        "}"+
			      "]"+
			   " }"+
			 " }"+
			"}";
		}else if(flag==0){//新增
			query="{"+
				  "\"query\": {"+
				  "  \"bool\": {"+
				  "    \"must\": ["+
				  "      {"+
				  "        \"match\": {"+
				  "          \"enterpriseId\": \""+enterpriseId+"\""+
				  "        }"+
				  "      },"+
				  "      {"+
				  "        \"match\": {"+
				  "          \"uniqueId\": \""+mainUniqueId+"\""+
				  "        }"+
				  "      }"+
				  "    ]"+
				  "  }"+
				  "}"+
				"}";
		}
		String indexFullName=indexName+"-"+startDate+"-"+"alias";
		Search search = new Search.Builder(query).addIndex(indexFullName).addType(typeName).ignoreUnavailable(true).build();
		CdrOb cdr = new CdrOb();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				//System.out.println("null!");
			} else {
				List<SearchResult.Hit<CdrOb, Void>> hits = result.getHits(CdrOb.class);
				for(Hit<CdrOb, Void> cdr0:hits){
					cdr=cdr0.source;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		return cdr;
	}*/

	/**
	 * 更新呼出详细记录
	 */
/*	@Override
	public boolean updateCdrObDetail(String enterpriseId, String mainUniqueId,
			String uniqueId, int sipCause, String startDate) {
		List<CdrOb> cdrobs = new ArrayList<CdrOb>();
		//先查询出所有details
		CdrOb co=getCdrObDetail(enterpriseId,mainUniqueId,null,startDate,"vlink-cdrob","cdr",0);
		if(co.getDetails()==null) return false;
		for(CdrObDetail cod:co.getDetails()){
			if(uniqueId.equals(cod.getUniqueId())){
				cod.setSipCause(sipCause);//变更指定uniqueId的sipCause
			}
		}
		cdrobs.add(co);
		boolean ret=this.indexCdrObDocuments(enterpriseId, cdrobs);
		return ret;
	}*/

	/**
	 * 新增呼出详细记录
	 */
/*	@Override
	public boolean addCdrObDetail(String enterpriseId, String mainUniqueId,CdrObDetail cdrObDetail, String startDate) {
		List<CdrOb> cdrobs = new ArrayList<CdrOb>();
		CdrOb co=getCdrObDetail(enterpriseId,mainUniqueId,null,startDate,"vlink-cdrob","cdr",0);
		if(co.getUniqueId() == null){//此时先写子表cdr_ob_detail，再写主表cdr-ob
			co.setUniqueId(mainUniqueId);
			co.setStartTime((long)DateUtil.getTimeStemp(startDate));
			co.setEnterpriseId(enterpriseId);
		}
		if(co.getDetails()!=null)
		co.getDetails().add(cdrObDetail);
		cdrobs.add(co);
		boolean ret=this.indexCdrObDocuments(enterpriseId, cdrobs);
		return ret;
	}*/

	/**
	 * 更新cdrOb
	 */
	@Override
	public boolean updateCdrOb(String enterpriseId, String mainUniqueId,
			int sipCause, String startDate) {

		String script = "{\n" +
                "    \"script\" : \"ctx._source.sipCause=sipCause\",\n" +
                "    \"params\" : {\n" +
                "        \"sipCause\" : \""+sipCause+"\"\n" +
                "    }\n" +
                "}";

		String indexFullName="vlink-cdrob"+"-"+startDate+"-"+"alias";
		DocumentResult dr =null;
		try {
			dr=this.getClient().execute(new Update.Builder(script).index(indexFullName).type("cdr").id(mainUniqueId).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dr==null?false:dr.isSucceeded();
	}

}
