package com.tinet.vlink.model;

import java.util.Date;

public class CdrIb {
    private Integer id;

    private String uniqueId;

    private String hotline;
    
    private String detailClid;

    private String numberTrunk;

    private String customerNumber;

    private Integer customerNumberType;

    private String customerAreaCode;

    private String customerProvince;

    private String customerCity;

    private String calleeNumber;

    private String calleeAreaCode;

    private Long startTime;

    private Long answerTime;

    private Long dialTime;

    private Long bridgeTime;

    private Long endTime;

    private Long monitorStartTime;

    private Long monitorEndTime;

    private Integer monitorDuration;

    private Integer billDuration;

    private Integer bridgeDuration;

    private Integer totalDuration;

    private Integer ivrId;

    private String ivrName;

    private String ivrFlow;

    private String telsetName;

    private String recordFile;

    private Integer callType;

    private Integer status;

    private Integer mark;

    private String markData;

    private Integer endReason;

    private String gwIp;

    private String userField;

    private Integer sipCause;

    private Date createTime;
    //private List<CdrIbDetail> details = new ArrayList<CdrIbDetail>();
    
    //--------------------------------
    private String enterpriseId;//企业id
    private Integer monitorDuration2;//计费时长

    private Integer billDuration2;//计费时长

    private Integer bridgeDuration2;//计费时长

