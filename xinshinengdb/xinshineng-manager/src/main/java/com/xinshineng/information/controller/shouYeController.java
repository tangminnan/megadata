package com.xinshineng.information.controller;

import com.xinshineng.information.service.shaicha.service.ShaichaStudentService;
import com.xinshineng.information.service.yanke.service.StudentService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/xinshineng/shouYe")
public class shouYeController {

    @Autowired
    private ShaichaStudentService shaichaStudentService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/Data")
    @ResponseBody
    public Map shouYeData(){
        Map<String,Object> resultMap = shaichaStudentService.getCount();
        return resultMap;
    }

    @GetMapping("/AreaData")
    @ResponseBody
    public Map AreaData(String checkCity, String[] areaList){
        Map<String,Object> resultMap;
        //获取点击市中各区县筛查流调的人数
        resultMap = redisTemplate.opsForHash().entries(checkCity+"AreaData");
        if (resultMap.size()==0){
            resultMap = shaichaStudentService.getAreaCount(checkCity,areaList);
        }
        redisTemplate.opsForHash().putAll(checkCity+"AreaData",resultMap);
        return resultMap;
        //Long
    }
    @GetMapping("/SchoolData")
    @ResponseBody
    public List<Map<String, Object>> SchoolData(String checkCity, String checkArea){
        List<Map<String, Object>> mapList = shaichaStudentService.getAreaSchoolData(checkCity, checkArea);
        return mapList;

        //Long
    }

    @GetMapping("/ManAndWomenCount")
    @ResponseBody
    public Map manAndWomenCount(String checkCity,String checkArea){
        Map<String,Object> resultMap = shaichaStudentService.manAndWomenCount(checkCity,checkArea);
        return resultMap;
    }

    @GetMapping("/XueBuHuanBingLv")
    @ResponseBody
    public Map XueBuHuanBingLv(String checkCity){
        Map<String,Object> resultMap = shaichaStudentService.XueBuHuanBingLv(checkCity);
        return resultMap;
    }
    @GetMapping("NianJiHuanBingLv")
    @ResponseBody
    public Map NianJiHuanBingLv(String checkCity,String checkArea){
        Map<String,Object> resultMap = shaichaStudentService.NianJiHuanBingLv(checkCity,checkArea);
        return resultMap;
    }

    @GetMapping("/HuanBingNum")
    @ResponseBody
    public Map huanBingNum(String checkCity,String checkArea){
        Map<String,Object> resultMap = shaichaStudentService.huanBingNum(checkCity,checkArea);
        return resultMap;
    }

    @GetMapping("/ShiFanXiaoHuanBingLv")
    @ResponseBody
    public List<Map<String, Object>> ShiFanXiaoHuanBingLv(String checkCity){
        List<Map<String, Object>> mapList = shaichaStudentService.getShiFanXiaoHuanBingLv(checkCity);
        return mapList;
        //Long
    }

}
