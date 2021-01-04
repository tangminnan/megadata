package com.xinshineng.information.controller;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.xinshineng.common.utils.PageUtils;
import com.xinshineng.common.utils.Query;
import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;
import com.xinshineng.information.domain.yanke.StudentDO;
import com.xinshineng.information.domain.zhenjiu.CheckDataDO;
import com.xinshineng.information.domain.zhenjiu.DataDO;
import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;
import com.xinshineng.information.service.jianhuyi.service.xinshinengUserServiceJianhuyi;
import com.xinshineng.information.service.yanke.service.StudentService;
import com.xinshineng.information.service.zhenjiu.service.CheckDataService;
import com.xinshineng.information.service.zhenjiu.service.DataService;
import com.xinshineng.information.service.zhenjiu.service.xinshinengUserService;
import com.xinshineng.information.service.zhenjiu.service.impl.xinshinengUserServiceimpl;


/**
 * 用户信息表
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2018-09-27 10:18:38
 */
 

@Controller
@RequestMapping("/xinshineng/users")
public class xinshinengUserController{
	@Autowired
	private xinshinengUserService xinshinengUserService;
	@Autowired
	private DataService dataService;
	@Autowired
	private CheckDataService checkDataService;
	
	
	@GetMapping("/userList")
	String User(){
	    return "information/users1/user";
	}
	
	@GetMapping("/selectAll")
	String selectAll(){
	    return "information/select/selectAll";
	}
	
	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<zhenjiuUserDO> userList = xinshinengUserService.list(query);
		int total = xinshinengUserService.count(query);
		PageUtils pageUtils = new PageUtils(userList, total);
		return pageUtils;
	}

	@GetMapping("/show/{id}")
	String show(@PathVariable("id") Integer id,Model model){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startmonth = "";// 月初日期
		String endmonth = "";// 月末日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		startmonth = sdf.format(cal.getTime());
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		endmonth = sdf.format(cal.getTime());
		
		String startweek = "";// 周初日期
		String endweek = "";// 周末日期
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date());
	    // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
	    int dayWeek = cal2.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
	    if(1 == dayWeek){
	    	cal2.add(Calendar.DAY_OF_MONTH,-1);
	    }
	    // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
	    cal2.setFirstDayOfWeek(Calendar.MONDAY);
	    // 获得当前日期是一个星期的第几天  
	    int day = cal2.get(Calendar.DAY_OF_WEEK);
	    // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
	    cal2.add(Calendar.DATE, cal2.getFirstDayOfWeek() - day);
	    startweek = sdf.format(cal2.getTime());
	    cal2.add(Calendar.DATE,6);
	    endweek = sdf.format(cal2.getTime());
		
	    Calendar cal3 = Calendar.getInstance();
	    cal3.setTime(new Date()); 
	    String today = sdf.format(cal3.getTime());
	    
	    zhenjiuUserDO data = xinshinengUserService.get(id);
		model.addAttribute("data", data);
		model.addAttribute("startmonth", startmonth);
		model.addAttribute("endmonth", endmonth);
		model.addAttribute("startweek", startweek);
		model.addAttribute("endweek", endweek);
		model.addAttribute("today", today);
	    return "information/users1/show";
	}
	
	@ResponseBody
	@GetMapping("/checkList")
	public PageUtils checkList(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<CheckDataDO> checkDataList = checkDataService.list(query);
		int total = checkDataService.count(query);
		PageUtils pageUtils = new PageUtils(checkDataList, total);
		return pageUtils;
	}
	
	@GetMapping("/xiangqing/{id}")
	String xiangqing(@PathVariable("id") Integer id,Model model){
		CheckDataDO checkData = checkDataService.get(id);
		model.addAttribute("checkData", checkData);
	    return "information/users1/xiangqing";
	}
	
	@ResponseBody
	@GetMapping("/dataList")
	public PageUtils dataList(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DataDO> dataList = dataService.list(query);
		int total = dataService.count(query);
		PageUtils pageUtils = new PageUtils(dataList, total);
		return pageUtils;
	}
	
	
	@ResponseBody
	@GetMapping("/monthlist")
	public PageUtils monthlist(@RequestParam Map<String, Object> params){
		//按月查询列表数据
        Query query = new Query(params);
		List<DataDO> dataList = dataService.selectByMonth(query);
		int total = dataService.count(query);
		PageUtils pageUtils = new PageUtils(dataList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/weeklist")
	public PageUtils weeklist(@RequestParam Map<String, Object> params){
		//按月查询列表数据
        Query query = new Query(params);
		List<DataDO> dataList = dataService.selectByWeek(query);
		int total = dataService.count(query);
		PageUtils pageUtils = new PageUtils(dataList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/daylist")
	public PageUtils daylist(@RequestParam Map<String, Object> params){
		//按月查询列表数据
        Query query = new Query(params);
		List<DataDO> dataList = dataService.selectByDay(query);
		int total = dataService.count(query);
		PageUtils pageUtils = new PageUtils(dataList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/selectBytime")
	public PageUtils selectBytime(@RequestParam Map<String, Object> params){
		//查询列表数据
		System.out.println(params);
		Query query = new Query(params);
		List<DataDO> dataList = dataService.selectBytime(query);
		int total = dataService.count(query);
		PageUtils pageUtils = new PageUtils(dataList, total);
		return pageUtils;
	}



}
