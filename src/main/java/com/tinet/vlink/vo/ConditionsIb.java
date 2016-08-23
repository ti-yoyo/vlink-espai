package com.tinet.vlink.vo;

/**
 * 搜索条件
 * @author wangguiyu
 *
 */
public class ConditionsIb {

	private String uniqueId;
	private String answerType; //为空表示全部，telBridged呼转接听，telCalled已呼转未接听 ，systemAnswer系统接听，systemNoAnswer系统未接听
	private String title;      //tsno电话组，customer_number客户电话，hotline热线号码，callee_number呼转号码
	private String titleValue; //对应title的值
	private Integer mark;      //1.留言
	private String province;
	private String city;
	private String userFiled;

	public String getUniqueId() {
		return uniqueId;
	}
	public String getAnswerType() {
		return answerType;
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
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
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
	public String getUserFiled() {
		return userFiled;
	}
	public void setUserFiled(String userFiled) {
		this.userFiled = userFiled;
	}

}
