
package com.tinet.vlink.service.impl;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.SumAggregation;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tinet.escore.utils.DateUtil;
import com.tinet.vlink.service.VlinkDataESService;
import com.tinet.vlink.service.VlinkESFactory;
import com.tinet.vlink.utils.VlinkUtils;
import com.tinet.vlink.vo.CallDurationResponse;
import com.tinet.vlink.vo.CdrIbStatis;
import com.tinet.vlink.vo.CdrIbStatisBean;
import com.tinet.vlink.vo.CdrObStatis;
import com.tinet.vlink.vo.CdrObStatisBean;

/**
 * @author wangguiyu
 *
 */
@Service("vlinkDataESService")
public class VlinkDataESServiceImpl extends VlinkESServiceImpl implements VlinkDataESService {
	
	public VlinkDataESServiceImpl() {
		
	}
	@Autowired
	public VlinkDataESServiceImpl(@Qualifier("vlinkESFactory")VlinkESFactory vlinkESFactory) {
		super(vlinkESFactory);
	}


	@Override
	public CallDurationResponse getCallDuration(String enterpriseId, String hotline,String startDate,String endDate) {
		int iStartDate=DateUtil.getTimeStemp(startDate);
		Date dd=null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd=DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate=DateUtil.getTimeStemp(sd);
		String	query ="{"
				  +"\"query\": {"
				   +" \"bool\": {"
				   +"   \"must\": ["
				   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},"
				   +"{\"term\":{\"hotline\":\""+hotline+"\"}},"
				   +"    {"
				   +"      \"range\": {"
				   +"        \"startTime\": {"
				   +"           \"gt\":"+iStartDate+","
				   +"           \"lt\":"+iEndDate
				   +"         }"
				   +"       }"
				   +"     }"
				   +"   ],"
				   +"   \"must_not\": [],"
				   +"   \"should\": []"
				   +" }"
				   +"},"
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
		Search search = new Search.Builder(query).addIndex(indexs).ignoreUnavailable(true).build();
		CallDurationResponse cdr = new CallDurationResponse();
		cdr.setDescription("failure");
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD=result.getAggregations().getSumAggregation("billDurations");
				Double   dBillDurations=saBillD==null?0:saBillD.getSum();
				
				SumAggregation saBidgeD=result.getAggregations().getSumAggregation("bridgeDurations");
				Double   dBidgeDurations=saBidgeD==null?0:saBidgeD.getSum();
				
