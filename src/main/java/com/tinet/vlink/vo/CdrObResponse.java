package com.tinet.vlink.vo;

import java.util.ArrayList;
import java.util.List;

import com.tinet.vlink.model.CdrOb;



public class CdrObResponse {
	//查询条件
	private String enterpriseId;
	private String startDate;
	private String endDate;
	private int startNo;
	private int pageSize;
	//统计结果
	private List<CdrOb>  cdrs = new ArrayList<CdrOb>();
	private int totalNum;
	private long billDurationsTotal;
	private long bridgeDurationsTotal; 

	
	
	public CdrObResponse(){
		
	}
	
	public CdrObResponse(List<CdrOb> cdrs, int startNo, int pageSize,
			int totalNum, long billDurationsTotal, long bridgeDurationsTotal) {
		super();
		this.cdrs = cdrs;
		this.startNo = startNo;
		this.pageSize = pageSize;
		this.totalNum = totalNum;
		this.billDurationsTotal = billDurationsTotal;
		this.bridgeDurationsTotal = bridgeDurationsTotal;
	}
	public List<CdrOb> getCdrs() {
		return cdrs;
	}
	public int getStartNo() {
		return startNo;
	}
	public int getPageSize() {
		return pageSize;
	}

	public void setCdrs(List<CdrOb> cdrs) {
		this.cdrs = cdrs;
	}
	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public long getBillDurationsTotal() {
		return billDurationsTotal;
	}
	public long getBridgeDurationsTotal() {
		return bridgeDurationsTotal;
	}
	public void setBillDurationsTotal(long billDurationsTotal) {
		this.billDurationsTotal = billDurationsTotal;
	}
	public void setBridgeDurationsTotal(long bridgeDurationsTotal) {
		this.bridgeDurationsTotal = bridgeDurationsTotal;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
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

	@Override
	public String toString() {
		return "CdrObResponse [cdrs=" + cdrs + ", startNo=" + startNo
				+ ", pageSize=" + pageSize + ", totalNum=" + totalNum
				+ ", billDurationsTotal=" + billDurationsTotal
				+ ", bridgeDurationsTotal=" + bridgeDurationsTotal
				+ ", enterpriseId=" + enterpriseId + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}


}
