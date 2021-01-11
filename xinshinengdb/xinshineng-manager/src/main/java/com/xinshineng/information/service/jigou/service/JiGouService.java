package com.xinshineng.information.service.jigou.service;

import java.util.List;
import java.util.Map;

public interface JiGouService {
    Map getOneData(Integer sys_id);

    Map getTwoData(Integer sys_id);

    Map getThreeData(Integer sys_id);

    Map getFourData(Integer sys_id);

    List<Map> getSchoolList(Integer sys_id);

    List<Map> getGerenList(Integer sys_id);

    List<Map> getEyeaxisList(Integer sys_id);

    List<Map> getCornealList(Integer sys_id);
}
