package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

public interface schoolService {
    Map<String,String> dataOne(String school, String CityName, String AreaName, String checkdate,String checkType,String nianji,String banji);
    Map<String, List> dataTwo(String school, String CityName, String AreaName, String checkdateS,String checkType,String nianji,String banji);
    List<Map> dataThree(String school, String CityName, String AreaName, String checkdate,String checkType,String nianji,String banji);
    List<Map> dataFour(String school, String CityName, String AreaName, String checkdate,String checkType,String nianji,String banji);
    List<Map> dataFive(String school, String CityName, String AreaName, String checkdate,String checkType,String nianji,String banji);
    List<Map> dataSix(String school, String CityName, String AreaName, String checkdate,String checkType,String nianji,String banji);
    Map gerenAdvice ( String name,String idCard,String checkdate,String age,String checkType);
    Map report ( String name,String idCard,String checkdate,String checkType);
    Map reportld ( String name,String idCard,String checkdate,String checkType,String age);
    List<Map> table ( String name,String idCard,String checkType,String age);
    Map<String,List> echarts ( String name,String idCard,String checkType);
}
