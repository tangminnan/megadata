package com.xinshineng.information.dao.shaicha;


import java.util.Date;
import java.util.List;
import java.util.Map;

import groovy.util.ObjectGraphBuilder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xinshineng.information.domain.shaicha.ShaichaAnswerResultDO;
import com.xinshineng.information.domain.shaicha.ShaichaResultDiopterDO;
import com.xinshineng.information.domain.shaicha.ShaichaResultEyeaxisDO;
import com.xinshineng.information.domain.shaicha.ShaichaResultEyepressureDO;
import com.xinshineng.information.domain.shaicha.ShaichaResultEyesightDO;
import com.xinshineng.information.domain.shaicha.ShaichaStudentDO;
import org.springframework.security.access.method.P;

/**
 * 学生表
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-14 17:21:00
 */
@Mapper
public interface ShaichaStudentDao {

	ShaichaStudentDO get(Integer id);
	
	List<ShaichaStudentDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaStudentDO student);
	
	int update(ShaichaStudentDO student);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	List<ShaichaStudentDO> getList();

	void saveAnswer(ShaichaAnswerResultDO answerResultDO);

	List<ShaichaAnswerResultDO> listDati(Map<String, Object> map);

	int countDati(Map<String, Object> map);
	/**
		导出最新的检测视力
	 */
	List<ShaichaResultEyesightDO> getLatestResultEyesightDO(@Param("studentId") Integer studentId,@Param("lastCheckTime") Date lastCheckTime);
	/**
	 获取电脑验光最新数据
	 */
	List<ShaichaResultDiopterDO> getLatestResultDiopterDOListL(@Param("studentId") Integer studentId,@Param("lastCheckTime") Date lastCheckTime,@Param("ifRL") String ifRL);
	/**
	 * 获取最新的眼内压数据
	 */
	List<ShaichaResultEyepressureDO> getLatestResultEyepressureDO(@Param("studentId") Integer studentId,@Param("lastCheckTime") Date lastCheckTime);

	/**
	 * 获取最新的眼轴长度检测数据
	 */
	List<ShaichaResultEyeaxisDO> getLatelestResultEyeaxisDO(@Param("studentId") Integer studentId,@Param("lastCheckTime") Date lastCheckTime);

//	Map<String,Object> getShaiChaTotalOld();

	List<Map<String,Long>> getShaiChaTotalnew();

	List<Map<String,Long>> getShaiChaTotalOld();

    List<Map<String, Object>> getOldCheckData();
    List<Map<String, Object>> getOldCheckDataForCity(String cityName);

	List<Map<String, Object>> getNewCheckData();
	List<Map<String, Object>> getNewCheckDataForCity(String cityName);

    Long getShaiChaNewTotalNum();

	Long getShaiChaOldTotalNum();

    Long getShaiChaNewEveryYearCount();
    Long getShaiChaNewEveryYearCountForCity(String cityName);

	List<Map<String, Long>> getShaiChaOldEveryYearCount();
	List<Map<String, Long>> getShaiChaOldEveryYearCountForCity(String cityName);

	List<Map<String, Object>> getNewSexNum();
	List<Map<String, Object>> getNewSexNumForCity(String cityName);

	List<Map<String, Object>> getOldSexNum();
	List<Map<String, Object>> getOldSexNumForCity(String cityName);

	Integer getNewCount();
	Integer getNewCountForCity(String cityName);

	Integer getOldCount();
	Integer getOldCountForCity(String cityName);

	List<Map<String, Object>> getDevelopmentAndWarningDataForNew();
	List<Map<String, Object>> getDevelopmentAndWarningDataForNewForCity(String cityName);

	List<Map<String, Object>> getDevelopmentAndWarningDataForOld();
	List<Map<String, Object>> getDevelopmentAndWarningDataForOldForCity(String cityName);

	List<Map<String, Long>> getOldAreaCount(String checkCity);

	List<Map<String, Long>> getNewAreaCount(String checkCity);

	List<Map<String, Object>> getOldSchoolNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	List<Map<String, Object>> getNewSchoolNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	List<Map<String, Object>> getSchoolCheckData(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	Integer getSchoolCheckDataNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getSchoolCheckDataForNew(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	Integer getSchoolCheckDataNumForNew(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getEveryYearCount();


	List<Map<String, Object>> get2018CheckData();

	List<Map<String, Object>> get2019CheckData();

	List<Map<String, Object>> get2020CheckData();


    Integer getManNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	Integer getJinShiManNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	Integer getWomenNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	Integer getJinShiWomenNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	List<Map<String,Object>> getEveryXueBuNum(@Param("checkCity") String checkCity);

	List<Map<String,Object>> getEveryXueBuHuanBingNum(@Param("checkCity") String checkCity);

	List<Map<String, Object>> getNianJiNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	List<Map<String, Object>> getNianJiHuanBingNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	List<Map<String, Object>> getSchoolNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	Long getSchoolLCNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("school") String school,@Param("check_time") String check_time);
	Long getSchoolZXNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("school") String school,@Param("check_time") String check_time);
	Long getSchoolJXNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("school") String school,@Param("check_time") String check_time);

	Long getCityOrAreaNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	Long getCityOrAreaSLDXNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);
	Long getCityOrAreaYSNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);
	Long getCityOrAreaLCQQNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);
	Long getCityOrAreaJXJSNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);
	Long getCityOrAreaZXJSNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

	List<Map<String, Object>> getShiFanXiaoNumList(String checkCity);

	Long getSchoolHuanBingNum(@Param("checkCity") String checkCity,@Param("school") String school);

	Integer schoolAllNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName);
	Integer schoolThisNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Integer illNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Integer sexThisNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate,@Param("sex")Integer sex);
	List<Map> everyCheck(@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName);
	Integer mildNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Integer moderateNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Integer highlyNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Integer lcqqNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Integer jxjsNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	List<Map> studentList (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	String getXueBu (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	List<Map> gradeNumber (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	List<Map> paiMing (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	List<Map> gradeLv (@Param("school")String school,@Param("CityName")String CityName,@Param("AreaName")String AreaName,@Param("checkdate")String checkdate);
	Map advice (@Param("name")String name, @Param("idCard")String idCard,@Param("checkdate")String checkdate);
	List<Map> table (@Param("name")String name, @Param("idCard")String idCard);
}