				cdr.setDescription("success");
				cdr.setBillDuration(dBillDurations.longValue());
				cdr.setBridgeDuration(dBidgeDurations.longValue());
				cdr.setEnterpriseId(enterpriseId);
				cdr.setHotline(hotline);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	
		return cdr;
	}
	
	//----------------------呼入统计---start------------------------------------------------------------------
	/**
	 * 统计指定客户，指定时间段，符合某规则的号码，呼入记录条数、计费总时长、桥接总时长
	 */
	@Override
	public CdrIbStatis getCdrIbStatis(String enterpriseId,Set<String> conditions, String startDate, String endDate) {
		CdrIbStatis cibs = new CdrIbStatis();
		cibs.setEndDate(endDate);
		cibs.setStartDate(startDate);
		cibs.setEnterpriseId(enterpriseId);
		cibs.setConditions(conditions);
		List<CdrIbStatisBean> cisbs=new ArrayList<CdrIbStatisBean>();
		for(String hotline:conditions){
			CdrIbStatisBean cisb= new CdrIbStatisBean();
			cisb=getCdrIbStatisMust(enterpriseId,hotline,startDate,endDate);
			cisbs.add(cisb);
		}
		
		CdrIbStatisBean cisb1=getCdrIbStatisMustNot(enterpriseId,conditions,startDate,endDate);
		cisbs.add(cisb1);
		
		cibs.setStatis(cisbs);
		return cibs;
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrIbStatisBean getCdrIbStatisMust(String enterpriseId,
			String hotline, String startDate, String endDate) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String query="";
			query = "{" + "\"query\": {" + " \"bool\": {" + "   \"must\": ["
				+ "{\"term\":{\"enterpriseId\":\""
				+ enterpriseId
				+ "\"}},"
				+ "{\"wildcard\":{\"hotline\":\""
				+ hotline
				+ "*"
				+ "\"}},"
				+ "    {"
				+ "      \"range\": {"
				+ "        \"startTime\": {"
				+ "           \"gt\":"
				+ iStartDate
				+ ","
				+ "           \"lt\":"
				+ iEndDate
				+ "         }"
				+ "       }"
				+ "     }"
				+ "   ],"
				+ "   \"must_not\": [],"
				+ "   \"should\": []"
				+ " }"
				+ "},"
				+ "\"aggs\": {"
				+ " \"billDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"billDuration2\""
				+ "   }"
				+ " },"
				+ " \"bridgeDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"bridgeDuration2\"" + "   }" + " }" + "}" + "}";
	
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrib", "alias",
				startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrIbStatisBean cisb = new CdrIbStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cisb.setBillDurationsTotal(dBillDurations.longValue());
				cisb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cisb.setTotalNum(result.getTotal());
				cisb.setCondition(hotline);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return cisb;
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrIbStatisBean getCdrIbStatisMustNot(String enterpriseId,Set<String> hotlines, String startDate, String endDate) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String	query="";
			query ="{"
				  +"\"query\": {"
				   +" \"bool\": {"
				   +"   \"must\": ["
				   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},";
	
		query =query+"    {"
				   +"      \"range\": {"
				   +"        \"startTime\": {"
				   +"           \"gt\":"+iStartDate+","
				   +"           \"lt\":"+iEndDate
				   +"         }"
				   +"       }"
				   +"     }"
				   +"   ],"
				   +"   \"must_not\": [";
		
		String temp="";
		for(String hotline:hotlines){
			temp=temp+"{\"wildcard\":{\"hotline\":\""+hotline+"*"+"\"}},";
		}
		query =query+temp.subSequence(0, temp.length()-1);//去掉temp最后一个逗号		   
		query = query	   
				   + "],"
				   +"   \"should\": []"
				   +" }"
				   +"},"
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
		
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrib", "alias",
				startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrIbStatisBean cisb = new CdrIbStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cisb.setBillDurationsTotal(dBillDurations.longValue());
				cisb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cisb.setTotalNum(result.getTotal());
				cisb.setCondition("-1");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return cisb;
	}

	//--------------------呼入转接统计----start---------------------------------------------------------------------------
	/**
	 * 统计指定客户，指定时间段、呼转外显号码、呼转被叫号码且符合某规则的热线号码的呼入记录条数、计费总时长、桥接总时长
	 */
	@Override
	public CdrIbStatis getCdrIbForwardStatis(String enterpriseId, Set<String> conditions,String startDate, String endDate) {
		CdrIbStatis cibs = new CdrIbStatis();
		cibs.setEndDate(endDate);
		cibs.setStartDate(startDate);
		cibs.setEnterpriseId(enterpriseId);
		cibs.setConditions(conditions);
		List<CdrIbStatisBean> cisbs=new ArrayList<CdrIbStatisBean>();
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String hotline = conditionStr[0];
			String clidNumber=conditionStr[1];
		    String calleeNumber=conditionStr[2];
			CdrIbStatisBean cisb= new CdrIbStatisBean();
			cisb=getCdrIbForwardStatisMust(enterpriseId,hotline,startDate,endDate,clidNumber,calleeNumber);
			cisbs.add(cisb);
		}
		CdrIbStatisBean cisb1=getCdrIbForwardStatisMustNot(enterpriseId,conditions,startDate,endDate);
		cisbs.add(cisb1);
		
		cibs.setStatis(cisbs);
		return cibs;
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrIbStatisBean getCdrIbForwardStatisMust(String enterpriseId,
			String hotline, String startDate, String endDate,String clidNumber,String calleeNumber) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String query="";
			query = "{" + "\"query\": {" + " \"bool\": {" + "   \"must\": ["
				+ "{\"term\":{\"enterpriseId\":\""
				+ enterpriseId
				+ "\"}},"
				+ "{\"wildcard\":{\"hotline\":\""
				+ hotline
				+ "*"
				+ "\"}},"
				+ "    {"
				+ "      \"range\": {"
				+ "        \"startTime\": {"
				+ "           \"gt\":"
				+ iStartDate
				+ ","
				+ "           \"lt\":"
				+ iEndDate
				+ "         }"
				+ "       }"
				+ "     },"
				
		        //+"{"
		        //+"      \"nested\": {"
		       // +"        \"path\": \"details\","
		       // +"        \"query\": {"
		       // +"          \"bool\": {"
		       // +"            \"must\": ["
		        +"              {"
		        +"                \"wildcard\": {"
		        +"                   \"detailClid\": \""+clidNumber+"*"+"\""
		        +"                }"
		        +"              },"
		        +"              {"
		        +"                  \"wildcard\": {"
		        +"                     \"calleeNumber\": \""+calleeNumber+"*"+"\""
		        +"                  }"
		        +"              }"
		        //+"            ]"
		        //+"          }"
		       // +"        }"
		        //+"      }"
		        //+"    }"
			
				+ "   ],"
				+ "   \"must_not\": [],"
				+ "   \"should\": []"
				+ " }"
				+ "},"
				+ "\"aggs\": {"
				+ " \"billDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"billDuration2\""
				+ "   }"
				+ " },"
				+ " \"bridgeDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"bridgeDuration2\"" + "   }" + " }" + "}" + "}";
	
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrib", "alias",
				startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrIbStatisBean cisb = new CdrIbStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cisb.setBillDurationsTotal(dBillDurations.longValue());
				cisb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cisb.setTotalNum(result.getTotal());
				cisb.setCondition(hotline+"|"+clidNumber+"|"+calleeNumber);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return cisb;
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrIbStatisBean getCdrIbForwardStatisMustNot(String enterpriseId,Set<String> conditions, String startDate, String endDate) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String	query="";
			query ="{"
				  +"\"query\": {"
				   +" \"bool\": {"
				   +"   \"must\": ["
				   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},";
	
		query =query+"    {"
				   +"      \"range\": {"
				   +"        \"startTime\": {"
				   +"           \"gt\":"+iStartDate+","
				   +"           \"lt\":"+iEndDate
				   +"         }"
				   +"       }"
				   +"     }"
				   
