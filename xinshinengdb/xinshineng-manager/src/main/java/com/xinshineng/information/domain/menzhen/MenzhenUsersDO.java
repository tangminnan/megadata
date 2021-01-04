package com.xinshineng.information.domain.menzhen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;



/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-04-24 16:22:03
 */
public class MenzhenUsersDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long uid;
	//
	private String uname;
	//单位或学校
	private String uorganization;
	//性别  0男  1女
	private Integer ugender;
	//年级或职业
	private String ugrand;
	//年龄
	private Integer uage;
	//出生日期
	private Date ubirthday;
	//身份证号
	private String uidcard;
	//联系电话
	private String uphone;
	//身高 单位cm
	private Double uheight;
	//体重 单位kg
	private Double uweight;
	//3D扫描数据存放目录
	private String ufolder;
	//负责人（医生）
	private String mname;
	//创建时间
	private Date uupdatedate;
	//二维码url
	private String QRCode;
	//头像
	private String uimg;
	//
	private MultipartFile img;
	public String formateTime(Date date) {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    	return df.format(date);
    }
	public String getUimg() {
		return uimg;
	}
	public void setUimg(String uimg) {
		this.uimg = uimg;
	}
	//excel导入
	private MultipartFile excelUser;
	
	public MultipartFile getExcelUser() {
		return excelUser;
	}
	public void setExcelUser(MultipartFile excelUser) {
		this.excelUser = excelUser;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUorganization() {
		return uorganization;
	}
	public void setUorganization(String uorganization) {
		this.uorganization = uorganization;
	}
	public Integer getUgender() {
		return ugender;
	}
	public void setUgender(Integer ugender) {
		this.ugender = ugender;
	}
	public String getUgrand() {
		return ugrand;
	}
	public void setUgrand(String ugrand) {
		this.ugrand = ugrand;
	}
	public Integer getUage() {
		return uage;
	}
	public void setUage(Integer uage) {
		this.uage = uage;
	}
	public Date getUbirthday() {
		return ubirthday;
	}
	public void setUbirthday(Date date) {
		this.ubirthday = date;
	}
	public String getUidcard() {
		return uidcard;
	}
	public void setUidcard(String uidcard) {
		this.uidcard = uidcard;
	}
	public String getUphone() {
		return uphone;
	}
	public void setUphone(String uphone) {
		this.uphone = uphone;
	}
	public Double getUheight() {
		return uheight;
	}
	public void setUheight(Double uheight) {
		this.uheight = uheight;
	}
	public Double getUweight() {
		return uweight;
	}
	public void setUweight(Double uweight) {
		this.uweight = uweight;
	}
	public String getUfolder() {
		return ufolder;
	}
	public void setUfolder(String ufolder) {
		this.ufolder = ufolder;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public Date getUupdatedate() {
		return uupdatedate;
	}
	public void setUupdatedate(Date uupdatedate) {
		this.uupdatedate = uupdatedate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "UsersDO [uid=" + uid + ", uname=" + uname + ", uorganization=" + uorganization + ", ugender=" + ugender
				+ ", ugrand=" + ugrand + ", uage=" + uage + ", ubirthday=" + ubirthday + ", uidcard=" + uidcard
				+ ", uphone=" + uphone + ", uheight=" + uheight + ", uweight=" + uweight + ", ufolder=" + ufolder
				+ ", mname=" + mname + ", uupdatedate=" + uupdatedate + ", uimg=" + uimg + ", excelUser=" + excelUser
				+ "]";
	}
	public String getQRCode() {
		return QRCode;
	}
	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}
	public MultipartFile getImg() {
		return img;
	}
	public void setImg(MultipartFile img) {
		this.img = img;
	}
	
}