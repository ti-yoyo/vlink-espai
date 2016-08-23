package com.tinet.vlink.vo;


public class CdrIbStatisBean {

	private int  totalNum;
	private long billDurationsTotal;
	private long bridgeDurationsTotal;
	private String condition;

	public int getTotalNum() {
		return totalNum;
	}
	public long getBillDurationsTotal() {
		return billDurationsTotal;
	}
	public long getBridgeDurationsTotal() {
		return bridgeDurationsTotal;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public void setBillDurationsTotal(long billDurationsTotal) {
		this.billDurationsTotal = billDurationsTotal;
	}
	public void setBridgeDurationsTotal(long bridgeDurationsTotal) {
		this.bridgeDurationsTotal = bridgeDurationsTotal;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "CdrIbStatisBean [totalNum=" + totalNum
				+ ", billDurationsTotal=" + billDurationsTotal
				+ ", bridgeDurationsTotal=" + bridgeDurationsTotal
				+ ", condition=" + condition + "]";
	}

}