/*				          +",{"
				        +"      \"nested\": {"
				        +"        \"path\": \"details\","
				        +"        \"query\": {"
				        +"          \"bool\": {"
				        +"            \"must\": ["
				        +"              {"
				        +"                \"wildcard\": {"
				        +"                   \"details.clid\": \""+clidNumber+"*"+"\""
				        +"                }"
				        +"              },"
				        +"              {"
				        +"                  \"wildcard\": {"
				        +"                     \"details.calleeNumber\": \""+calleeNumber+"*"+"\""
				        +"                  }"
				        +"              }"
				        +"            ]"
				        +"          }"
				        +"        }"
				        +"      }"
				        +"    }"*/
				   +"   ],"
				   +"   \"must_not\": [";
		
		
		String temp="";
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String hotline = conditionStr[0];
			temp=temp+"{\"wildcard\":{\"hotline\":\""+hotline+"*"+"\"}},";
		}
		//query =query+temp.subSequence(0, temp.length()-1);//去掉temp最后一个逗号
		query = query +temp;
		
		temp="";
		//temp=temp
		//+		"{"
        //+"      \"nested\": {"
       // +"        \"path\": \"details\","
        //+"        \"query\": {"
        //+"          \"bool\": {"
       // +"            \"must\": [";
		
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String clidNumber = conditionStr[1];
			String calleeNumber = conditionStr[2];
	        temp=temp
	        +"              {"
	        +"                \"wildcard\": {"
	        +"                   \"detailClid\": \""+clidNumber+"*"+"\""
	        +"                }"
	        +"              },"
	        +"              {"
	        +"                  \"wildcard\": {"
	        +"                     \"calleeNumber\": \""+calleeNumber+"*"+"\""
	        +"                  }"
	        +"              },";
		}
		temp=""+temp.subSequence(0, temp.length()-1);//去掉temp最后一个逗号
		
        //temp=temp
       // +"            ]"
        //+"          }"
        //+"        }"
        //+"      }"
       // +"    }"
        ;
		
        query=query + temp;
		query = query	   
				   + "],"
				   +"   \"should\": []"
				   +" }"
				   +"},"
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
	
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrib", "alias",startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrIbStatisBean cisb = new CdrIbStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cisb.setBillDurationsTotal(dBillDurations.longValue());
				cisb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cisb.setTotalNum(result.getTotal());
				cisb.setCondition("-1");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return cisb;
	}

	//--------------------外呼统计-------start-----------------------------------------------------------------

	@Override
	public CdrObStatis getCdrObStatis(String enterpriseId,Set<String> conditions, String startDate, String endDate) {
		CdrObStatis cobs = new CdrObStatis();
		cobs.setEndDate(endDate);
		cobs.setStartDate(startDate);
		cobs.setEnterpriseId(enterpriseId);
		cobs.setConditions(conditions);
		List<CdrObStatisBean> cosbs=new ArrayList<CdrObStatisBean>();
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String clidNumber=conditionStr[0];
		    String calleeNumber=conditionStr[1];
			CdrObStatisBean cosb= new CdrObStatisBean();
			cosb=getCdrObStatisMust(enterpriseId,startDate,endDate,clidNumber,calleeNumber);
			cosbs.add(cosb);
		}
		CdrObStatisBean cosb1=getCdrObStatisMustNot(enterpriseId,conditions,startDate,endDate);
		cosbs.add(cosb1);
		cobs.setStatis(cosbs);
		return cobs;	
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrObStatisBean getCdrObStatisMust(String enterpriseId,String startDate, String endDate,String clidNumber,String calleeNumber) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String query="";
			query = "{" + "\"query\": {" + " \"bool\": {" + "   \"must\": ["
				+ "{\"term\":{\"enterpriseId\":\""
				+ enterpriseId
				+ "\"}},"
				
