package com.xinshineng.information.dao.shaicha;

import com.xinshineng.information.domain.yanke.StudentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewHuYanDao {
    List<String> getXueBuList(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName);

    List<String> getGradeList(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu);

    List<String> getClazzList(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu,@Param("grade") String grade);

    Long getClazzNum(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu,@Param("grade") String grade,@Param("clazz") String clazz);

    Long getClazzJSNum(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu,@Param("grade") String grade,@Param("clazz") String clazz);

    List<String> getXueBuListForLd(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName);

    List<String> getGradeListForLd(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu);

    List<String> getClazzListForLd(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu,@Param("grade") String grade);

    Long getClazzNumForLd(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu,@Param("grade") String grade,@Param("clazz") String clazz);

    Long getClazzJSNumForLd(@Param("school") String school,@Param("cityName") String cityName,@Param("areaName") String areaName,@Param("xuebu") String xuebu,@Param("grade") String grade,@Param("clazz") String clazz);

    List<StudentDO> getClazzIdCards(@Param("school") String school, @Param("cityName") String cityName, @Param("areaName") String areaName, @Param("xuebu") String xuebu, @Param("grade") String grade, @Param("clazz") String clazz);
}