    private Integer totalDuration2;//计费时长
    //---------------------------------
    
   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId == null ? null : uniqueId.trim();
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline == null ? null : hotline.trim();
    }

    public String getNumberTrunk() {
        return numberTrunk;
    }

    public void setNumberTrunk(String numberTrunk) {
        this.numberTrunk = numberTrunk == null ? null : numberTrunk.trim();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber == null ? null : customerNumber.trim();
    }

    public Integer getCustomerNumberType() {
        return customerNumberType;
    }

    public void setCustomerNumberType(Integer customerNumberType) {
        this.customerNumberType = customerNumberType;
    }

    public String getCustomerAreaCode() {
        return customerAreaCode;
    }

    public void setCustomerAreaCode(String customerAreaCode) {
        this.customerAreaCode = customerAreaCode == null ? null : customerAreaCode.trim();
    }

    public String getCustomerProvince() {
        return customerProvince;
    }

    public void setCustomerProvince(String customerProvince) {
        this.customerProvince = customerProvince == null ? null : customerProvince.trim();
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity == null ? null : customerCity.trim();
    }

    public String getCalleeNumber() {
        return calleeNumber;
    }

    public void setCalleeNumber(String calleeNumber) {
        this.calleeNumber = calleeNumber == null ? null : calleeNumber.trim();
    }

    public String getCalleeAreaCode() {
        return calleeAreaCode;
    }

    public void setCalleeAreaCode(String calleeAreaCode) {
        this.calleeAreaCode = calleeAreaCode == null ? null : calleeAreaCode.trim();
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Long answerTime) {
        this.answerTime = answerTime;
    }

    public Long getDialTime() {
        return dialTime;
    }

    public void setDialTime(Long dialTime) {
        this.dialTime = dialTime;
    }

    public Long getBridgeTime() {
        return bridgeTime;
    }

    public void setBridgeTime(Long bridgeTime) {
        this.bridgeTime = bridgeTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getMonitorStartTime() {
        return monitorStartTime;
    }

    public void setMonitorStartTime(Long monitorStartTime) {
        this.monitorStartTime = monitorStartTime;
    }

    public Long getMonitorEndTime() {
        return monitorEndTime;
    }

    public void setMonitorEndTime(Long monitorEndTime) {
        this.monitorEndTime = monitorEndTime;
    }

    public Integer getMonitorDuration() {
        return monitorDuration;
    }

    public void setMonitorDuration(Integer monitorDuration) {
        this.monitorDuration = monitorDuration;
    }

    public Integer getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(Integer billDuration) {
        this.billDuration = billDuration;
    }

    public Integer getBridgeDuration() {
        return bridgeDuration;
    }

    public void setBridgeDuration(Integer bridgeDuration) {
        this.bridgeDuration = bridgeDuration;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getIvrId() {
        return ivrId;
    }

    public void setIvrId(Integer ivrId) {
        this.ivrId = ivrId;
    }

    public String getIvrName() {
        return ivrName;
    }

    public void setIvrName(String ivrName) {
        this.ivrName = ivrName == null ? null : ivrName.trim();
    }

    public String getIvrFlow() {
        return ivrFlow;
    }

    public void setIvrFlow(String ivrFlow) {
        this.ivrFlow = ivrFlow == null ? null : ivrFlow.trim();
    }

    public String getTelsetName() {
        return telsetName;
    }

    public void setTelsetName(String telsetName) {
        this.telsetName = telsetName == null ? null : telsetName.trim();
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile == null ? null : recordFile.trim();
    }

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getMarkData() {
        return markData;
    }

    public void setMarkData(String markData) {
        this.markData = markData == null ? null : markData.trim();
    }

    public Integer getEndReason() {
        return endReason;
    }

    public void setEndReason(Integer endReason) {
        this.endReason = endReason;
    }

    public String getGwIp() {
        return gwIp;
    }

    public void setGwIp(String gwIp) {
        this.gwIp = gwIp == null ? null : gwIp.trim();
    }

    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField == null ? null : userField.trim();
    }

    public Integer getSipCause() {
        return sipCause;
    }

    public void setSipCause(Integer sipCause) {
        this.sipCause = sipCause;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public Integer getMonitorDuration2() {
		return monitorDuration2;
	}

	public Integer getBillDuration2() {
		return billDuration2;
	}

	public Integer getBridgeDuration2() {
		return bridgeDuration2;
	}

	public Integer getTotalDuration2() {
		return totalDuration2;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public void setMonitorDuration2(Integer monitorDuration2) {
		this.monitorDuration2 = monitorDuration2;
	}

	public void setBillDuration2(Integer billDuration2) {
		this.billDuration2 = billDuration2;
	}

	public void setBridgeDuration2(Integer bridgeDuration2) {
		this.bridgeDuration2 = bridgeDuration2;
	}

	public void setTotalDuration2(Integer totalDuration2) {
		this.totalDuration2 = totalDuration2;
	}

	public String getDetailClid() {
		return detailClid;
	}

	public void setDetailClid(String detailClid) {
		this.detailClid = detailClid;
	}

	@Override
	public String toString() {
		return "CdrIb [id=" + id + ", uniqueId=" + uniqueId + ", hotline="
				+ hotline + ", detailClid=" + detailClid + ", numberTrunk="
				+ numberTrunk + ", customerNumber=" + customerNumber
				+ ", customerNumberType=" + customerNumberType
				+ ", customerAreaCode=" + customerAreaCode
				+ ", customerProvince=" + customerProvince + ", customerCity="
				+ customerCity + ", calleeNumber=" + calleeNumber
				+ ", calleeAreaCode=" + calleeAreaCode + ", startTime="
				+ startTime + ", answerTime=" + answerTime + ", dialTime="
				+ dialTime + ", bridgeTime=" + bridgeTime + ", endTime="
				+ endTime + ", monitorStartTime=" + monitorStartTime
				+ ", monitorEndTime=" + monitorEndTime + ", monitorDuration="
				+ monitorDuration + ", billDuration=" + billDuration
				+ ", bridgeDuration=" + bridgeDuration + ", totalDuration="
				+ totalDuration + ", ivrId=" + ivrId + ", ivrName=" + ivrName
				+ ", ivrFlow=" + ivrFlow + ", telsetName=" + telsetName
				+ ", recordFile=" + recordFile + ", callType=" + callType
				+ ", status=" + status + ", mark=" + mark + ", markData="
				+ markData + ", endReason=" + endReason + ", gwIp=" + gwIp
				+ ", userField=" + userField + ", sipCause=" + sipCause
				+ ", createTime=" + createTime + ", enterpriseId="
				+ enterpriseId + ", monitorDuration2=" + monitorDuration2
				+ ", billDuration2=" + billDuration2 + ", bridgeDuration2="
				+ bridgeDuration2 + ", totalDuration2=" + totalDuration2 + "]";
	}
}