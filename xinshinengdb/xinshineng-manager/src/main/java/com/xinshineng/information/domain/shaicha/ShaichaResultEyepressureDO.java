package com.xinshineng.information.domain.shaicha;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;



/**
 * 眼内压
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public class ShaichaResultEyepressureDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer tEyepressureId;
	//学生id
	private Integer studentId;
	//眼内压检查者id
	private Integer checkorId;
	//右眼眼内压
	private Integer eyePressureOd;
	//左眼眼内压
	private Integer eyePressureOs;
	//检查日期
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date checkDate;
	//状态0：正常1：禁止
	private Integer deleteFlag;

private String identityCard;
	
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public Integer gettEyepressureId() {
		return tEyepressureId;
	}
	public void settEyepressureId(Integer tEyepressureId) {
		this.tEyepressureId = tEyepressureId;
	}
	/**
	 * 设置：id
	 */
	public void setTEyepressureId(Integer tEyepressureId) {
		this.tEyepressureId = tEyepressureId;
	}
	/**
	 * 获取：id
	 */
	public Integer getTEyepressureId() {
		return tEyepressureId;
	}
	/**
	 * 设置：学生id
	 */
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	/**
	 * 获取：学生id
	 */
	public Integer getStudentId() {
		return studentId;
	}
	/**
	 * 设置：眼内压检查者id
	 */
	public void setCheckorId(Integer checkorId) {
		this.checkorId = checkorId;
	}
	/**
	 * 获取：眼内压检查者id
	 */
	public Integer getCheckorId() {
		return checkorId;
	}
	/**
	 * 设置：右眼眼内压
	 */
	public void setEyePressureOd(Integer eyePressureOd) {
		this.eyePressureOd = eyePressureOd;
	}
	/**
	 * 获取：右眼眼内压
	 */
	public Integer getEyePressureOd() {
		return eyePressureOd;
	}
	/**
	 * 设置：左眼眼内压
	 */
	public void setEyePressureOs(Integer eyePressureOs) {
		this.eyePressureOs = eyePressureOs;
	}
	/**
	 * 获取：左眼眼内压
	 */
	public Integer getEyePressureOs() {
		return eyePressureOs;
	}
	/**
	 * 设置：检查日期
	 */
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	/**
	 * 获取：检查日期
	 */
	public Date getCheckDate() {
		return checkDate;
	}
	/**
	 * 设置：状态0：正常1：禁止
	 */
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/**
	 * 获取：状态0：正常1：禁止
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
}
