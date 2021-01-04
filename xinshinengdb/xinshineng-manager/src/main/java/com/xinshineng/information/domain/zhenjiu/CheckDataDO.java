package com.xinshineng.information.domain.zhenjiu;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-11-23 18:14:30
 */
public class CheckDataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//学生id
	private Integer studentId;
	//状态0：正常1：禁止
	private Integer deleteFlag;
	//裸眼OD
	private String nakedEyeOd;
	//裸眼OS
	private String nakedEyeOs;
	//眼压OD
	private String eyePressureOd;
	//眼压OS
	private String eyePressureOs;
	//原镜视力OD
	private String formerEyeOd;
	//原镜视力OS
	private String formerEyeOs;
	//矫正视力OD
	private String correctSightOd;
	//矫正视力OS
	private String correctSightOs;
	//眼位检查近距  （1正位 2内隐斜 3外隐斜）
	private Integer checkEyePositionNear;
	//眼位检查远距（1正位 2内隐斜 3外隐斜）
	private Integer checkEyePositionFar;
	//电脑验光OD(S)
	private String sAutorefractionOd;
	//电脑验光OD(C)
	private String cAutorefractionOd;
	//电脑验光OD(A)
	private String aAutorefractionOd;
	//电脑验光OS(S)
	private String sAutorefractionOs;
	//电脑验光OS(C)
	private String cAutorefractionOs;
	//电脑验光OS(A)
	private String aAutorefractionOs;
	//综合验光OD(S)
	private String sComprehensiveOptometryOd;
	//综合验光OD(C)
	private String cComprehensiveOptometryOd;
	//综合验光OD(A)
	private String aComprehensiveOptometryOd;
	//综合验光OS(S)
	private String sComprehensiveOptometryOs;
	//综合验光OS(C)
	private String cComprehensiveOptometryOs;
	//综合验光OS(A)
	private String aComprehensiveOptometryOs;
	//worth:4点（1-2个(红) 2-3个(绿)  3-4个 4-5个(2红3绿)）
	private Integer worthFour;
	//立体视
	private String stereopsis;
	//NRA
	private String eyeNra;
	//PRA
	private String eyePra;
	//BCC
	private String eyeBcc;
	//ACA
	private String eyeAca;
	//NPC
	private String eyeNpc;
	//AMP(OD)
	private String ampOd;
	//AMP(OS)
	private String ampOs;
	//AMP(OU)
	private String ampOu;
	//AF(OD)
	private String afOd;
	//AF(OS)
	private String afOs;
	//AF(OU)
	private String afOu;
	//验光师
	private String refractionist;
	//眼轴长度(OD)
	private String axialLengthOd;
	//角膜曲率(一)OD
	private String kchmFirstOd;
	//角膜曲率(二)OD
	private String kchmSecondOd;
	//角膜直径OD
	private String cornealDiameterOd;
	//前房深度OD
	private String anteriorChamberDepthOd;
	//晶体厚度OD
	private String crystalThicknessOd;
	//玻璃体腔长度OD
	private String vitreousCavityOd;
	//眼轴长度(OS)
	private String axialLengthOs;
	//角膜曲率(一)OS
	private String kchmFirstOs;
	//角膜曲率(二)OS
	private String kchmSecondOs;
	//角膜直径OS
	private String cornealDiameterOs;
	//前房深度OS
	private String anteriorChamberDepthOs;
	//晶体厚度OS
	private String crystalThicknessOs;
	//玻璃体腔长度OS
	private String vitreousCavityOs;
	//检查者
	private String checkor;
	//检查人单位
	private String checkorCompany;
	//电脑验光日期
	private Date checkDate;
	
	private Date startmonth;
	private Date endmonth;
		
	private Date startweek;
	private Date endweek;
		
	private Date today;
		
	private Date endTime;
	private Date startTime;
	//第几次检查
	private String firstSecond;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getNakedEyeOd() {
		return nakedEyeOd;
	}
	public void setNakedEyeOd(String nakedEyeOd) {
		this.nakedEyeOd = nakedEyeOd;
	}
	public String getNakedEyeOs() {
		return nakedEyeOs;
	}
	public void setNakedEyeOs(String nakedEyeOs) {
		this.nakedEyeOs = nakedEyeOs;
	}
	public String getEyePressureOd() {
		return eyePressureOd;
	}
	public void setEyePressureOd(String eyePressureOd) {
		this.eyePressureOd = eyePressureOd;
	}
	public String getEyePressureOs() {
		return eyePressureOs;
	}
	public void setEyePressureOs(String eyePressureOs) {
		this.eyePressureOs = eyePressureOs;
	}
	public String getFormerEyeOd() {
		return formerEyeOd;
	}
	public void setFormerEyeOd(String formerEyeOd) {
		this.formerEyeOd = formerEyeOd;
	}
	public String getFormerEyeOs() {
		return formerEyeOs;
	}
	public void setFormerEyeOs(String formerEyeOs) {
		this.formerEyeOs = formerEyeOs;
	}
	public String getCorrectSightOd() {
		return correctSightOd;
	}
	public void setCorrectSightOd(String correctSightOd) {
		this.correctSightOd = correctSightOd;
	}
	public String getCorrectSightOs() {
		return correctSightOs;
	}
	public void setCorrectSightOs(String correctSightOs) {
		this.correctSightOs = correctSightOs;
	}
	public Integer getCheckEyePositionNear() {
		return checkEyePositionNear;
	}
	public void setCheckEyePositionNear(Integer checkEyePositionNear) {
		this.checkEyePositionNear = checkEyePositionNear;
	}
	public Integer getCheckEyePositionFar() {
		return checkEyePositionFar;
	}
	public void setCheckEyePositionFar(Integer checkEyePositionFar) {
		this.checkEyePositionFar = checkEyePositionFar;
	}
	public String getsAutorefractionOd() {
		return sAutorefractionOd;
	}
	public void setsAutorefractionOd(String sAutorefractionOd) {
		this.sAutorefractionOd = sAutorefractionOd;
	}
	public String getcAutorefractionOd() {
		return cAutorefractionOd;
	}
	public void setcAutorefractionOd(String cAutorefractionOd) {
		this.cAutorefractionOd = cAutorefractionOd;
	}
	public String getaAutorefractionOd() {
		return aAutorefractionOd;
	}
	public void setaAutorefractionOd(String aAutorefractionOd) {
		this.aAutorefractionOd = aAutorefractionOd;
	}
	public String getsAutorefractionOs() {
		return sAutorefractionOs;
	}
	public void setsAutorefractionOs(String sAutorefractionOs) {
		this.sAutorefractionOs = sAutorefractionOs;
	}
	public String getcAutorefractionOs() {
		return cAutorefractionOs;
	}
	public void setcAutorefractionOs(String cAutorefractionOs) {
		this.cAutorefractionOs = cAutorefractionOs;
	}
	public String getaAutorefractionOs() {
		return aAutorefractionOs;
	}
	public void setaAutorefractionOs(String aAutorefractionOs) {
		this.aAutorefractionOs = aAutorefractionOs;
	}
	public String getsComprehensiveOptometryOd() {
		return sComprehensiveOptometryOd;
	}
	public void setsComprehensiveOptometryOd(String sComprehensiveOptometryOd) {
		this.sComprehensiveOptometryOd = sComprehensiveOptometryOd;
	}
	public String getcComprehensiveOptometryOd() {
		return cComprehensiveOptometryOd;
	}
	public void setcComprehensiveOptometryOd(String cComprehensiveOptometryOd) {
		this.cComprehensiveOptometryOd = cComprehensiveOptometryOd;
	}
	public String getaComprehensiveOptometryOd() {
		return aComprehensiveOptometryOd;
	}
	public void setaComprehensiveOptometryOd(String aComprehensiveOptometryOd) {
		this.aComprehensiveOptometryOd = aComprehensiveOptometryOd;
	}
	public String getsComprehensiveOptometryOs() {
		return sComprehensiveOptometryOs;
	}
	public void setsComprehensiveOptometryOs(String sComprehensiveOptometryOs) {
		this.sComprehensiveOptometryOs = sComprehensiveOptometryOs;
	}
	public String getcComprehensiveOptometryOs() {
		return cComprehensiveOptometryOs;
	}
	public void setcComprehensiveOptometryOs(String cComprehensiveOptometryOs) {
		this.cComprehensiveOptometryOs = cComprehensiveOptometryOs;
	}
	public String getaComprehensiveOptometryOs() {
		return aComprehensiveOptometryOs;
	}
	public void setaComprehensiveOptometryOs(String aComprehensiveOptometryOs) {
		this.aComprehensiveOptometryOs = aComprehensiveOptometryOs;
	}
	public Integer getWorthFour() {
		return worthFour;
	}
	public void setWorthFour(Integer worthFour) {
		this.worthFour = worthFour;
	}
	public String getStereopsis() {
		return stereopsis;
	}
	public void setStereopsis(String stereopsis) {
		this.stereopsis = stereopsis;
	}
	public String getEyeNra() {
		return eyeNra;
	}
	public void setEyeNra(String eyeNra) {
		this.eyeNra = eyeNra;
	}
	public String getEyePra() {
		return eyePra;
	}
	public void setEyePra(String eyePra) {
		this.eyePra = eyePra;
	}
	public String getEyeBcc() {
		return eyeBcc;
	}
	public void setEyeBcc(String eyeBcc) {
		this.eyeBcc = eyeBcc;
	}
	public String getEyeAca() {
		return eyeAca;
	}
	public void setEyeAca(String eyeAca) {
		this.eyeAca = eyeAca;
	}
	public String getEyeNpc() {
		return eyeNpc;
	}
	public void setEyeNpc(String eyeNpc) {
		this.eyeNpc = eyeNpc;
	}
	public String getAmpOd() {
		return ampOd;
	}
	public void setAmpOd(String ampOd) {
		this.ampOd = ampOd;
	}
	public String getAmpOs() {
		return ampOs;
	}
	public void setAmpOs(String ampOs) {
		this.ampOs = ampOs;
	}
	public String getAmpOu() {
		return ampOu;
	}
	public void setAmpOu(String ampOu) {
		this.ampOu = ampOu;
	}
	public String getAfOd() {
		return afOd;
	}
	public void setAfOd(String afOd) {
		this.afOd = afOd;
	}
	public String getAfOs() {
		return afOs;
	}
	public void setAfOs(String afOs) {
		this.afOs = afOs;
	}
	public String getAfOu() {
		return afOu;
	}
	public void setAfOu(String afOu) {
		this.afOu = afOu;
	}
	public String getRefractionist() {
		return refractionist;
	}
	public void setRefractionist(String refractionist) {
		this.refractionist = refractionist;
	}
	public String getAxialLengthOd() {
		return axialLengthOd;
	}
	public void setAxialLengthOd(String axialLengthOd) {
		this.axialLengthOd = axialLengthOd;
	}
	public String getKchmFirstOd() {
		return kchmFirstOd;
	}
	public void setKchmFirstOd(String kchmFirstOd) {
		this.kchmFirstOd = kchmFirstOd;
	}
	public String getKchmSecondOd() {
		return kchmSecondOd;
	}
	public void setKchmSecondOd(String kchmSecondOd) {
		this.kchmSecondOd = kchmSecondOd;
	}
	public String getCornealDiameterOd() {
		return cornealDiameterOd;
	}
	public void setCornealDiameterOd(String cornealDiameterOd) {
		this.cornealDiameterOd = cornealDiameterOd;
	}
	public String getAnteriorChamberDepthOd() {
		return anteriorChamberDepthOd;
	}
	public void setAnteriorChamberDepthOd(String anteriorChamberDepthOd) {
		this.anteriorChamberDepthOd = anteriorChamberDepthOd;
	}
	public String getCrystalThicknessOd() {
		return crystalThicknessOd;
	}
	public void setCrystalThicknessOd(String crystalThicknessOd) {
		this.crystalThicknessOd = crystalThicknessOd;
	}
	public String getVitreousCavityOd() {
		return vitreousCavityOd;
	}
	public void setVitreousCavityOd(String vitreousCavityOd) {
		this.vitreousCavityOd = vitreousCavityOd;
	}
	public String getAxialLengthOs() {
		return axialLengthOs;
	}
	public void setAxialLengthOs(String axialLengthOs) {
		this.axialLengthOs = axialLengthOs;
	}
	public String getKchmFirstOs() {
		return kchmFirstOs;
	}
	public void setKchmFirstOs(String kchmFirstOs) {
		this.kchmFirstOs = kchmFirstOs;
	}
	public String getKchmSecondOs() {
		return kchmSecondOs;
	}
	public void setKchmSecondOs(String kchmSecondOs) {
		this.kchmSecondOs = kchmSecondOs;
	}
	public String getCornealDiameterOs() {
		return cornealDiameterOs;
	}
	public void setCornealDiameterOs(String cornealDiameterOs) {
		this.cornealDiameterOs = cornealDiameterOs;
	}
	public String getAnteriorChamberDepthOs() {
		return anteriorChamberDepthOs;
	}
	public void setAnteriorChamberDepthOs(String anteriorChamberDepthOs) {
		this.anteriorChamberDepthOs = anteriorChamberDepthOs;
	}
	public String getCrystalThicknessOs() {
		return crystalThicknessOs;
	}
	public void setCrystalThicknessOs(String crystalThicknessOs) {
		this.crystalThicknessOs = crystalThicknessOs;
	}
	public String getVitreousCavityOs() {
		return vitreousCavityOs;
	}
	public void setVitreousCavityOs(String vitreousCavityOs) {
		this.vitreousCavityOs = vitreousCavityOs;
	}
	public String getCheckor() {
		return checkor;
	}
	public void setCheckor(String checkor) {
		this.checkor = checkor;
	}
	public String getCheckorCompany() {
		return checkorCompany;
	}
	public void setCheckorCompany(String checkorCompany) {
		this.checkorCompany = checkorCompany;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getStartmonth() {
		return startmonth;
	}
	public void setStartmonth(Date startmonth) {
		this.startmonth = startmonth;
	}
	public Date getEndmonth() {
		return endmonth;
	}
	public void setEndmonth(Date endmonth) {
		this.endmonth = endmonth;
	}
	public Date getStartweek() {
		return startweek;
	}
	public void setStartweek(Date startweek) {
		this.startweek = startweek;
	}
	public Date getEndweek() {
		return endweek;
	}
	public void setEndweek(Date endweek) {
		this.endweek = endweek;
	}
	public Date getToday() {
		return today;
	}
	public void setToday(Date today) {
		this.today = today;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getFirstSecond() {
		return firstSecond;
	}
	public void setFirstSecond(String firstSecond) {
		this.firstSecond = firstSecond;
	}

	
	
}