/*				+ "{\"wildcard\":{\"hotline\":\""
				+ hotline
				+ "*"
				+ "\"}},"*/
				
				
				+ "    {"
				+ "      \"range\": {"
				+ "        \"startTime\": {"
				+ "           \"gt\":"
				+ iStartDate
				+ ","
				+ "           \"lt\":"
				+ iEndDate
				+ "         }"
				+ "       }"
				+ "     },"
				
		        +"              {"
		        +"                \"wildcard\": {"
		        +"                   \"clid\": \""+clidNumber+"*"+"\""
		        +"                }"
		        +"              },"
		        +"              {"
		        +"                  \"wildcard\": {"
		        +"                     \"calleeNumber\": \""+calleeNumber+"*"+"\""
		        +"                  }"
		        +"              }"
			
				+ "   ],"
				+ "   \"must_not\": [],"
				+ "   \"should\": []"
				+ " }"
				+ "},"
				+ "\"aggs\": {"
				+ " \"billDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"billDuration2\""
				+ "   }"
				+ " },"
				+ " \"bridgeDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"bridgeDuration2\"" + "   }" + " }" + "}" + "}";
	
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrob", "alias",
				startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrObStatisBean cosb = new CdrObStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				//System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cosb.setBillDurationsTotal(dBillDurations.longValue());
				cosb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cosb.setTotalNum(result.getTotal());
				cosb.setCondition(clidNumber+"|"+calleeNumber);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return cosb;
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrObStatisBean getCdrObStatisMustNot(String enterpriseId,Set<String> conditions, String startDate, String endDate) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String	query="";
			query ="{"
				  +"\"query\": {"
				   +" \"bool\": {"
				   +"   \"must\": ["
				   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},";
	
		query =query+"    {"
				   +"      \"range\": {"
				   +"        \"startTime\": {"
				   +"           \"gt\":"+iStartDate+","
				   +"           \"lt\":"+iEndDate
				   +"         }"
				   +"       }"
				   +"     }"
				   
				   +"   ],"
				   +"   \"must_not\": [";
		
		String temp="";
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String clidNumber = conditionStr[0];
			String calleeNumber = conditionStr[1];
	        temp=temp
	        +"              {"
	        +"                \"wildcard\": {"
	        +"                   \"clid\": \""+clidNumber+"*"+"\""
	        +"                }"
	        +"              },"
	        +"              {"
	        +"                  \"wildcard\": {"
	        +"                     \"calleeNumber\": \""+calleeNumber+"*"+"\""
	        +"                  }"
	        +"              },";
		}

		temp=""+temp.subSequence(0, temp.length()-1);//去掉temp最后一个逗号
        query=query + temp;
		query = query	   
				   + "],"
				   +"   \"should\": []"
				   +" }"
				   +"},"
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
	
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrob", "alias",startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrObStatisBean cosb = new CdrObStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cosb.setBillDurationsTotal(dBillDurations.longValue());
				cosb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cosb.setTotalNum(result.getTotal());
				cosb.setCondition("-1");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return cosb;
	}

	//-------------------------外呼转接统计----------start---------------------------------------------------
	@Override
	public CdrObStatis getCdrObForwardStatis(String enterpriseId,
			Set<String> conditions, String startDate, String endDate) {
		CdrObStatis cobs = new CdrObStatis();
		cobs.setEndDate(endDate);
		cobs.setStartDate(startDate);
		cobs.setEnterpriseId(enterpriseId);
		cobs.setConditions(conditions);
		List<CdrObStatisBean> cosbs=new ArrayList<CdrObStatisBean>();
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String clidNumber=conditionStr[0];
		    String calleeNumber=conditionStr[1];
			CdrObStatisBean cosb= new CdrObStatisBean();
			cosb=getCdrObForwardStatisMust(enterpriseId,startDate,endDate,clidNumber,calleeNumber);
			cosbs.add(cosb);
		}
		CdrObStatisBean cosb1=getCdrObForwardStatisMustNot(enterpriseId,conditions,startDate,endDate);
		cosbs.add(cosb1);
		cobs.setStatis(cosbs);
		return cobs;	
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrObStatisBean getCdrObForwardStatisMust(String enterpriseId,String startDate, String endDate,String clidNumber,String calleeNumber) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String query="";
			query = "{" + "\"query\": {" + " \"bool\": {" + "   \"must\": ["
				+ "{\"term\":{\"enterpriseId\":\""
				+ enterpriseId
				+ "\"}},"
				
