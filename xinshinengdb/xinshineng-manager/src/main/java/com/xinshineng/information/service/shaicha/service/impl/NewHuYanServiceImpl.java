package com.xinshineng.information.service.shaicha.service.impl;

import com.xinshineng.information.dao.shaicha.NewHuYanDao;
import com.xinshineng.information.service.shaicha.service.NewHuYanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewHuYanServiceImpl implements NewHuYanService {

    @Autowired
    private NewHuYanDao newHuYanDao;

    @Override
    public List<String> getXueBuList(String school, String cityName, String areaName, String checkType) {
        List<String> xueBuList = new ArrayList<>();
        if ("sc".equals(checkType)){
             xueBuList = newHuYanDao.getXueBuList(school,cityName,areaName);
            return xueBuList;
        }
        if ("ld".equals(checkType)){
            xueBuList = newHuYanDao.getXueBuListForLd(school,cityName,areaName);
            return xueBuList;
        }

        return xueBuList;
    }

    @Override
    public List<String> getGradeList(String school, String cityName, String areaName, String checkType, String xuebu) {
        List<String> gradeList = new ArrayList<>();
        if ("sc".equals(checkType)){
            gradeList = newHuYanDao.getGradeList(school,cityName,areaName,xuebu);
            return gradeList;
        }
        if ("ld".equals(checkType)){
            gradeList = newHuYanDao.getGradeListForLd(school,cityName,areaName,xuebu);
            return gradeList;
        }

        return gradeList;
    }

    @Override
    public List<String> getClazzList(String school, String cityName, String areaName, String checkType, String xuebu, String grade) {
        List<String> clazzList = new ArrayList<>();
        if ("sc".equals(checkType)){
            clazzList = newHuYanDao.getClazzList(school,cityName,areaName,xuebu,grade);
            return clazzList;
        }
        if ("ld".equals(checkType)){
            clazzList = newHuYanDao.getClazzListForLd(school,cityName,areaName,xuebu,grade);
            return clazzList;
        }
        return clazzList;
    }

    @Override
    public String getClazzJSL(String school, String cityName, String areaName, String checkType, String xuebu, String grade, String clazz) {
        DecimalFormat df = new DecimalFormat("0.0");
        if ("sc".equals(checkType)){
            Long clazzNum = newHuYanDao.getClazzNum(school,cityName,areaName,xuebu,grade,clazz);
            Long clazzJSNum = newHuYanDao.getClazzJSNum(school,cityName,areaName,xuebu,grade,clazz);
            if (clazzNum == 0 || clazzJSNum ==0){
                return "0";
            }
            BigDecimal bg = new BigDecimal((float) clazzJSNum / clazzNum);
            String classJSL = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            return classJSL;
        }
        if ("ld".equals(checkType)){
            Long clazzNum = newHuYanDao.getClazzNumForLd(school,cityName,areaName,xuebu,grade,clazz);
            Long clazzJSNum = newHuYanDao.getClazzJSNumForLd(school,cityName,areaName,xuebu,grade,clazz);
            if (clazzNum == 0 || clazzJSNum ==0){
                return "0";
            }
            BigDecimal bg = new BigDecimal((float) clazzJSNum / clazzNum);
            String classJSL = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            return classJSL;
        }
        return "0";
    }

    @Override
    public List<String> getClazzIdCards(String school, String cityName, String areaName, String checkType, String xuebu, String grade, String clazz) {
        List<String> clazzIdCards = newHuYanDao.getClazzIdCards(school,cityName,areaName,xuebu,grade,clazz);
        return clazzIdCards;
    }
}
