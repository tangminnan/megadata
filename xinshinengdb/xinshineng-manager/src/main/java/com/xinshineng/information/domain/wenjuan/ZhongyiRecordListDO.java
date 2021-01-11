package com.xinshineng.information.domain.wenjuan;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2020-06-17 18:16:53
 */
public class ZhongyiRecordListDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//产品id
	private Integer chanpinId;
	//产品名称
	private String chanpinName;
	//添加时间
	private Date addTime;
	//状态1：正常2：禁用
	private Integer delFlag;
	
	private String uname;
	private String sex;
	private String identityCard;
	private String minzu;
	private String birthday;
	private String school;
	private String gradeclass;
	private String address;
	private String phone;
	/**
	 *  答题结束时间
	 */
	private Date startTime;

	private List<ZhongyiRecordDetailsDO> titlelist;
	private List<ZhongyiRecordDetailsDO> titlexinxi;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public List<ZhongyiRecordDetailsDO> getTitlexinxi() {
		return titlexinxi;
	}
	public void setTitlexinxi(List<ZhongyiRecordDetailsDO> titlexinxi) {
		this.titlexinxi = titlexinxi;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getMinzu() {
		return minzu;
	}
	public void setMinzu(String minzu) {
		this.minzu = minzu;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getGradeclass() {
		return gradeclass;
	}
	public void setGradeclass(String gradeclass) {
		this.gradeclass = gradeclass;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<ZhongyiRecordDetailsDO> getTitlelist() {
		return titlelist;
	}
	public void setTitlelist(List<ZhongyiRecordDetailsDO> titlelist) {
		this.titlelist = titlelist;
	}
	public String getChanpinName() {
		return chanpinName;
	}
	public void setChanpinName(String chanpinName) {
		this.chanpinName = chanpinName;
	}
	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：产品id
	 */
	public void setChanpinId(Integer chanpinId) {
		this.chanpinId = chanpinId;
	}
	/**
	 * 获取：产品id
	 */
	public Integer getChanpinId() {
		return chanpinId;
	}
	/**
	 * 设置：添加时间
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	/**
	 * 获取：添加时间
	 */
	public Date getAddTime() {
		return addTime;
	}
	/**
	 * 设置：状态1：正常2：禁用
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：状态1：正常2：禁用
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
