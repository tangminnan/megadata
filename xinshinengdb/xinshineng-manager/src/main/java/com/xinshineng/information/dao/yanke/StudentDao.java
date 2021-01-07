package com.xinshineng.information.dao.yanke;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.yanke.AnswerResultDO;
import com.xinshineng.information.domain.yanke.StudentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;


/**
 * 学生表
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-14 17:21:00
 */
@Mapper
public interface StudentDao {

	StudentDO get(Integer id);
	
	List<StudentDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(StudentDO student);
	
	int update(StudentDO student);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	List<StudentDO> getList();

	void saveAnswer(AnswerResultDO answerResultDO);

	List<AnswerResultDO> listDati(Map<String, Object> map);

	int countDati(Map<String, Object> map);

	List<Map<String,Long>> getLiuDiaoTotalOld();

	List<Map<String,Long>> getLiuDiaoTotalNew();

    List<Map<String, Object>> getNewCheckData();
    List<Map<String, Object>> getNewCheckDataForCity(String cityName);

	List<Map<String, Object>> getOldCheckData();
	List<Map<String, Object>> getOldCheckDataForCity(String cityName);

    Long getLiuDiaoNewTotalNum();

	Long getLiuDiaoOldTotalNum();

    Long getLiuDiaoNewEveryYearCount();
    Long getLiuDiaoNewEveryYearCountForCity(String cityName);

	List<Map<String, Long>> getLiuDiaoOldEveryYearCount();
	List<Map<String, Long>> getLiuDiaoOldEveryYearCountForCity(String cityName);

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

	List<Map<String, Object>> getOldSchoolNum(@Param("checkCity") String checkCity, @Param("checkArea") String checkArea);

	List<Map<String, Object>> getNewSchoolNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

    List<Map<String, Object>> getOldSchoolCheckDataForLF(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getOldSchoolCheckDataForRF(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getNewSchoolCheckDataForLF(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getNewSchoolCheckDataForRF(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getOldSchoolCheckDataForLS(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getOldSchoolCheckDataForRS(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getNewSchoolCheckDataForLS(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getNewSchoolCheckDataForRS(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("orgName") String orgName);

	List<Map<String, Object>> getEveryYearCount();

	List<Map<String, Object>> get2012CheckDataFL();

	List<Map<String, Object>> get2012CheckDataFR();

	List<Map<String, Object>> get2012CheckDataSL();

	List<Map<String, Object>> get2012CheckDataSR();

	List<Map<String, Object>> get2013CheckDataFL();

	List<Map<String, Object>> get2013CheckDataFR();

	List<Map<String, Object>> get2013CheckDataSL();

	List<Map<String, Object>> get2013CheckDataSR();

	List<Map<String, Object>> get2014CheckDataFL();

	List<Map<String, Object>> get2014CheckDataFR();

	List<Map<String, Object>> get2014CheckDataSL();

	List<Map<String, Object>> get2014CheckDataSR();

	List<Map<String, Object>> get2015CheckDataFL();

	List<Map<String, Object>> get2015CheckDataFR();

	List<Map<String, Object>> get2015CheckDataSL();

	List<Map<String, Object>> get2015CheckDataSR();

	List<Map<String, Object>> get2018CheckDataFL();

	List<Map<String, Object>> get2018CheckDataFR();

	List<Map<String, Object>> get2018CheckDataSL();

	List<Map<String, Object>> get2018CheckDataSR();

	List<Map<String, Object>> get2019CheckDataFL();

	List<Map<String, Object>> get2019CheckDataFR();

	List<Map<String, Object>> get2019CheckDataSL();

	List<Map<String, Object>> get2019CheckDataSR();

	List<Map<String, Object>> get2020CheckDataFL();

	List<Map<String, Object>> get2020CheckDataFR();

	List<Map<String, Object>> get2020CheckDataSL();

	List<Map<String, Object>> get2020CheckDataSR();


	List<Map<String, Object>> get2020YuCeData();

    List<Map<String, Object>> getData();
    Map<String,Object> getAvg();

	void addYuce(@Param("id") String id,@Param("y1Y") double y1Y,@Param("y2Y") double y2Y,@Param("ifRL") String ifRl);

	Integer getNumber();
	List<Map<String, Object>> getSchoolNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea);

    Long getSchoolLCNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("school") String school, @Param("check_time") String check_time);
	Long getSchoolZXNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("school") String school, @Param("check_time") String check_time);
	Long getSchoolJXNum(@Param("checkCity") String checkCity,@Param("checkArea") String checkArea,@Param("school") String school, @Param("check_time") String check_time);

}
