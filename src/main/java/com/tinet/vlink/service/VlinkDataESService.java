
package com.tinet.vlink.service;

import java.util.Set;

import com.tinet.vlink.vo.CallDurationResponse;
import com.tinet.vlink.vo.CdrIbStatis;
import com.tinet.vlink.vo.CdrObStatis;

/**
 * @author wangguiyu
 *
 */
public interface VlinkDataESService extends VlinkESService {

	/**
	 * 获取指定呼入号码的计费时长
	 * @param enterpriseId    企业id
	 * @param customerNumber  呼入号码
	 * @param startDate  起始日期
	 * @param endDate    结束日期
	 */
	public CallDurationResponse getCallDuration(String enterpriseId,String hotline,String startDate,String endDate);
	
	/**
	 *  统计指定客户，指定时间段，符合某规则的热线号码，呼入记录条数、计费总时长、桥接总时长。
	 * @param enterpriseId
	 * @param hotline
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public CdrIbStatis getCdrIbStatis(String enterpriseId,Set<String> hotlines,String startDate,String endDate);

	/**
	 * 统计指定客户，指定时间段、符合某规则的呼入转接外显号码、呼入转接被叫号码、呼入转接热线号码的：呼入记录条数、计费总时长、桥接总时长。
	 * @param enterpriseId
	 * @param conditions  统计条件：  hotline规则｜clidNumber规则｜calleeNumber规则  如：   400|010|131,统计热线号码400开头，外显号码010开头，被叫号码131开头的相关数据
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public CdrIbStatis getCdrIbForwardStatis(String enterpriseId,Set<String> conditions,String startDate,String endDate);

	/**
	 *  统计指定客户，指定时间段，符合某规则的呼出外显号码和被叫号码的：外呼记录条数、计费总时长、桥接总时长。
	 * @param enterpriseId
	 * @param conditions    统计条件：  clidNumber规则｜calleeNumber规则  如：   010|131,统计 外显号码010开头，被叫号码131开头的相关数据
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public CdrObStatis getCdrObStatis(String enterpriseId,Set<String> conditions,String startDate,String endDate);

	/**
	 * 统计指定客户，指定时间段、符合某规则的呼出转接外显号码、呼出转接被叫号码的：呼入记录条数、计费总时长、桥接总时长。
	 * @param enterpriseId
	 * @param conditions  统计条件：  clidNumber规则｜calleeNumber规则  如：   010|131,统计 外显号码010开头，被叫号码131开头的相关数据
	 * @param startDate
	 * @param endDate
	 * @param clidNumber    呼转外显示号码
	 * @param calleeNumber  呼转被叫号码
	 * @return
	 */
	public CdrObStatis getCdrObForwardStatis(String enterpriseId,Set<String> conditions,String startDate,String endDate);

}
