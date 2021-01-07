package com.xinshineng.information.controller;

import com.xinshineng.information.service.shaicha.service.NewHuYanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/Newhuyan")
public class newHuYanController {

    @Autowired
    private NewHuYanService newHuYanService;

    @GetMapping("/XueBu")
    @ResponseBody
    public List<String> getXueBuList(String school, String CityName, String AreaName, String checkType){
        List<String> xueBuList =  newHuYanService.getXueBuList(school,CityName,AreaName,checkType);

        return xueBuList;
    }

    @GetMapping("/Grade")
    @ResponseBody
    public List<String> getGradeList(String school, String CityName, String AreaName, String checkType,String xuebu){
        List<String> xueBuList =  newHuYanService.getGradeList(school,CityName,AreaName,checkType,xuebu);
        return xueBuList;
    }


    @GetMapping("/Clazz")
    @ResponseBody
    public List<String> getClazzList(String school, String CityName, String AreaName, String checkType,String xuebu,String grade){
        List<String> clazzList =  newHuYanService.getClazzList(school,CityName,AreaName,checkType,xuebu,grade);
        return clazzList;
    }

    @GetMapping("/ClazzJSL")
    @ResponseBody
    public String getClazzJSL(String school, String CityName, String AreaName, String checkType,String xuebu,String grade,String clazz){
        String  ClazzJSL =  newHuYanService.getClazzJSL(school,CityName,AreaName,checkType,xuebu,grade,clazz);
        return ClazzJSL;
    }
}
