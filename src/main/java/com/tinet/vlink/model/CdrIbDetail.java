package com.tinet.vlink.model;

import java.util.Date;

public class CdrIbDetail {
    private Integer id;

    private String uniqueId;

    private String detailClid;

    private String mainUniqueId;

    private String calleeNumber;

    private Long startTime;

    private Long answerTime;

    private Long endTime;

    private Integer billDuration;

    private Integer totalDuration;

    private Integer callType;

    private Integer status;

    private String gwIp;

    private Integer endReason;

    private Integer sipCause;

    private String recordFile;

    private Date createTime;

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



    public String getMainUniqueId() {
        return mainUniqueId;
    }

    public void setMainUniqueId(String mainUniqueId) {
        this.mainUniqueId = mainUniqueId == null ? null : mainUniqueId.trim();
    }

    public String getCalleeNumber() {
        return calleeNumber;
    }

    public void setCalleeNumber(String calleeNumber) {
        this.calleeNumber = calleeNumber == null ? null : calleeNumber.trim();
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(Integer billDuration) {
        this.billDuration = billDuration;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
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

    public String getGwIp() {
        return gwIp;
    }

    public void setGwIp(String gwIp) {
        this.gwIp = gwIp == null ? null : gwIp.trim();
    }

    public Integer getEndReason() {
        return endReason;
    }

    public void setEndReason(Integer endReason) {
        this.endReason = endReason;
    }

    public Integer getSipCause() {
        return sipCause;
    }

    public void setSipCause(Integer sipCause) {
        this.sipCause = sipCause;
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile == null ? null : recordFile.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getDetailClid() {
		return detailClid;
	}

	public void setDetailClid(String detailClid) {
		this.detailClid = detailClid;
	}

	@Override
	public String toString() {
		return "CdrIbDetail [id=" + id + ", uniqueId=" + uniqueId
				+ ", detailClid=" + detailClid + ", mainUniqueId="
				+ mainUniqueId + ", calleeNumber=" + calleeNumber
				+ ", startTime=" + startTime + ", answerTime=" + answerTime
				+ ", endTime=" + endTime + ", billDuration=" + billDuration
				+ ", totalDuration=" + totalDuration + ", callType=" + callType
				+ ", status=" + status + ", gwIp=" + gwIp + ", endReason="
				+ endReason + ", sipCause=" + sipCause + ", recordFile="
				+ recordFile + ", createTime=" + createTime + "]";
	}
}