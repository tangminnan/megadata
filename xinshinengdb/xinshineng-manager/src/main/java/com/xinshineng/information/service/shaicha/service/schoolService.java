package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

public interface schoolService {
    Map<String,String> dataOne(String school, String CityName, String AreaName, String checkdate);
    Map<String, List> dataTwo(String school, String CityName, String AreaName, String checkdate);
    List<Map> dataThree(String school, String CityName, String AreaName, String checkdate,String checkType);
    List<Map> dataFour(String school, String CityName, String AreaName, String checkdate);
    List<Map> dataFive(String school, String CityName, String AreaName, String checkdate);
    List<Map> dataSix(String school, String CityName, String AreaName, String checkdate);
    Map gerenAdvice ( String name,String idCard,String checkdate);
    Map report ( String name,String idCard,String checkdate,String checkType);
    List<Map> table ( String name,String idCard,String checkType);
    Map<String,List> echarts ( String name,String idCard);

    Map<String,String> dataOneld(String school, String CityName, String AreaName, String checkdate);
}
