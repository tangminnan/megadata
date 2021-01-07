package com.xinshineng.information.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xinshineng.information.service.shaicha.service.schoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/skip")
public class skipController {
    @Autowired
    private schoolService schoolservice;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/school")
    public String skipToSchool(Model model, String school, String CityName, String AreaName, String checkdate,String checkType){
        System.out.println(checkType);
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
        return "school";
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


        return "gerenchakan";
    }

    @GetMapping("/jiancebiao")
    public String skipToJianCeBiao(){
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
    public String skipToEyeTeas(){
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
}
