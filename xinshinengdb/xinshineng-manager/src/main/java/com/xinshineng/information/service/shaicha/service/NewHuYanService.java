package com.xinshineng.information.service.shaicha.service;

import com.xinshineng.information.domain.yanke.StudentDO;

import java.util.List;

public interface NewHuYanService {
    List<String> getXueBuList(String school, String cityName, String areaName, String checkType);

    List<String> getGradeList(String school, String cityName, String areaName, String checkType, String xuebu);

    List<String> getClazzList(String school, String cityName, String areaName, String checkType, String xuebu, String grade);

    String getClazzJSL(String school, String cityName, String areaName, String checkType, String xuebu, String grade, String clazz);

    List<StudentDO> getClazzIdCards(String school, String cityName, String areaName, String checkType, String xuebu, String grade, String clazz);
}
