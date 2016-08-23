package com.tinet.vlink.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.tinet.escore.utils.DateUtil;

public class VlinkUtils {
	
	public static final int  CALL_TYPE_IB = 1;//呼叫类型：呼入
	public static final int  CALL_TYPE_OB = 2;//呼叫类型：呼出
	public static final int CAL_BILL_DURATION_STATUS_VALUE_OB = 21;//计算呼出计费时长，状态值
	public static final int CAL_BRIDGE_DURATION_STATUS_VALUE_OB = 23;//计算呼出桥接时长，状态值
	public static final int CAL_BILL_DURATION_STATUS_VALUE_IB = 4;//计算呼入计费时长，状态值
	public static final int CAL_BRIDGE_DURATION_STATUS_VALUE_IB = 2;//计算呼入桥接时长，状态值

	/**
	 * 计算呼入计费时长
	 * @param obStatus  外呼状态
	 * @param billDuration 时长
	 * @param ibStatus  呼入状态
	 * @return
	 */
	public static int calculateBillDuration(int obStatus, int billDuration,int callType) {
		boolean flag=false;
		if((VlinkUtils.CALL_TYPE_OB==callType&&obStatus>VlinkUtils.CAL_BILL_DURATION_STATUS_VALUE_OB)
				||(VlinkUtils.CALL_TYPE_IB==callType&&obStatus<VlinkUtils.CAL_BILL_DURATION_STATUS_VALUE_IB))
			flag = true;
		if (flag) {
			if (billDuration == 0)
				return 1;
			else if (billDuration % 60 == 0)
				return billDuration / 60;
			else if (billDuration % 60 > 0)
				return (billDuration / 60) + 1;
			else
				return billDuration;
		} else
			return 0;
	}
	
	/**
	 * 计算桥接时长
	 * @param status
	 * @param bridgeDuration  时长
	 * @return
	 */
	public static int calculateBridgeDuration(int obStatus, int bridgeDuration,int callType) {
		boolean flag=false;
		if((VlinkUtils.CALL_TYPE_OB==callType&&obStatus>VlinkUtils.CAL_BRIDGE_DURATION_STATUS_VALUE_OB)
				||(VlinkUtils.CALL_TYPE_IB==callType&&obStatus<VlinkUtils.CAL_BRIDGE_DURATION_STATUS_VALUE_IB))
			flag = true;
		if (flag) {
			if (bridgeDuration == 0)
				return 1;
			else if (bridgeDuration % 60 == 0)
				return bridgeDuration / 60;
			else if (bridgeDuration % 60 > 0)
				return (bridgeDuration / 60) + 1;
			else
				return bridgeDuration;
		} else
			return 0;
	}
	
	
	/**
	 * 计算计费时长
	 * @param status
	 * @param bridgeDuration
	 * @return
	 */
	public static int calculateDuration(int duration) {
		if (duration == 0)
			return 1;
		else if (duration % 60 == 0)
			return duration / 60;
		else if (duration % 60 > 0)
			return (duration / 60) + 1;
		else
			return duration;
	}
	
	/**
	 * 
	 * @param indexPrefix
	 * @param indexSuffix
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Set<String> generateIndexs(String indexPrefix,String indexSuffix,String startDate,String endDate){
		Set<String> sets = new HashSet<String>(); 
		int n=DateUtil.daysBetween(startDate,endDate);//日期间隔天数
		for(int i=0;i<n+1;i++){
			Date dd=null;
			try {
				dd = DateUtil.addDay(DateUtil.parse(startDate), i);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String sd=DateUtil.format(dd, DateUtil.FMT_DATE_YYYY_MM_DD);
			String sindex=sd;
			if(indexPrefix!=null&&!"".equals(indexPrefix)){
				sindex=indexPrefix+"-"+sindex;
			}
			if(indexSuffix!=null&&!"".equals(indexSuffix)){
				sindex=sindex+"-"+indexSuffix;
			}
			sets.add(sindex);
		}
		
		return sets;
		
	}
	
	public static void main(String[] args) {
		//String startTime = "2016-06-12 12:23:12";
		//String endTime = "2016-06-15 14:56:11";
		//Set<String> s= generateIndexs("vlink-cdrib","alias",startTime,endTime);
		//System.out.println(s.toString());
		
		
		String d=DateUtil.format("1458785525", DateUtil.FMT_DATE_YYYY_MM_DD_HH_mm_ss);
		System.out.println(d);
	}

}
