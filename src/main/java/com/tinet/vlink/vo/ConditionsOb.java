package com.tinet.vlink.vo;


/**
 * 搜索条件
 * @author wangguiyu
 *
 */
public class ConditionsOb {

	private String uniqueId;
	private String requestUniqueId;//外呼条件
	private Integer callType;//为空：表全部   2.weball
	private Integer status;//为空表示全部，21:webcall未接通 22:webcall已接通 23:webcall已呼转24:webcall呼转已桥接
	private String title;//callee_number 呼转号码,customer_number客户号码,clid外显号码
	private String titleValue;//对应title的值
	private Integer mark;//1.留言2.AMD 4按键 8这次呼叫属于试呼叫
	private String province;
	private String city;
	private String filterRetry;//String类型 true:过滤 false:不过滤 默认为false
	private String userField;

	public String getUniqueId() {
		return uniqueId;
	}
	public String getRequestUniqueId() {
		return requestUniqueId;
	}
	public Integer getCallType() {
		return callType;
	}
	public Integer getStatus() {
		return status;
	}
	public String getTitle() {
		return title;
	}
	public String getTitleValue() {
		return titleValue;
	}
	public Integer getMark() {
		return mark;
	}
	public String getProvince() {
		return province;
	}
	public String getCity() {
		return city;
	}
	public String getFilterRetry() {
		return filterRetry;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public void setRequestUniqueId(String requestUniqueId) {
		this.requestUniqueId = requestUniqueId;
	}
	public void setCallType(Integer callType) {
		this.callType = callType;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTitleValue(String titleValue) {
		this.titleValue = titleValue;
	}
	public void setMark(Integer mark) {
		this.mark = mark;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setFilterRetry(String filterRetry) {
		this.filterRetry = filterRetry;
	}
	public String getUserField() {
		return userField;
	}
	public void setUserField(String userField) {
		this.userField = userField;
	}

}
