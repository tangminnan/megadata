package com.xinshineng.information.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xinshineng.information.domain.yanke.AnswerResultDO;
import com.xinshineng.information.domain.yanke.ResultAdjustingDO;
import com.xinshineng.information.domain.yanke.ResultCornealDO;
import com.xinshineng.information.domain.yanke.ResultDiopterDO;
import com.xinshineng.information.domain.yanke.ResultEyeaxisDO;
import com.xinshineng.information.domain.yanke.ResultEyepressureDO;
import com.xinshineng.information.domain.yanke.ResultEyesightDO;
import com.xinshineng.information.domain.yanke.ResultOptometryDO;
import com.xinshineng.information.domain.yanke.ResultVisibilityDO;
import com.xinshineng.information.domain.yanke.StudentDO;
import com.xinshineng.information.service.yanke.service.ResultAdjustingService;
import com.xinshineng.information.service.yanke.service.ResultCornealService;
import com.xinshineng.information.service.yanke.service.ResultDiopterService;
import com.xinshineng.information.service.yanke.service.ResultEyeaxisService;
import com.xinshineng.information.service.yanke.service.ResultEyepressureService;
import com.xinshineng.information.service.yanke.service.ResultEyesightService;
import com.xinshineng.information.service.yanke.service.ResultOptometryService;
import com.xinshineng.information.service.yanke.service.ResultVisibilityService;
import com.xinshineng.information.service.yanke.service.StudentService;





@Controller
@RequestMapping("/xinshineng/yanke")
public class yankeUserController {
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private ResultEyesightService resultEyesightService;
	@Autowired
	private ResultOptometryService resultOptometryService;
	@Autowired
	private ResultAdjustingService resultAdjustingService;
	@Autowired
	private ResultCornealService resultCornealService;
	@Autowired
	private ResultDiopterService resultDiopterService;
	@Autowired
	private ResultEyeaxisService resultEyeaxisService;
	@Autowired
	private ResultEyepressureService resultEyepressureService;
	@Autowired
	private ResultVisibilityService resultVisibilityService;
	
	@GetMapping("/userList")
	String Student(Model model){
		List<StudentDO> studentList = studentService.getList();
		model.addAttribute("studentList", studentList);
	    return "information/student/student";
	}
	
	@GetMapping("/userList2")
	String Student2(Model model){
		List<StudentDO> studentList = studentService.getList();
		model.addAttribute("studentList", studentList);
	    return "information/student/student2";
	}
	
