package com.xinshineng.information.controller;

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
    public String skipToSchool(Model model, String school, String CityName, String AreaName, String checkdate){

        Map<String, String> dataOne;
        dataOne = redisTemplate.opsForHash().entries(school+AreaName+checkdate+"dataOne");
        if(dataOne.isEmpty()){
            dataOne = schoolservice.dataOne(school, CityName, AreaName, checkdate);
        }
//        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+"dataOne",dataOne);

        Map<String, List> dataTwo;
        dataTwo = redisTemplate.opsForHash().entries(school+AreaName+checkdate+"dataTwo");
        if (dataTwo.isEmpty()){
            dataTwo = schoolservice.dataTwo(school, CityName, AreaName, checkdate);
        }
//        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+"dataTwo",dataTwo);

        List<Map> dataThree;
        dataThree = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataThree",0,-1);
        if (dataThree.size()==0){
            dataThree = schoolservice.dataThree(school, CityName, AreaName, checkdate);
        }
//        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataThree",dataThree);

        List<Map> dataFour;
        dataFour = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataFour",0,-1);
        if (dataFour.size()==0){
            dataFour = schoolservice.dataFour(school, CityName, AreaName, checkdate);
        }
//        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataFour",dataFour);

        List<Map> dataFive;
        dataFive = redisTemplate.opsForList().range(school+AreaName+checkdate+"dataFive",0,-1);
        if (dataFive.size()==0){
            dataFive = schoolservice.dataFive(school, CityName, AreaName, checkdate);
        }
//        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataFive",dataFive);
        model.addAttribute("dataOne",dataOne);
        model.addAttribute("dataTwo",dataTwo);
        model.addAttribute("dataThree",dataThree);
        model.addAttribute("dataFour",dataFour);
        model.addAttribute("dataFive",dataFive);
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
    public String skipToChaKan(){
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
    public String skipToGeRen(){
        return "geren";
    }
}
