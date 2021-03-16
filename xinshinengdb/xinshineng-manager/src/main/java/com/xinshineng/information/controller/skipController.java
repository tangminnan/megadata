package com.xinshineng.information.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xinshineng.common.utils.StringUtils;
import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.service.jigou.service.JiGouService;
import com.xinshineng.information.service.shaicha.service.ShaichaStudentService;
import com.xinshineng.information.service.shaicha.service.schoolService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions("sys:zhengfu")
    @GetMapping("/zhengfu")
    public String skipToSchool(){
        return "main";
    }
    @RequiresPermissions("sys:xuexiao")
    @GetMapping("/school")
    public String skipToSchool(Model model, String school, String CityName, String AreaName, String checkdate,String checkType,String nianji,String banji){

        Map<String, String> dataOne;
        Map<String, List> dataTwo;
        List<Map> dataThree;
        List<Map> dataFour;
        List<Map> dataFive;
        List<Map> dataSix;
        if (StringUtils.isBlank(nianji) && StringUtils.isBlank(banji)){
            dataOne = redisTemplate.opsForHash().entries(school+AreaName+checkdate+checkType+"dataOne");
            dataTwo = redisTemplate.opsForHash().entries(school+AreaName+checkdate+checkType+"dataTwo");
            dataThree = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+"dataThree",0,-1);
            dataFour = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+"dataFour",0,-1);
            dataFive = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+"dataFive",0,-1);
            dataSix = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+"dataSix",0,-1);


        }else{
            if (StringUtils.isBlank(banji)){
                dataOne = redisTemplate.opsForHash().entries(school+AreaName+checkdate+checkType+nianji+"dataOne");
                dataTwo = redisTemplate.opsForHash().entries(school+AreaName+checkdate+checkType+nianji+"dataTwo");
                dataThree = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+"dataThree",0,-1);
                dataFour = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+"dataFour",0,-1);
                dataFive = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+"dataFive",0,-1);
                dataSix = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+"dataSix",0,-1);


            }else {
                dataOne = redisTemplate.opsForHash().entries(school+AreaName+checkdate+checkType+nianji+banji+"dataOne");
                dataTwo = redisTemplate.opsForHash().entries(school+AreaName+checkdate+checkType+nianji+banji+"dataTwo");
                dataThree = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+banji+"dataThree",0,-1);
                dataFour = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+banji+"dataFour",0,-1);
                dataFive = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+banji+"dataFive",0,-1);
                dataSix = redisTemplate.opsForList().range(school+AreaName+checkdate+checkType+nianji+banji+"dataSix",0,-1);
            }
        }

        if(dataOne.isEmpty()){
            dataOne = schoolservice.dataOne(school, CityName, AreaName, checkdate,checkType,nianji,banji);
        }

        if (dataTwo.isEmpty()){
            dataTwo = schoolservice.dataTwo(school, CityName, AreaName, checkdate,checkType,nianji,banji);
        }


        if (dataThree.size()==0){
            dataThree = schoolservice.dataThree(school, CityName, AreaName, checkdate,checkType,nianji,banji);
        }


        if (dataFour.size()==0){
            dataFour = schoolservice.dataFour(school, CityName, AreaName, checkdate,checkType,nianji,banji);
        }


        if (dataFive.size()==0){
            dataFive = schoolservice.dataFive(school, CityName, AreaName, checkdate,checkType,nianji,banji);
        }

        if(dataSix.size()==0) {
            dataSix = schoolservice.dataSix(school, CityName, AreaName, checkdate, checkType,nianji,banji);
        }
        model.addAttribute("dataOne",dataOne);
        model.addAttribute("dataTwo",dataTwo);
        model.addAttribute("dataThree",dataThree);
        model.addAttribute("dataFour",dataFour);
        model.addAttribute("dataFive",dataFive);
        model.addAttribute("dataSix",dataSix);


        model.addAttribute("checkType",checkType);
        model.addAttribute("CityName",CityName);
        model.addAttribute("AreaName",AreaName);
        return "school";
    }

    @RequiresPermissions("sys:jigou")
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
    public String skipToWenJuan1(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "wenjuan1";
    }
    @GetMapping("/wenjuan2")
    public String skipToWenJuan2(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "wenjuan2";
    }
    @GetMapping("/wenjuan3")
    public String skipToWenJuan3(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "wenjuan3";
    }

    @GetMapping("/gerenchakan")
    public String skipToChaKan(Model model,String name,String idCard,String checkdate,String checkType,String age){
        if ("sc".equals(checkType)) {
            Map report = schoolservice.report(name, idCard, checkdate, checkType);
            model.addAttribute("report", report);
            return "gerenchakan";
        }else {
            Map report = schoolservice.reportld(name, idCard, checkdate, checkType,age);
            model.addAttribute("report",report);
            return "gerenliudiaochakan";
        }
    }

    @GetMapping("/jiancebiao")
    public String skipToJianCeBiao(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "jiancebiao";
    }

    @GetMapping("/jiaose/jiazhang")
    public String skipToJiaZhang(Model model,String idCard){
        model.addAttribute("idCard",idCard);
        return "jiaose-jiazhang";
    }
    @GetMapping("/jiaose/haizi")
    public String skipToHaiZi(Model model,String idCard){
        model.addAttribute("idCard",idCard);
        return "jiaose-haizi";
    }
    @GetMapping("/EyeTEAS")
    public String skipToEyeTeas(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        return "biaoge-teas";
    }
    @Autowired
    private ShaichaStudentDao shaichaStudentDao;
    @GetMapping("/success")
    public String skipToSuccess(Model model,String name,String idCard){
        model.addAttribute("name",name);
        model.addAttribute("idCard",idCard);
        String checkDate = shaichaStudentDao.getLastCheckDate(name, idCard);
        model.addAttribute("checkDate",checkDate);
        return "success";
    }

    @GetMapping("/geren")
    public String skipToGeRen(Model model, String name,String idCard,String checkdate,String checkType,String age){
        model.addAttribute("name","您好，"+name+"!");
        Map gerenAdvice  = schoolservice.gerenAdvice(name, idCard, checkdate,age,checkType);
        List<Map> table = schoolservice.table(name, idCard,checkType,age);
        Map<String,List> echarts = schoolservice.echarts(name, idCard,checkType);

        model.addAttribute("gerenAdvice",gerenAdvice);
        model.addAttribute("table",table);
        model.addAttribute("echarts",echarts);
        model.addAttribute("idCard",idCard);

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
