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
public class correctController {

    @GetMapping("/Newzidingyi")
    public String Newzidingyi(Model model,String idCard,String giftId){
        model.addAttribute("idCard",idCard);
        model.addAttribute("giftId",giftId);
        return "Newzidingyi";
    }


}
