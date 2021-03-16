package com.xinshineng.system.controller;

import com.xinshineng.common.annotation.Log;
import com.xinshineng.common.controller.BaseController;
import com.xinshineng.common.domain.FileDO;
import com.xinshineng.common.domain.Tree;
import com.xinshineng.common.service.FileService;
import com.xinshineng.common.utils.MD5Utils;
import com.xinshineng.common.utils.R;
import com.xinshineng.common.utils.ShiroUtils;
import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.dao.yanke.StudentDao;
import com.xinshineng.system.dao.UserDao;
import com.xinshineng.system.domain.MenuDO;
import com.xinshineng.system.service.MenuService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class LoginController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MenuService menuService;
	@Autowired
	FileService fileService;
	@GetMapping({ "/", "" })
	String welcome(Model model) {

		return "redirect:/login";
	}

	@Log("请求访问主页")
	@GetMapping({ "/index" })
	String index(Model model) {
		List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
		model.addAttribute("menus", menus);
		model.addAttribute("name", getUser().getName());
		FileDO fileDO = fileService.get(getUser().getPicId());
		if(fileDO!=null&&fileDO.getUrl()!=null){
			if(fileService.isExist(fileDO.getUrl())){
				model.addAttribute("picUrl",fileDO.getUrl());
			}else {
				model.addAttribute("picUrl","/img/photo_s.jpg");
			}
		}else {
			model.addAttribute("picUrl","/img/photo_s.jpg");
		}
		model.addAttribute("username", getUser().getUsername());
		return "main";
	}

	@GetMapping("/login")
	String login() {
		return "login";
	}

	@Autowired
	private ShaichaStudentDao shaichaStudentDao;
	@Autowired
	private StudentDao studentDao;

	@Autowired
	private UserDao userDao;

	@Log("登录")
	@PostMapping("/login")
	@ResponseBody
	R ajaxLogin(String username, String password,String choseType) {
		if ("医疗机构".equals(choseType)){
			password = MD5Utils.encrypt(username, password);
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				Subject subject = SecurityUtils.getSubject();
				try {
					subject.login(token);
					int id  = userDao.getUserId(username,password);
					return R.ok().put("url","/skip/jigou?sys_id="+id);
				}catch (AuthenticationException e){
					return R.error("用户名或密码错误");
				}

		}
		if ("学校".equals(choseType)){
			if ("htsyxx".equals(username) && "123456".equals(password)){
				String admin= "sdjsfk";
				String psw="123456";
				psw = MD5Utils.encrypt(admin,psw);
				UsernamePasswordToken token = new UsernamePasswordToken(admin, psw);
				Subject subject = SecurityUtils.getSubject();
				try {
					subject.login(token);

					return R.ok().put("url","/skip/school?school=桓台实验学校小学部&CityName=淄博市&AreaName=桓台县&checkdate=2020-11&checkType=sc&nianji=一年级");
				} catch (Exception e) {
					return R.error("用户名或密码错误");
				}
			}
			return R.error("用户名或密码错误");
		}
		password = MD5Utils.encrypt(username, password);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			if ("政府部门".equals(choseType)){
				return R.ok().put("url","/skip/zhengfu");
			}

		} catch (AuthenticationException e) {
			return R.error("用户名或密码错误");
		}
		return R.error("用户名或密码错误");
	}


	@PostMapping("/loginForPerson")
	@ResponseBody
	R LoginForPerson(String username, String password,String choseType) {
			int cow = shaichaStudentDao.login(username,password);
			if (cow>0){
				String checkdate = shaichaStudentDao.getLastCheckDate(username,password);
				return R.ok().put("url","/skip/geren?name="+username+"&idCard="+password+"&checkdate="+checkdate+"&checkType=sc");
			}
			if (cow==0){
				cow = studentDao.login(username,password);
				if (cow>0){
					String checkdate = studentDao.getLastCheckDate(username,password);
					return R.ok().put("url","/skip/geren?name="+username+"&idCard="+password+"&checkdate="+checkdate+"&checkType=ld");
				}
				else {
					return  R.error("姓名或身份证有误");
				}
			}

		 return  R.error("姓名或身份证有误");
	}


	@GetMapping("/logout")
	String logout() {
		ShiroUtils.logout();
		return "redirect:/login";
	}

	@GetMapping("/main")
	String main() {
		return "main";
	}

}