/*				+ "{\"wildcard\":{\"hotline\":\""
				+ hotline
				+ "*"
				+ "\"}},"*/
				+ "    {"
				+ "      \"range\": {"
				+ "        \"startTime\": {"
				+ "           \"gt\":"
				+ iStartDate
				+ ","
				+ "           \"lt\":"
				+ iEndDate
				+ "         }"
				+ "       }"
				+ "     },"
				
		        //+"{"
		        //+"      \"nested\": {"
		       // +"        \"path\": \"details\","
		        //+"        \"query\": {"
		       // +"          \"bool\": {"
		        //+"            \"must\": ["
		        +"              {"
		        +"                \"wildcard\": {"
		        +"                   \"detailClid\": \""+clidNumber+"*"+"\""
		        +"                }"
		        +"              },"
		        +"              {"
		        +"                  \"wildcard\": {"
		        +"                     \"calleeNumber\": \""+calleeNumber+"*"+"\""
		        +"                  }"
		        +"              }"
		       // +"            ]"
		       // +"          }"
		       // +"        }"
		        //+"      }"
		        //+"    }"
			
				+ "   ],"
				+ "   \"must_not\": [],"
				+ "   \"should\": []"
				+ " }"
				+ "},"
				+ "\"aggs\": {"
				+ " \"billDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"billDuration2\""
				+ "   }"
				+ " },"
				+ " \"bridgeDurations\": {"
				+ "   \"sum\": {"
				+ "\"field\": \"bridgeDuration2\"" + "   }" + " }" + "}" + "}";
	
		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrob", "alias",
				startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrObStatisBean cosb = new CdrObStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				//System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cosb.setBillDurationsTotal(dBillDurations.longValue());
				cosb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cosb.setTotalNum(result.getTotal());
				cosb.setCondition(clidNumber+"|"+calleeNumber);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return cosb;
	}

	/**
	 * 
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private CdrObStatisBean getCdrObForwardStatisMustNot(String enterpriseId,Set<String> conditions, String startDate, String endDate) {
		int iStartDate = DateUtil.getTimeStemp(startDate);
		Date dd = null;
		try {
			dd = DateUtil.addDay(DateUtil.parse(endDate), 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String sd = DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
		int iEndDate = DateUtil.getTimeStemp(sd);
		String	query="";
			query ="{"
				  +"\"query\": {"
				   +" \"bool\": {"
				   +"   \"must\": ["
				   +"{\"term\":{\"enterpriseId\":\""+enterpriseId+"\"}},";
	
		query =query+"    {"
				   +"      \"range\": {"
				   +"        \"startTime\": {"
				   +"           \"gt\":"+iStartDate+","
				   +"           \"lt\":"+iEndDate
				   +"         }"
				   +"       }"
				   +"     }"
				   
				   +"   ],"
				   +"   \"must_not\": [";

		String temp="";
/*		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String hotline = conditionStr[0];
			temp=temp+"{\"wildcard\":{\"hotline\":\""+hotline+"*"+"\"}},";
		}
		query = query +temp;
		temp="";*/
		
		//temp=temp
				//+"{"
        //+"      \"nested\": {"
        //+"        \"path\": \"details\","
        //+"        \"query\": {"
        //+"          \"bool\": {"
       // +"            \"must\": [";
		
		for(String condition:conditions){
			String[] conditionStr=condition.split("\\|");
			String clidNumber = conditionStr[0];
			String calleeNumber = conditionStr[1];
	        temp=temp
	        +"              {"
	        +"                \"wildcard\": {"
	        +"                   \"detailsClid\": \""+clidNumber+"*"+"\""
	        +"                }"
	        +"              },"
	        +"              {"
	        +"                  \"wildcard\": {"
	        +"                     \"calleeNumber\": \""+calleeNumber+"*"+"\""
	        +"                  }"
	        +"              },";
		}
		temp=""+temp.subSequence(0, temp.length()-1);//去掉temp最后一个逗号

       // temp=temp
       // +"            ]"
        //+"          }"
        //+"        }"
       // +"      }"
        //+"    }"
        ;

        query=query + temp;
		query = query	   
				   + "],"
				   +"   \"should\": []"
				   +" }"
				   +"},"
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

		Set<String> indexs = VlinkUtils.generateIndexs("vlink-cdrob", "alias",startDate, endDate);
		Search search = new Search.Builder(query).addIndex(indexs)
				.ignoreUnavailable(true).build();
		CdrObStatisBean cosb = new CdrObStatisBean();
		try {
			SearchResult result = this.getClient().execute(search);
			if (result == null) {
				System.out.println("null!");
			} else {
				SumAggregation saBillD = result.getAggregations()
						.getSumAggregation("billDurations");
				Double dBillDurations = saBillD == null ? 0 : saBillD.getSum();

				SumAggregation saBidgeD = result.getAggregations()
						.getSumAggregation("bridgeDurations");
				Double dBidgeDurations = saBidgeD == null ? 0 : saBidgeD
						.getSum();
				cosb.setBillDurationsTotal(dBillDurations.longValue());
				cosb.setBridgeDurationsTotal(dBidgeDurations.longValue());
				cosb.setTotalNum(result.getTotal());
				cosb.setCondition("-1");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return cosb;
	}
	//--------------------------------------------------------------------------------------------------------------
}
