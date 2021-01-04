package com.xinshineng.information.domain.zhenjiu;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-04-01 21:32:50
 */
public class DataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//治疗时长
	private Integer treatTime;
	//治疗时间
	private Date adddate;
	//用户
	private Integer userid;
	//治疗强度
	private Integer treatStrength;
	//治疗频率
	private Integer treatFrequency;
	//治疗波形
	private Integer treatWaveform;
	//治疗方式
	private Integer treatWorkmethod;
	//治疗电极
	private Integer treatElectrode;
	//
	private Date updateTime;
	//
	private String name;
	//
	private Integer count;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：治疗时长
	 */
	public void setTreatTime(Integer treatTime) {
		this.treatTime = treatTime;
	}
	/**
	 * 获取：治疗时长
	 */
	public Integer getTreatTime() {
		return treatTime;
	}
	/**
	 * 设置：治疗时间
	 */
	public void setAdddate(Date adddate) {
		this.adddate = adddate;
	}
	/**
	 * 获取：治疗时间
	 */
	public Date getAdddate() {
		return adddate;
	}
	/**
	 * 设置：用户
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	/**
	 * 获取：用户
	 */
	public Integer getUserid() {
		return userid;
	}
	/**
	 * 设置：治疗强度
	 */
	public void setTreatStrength(Integer treatStrength) {
		this.treatStrength = treatStrength;
	}
	/**
	 * 获取：治疗强度
	 */
	public Integer getTreatStrength() {
		return treatStrength;
	}
	/**
	 * 设置：治疗频率
	 */
	public void setTreatFrequency(Integer treatFrequency) {
		this.treatFrequency = treatFrequency;
	}
	/**
	 * 获取：治疗频率
	 */
	public Integer getTreatFrequency() {
		return treatFrequency;
	}
	/**
	 * 设置：治疗波形
	 */
	public void setTreatWaveform(Integer treatWaveform) {
		this.treatWaveform = treatWaveform;
	}
	/**
	 * 获取：治疗波形
	 */
	public Integer getTreatWaveform() {
		return treatWaveform;
	}
	/**
	 * 设置：治疗方式
	 */
	public void setTreatWorkmethod(Integer treatWorkmethod) {
		this.treatWorkmethod = treatWorkmethod;
	}
	/**
	 * 获取：治疗方式
	 */
	public Integer getTreatWorkmethod() {
		return treatWorkmethod;
	}
	/**
	 * 设置：治疗电极
	 */
	public void setTreatElectrode(Integer treatElectrode) {
		this.treatElectrode = treatElectrode;
	}
	/**
	 * 获取：治疗电极
	 */
	public Integer getTreatElectrode() {
		return treatElectrode;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * 获取：
	 */
	public Integer getCount() {
		return count;
	}
	@Override
	public String toString() {
		return "DataDO [id=" + id + ", treatTime=" + treatTime + ", adddate=" + adddate + ", userid=" + userid
				+ ", treatStrength=" + treatStrength + ", treatFrequency="
				+ treatFrequency + ", treatWaveform=" + treatWaveform + ", treatWorkmethod=" + treatWorkmethod
				+ ", treatElectrode=" + treatElectrode + ", updateTime=" + updateTime + ", name=" + name + ", count="
				+ count + "]";
	}
	
}
