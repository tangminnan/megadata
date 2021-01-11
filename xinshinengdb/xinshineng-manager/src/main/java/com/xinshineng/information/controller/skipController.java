package com.xinshineng.information.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xinshineng.information.service.jigou.service.JiGouService;
import com.xinshineng.information.service.shaicha.service.schoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/skip")
public class skipController {
    @Autowired
    private schoolService schoolservice;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JiGouService jiGouService;

    @GetMapping("/school")
    public String skipToSchool(Model model, String school, String CityName, String AreaName, String checkdate,String checkType){
        if ("sc".equals(checkType)){
        Map<String, String> dataOne;
        dataOne = redisTemplate.opsForHash().entries(school+AreaName+checkdate+"dataOne");
        if(dataOne.isEmpty()){
            dataOne = schoolservice.dataOne(school, CityName, AreaName, checkdate);
        }

        Map<String, List> dataTwo;
        dataTwo = redisTemplate.opsForHash().entries(school+AreaName+checkdate+"dataTwo");
        if (dataTwo.isEmpty()){
            dataTwo = schoolservice.dataTwo(school, CityName, AreaName, checkdate);
        }

        List<Map> dataThree;
        dataThree = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataThree",0,-1);
        if (dataThree.size()==0){
            dataThree = schoolservice.dataThree(school, CityName, AreaName, checkdate,checkType);
        }

        List<Map> dataFour;
        dataFour = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataFour",0,-1);
        if (dataFour.size()==0){
            dataFour = schoolservice.dataFour(school, CityName, AreaName, checkdate);
        }

        List<Map> dataFive;
        dataFive = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataFive",0,-1);
        if (dataFive.size()==0){
            dataFive = schoolservice.dataFive(school, CityName, AreaName, checkdate);
        }
        List<Map> dataSix;
        dataSix = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataSix",0,-1);
        if(dataSix.size()==0){
            dataSix = schoolservice.dataSix(school, CityName, AreaName, checkdate);
        }
        model.addAttribute("dataOne",dataOne);
        model.addAttribute("dataTwo",dataTwo);
        model.addAttribute("dataThree",dataThree);
        model.addAttribute("dataFour",dataFour);
        model.addAttribute("dataFive",dataFive);
        model.addAttribute("dataSix",dataSix);
        }else if ("ld".equals(checkType)){
            Map<String, String> dataOne;
            dataOne = redisTemplate.opsForHash().entries(school+AreaName+checkdate+"dataOneld");
            if(dataOne.isEmpty()){
                dataOne = schoolservice.dataOneld(school, CityName, AreaName, checkdate);
            }
            model.addAttribute("dataOne",dataOne);
        }

        model.addAttribute("checkType",checkType);
        model.addAttribute("CityName",CityName);
        model.addAttribute("AreaName",AreaName);
        return "school";
    }

