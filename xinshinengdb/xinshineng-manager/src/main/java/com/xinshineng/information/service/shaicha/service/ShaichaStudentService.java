package com.xinshineng.information.service.shaicha.service;


import java.util.List;
import java.util.Map;


/**
 * 学生表
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-14 17:21:00
 */
public interface ShaichaStudentService {
	


    Map<String, Object> getCount();

	Map getEveryYearCount(String checkType);

	Map manAndWomenCount(String checkCity,String checkArea);

	Map huanBingNum(String checkCity,String checkArea);

	Map developmentAndWarning(String checkType);

	Map getEveryYearCountForCity(String cityName,String checkType);

	Map developmentAndWarningForCity(String cityName,String checkType);

	Map<String, Object> getAreaCount(String checkCity, String[] areaList);

	Map<String, Object> getAreaCountForShaiCha(String checkCity, String[] areaList);

	Map<String, Object> getAreaCountForLiuDiao(String checkCity, String[] areaList);

	List<Map<String, Object>> getAreaSchoolData(String checkCity, String checkArea);

	Map<String, Object> getAreaSchoolDataForShaiCha(String checkCity, String checkArea);

	Map<String, Object> getAreaSchoolDataForLiuDiao(String checkCity, String checkArea);

	Map<String,Double> getYuCe();

	Map XueBuHuanBingLv(String checkCity);

	Map NianJiHuanBingLv(String checkCity, String checkArea);

	List<Map<String, Object>> getShiFanXiaoHuanBingLv(String checkCity);


	//Map<String, String> getTotalCheckData(Map<String,Long> totalMap);
}
