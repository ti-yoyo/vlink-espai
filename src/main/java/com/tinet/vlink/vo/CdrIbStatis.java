package com.tinet.vlink.vo;

import java.util.List;
import java.util.Set;

/**
 * 指定客户，指定时间段，符合某规则的号码，呼入记录条数、计费总时长、桥接总时长
 * @author wangguiyu
 *
 */
public class CdrIbStatis {

	private String enterpriseId;
	private Set<String> conditions;
	private String startDate;
	private String endDate;

	private List<CdrIbStatisBean> statis;

	public String getEnterpriseId() {
		return enterpriseId;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public List<CdrIbStatisBean> getStatis() {
		return statis;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setStatis(List<CdrIbStatisBean> statis) {
		this.statis = statis;
	}
	public Set<String> getConditions() {
		return conditions;
	}
	public void setConditions(Set<String> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return "CdrIbStatis [enterpriseId=" + enterpriseId + ", conditions="
				+ conditions + ", startDate=" + startDate + ", endDate="
				+ endDate + ", statis=" + statis + "]";
	}

}
