package com.tinet.vlink.vo;

public class CallDurationResponse {

	private String enterpriseId;
	private String hotline;
	private String description;
	private long billDuration;
	private long bridgeDuration;

	public String getDescription() {
		return description;
	}
	public long getBillDuration() {
		return billDuration;
	}
	public long getBridgeDuration() {
		return bridgeDuration;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setBillDuration(long billDuration) {
		this.billDuration = billDuration;
	}
	public void setBridgeDuration(long bridgeDuration) {
		this.bridgeDuration = bridgeDuration;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getHotline() {
		return hotline;
	}
	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	@Override
	public String toString() {
		return "CallDurationResponse [enterpriseId=" + enterpriseId
				+ ", hotline=" + hotline + ", description=" + description
				+ ", billDuration=" + billDuration + ", bridgeDuration="
				+ bridgeDuration + "]";
	}

}
