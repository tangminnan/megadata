package com.xinshineng.information.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinshineng.common.utils.PageUtils;
import com.xinshineng.common.utils.Query;
import com.xinshineng.information.domain.jianhuyi.UseJianhuyiLogDO;
import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;
import com.xinshineng.information.service.jianhuyi.service.UseJianhuyiLogService;
import com.xinshineng.information.service.jianhuyi.service.xinshinengUserServiceJianhuyi;

@Controller
@RequestMapping("/xinshineng/jianhuyi")
public class jianhuyiUserController {
	
	@Autowired
	private xinshinengUserServiceJianhuyi jhy;
	@Autowired
	private UseJianhuyiLogService useJianhuyiLogService;
	
	@GetMapping("/userList")
	String User(){
	    return "information/users2/user";
	}
	
	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<jianhuyiUserDO> userList = jhy.list(query);
		int total = jhy.count(query);
		PageUtils pageUtils = new PageUtils(userList, total);
		return pageUtils;
	}
	
	@GetMapping("/detail/{id}")
	String detail(@PathVariable("id") Integer id,Model model){
		model.addAttribute("id", id);
	    return "information/users2/useJianhuyiLog";
	}
	
	@ResponseBody
	@GetMapping("/detailList")
	public PageUtils detailList(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UseJianhuyiLogDO> useJianhuyiLogList = useJianhuyiLogService.list(query);
		int total = useJianhuyiLogService.count(query);
		PageUtils pageUtils = new PageUtils(useJianhuyiLogList, total);
		return pageUtils;
	}

}