	@GetMapping("/userList3")
	String Student3(Model model){
		List<StudentDO> studentList = studentService.getList();
		model.addAttribute("studentList", studentList);
	    return "information/student/student3";
	}
	
	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<StudentDO> studentList = studentService.list(query);
		int total = studentService.count(query);
		PageUtils pageUtils = new PageUtils(studentList, total);
		return pageUtils;
	}
	
	
	@GetMapping("/code/{id}")
	String code(@PathVariable("id") Integer id,Model model){
		StudentDO student = studentService.get(id);
		model.addAttribute("student", student);
	    return "information/student/QrCode";
	}
	
	@GetMapping("/detail/{id}")
	String detail(@PathVariable("id") Integer id,Model model){
		Map<String,Object> map = new HashMap<String,Object>();
		StudentDO student = studentService.get(id);
		List<ResultEyesightDO> list = resultEyesightService.list(map);
		model.addAttribute("list", list);
		model.addAttribute("student", student);
	    return "information/student/detailed";
	}
	
	@GetMapping("/optometry/{id}")
	String optometry(@PathVariable("id") Integer id,Model model){
		Map<String,Object> map = new HashMap<String,Object>();
		StudentDO student = studentService.get(id);
		List<ResultOptometryDO> list = resultOptometryService.list(map);
		model.addAttribute("list", list);
		model.addAttribute("student", student);
	    return "information/student/optometry";
	}

	@ResponseBody
	@GetMapping("/getUserDetail/{id}")
	public PageUtils getUserDetail(@PathVariable("id") Integer id){
		//查询列表数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultEyesightDO> resultEyesightList = resultEyesightService.list(params);
		int total = resultEyesightService.count(params);
		PageUtils pageUtils = new PageUtils(resultEyesightList, total);
		return pageUtils;
	}

	@ResponseBody
	@GetMapping("/getUserDetail2/{id}")
	public PageUtils getUserDetail2(@PathVariable("id") Integer id){
		//查询列表数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultEyeaxisDO> resultEyeaxisList = resultEyeaxisService.list(params);
		int total = resultEyeaxisService.count(params);
		PageUtils pageUtils = new PageUtils(resultEyeaxisList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/getUserDetail3/{id}")
	public PageUtils getUserDetail3(@PathVariable("id") Integer id){
		//查询列表数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultEyepressureDO> resultEyepressureList = resultEyepressureService.list(params);
		int total = resultEyepressureService.count(params);
		PageUtils pageUtils = new PageUtils(resultEyepressureList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/getUserDetail4/{id}")
	public PageUtils getUserDetail4(@PathVariable("id") Integer id){
		//查询列表数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultVisibilityDO> resultVisibilityList = resultVisibilityService.list(params);
		int total = resultVisibilityService.count(params);
		PageUtils pageUtils = new PageUtils(resultVisibilityList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/getUserDetail5/{id}")
	public PageUtils getUserDetail5(@PathVariable("id") Integer id){
		//查询列表数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultAdjustingDO> resultAdjustingList = resultAdjustingService.list(params);
		int total = resultAdjustingService.count(params);
		PageUtils pageUtils = new PageUtils(resultAdjustingList, total);
		return pageUtils;
	}
	
	
	@ResponseBody
	@GetMapping("/getUserDetail6/{id}")
	public PageUtils getUserDetail6(@PathVariable("id") Integer id){
		//查询列表数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultOptometryDO> resultOptometryList = resultOptometryService.list(params);
		int total = resultOptometryService.count(params);
		PageUtils pageUtils = new PageUtils(resultOptometryList, total);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/getUserDetail7/{id}")
	public PageUtils getUserDetail7(@PathVariable("id") Integer id){
		//查询列表数据
		List<ResultCornealDO> resultCornealList = new ArrayList<ResultCornealDO>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultOptometryDO> list = resultOptometryService.list(params);
		for (ResultOptometryDO resultOptometryDO : list) {
			Integer tOptometryId = resultOptometryDO.gettOptometryId();
			List<ResultCornealDO> resultCornealDO = resultCornealService.queryByToptometryTd(tOptometryId);
			resultCornealList.addAll(resultCornealDO);
		}
		int size = resultCornealList.size();
		PageUtils pageUtils = new PageUtils(resultCornealList, size);
		return pageUtils;
	}
	
	@ResponseBody
	@GetMapping("/getUserDetail8/{id}")
	public PageUtils getUserDetail8(@PathVariable("id") Integer id){
		//查询列表数据
		List<ResultDiopterDO> resultDiopterList = new ArrayList<ResultDiopterDO>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", id);
		List<ResultOptometryDO> list = resultOptometryService.list(params);
		for (ResultOptometryDO resultOptometryDO : list) {
			Integer tOptometryId = resultOptometryDO.gettOptometryId();
			List<ResultDiopterDO> resultDiopterDO = resultDiopterService.getByToptometryId(tOptometryId);
			resultDiopterList.addAll(resultDiopterDO);
		}
		int size = resultDiopterList.size();
		PageUtils pageUtils = new PageUtils(resultDiopterList, size);
		return pageUtils;
	}
	
	/**
	 * 答题结果
	 */
	@GetMapping("/datijieguoR/{identityCard}")
	public String datijieguoR(@PathVariable("identityCard") String identityCard,Model model){
		model.addAttribute("identityCard", identityCard);
		return "information/student/datijieguoR";
	}
	
	@ResponseBody
	@GetMapping("/listDati")
	public PageUtils listDati(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<AnswerResultDO> studentList = studentService.listDati(query);
		int total = studentService.countDati(query);
		PageUtils pageUtils = new PageUtils(studentList, total);
		return pageUtils;
	}
	
	
}
