package com.xinshineng.information.service.shaicha.service;

import java.util.List;

public interface NewHuYanService {
    List<String> getXueBuList(String school, String cityName, String areaName, String checkType);

    List<String> getGradeList(String school, String cityName, String areaName, String checkType, String xuebu);

    List<String> getClazzList(String school, String cityName, String areaName, String checkType, String xuebu, String grade);

    String getClazzJSL(String school, String cityName, String areaName, String checkType, String xuebu, String grade, String clazz);

    List<String> getClazzIdCards(String school, String cityName, String areaName, String checkType, String xuebu, String grade, String clazz);
}
