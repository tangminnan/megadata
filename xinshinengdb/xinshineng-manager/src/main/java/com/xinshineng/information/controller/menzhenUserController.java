package com.xinshineng.information.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xinshineng.common.config.BootdoConfig;
import com.xinshineng.common.utils.PageUtils;
import com.xinshineng.common.utils.Query;
import com.xinshineng.common.utils.ShiroUtils;
import com.xinshineng.information.domain.menzhen.MenzhenUsersDO;
import com.xinshineng.information.service.menzhen.service.MenzhenUsersService;


@Controller
@RequestMapping("/xinshineng/menzhen")
public class menzhenUserController {
	@Autowired
	private MenzhenUsersService usersService;

	private static Logger logger = LoggerFactory.getLogger(menzhenUserController.class);
	
	@GetMapping("/userList")
	String User(){
	    return "information/menzhen/user";
	}
	
	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params){
		
		String admin = ShiroUtils.getUser().getUsername();		
        Query query = new Query(params);
        
		List<MenzhenUsersDO> userList = usersService.list(query);
		int total = usersService.count(query);
		PageUtils pageUtils = new PageUtils(userList, total);
		return pageUtils;
	}
	
	
	@ResponseBody
	@GetMapping("/selectlist")
	public PageUtils selectlist(@RequestParam Map<String, Object> params){
		
        Query query = new Query(params);
		List<MenzhenUsersDO> userList = usersService.selectlist(query);
		int total = usersService.count(query);
		PageUtils pageUtils = new PageUtils(userList, total);
		return pageUtils;
	}
	



}