    @GetMapping("/jigou")
    public String skipToJiGou(Model model,Integer sys_id){
        Map oneData = redisTemplate.opsForHash().entries(sys_id + "JiGouMainData");
        if (oneData.isEmpty()){
            oneData = jiGouService.getOneData(sys_id);
            redisTemplate.opsForHash().putAll(sys_id + "JiGouMainData",oneData);
        }
        model.addAttribute("oneData",oneData);
        Map twoData = redisTemplate.opsForHash().entries(sys_id + "JiGouLeftData");
        if (twoData.isEmpty()){
            twoData = jiGouService.getTwoData(sys_id);
            redisTemplate.opsForHash().putAll(sys_id + "JiGouLeftData",twoData);
        }
        model.addAttribute("twoData",twoData);
        Map threeData = redisTemplate.opsForHash().entries(sys_id + "JiGouJsData");
        if (threeData.isEmpty()){
            threeData = jiGouService.getThreeData(sys_id);
            redisTemplate.opsForHash().putAll(sys_id + "JiGouJsData",threeData);
        }
        model.addAttribute("threeData",threeData);
        Map fourData = redisTemplate.opsForHash().entries(sys_id + "JiGouAgeData");
        if (fourData.isEmpty()){
            fourData = jiGouService.getFourData(sys_id);
            redisTemplate.opsForHash().putAll(sys_id + "JiGouAgeData",fourData);
        }
        model.addAttribute("fourData",fourData);

        Boolean hasSchool = redisTemplate.hasKey(sys_id + "SchoolList");
        List<Map> JGSchoolList = new ArrayList<>();
        if (!hasSchool){
            JGSchoolList = jiGouService.getSchoolList(sys_id);
            redisTemplate.opsForList().rightPushAll(sys_id + "SchoolList",JGSchoolList);
        }
        if (hasSchool){
            JGSchoolList = redisTemplate.opsForList().range(sys_id + "SchoolList", 0, -1);
        }
        model.addAttribute("JGSchoolList",JGSchoolList);

        Boolean hasGerenList = redisTemplate.hasKey(sys_id + "GerenList");
        List<Map> gerenList = new ArrayList<>();
        if (!hasGerenList){
            gerenList = jiGouService.getGerenList(sys_id);
            redisTemplate.opsForList().rightPushAll(sys_id + "GerenList",gerenList);
        }
        if (hasGerenList){
            gerenList = redisTemplate.opsForList().range(sys_id + "GerenList", 0, -1);
        }
        model.addAttribute("gerenList",gerenList);

        Boolean hasEyeaxisList = redisTemplate.hasKey(sys_id + "eyeaxisList");
        List<Map> eyeaxisList = new ArrayList<>();
        if (!hasEyeaxisList){
            eyeaxisList = jiGouService.getEyeaxisList(sys_id);
            redisTemplate.opsForList().rightPushAll(sys_id + "eyeaxisList",eyeaxisList);
        }
        if (hasEyeaxisList){
            eyeaxisList = redisTemplate.opsForList().range(sys_id + "eyeaxisList", 0, -1);
        }
        model.addAttribute("eyeaxisList",eyeaxisList);

        Boolean hasCornealList = redisTemplate.hasKey(sys_id + "cornealList");
        List<Map> cornealList = new ArrayList<>();
        if (!hasCornealList){
            cornealList = jiGouService.getCornealList(sys_id);
            redisTemplate.opsForList().rightPushAll(sys_id + "cornealList",cornealList);
        }
        if (hasCornealList){
            cornealList = redisTemplate.opsForList().range(sys_id + "cornealList", 0, -1);
        }
        model.addAttribute("cornealList",cornealList);

        return "jigou";
    }

    @GetMapping("/wenjuan1")
    public String skipToWenJuan1(){
        return "wenjuan1";
    }
    @GetMapping("/wenjuan2")
    public String skipToWenJuan2(){
        return "wenjuan2";
    }
    @GetMapping("/wenjuan3")
    public String skipToWenJuan3(){
        return "wenjuan3";
    }

    @GetMapping("/gerenchakan")
    public String skipToChaKan(Model model,String name,String idCard,String checkdate,String checkType){
        Map report = schoolservice.report(name, idCard, checkdate, checkType);
        model.addAttribute("report",report);
        return "gerenchakan";
    }

    @GetMapping("/jiancebiao")
    public String skipToJianCeBiao(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "jiancebiao";
    }

    @GetMapping("/jiaose/jiazhang")
    public String skipToJiaZhang(){
        return "jiaose-jiazhang";
    }
    @GetMapping("/jiaose/haizi")
    public String skipToHaiZi(){
        return "jiaose-haizi";
    }
    @GetMapping("/EyeTEAS")
    public String skipToEyeTeas(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "biaoge-teas";
    }
    @GetMapping("/success")
    public String skipToSuccess(){
        return "success";
    }

    @GetMapping("/geren")
    public String skipToGeRen(Model model, String name,String idCard,String checkdate,String checkType){
        model.addAttribute("name","您好，"+name+"!");
        Map gerenAdvice  = schoolservice.gerenAdvice(name, idCard, checkdate);
        List<Map> table = schoolservice.table(name, idCard,checkType);
        Map<String,List> echarts = schoolservice.echarts(name, idCard);

        model.addAttribute("gerenAdvice",gerenAdvice);
        model.addAttribute("table",table);
        model.addAttribute("echarts",echarts);

        return "geren";
    }
    @GetMapping("/Newhuyan")
    public String skipToNewhuyan(Model model,String school, String CityName, String AreaName, String checkType){
        model.addAttribute("CityName",CityName);
        model.addAttribute("AreaName",AreaName);
        model.addAttribute("checkType",checkType);
        model.addAttribute("school",school);
        return "NEWrenwu";
    }

}
