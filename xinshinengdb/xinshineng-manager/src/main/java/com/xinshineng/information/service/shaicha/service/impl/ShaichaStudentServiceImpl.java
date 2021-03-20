package com.xinshineng.information.service.shaicha.service.impl;

import com.alibaba.fastjson.JSON;
import com.xinshineng.common.utils.ShiLiZhuanHuanUtils;
import com.xinshineng.information.dao.yanke.StudentDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.service.shaicha.service.ShaichaStudentService;

import java.math.BigDecimal;
import java.text.DecimalFormat;


import java.util.*;


@Service
public class ShaichaStudentServiceImpl implements ShaichaStudentService {
	@Autowired
	private ShaichaStudentDao studentDao;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private StudentDao liuDiaoDao;

	@Autowired
	private RestTemplate restTemplate;
	@Override
	public Map<String, Double> getYuCe() {
		Integer num = liuDiaoDao.getNumber();
		List<Map<String,Object>> data = liuDiaoDao.getData();
		Map<String, Object> avg = liuDiaoDao.getAvg();
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");*/
		Object tw = avg.get("tw");
		Object gzrsm = avg.get("gzrsm");
		Object zmgz = avg.get("zmgz");
		Object zmsm = avg.get("zmsm");
		Object zmsw = avg.get("zmsw");
		Object zxsw = avg.get("zxsw");
		Object xwsw = avg.get("xwsw");
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			if (map.get("shifou_tangwo")==null || map.get("shifou_tangwo")==""){
				map.put("shitou_tangwo",tw);
			}
			if (map.get("gzr_sm")==null || map.get("gzr_sm")==""){
				map.put("gzr_sm",gzrsm);
			}
			if (map.get("zm_mtgz")==null || map.get("zm_mtgz")==""){
				map.put("zm_mtgz",zmgz);
			}
			if (map.get("zm_sm")==null || map.get("zm_sm")==""){
				map.put("zm_sm",zmsm);
			}
			if (map.get("zm_swhd")==null || map.get("zm_swhd")==""){
				map.put("zm_swhd",zmsw);
			}
			if (map.get("zx_swhd")==null || map.get("zx_swhd")==""){
				map.put("zx_swhd",zxsw);
			}
			if (map.get("xw_swhd")==null || map.get("xw_swhd")==""){
				map.put("xw_swhd",xwsw);
			}
			if ("10/10".equals(map.get("life_farvision"))){
				map.put("life_farvision",1.0);
			}
			if ("10/12.5".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.8);
			}
			if ("10/15".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.6);
			}
			if ("10/20".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.5);
			}
			if ("10/25".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.4);
			}
			if ("10/30".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.3);
			}
			if ("10/40".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.25);
			}
			if ("10/50".equals(map.get("life_farvision"))){
				map.put("life_farvision",0.2);
			}


			if ("10/10".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",1.0);
			}
			if ("10/12.5".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.8);
			}
			if ("10/15".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.6);
			}
			if ("10/20".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.5);
			}
			if ("10/25".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.4);
			}
			if ("10/30".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.3);
			}
			if ("10/40".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.25);
			}
			if ("10/50".equals(map.get("naked_farvision"))){
				map.put("naked_farvision",0.2);
			}
/*
			map.put("lasy_check_date",dateFormat.format((Date) map.get("lasy_check_date")));
*/
			data.set(i,map);

		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(data, httpHeaders);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entity, String.class);
		String response = responseEntity.getBody();
		Map map = JSON.parseObject(response, Map.class);
		List<Map<String,Object>> data1 = (List<Map<String, Object>>) map.get("data");
		List<Map<String,Object>> L = new ArrayList<>();
		List<Map<String,Object>> R = new ArrayList<>();
		for (Map<String, Object> objectMap : data1) {
			String student = (String) objectMap.get("student");
			int index = student.indexOf("|");
			String ifRl = student.substring(index+1);
			String id = student.substring(0, index);
			Map<String,Object> map1 = new HashMap<>();
			if (ifRl.equals("L")){
				map1.put("id",id);
				BigDecimal y1Y = (BigDecimal) objectMap.get("y1Y");
				BigDecimal y2Y = (BigDecimal) objectMap.get("y2Y");
				map1.put("y1Y",y1Y.doubleValue());
				map1.put("y2Y",y2Y.doubleValue());
				L.add(map1);
				liuDiaoDao.addYuce(id,y1Y.doubleValue(),y2Y.doubleValue(),ifRl);
			}
			if (ifRl.equals("R")){
				map1.put("id",id);
				BigDecimal y1Y = (BigDecimal) objectMap.get("y1Y");
				BigDecimal y2Y = (BigDecimal) objectMap.get("y2Y");
				map1.put("y1Y",y1Y.doubleValue());
				map1.put("y2Y",y2Y.doubleValue());
				R.add(map1);
				liuDiaoDao.addYuce(id,y1Y.doubleValue(),y2Y.doubleValue(),ifRl);
			}
		}
		Integer jx=0;
		Integer zx=0;
		Integer lc=0;
		for (Map<String, Object> Lmap : L) {
			for (Map<String, Object> Rmap : R) {
				if (Lmap.get("id").equals(Rmap.get("id"))){
					Double Ly1Y = (Double) Lmap.get("y1Y");
					Double Ly2Y = (Double) Lmap.get("y2Y");
					Double Ry1Y = (Double) Rmap.get("y1Y");
					Double Ry2Y = (Double) Rmap.get("y2Y");
					Double y1Y = Ly1Y>Ry1Y?Ly1Y:Ry1Y;
					Double y2Y = Ly2Y>Ry2Y?Ly2Y:Ry2Y;
					if (y1Y<-0.5 && y2Y>-0.5 && y2Y-y1Y>0.5){
						jx++;
					}
					if (y2Y>-0.5 && y2Y<=0.75){
						lc++;
					}
					if (y2Y<=-0.5){
						zx++;
					}
				}
			}
		}
		BigDecimal bd = new BigDecimal((float) jx / num);
		double jxjs = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		bd=new BigDecimal((float)zx/num);
		double zxjs = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		bd=new BigDecimal((float)lc/num);
		double lcqq = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		Map<String,Double> result = new HashMap<>();
		result.put("lc",lcqq);
		result.put("jx",jxjs);
		result.put("zx",zxjs);
		return result;
	}

	@Override
	public Map XueBuHuanBingLv(String checkCity) {
		Map<String,String> resultMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.0");
		//判断城市是否为空
		if (StringUtils.isNotBlank(checkCity)){
			resultMap = redisTemplate.opsForHash().entries(checkCity+"XueBuHuanBingLv");
			if (resultMap.size()==0){
				Long youNum = 0L;
				Long xiaoNum = 0L;
				Long chuNum = 0L;
				Long gaoNum = 0L;
				Long zNum = 0L;

				Long youBNum = 0L;
				Long xiaoBNum = 0L;
				Long chuBNum = 0L;
				Long gaoBNum = 0L;
				Long zBNum = 0L;

				List<Map<String,Object>> NumList = studentDao.getEveryXueBuNum(checkCity);
				List<Map<String,Object>> HuanBingNumList =studentDao.getEveryXueBuHuanBingNum(checkCity);
				for (Map<String, Object> map : NumList) {
					if (map.get("xue_bu") != "" && map.get("xue_bu")!=null){
						String xue_bu = (String) map.get("xue_bu");
						if ("幼儿园".equals(xue_bu)){
							youNum = (Long) map.get("num");
						}
						if ("小学".equals(xue_bu)){
							xiaoNum = (Long) map.get("num");
						}
						if ("初中".equals(xue_bu)){
							chuNum = (Long) map.get("num");
						}
						if ("高中".equals(xue_bu)){
							gaoNum = (Long) map.get("num");
						}
					}
				}
				for (Map<String, Object> map : HuanBingNumList) {
					if (map.get("xue_bu") != "" && map.get("xue_bu")!=null){
						String xue_bu = (String) map.get("xue_bu");
						if ("幼儿园".equals(xue_bu)){
							youBNum = (Long) map.get("num");
						}
						if ("小学".equals(xue_bu)){
							xiaoBNum = (Long) map.get("num");
						}
						if ("初中".equals(xue_bu)){
							chuBNum = (Long) map.get("num");
						}
						if ("高中".equals(xue_bu)){
							gaoBNum = (Long) map.get("num");
						}
					}
				}
				zNum=youNum+xiaoNum+chuNum+gaoNum;
				zBNum=youBNum+xiaoBNum+chuBNum+gaoBNum;
				BigDecimal bg = new BigDecimal((float) youBNum / youNum);
				String youBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) xiaoBNum / xiaoNum);
				String xiaoBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) chuBNum / chuNum);
				String chuBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) gaoBNum / gaoNum);
				String gaoBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) zBNum / zNum);
				String zBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				resultMap.put("youBLv",youBLv);
				resultMap.put("xiaoBLv",xiaoBLv);
				resultMap.put("chuBLv",chuBLv);
				resultMap.put("gaoBLv",gaoBLv);
				resultMap.put("zBLv",zBLv);
				redisTemplate.opsForHash().putAll(checkCity+"XueBuHuanBingLv",resultMap);
		}
		}
		//城市为空，获取省份男女患病率
		if (StringUtils.isBlank(checkCity)){
			resultMap = redisTemplate.opsForHash().entries("XueBuHuanBingLv");
			if (resultMap.size()==0){
				Long youNum = 0L;
				Long xiaoNum = 0L;
				Long chuNum = 0L;
				Long gaoNum = 0L;
				Long zNum = 0L;

				Long youBNum = 0L;
				Long xiaoBNum = 0L;
				Long chuBNum = 0L;
				Long gaoBNum = 0L;
				Long zBNum = 0L;

				List<Map<String,Object>> NumList = studentDao.getEveryXueBuNum(checkCity);
				List<Map<String,Object>> HuanBingNumList =studentDao.getEveryXueBuHuanBingNum(checkCity);
				for (Map<String, Object> map : NumList) {
					if (map.get("xue_bu") != "" && map.get("xue_bu")!=null){
						String xue_bu = (String) map.get("xue_bu");
						if ("幼儿园".equals(xue_bu)){
							youNum = (Long) map.get("num");
						}
						if ("小学".equals(xue_bu)){
							xiaoNum = (Long) map.get("num");
						}
						if ("初中".equals(xue_bu)){
							chuNum = (Long) map.get("num");
						}
						if ("高中".equals(xue_bu)){
							gaoNum = (Long) map.get("num");
						}
					}
				}
				for (Map<String, Object> map : HuanBingNumList) {
					if (map.get("xue_bu") != "" && map.get("xue_bu")!=null){
						String xue_bu = (String) map.get("xue_bu");
						if ("幼儿园".equals(xue_bu)){
							youBNum = (Long) map.get("num");
						}
						if ("小学".equals(xue_bu)){
							xiaoBNum = (Long) map.get("num");
						}
						if ("初中".equals(xue_bu)){
							chuBNum = (Long) map.get("num");
						}
						if ("高中".equals(xue_bu)){
							gaoBNum = (Long) map.get("num");
						}
					}
				}
				zNum=youNum+xiaoNum+chuNum+gaoNum;
				zBNum=youBNum+xiaoBNum+chuBNum+gaoBNum;
				BigDecimal bg = new BigDecimal((float) youBNum / youNum);
				String youBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) xiaoBNum / xiaoNum);
				String xiaoBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) chuBNum / chuNum);
				String chuBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) gaoBNum / gaoNum);
				String gaoBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				bg = new BigDecimal((float) zBNum / zNum);
				String zBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
				resultMap.put("youBLv",youBLv);
				resultMap.put("xiaoBLv",xiaoBLv);
				resultMap.put("chuBLv",chuBLv);
				resultMap.put("gaoBLv",gaoBLv);
				resultMap.put("zBLv",zBLv);
				redisTemplate.opsForHash().putAll("XueBuHuanBingLv",resultMap);
			}
		}
		return resultMap;
	}

	@Override
	public Map NianJiHuanBingLv(String checkCity, String checkArea) {
		Map<String,String> resultMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.0");
		resultMap = redisTemplate.opsForHash().entries(checkCity+checkArea+"NianJiHuanBingLv");
		if (resultMap.size()==0){
			Long oneNum = 0L;
			Long twoNum = 0L;
			Long threeNum = 0L;
			Long fourNum = 0L;
			Long fiveNum = 0L;
			Long sixNum = 0L;
			Long oneBNum = 0L;
			Long twoBNum = 0L;
			Long threeBNum = 0L;
			Long fourBNum = 0L;
			Long fiveBNum = 0L;
			Long sixBNum = 0L;
			List<Map<String,Object>> NianJiNum = studentDao.getNianJiNum(checkCity,checkArea);
			List<Map<String,Object>> NianJiHuanBingNum = studentDao.getNianJiHuanBingNum(checkCity,checkArea);

			for (Map<String, Object> map : NianJiNum) {
				String grade = (String) map.get("grade");
				if ("一年级".equals(grade)){
					oneNum=(Long)map.get("num");
				}
				if ("二年级".equals(grade)){
					twoNum=(Long)map.get("num");
				}
				if ("三年级".equals(grade)){
					threeNum=(Long)map.get("num");
				}
				if ("四年级".equals(grade)){
					fourNum=(Long)map.get("num");
				}
				if ("五年级".equals(grade)){
					fiveNum=(Long)map.get("num");
				}
				if ("六年级".equals(grade)){
					sixNum=(Long)map.get("num");
				}
			}
			for (Map<String, Object> map : NianJiHuanBingNum) {
				String grade = (String) map.get("grade");
				if ("一年级".equals(grade)){
					oneBNum=(Long)map.get("num");
				}
				if ("二年级".equals(grade)){
					twoBNum=(Long)map.get("num");
				}
				if ("三年级".equals(grade)){
					threeBNum=(Long)map.get("num");
				}
				if ("四年级".equals(grade)){
					fourBNum=(Long)map.get("num");
				}
				if ("五年级".equals(grade)){
					fiveBNum=(Long)map.get("num");
				}
				if ("六年级".equals(grade)){
					sixBNum=(Long)map.get("num");
				}
			}

			BigDecimal bg = new BigDecimal((float) oneBNum / oneNum);
			String oneBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
			bg = new BigDecimal((float) twoBNum / twoNum);
			String twoBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
			bg = new BigDecimal((float) threeBNum / threeNum);
			String threeBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
			bg = new BigDecimal((float) fourBNum / fourNum);
			String fourBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
			bg = new BigDecimal((float) fiveBNum / fiveNum);
			String fiveBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
			bg = new BigDecimal((float) sixBNum / sixNum);
			String sixBLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
			resultMap.put("oneBLv",oneBLv);
			resultMap.put("twoBLv",twoBLv);
			resultMap.put("threeBLv",threeBLv);
			resultMap.put("fourBLv",fourBLv);
			resultMap.put("fiveBLv",fiveBLv);
			resultMap.put("sixBLv",sixBLv);
			redisTemplate.opsForHash().putAll(checkCity+checkArea+"NianJiHuanBingLv",resultMap);
		}


		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getShiFanXiaoHuanBingLv(String checkCity) {
		DecimalFormat df = new DecimalFormat("0.0");
		List<Map<String,Object>> resultList = new ArrayList<>();
		Boolean flag = redisTemplate.hasKey(checkCity + "ShiFanXiaoHuanBingLv");
		if (!flag){
			resultList = studentDao.getShiFanXiaoNumList(checkCity);
			if (resultList.isEmpty()){
				resultList = studentDao.getShiFanXiaoNumListForOld(checkCity);
				if (resultList.isEmpty()){
					return null;
				}
			}
			if(resultList.size()<5){
				List<Map<String, Object>> shiFanXiaoNumListForOld = studentDao.getShiFanXiaoNumListForOld(checkCity);
				resultList.addAll(shiFanXiaoNumListForOld);
			}
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = resultList.get(i);
				String school = (String) map.get("school");
				Long num = (Long) map.get("num");
				Long Bnum = studentDao.getSchoolHuanBingNumForOld(checkCity,school);
				BigDecimal bg = new BigDecimal((float) Bnum / num);
				String HBL = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				map.put("HBL",HBL);
				resultList.set(i,map);
			}
			redisTemplate.opsForList().rightPushAll(checkCity+"ShiFanXiaoHuanBingLv",resultList);
			return resultList;
		}
		if (flag){
			resultList = redisTemplate.opsForList().range(checkCity+"ShiFanXiaoHuanBingLv",0,-1);
			return resultList;
		}
		return resultList;
	}

	@Override
	public Map<String,Object> getCount() {
		Map<String,Object> firstMap;
		firstMap = redisTemplate.opsForHash().entries("shouYeData");
		if (firstMap.size()==0){
			firstMap.put("JiNanShi",0L);
			firstMap.put("QingDaoShi",0L);
			firstMap.put("ZiBoShi",0L);
			firstMap.put("ZaoZhuangShi",0L);
			firstMap.put("DongYingShi",0L);
			firstMap.put("YanTaiShi",0L);
			firstMap.put("WeiFangShi",0L);
			firstMap.put("JiNingShi",0L);
			firstMap.put("TaiAnShi",0L);
			firstMap.put("WeiHaiShi",0L);
			firstMap.put("RiZhaoShi",0L);
			firstMap.put("BinZhouShi",0L);
			firstMap.put("LinYiShi",0L);
			firstMap.put("DeZhouShi",0L);
			firstMap.put("HeZeShi",0L);
			firstMap.put("LiaoChengShi",0L);

			//筛查新
			List<Map<String,Long>> shaiChanewTotal = studentDao.getShaiChaTotalnew();
			AddCityCount(firstMap,shaiChanewTotal);
			//筛查老
			List<Map<String,Long>> shaiChaOldTotal = studentDao.getShaiChaTotalOld();
			AddCityCount(firstMap,shaiChaOldTotal);
			//流调新
			List<Map<String,Long>> liuDiaoNewTotal = liuDiaoDao.getLiuDiaoTotalNew();
			AddCityCount(firstMap,liuDiaoNewTotal);
			//流调老
			List<Map<String,Long>> liuDiaoOldTotal = liuDiaoDao.getLiuDiaoTotalOld();
			AddCityCount(firstMap,liuDiaoOldTotal);

			getTotalNum(firstMap);
			Long shaiChaNewTotalNum = studentDao.getShaiChaNewTotalNum();
			Long shaiChaOldTotalNum = studentDao.getShaiChaOldTotalNum();
			firstMap.put("shaiChaNum",shaiChaNewTotalNum+shaiChaOldTotalNum);
			Long liuDiaoNewTotalNum = liuDiaoDao.getLiuDiaoNewTotalNum();
			Long liuDiaoOldTotalNum = liuDiaoDao.getLiuDiaoOldTotalNum();
			firstMap.put("liuDiaoNum",liuDiaoNewTotalNum+liuDiaoOldTotalNum);
			firstMap.put("totalNum",shaiChaOldTotalNum+shaiChaNewTotalNum+liuDiaoNewTotalNum+liuDiaoOldTotalNum);

			/*Date date = new Date();
			int year = date.getYear();*/
			Calendar instance = Calendar.getInstance();
			int year = instance.get(Calendar.YEAR);
			//获取筛查流调当年每个城市受检人数
			List<Map<String,Long>> thisYearCountForsc = studentDao.getThisYearCheckCount(year);
			List<Map<String,Long>> thisYearCountForld = liuDiaoDao.getThisYearCheckCount(year);
			Map<String,Object> someMap = jiSuanJinShi(thisYearCountForld,thisYearCountForsc,year);

			shifoudabiao(someMap,firstMap,year);


			redisTemplate.opsForHash().putAll("shouYeData",firstMap);
		}
		return firstMap;
	}

	private void shifoudabiao(Map<String, Object> someMap, Map<String, Object> firstMap,int year) {
		if ((Long) someMap.get("JiNanShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("JiNanShi");
			Long jiNanShiJS = (Long) someMap.get("JiNanShiJS");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"济南市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "济南市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"济南市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"济南市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("JiNanShiJS",2);
					}
					if (jsl-qnjsl<=0 && jsl-qnjsl>=-1){
						firstMap.put("JiNanShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("JiNanShiJS",0);
					}
				}else {
					firstMap.put("JiNanShiJS",1);
				}
			}else {
				firstMap.put("JiNanShiJS",1);
			}
		}else {
			firstMap.put("JiNanShiJS",1);
		}

		if ((Long) someMap.get("QingDaoShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("QingDaoShi");
			Long jiNanShiJS = (Long) someMap.get("QingDaoShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"青岛市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "青岛市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"青岛市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"青岛市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("QingDaoShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("QingDaoShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("QingDaoShiJS",0);
					}
				}else {
					firstMap.put("QingDaoShiJS",1);
				}
			}else {
				firstMap.put("QingDaoShiJS",1);
			}
		}else {
			firstMap.put("QingDaoShiJS",1);
		}

		if ((Long) someMap.get("ZiBoShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("ZiBoShi");
			Long jiNanShiJS = (Long) someMap.get("ZiBoShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"淄博市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "淄博市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"淄博市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"淄博市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("ZiBoShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("ZiBoShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("ZiBoShiJS",0);
					}
				}else {
					firstMap.put("ZiBoShiJS",1);
				}
			}else {
				firstMap.put("ZiBoShiJS",1);
			}
		}else {
			firstMap.put("ZiBoShiJS",1);
		}

		if ((Long) someMap.get("ZaoZhuangShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("ZaoZhuangShi");
			Long jiNanShiJS = (Long) someMap.get("ZaoZhuangShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"枣庄市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "枣庄市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"枣庄市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"枣庄市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("ZaoZhuangShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("ZaoZhuangShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("ZaoZhuangShiJS",0);
					}
				}else {
					firstMap.put("ZaoZhuangShiJS",1);
				}
			}else {
				firstMap.put("ZaoZhuangShiJS",1);
			}
		}else {
			firstMap.put("ZaoZhuangShiJS",1);
		}

		if ((Long) someMap.get("DongYingShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("DongYingShi");
			Long jiNanShiJS = (Long) someMap.get("DongYingShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"东营市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "东营市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"东营市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"东营市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("DongYingShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("DongYingShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("DongYingShiJS",0);
					}
				}else {
					firstMap.put("DongYingShiJS",1);
				}
			}else {
				firstMap.put("DongYingShiJS",1);
			}
		}else {
			firstMap.put("DongYingShiJS",1);
		}

		if ((Long) someMap.get("YanTaiShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("YanTaiShi");
			Long jiNanShiJS = (Long) someMap.get("YanTaiShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"烟台市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "烟台市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"烟台市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"烟台市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("YanTaiShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("YanTaiShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("YanTaiShiJS",0);
					}
				}else {
					firstMap.put("YanTaiShiJS",1);
				}
			}else {
				firstMap.put("YanTaiShiJS",1);
			}
		}else {
			firstMap.put("YanTaiShiJS",1);
		}

		if ((Long) someMap.get("WeiFangShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("WeiFangShi");
			Long jiNanShiJS = (Long) someMap.get("WeiFangShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"潍坊市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "潍坊市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"潍坊市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"潍坊市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("WeiFangShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("WeiFangShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("WeiFangShiJS",0);
					}
				}else {
					firstMap.put("WeiFangShiJS",1);
				}
			}else {
				firstMap.put("WeiFangShiJS",1);
			}
		}else {
			firstMap.put("WeiFangShiJS",1);
		}

		if ((Long) someMap.get("JiNingShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("JiNingShi");
			Long jiNanShiJS = (Long) someMap.get("JiNingShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"济宁市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "济宁市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"济宁市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"济宁市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("JiNingShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("JiNingShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("JiNingShiJS",0);
					}
				}else {
					firstMap.put("JiNingShiJS",1);
				}
			}else {
				firstMap.put("JiNingShiJS",1);
			}
		}else {
			firstMap.put("JiNingShiJS",1);
		}

		if ((Long) someMap.get("TaiAnShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("TaiAnShi");
			Long jiNanShiJS = (Long) someMap.get("TaiAnShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"泰安市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "泰安市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"泰安市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"泰安市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("TaiAnShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("TaiAnShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("TaiAnShiJS",0);
					}
				}else {
					firstMap.put("TaiAnShiJS",1);
				}
			}else {
				firstMap.put("TaiAnShiJS",1);
			}
		}else {
			firstMap.put("TaiAnShiJS",1);
		}

		if ((Long) someMap.get("WeiHaiShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("WeiHaiShi");
			Long jiNanShiJS = (Long) someMap.get("WeiHaiShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"威海市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "威海市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"威海市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"威海市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("WeiHaiShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("WeiHaiShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("WeiHaiShiJS",0);
					}
				}else {
					firstMap.put("WeiHaiShiJS",1);
				}
			}else {
				firstMap.put("WeiHaiShiJS",1);
			}
		}else {
			firstMap.put("WeiHaiShiJS",1);
		}

		if ((Long) someMap.get("RiZhaoShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("RiZhaoShi");
			Long jiNanShiJS = (Long) someMap.get("RiZhaoShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"日照市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "日照市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"日照市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"日照市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("RiZhaoShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("RiZhaoShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("RiZhaoShiJS",0);
					}
				}else {
					firstMap.put("RiZhaoShiJS",1);
				}
			}else {
				firstMap.put("RiZhaoShiJS",1);
			}
		}else {
			firstMap.put("RiZhaoShiJS",1);
		}

		if ((Long) someMap.get("BinZhouShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("BinZhouShi");
			Long jiNanShiJS = (Long) someMap.get("BinZhouShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"滨州市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "滨州市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"滨州市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"滨州市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("BinZhouShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("BinZhouShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("BinZhouShiJS",0);
					}
				}else {
					firstMap.put("BinZhouShiJS",1);
				}
			}else {
				firstMap.put("BinZhouShiJS",1);
			}
		}else {
			firstMap.put("BinZhouShiJS",1);
		}

		if ((Long) someMap.get("LinYiShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("LinYiShi");
			Long jiNanShiJS = (Long) someMap.get("LinYiShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"临沂市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "临沂市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"临沂市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"临沂市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("LinYiShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("LinYiShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("LinYiShiJS",0);
					}
				}else {
					firstMap.put("LinYiShiJS",1);
				}
			}else {
				firstMap.put("LinYiShiJS",1);
			}
		}else {
			firstMap.put("LinYiShiJS",1);
		}

		if ((Long) someMap.get("DeZhouShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("DeZhouShi");
			Long jiNanShiJS = (Long) someMap.get("DeZhouShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"德州市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "德州市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"德州市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"德州市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("DeZhouShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("DeZhouShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("DeZhouShiJS",0);
					}
				}else {
					firstMap.put("DeZhouShiJS",1);
				}
			}else {
				firstMap.put("DeZhouShiJS",1);
			}
		}else {
			firstMap.put("DeZhouShiJS",1);
		}

		if ((Long) someMap.get("HeZeShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("HeZeShi");
			Long jiNanShiJS = (Long) someMap.get("HeZeShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"菏泽市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "菏泽市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"菏泽市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"菏泽市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("HeZeShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("HeZeShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("HeZeShiJS",0);
					}
				}else {
					firstMap.put("HeZeShiJS",1);
				}
			}else {
				firstMap.put("HeZeShiJS",1);
			}
		}else {
			firstMap.put("HeZeShiJS",1);
		}

		if ((Long) someMap.get("LiaoChengShi")!=0L){
			Long jiNanShiNum = (Long) someMap.get("LiaoChengShi");
			Long jiNanShiJS = (Long) someMap.get("LiaoChengShi");
			BigDecimal bg = new BigDecimal((float) jiNanShiJS / jiNanShiNum);
			double jsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
			Long qnCheckNumsc = studentDao.getQNCheckNum(year-1,"聊城市");
			Long qnJinShisc = 0L;
			Long qnJinShild = 0L;
			if (qnCheckNumsc!=0L){
				qnJinShisc = studentDao.getThisYearJinShiCount(year - 1, "聊城市");
			}
			Long qnCheckNumld = liuDiaoDao.getQNCheckNum(year-1,"聊城市");
			if (qnCheckNumld!=0L){
				qnJinShild = liuDiaoDao.getThisYearJinShiCount(year-1,"聊城市");
			}
			long qnCheckNum = qnCheckNumld + qnCheckNumsc;
			if (qnCheckNum!=0){
				long qnJinShiNum = qnJinShisc + qnJinShild;
				if (qnJinShiNum!=0){
					bg = new BigDecimal((float) qnJinShiNum / qnCheckNum);
					double qnjsl = (bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue())*100;
					if (jsl-qnjsl>0){
						firstMap.put("LiaoChengShiJS",2);
					}
					if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
						firstMap.put("LiaoChengShiJS",1);
					}
					if (jsl-qnjsl<-1){
						firstMap.put("LiaoChengShiJS",0);
					}
				}else {
					firstMap.put("LiaoChengShiJS",1);
				}
			}else {
				firstMap.put("LiaoChengShiJS",1);
			}
		}else {
			firstMap.put("LiaoChengShiJS",1);
		}
	}

	private Map<String,Object> jiSuanJinShi(List<Map<String, Long>> thisYearCountForld, List<Map<String, Long>> thisYearCountForsc,int year) {
		Map<String,Object> smoeMap = new HashMap<>();
		smoeMap.put("JiNanShi",0L);
		smoeMap.put("QingDaoShi",0L);
		smoeMap.put("ZiBoShi",0L);
		smoeMap.put("ZaoZhuangShi",0L);
		smoeMap.put("DongYingShi",0L);
		smoeMap.put("YanTaiShi",0L);
		smoeMap.put("WeiFangShi",0L);
		smoeMap.put("JiNingShi",0L);
		smoeMap.put("TaiAnShi",0L);
		smoeMap.put("WeiHaiShi",0L);
		smoeMap.put("RiZhaoShi",0L);
		smoeMap.put("BinZhouShi",0L);
		smoeMap.put("LinYiShi",0L);
		smoeMap.put("DeZhouShi",0L);
		smoeMap.put("HeZeShi",0L);
		smoeMap.put("LiaoChengShi",0L);


		smoeMap.put("JiNanShiJS",0L);
		smoeMap.put("QingDaoShiJS",0L);
		smoeMap.put("ZiBoShiJS",0L);
		smoeMap.put("ZaoZhuangShiJS",0L);
		smoeMap.put("DongYingShiJS",0L);
		smoeMap.put("YanTaiShiJS",0L);
		smoeMap.put("WeiFangShiJS",0L);
		smoeMap.put("JiNingShiJS",0L);
		smoeMap.put("TaiAnShiJS",0L);
		smoeMap.put("WeiHaiShiJS",0L);
		smoeMap.put("RiZhaoShiJS",0L);
		smoeMap.put("BinZhouShiJS",0L);
		smoeMap.put("LinYiShiJS",0L);
		smoeMap.put("DeZhouShiJS",0L);
		smoeMap.put("HeZeShiJS",0L);
		smoeMap.put("LiaoChengShiJS",0L);

		for (Map<String, Long> scresult : thisYearCountForsc) {
			if ("济南市".equals(scresult.get("CityName"))){
				smoeMap.put("JiNanShi",(Long)smoeMap.get("JiNanShi")+scresult.get("num"));
				smoeMap.put("JiNanShiJS",(Long)smoeMap.get("JiNanShiJS")+studentDao.getThisYearJinShiCount(year, "济南市"));
			}
			if ("青岛市".equals(scresult.get("CityName"))){
				smoeMap.put("QingDaoShi",(Long)smoeMap.get("QingDaoShi")+scresult.get("num"));
				smoeMap.put("QingDaoShiJS",(Long)smoeMap.get("QingDaoShiJS")+studentDao.getThisYearJinShiCount(year, "青岛市"));
			}
			if ("淄博市".equals(scresult.get("CityName"))){
				smoeMap.put("ZiBoShi",(Long)smoeMap.get("ZiBoShi")+scresult.get("num"));
				smoeMap.put("ZiBoShiJS",(Long)smoeMap.get("ZiBoShiJS")+studentDao.getThisYearJinShiCount(year, "淄博市"));
			}
			if ("枣庄市".equals(scresult.get("CityName"))){
				smoeMap.put("ZaoZhuangShi",(Long)smoeMap.get("ZaoZhuangShi")+scresult.get("num"));
				smoeMap.put("ZaoZhuangShiJS",(Long)smoeMap.get("ZaoZhuangShiJS")+studentDao.getThisYearJinShiCount(year, "枣庄市"));
			}
			if ("东营市".equals(scresult.get("CityName"))){
				smoeMap.put("DongYingShi",(Long)smoeMap.get("DongYingShi")+scresult.get("num"));
				smoeMap.put("DongYingShiJS",(Long)smoeMap.get("DongYingShiJS")+studentDao.getThisYearJinShiCount(year, "东营市"));
			}
			if ("烟台市".equals(scresult.get("CityName"))){
				smoeMap.put("YanTaiShi",(Long)smoeMap.get("YanTaiShi")+scresult.get("num"));
				smoeMap.put("YanTaiShiJS",(Long)smoeMap.get("YanTaiShiJS")+studentDao.getThisYearJinShiCount(year, "烟台市"));
			}
			if ("潍坊市".equals(scresult.get("CityName"))){
				smoeMap.put("WeiFangShi",(Long)smoeMap.get("WeiFangShi")+scresult.get("num"));
				smoeMap.put("WeiFangShiJS",(Long)smoeMap.get("WeiFangShiJS")+studentDao.getThisYearJinShiCount(year, "潍坊市"));
			}
			if ("济宁市".equals(scresult.get("CityName"))){
				smoeMap.put("JiNingShi",(Long)smoeMap.get("JiNingShi")+scresult.get("num"));
				smoeMap.put("JiNingShiJS",(Long)smoeMap.get("JiNingShiJS")+studentDao.getThisYearJinShiCount(year, "济宁市"));
			}
			if ("泰安市".equals(scresult.get("CityName"))){
				smoeMap.put("TaiAnShi",(Long)smoeMap.get("TaiAnShi")+scresult.get("num"));
				smoeMap.put("TaiAnShiJS",(Long)smoeMap.get("TaiAnShiJS")+studentDao.getThisYearJinShiCount(year, "泰安市"));
			}
			if ("威海市".equals(scresult.get("CityName"))){
				smoeMap.put("WeiHaiShi",(Long)smoeMap.get("WeiHaiShi")+scresult.get("num"));
				smoeMap.put("WeiHaiShiJS",(Long)smoeMap.get("WeiHaiShiJS")+studentDao.getThisYearJinShiCount(year, "威海市"));
			}
			if ("日照市".equals(scresult.get("CityName"))){
				smoeMap.put("RiZhaoShi",(Long)smoeMap.get("RiZhaoShi")+scresult.get("num"));
				smoeMap.put("RiZhaoShiJS",(Long)smoeMap.get("RiZhaoShiJS")+studentDao.getThisYearJinShiCount(year, "日照市"));
			}
			if ("滨州市".equals(scresult.get("CityName"))){
				smoeMap.put("BinZhouShi",(Long)smoeMap.get("BinZhouShi")+scresult.get("num"));
				smoeMap.put("BinZhouShiJS",(Long)smoeMap.get("BinZhouShiJS")+studentDao.getThisYearJinShiCount(year, "滨州市"));
			}
			if ("临沂市".equals(scresult.get("CityName"))){
				smoeMap.put("LinYiShi",(Long)smoeMap.get("LinYiShi")+scresult.get("num"));
				smoeMap.put("LinYiShiJS",(Long)smoeMap.get("LinYiShiJS")+studentDao.getThisYearJinShiCount(year, "临沂市"));
			}
			if ("德州市".equals(scresult.get("CityName"))){
				smoeMap.put("DeZhouShi",(Long)smoeMap.get("DeZhouShi")+scresult.get("num"));
				smoeMap.put("DeZhouShiJS",(Long)smoeMap.get("DeZhouShiJS")+studentDao.getThisYearJinShiCount(year, "德州市"));
			}
			if ("菏泽市".equals(scresult.get("CityName"))){
				smoeMap.put("HeZeShi",(Long)smoeMap.get("HeZeShi")+scresult.get("num"));
				smoeMap.put("HeZeShiJS",(Long)smoeMap.get("HeZeShiJS")+studentDao.getThisYearJinShiCount(year, "菏泽市"));
			}
			if ("聊城市".equals(scresult.get("CityName"))){
				smoeMap.put("LiaoChengShi",(Long)smoeMap.get("LiaoChengShi")+scresult.get("num"));
				smoeMap.put("LiaoChengShiJS",(Long)smoeMap.get("LiaoChengShiJS")+studentDao.getThisYearJinShiCount(year, "聊城市"));
			}
			if ("莱芜区".equals(scresult.get("CityName"))){
				smoeMap.put("JiNanShi",(Long)smoeMap.get("JiNanShi")+scresult.get("num"));
				smoeMap.put("JiNanShiJS",(Long)smoeMap.get("JiNanShiJS")+studentDao.getThisYearJinShiCount(year, "莱芜区"));

			}
			if ("莱芜市".equals(scresult.get("CityName"))){
				smoeMap.put("JiNanShi",(Long)smoeMap.get("JiNanShi")+scresult.get("num"));
				smoeMap.put("JiNanShiJS",(Long)smoeMap.get("JiNanShiJS")+studentDao.getThisYearJinShiCount(year, "莱芜市"));
			}
		}

		for (Map<String, Long> ldresult : thisYearCountForld) {
			if ("济南市".equals(ldresult.get("CityName"))){
				smoeMap.put("JiNanShi",(Long)smoeMap.get("JiNanShi")+ldresult.get("num"));
				smoeMap.put("JiNanShiJS",(Long)smoeMap.get("JiNanShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "济南市"));
			}
			if ("青岛市".equals(ldresult.get("CityName"))){
				smoeMap.put("QingDaoShi",(Long)smoeMap.get("QingDaoShi")+ldresult.get("num"));
				smoeMap.put("QingDaoShiJS",(Long)smoeMap.get("QingDaoShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "青岛市"));
			}
			if ("淄博市".equals(ldresult.get("CityName"))){
				smoeMap.put("ZiBoShi",(Long)smoeMap.get("ZiBoShi")+ldresult.get("num"));
				smoeMap.put("ZiBoShiJS",(Long)smoeMap.get("ZiBoShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "淄博市"));
			}
			if ("枣庄市".equals(ldresult.get("CityName"))){
				smoeMap.put("ZaoZhuangShi",(Long)smoeMap.get("ZaoZhuangShi")+ldresult.get("num"));
				smoeMap.put("ZaoZhuangShiJS",(Long)smoeMap.get("ZaoZhuangShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "枣庄市"));
			}
			if ("东营市".equals(ldresult.get("CityName"))){
				smoeMap.put("DongYingShi",(Long)smoeMap.get("DongYingShi")+ldresult.get("num"));
				smoeMap.put("DongYingShiJS",(Long)smoeMap.get("DongYingShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "东营市"));
			}
			if ("烟台市".equals(ldresult.get("CityName"))){
				smoeMap.put("YanTaiShi",(Long)smoeMap.get("YanTaiShi")+ldresult.get("num"));
				smoeMap.put("YanTaiShiJS",(Long)smoeMap.get("YanTaiShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "烟台市"));
			}
			if ("潍坊市".equals(ldresult.get("CityName"))){
				smoeMap.put("WeiFangShi",(Long)smoeMap.get("WeiFangShi")+ldresult.get("num"));
				smoeMap.put("WeiFangShiJS",(Long)smoeMap.get("WeiFangShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "潍坊市"));
			}
			if ("济宁市".equals(ldresult.get("CityName"))){
				smoeMap.put("JiNingShi",(Long)smoeMap.get("JiNingShi")+ldresult.get("num"));
				smoeMap.put("JiNingShiJS",(Long)smoeMap.get("JiNingShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "济宁市"));
			}
			if ("泰安市".equals(ldresult.get("CityName"))){
				smoeMap.put("TaiAnShi",(Long)smoeMap.get("TaiAnShi")+ldresult.get("num"));
				smoeMap.put("TaiAnShiJS",(Long)smoeMap.get("TaiAnShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "泰安市"));
			}
			if ("威海市".equals(ldresult.get("CityName"))){
				smoeMap.put("WeiHaiShi",(Long)smoeMap.get("WeiHaiShi")+ldresult.get("num"));
				smoeMap.put("WeiHaiShiJS",(Long)smoeMap.get("WeiHaiShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "威海市"));
			}
			if ("日照市".equals(ldresult.get("CityName"))){
				smoeMap.put("RiZhaoShi",(Long)smoeMap.get("RiZhaoShi")+ldresult.get("num"));
				smoeMap.put("RiZhaoShiJS",(Long)smoeMap.get("RiZhaoShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "日照市"));
			}
			if ("滨州市".equals(ldresult.get("CityName"))){
				smoeMap.put("BinZhouShi",(Long)smoeMap.get("BinZhouShi")+ldresult.get("num"));
				smoeMap.put("BinZhouShiJS",(Long)smoeMap.get("BinZhouShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "滨州市"));
			}
			if ("临沂市".equals(ldresult.get("CityName"))){
				smoeMap.put("LinYiShi",(Long)smoeMap.get("LinYiShi")+ldresult.get("num"));
				smoeMap.put("LinYiShiJS",(Long)smoeMap.get("LinYiShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "临沂市"));
			}
			if ("德州市".equals(ldresult.get("CityName"))){
				smoeMap.put("DeZhouShi",(Long)smoeMap.get("DeZhouShi")+ldresult.get("num"));
				smoeMap.put("DeZhouShiJS",(Long)smoeMap.get("DeZhouShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "德州市"));
			}
			if ("菏泽市".equals(ldresult.get("CityName"))){
				smoeMap.put("HeZeShi",(Long)smoeMap.get("HeZeShi")+ldresult.get("num"));
				smoeMap.put("HeZeShiJS",(Long)smoeMap.get("HeZeShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "菏泽市"));
			}
			if ("聊城市".equals(ldresult.get("CityName"))){
				smoeMap.put("LiaoChengShi",(Long)smoeMap.get("LiaoChengShi")+ldresult.get("num"));
				smoeMap.put("LiaoChengShiJS",(Long)smoeMap.get("LiaoChengShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "聊城市"));
			}
			if ("莱芜区".equals(ldresult.get("CityName"))){
				smoeMap.put("JiNanShi",(Long)smoeMap.get("JiNanShi")+ldresult.get("num"));
				smoeMap.put("JiNanShiJS",(Long)smoeMap.get("JiNanShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "莱芜区"));
			}
			if ("莱芜市".equals(ldresult.get("CityName"))){
				smoeMap.put("JiNanShi",(Long)smoeMap.get("JiNanShi")+ldresult.get("num"));
				smoeMap.put("JiNanShiJS",(Long)smoeMap.get("JiNanShiJS")+liuDiaoDao.getThisYearJinShiCount(year, "莱芜市"));
			}
		}

		return smoeMap;
	}


	static void addEveryYearCount(Map<String,Long> resultMap,List<Map<String,Long>> dataMap){
		for (Map<String, Long> map : dataMap) {
			if ("2012".equals(map.get("year"))){
				resultMap.put("in2012",resultMap.get("in2012")+map.get("num"));
			}
			if ("2013".equals(map.get("year"))){
				resultMap.put("in2013",resultMap.get("in2013")+map.get("num"));
			}
			if ("2014".equals(map.get("year"))){
				resultMap.put("in2014",resultMap.get("in2014")+map.get("num"));
			}
			if ("2015".equals(map.get("year"))){
				resultMap.put("in2015",resultMap.get("in2015")+map.get("num"));
			}
			if ("2018".equals(map.get("year"))){
				resultMap.put("in2018",resultMap.get("in2018")+map.get("num"));
			}
			if ("2019".equals(map.get("year"))){
				resultMap.put("in2019",resultMap.get("in2019")+map.get("num"));
			}
			if ("2020".equals(map.get("year"))){
				resultMap.put("in2020",resultMap.get("in2020")+map.get("num"));
			}
		}
	}

	static void getTotalNum(Map<String,Object> firstMap){
		Long totalNum = 0L;
		totalNum+=(Long)firstMap.get("JiNanShi");
		totalNum+=(Long)firstMap.get("QingDaoShi");
		totalNum+=(Long)firstMap.get("ZiBoShi");
		totalNum+=(Long)firstMap.get("ZaoZhuangShi");
		totalNum+=(Long)firstMap.get("DongYingShi");
		totalNum+=(Long)firstMap.get("YanTaiShi");
		totalNum+=(Long)firstMap.get("WeiFangShi");
		totalNum+=(Long)firstMap.get("JiNingShi");
		totalNum+=(Long)firstMap.get("TaiAnShi");
		totalNum+=(Long)firstMap.get("WeiHaiShi");
		totalNum+=(Long)firstMap.get("RiZhaoShi");
		totalNum+=(Long)firstMap.get("BinZhouShi");
		totalNum+=(Long)firstMap.get("LinYiShi");
		totalNum+=(Long)firstMap.get("DeZhouShi");
		totalNum+=(Long)firstMap.get("HeZeShi");
		totalNum+=(Long)firstMap.get("LiaoChengShi");
		firstMap.put("totalNum",totalNum);
	}

	static void AddCityCount(Map<String,Object> firstMap,List<Map<String,Long>> resultMap){
		for (Map<String, Long> result : resultMap) {
			if ("济南市".equals(result.get("CityName"))){
				firstMap.put("JiNanShi",(Long)firstMap.get("JiNanShi")+result.get("num"));
			}
			if ("青岛市".equals(result.get("CityName"))){
				firstMap.put("QingDaoShi",(Long)firstMap.get("QingDaoShi")+result.get("num"));
			}
			if ("淄博市".equals(result.get("CityName"))){
				firstMap.put("ZiBoShi",(Long)firstMap.get("ZiBoShi")+result.get("num"));
			}
			if ("枣庄市".equals(result.get("CityName"))){
				firstMap.put("ZaoZhuangShi",(Long)firstMap.get("ZaoZhuangShi")+result.get("num"));
			}
			if ("东营市".equals(result.get("CityName"))){
				firstMap.put("DongYingShi",(Long)firstMap.get("DongYingShi")+result.get("num"));
			}
			if ("烟台市".equals(result.get("CityName"))){
				firstMap.put("YanTaiShi",(Long)firstMap.get("YanTaiShi")+result.get("num"));
			}
			if ("潍坊市".equals(result.get("CityName"))){
				firstMap.put("WeiFangShi",(Long)firstMap.get("WeiFangShi")+result.get("num"));
			}
			if ("济宁市".equals(result.get("CityName"))){
				firstMap.put("JiNingShi",(Long)firstMap.get("JiNingShi")+result.get("num"));
			}
			if ("泰安市".equals(result.get("CityName"))){
				firstMap.put("TaiAnShi",(Long)firstMap.get("TaiAnShi")+result.get("num"));
			}
			if ("威海市".equals(result.get("CityName"))){
				firstMap.put("WeiHaiShi",(Long)firstMap.get("WeiHaiShi")+result.get("num"));
			}
			if ("日照市".equals(result.get("CityName"))){
				firstMap.put("RiZhaoShi",(Long)firstMap.get("RiZhaoShi")+result.get("num"));
			}
			if ("滨州市".equals(result.get("CityName"))){
				firstMap.put("BinZhouShi",(Long)firstMap.get("BinZhouShi")+result.get("num"));
			}
			if ("临沂市".equals(result.get("CityName"))){
				firstMap.put("LinYiShi",(Long)firstMap.get("LinYiShi")+result.get("num"));
			}
			if ("德州市".equals(result.get("CityName"))){
				firstMap.put("DeZhouShi",(Long)firstMap.get("DeZhouShi")+result.get("num"));
			}
			if ("菏泽市".equals(result.get("CityName"))){
				firstMap.put("HeZeShi",(Long)firstMap.get("HeZeShi")+result.get("num"));
			}
			if ("聊城市".equals(result.get("CityName"))){
				firstMap.put("LiaoChengShi",(Long)firstMap.get("LiaoChengShi")+result.get("num"));
			}
			if ("莱芜区".equals(result.get("CityName"))){
				firstMap.put("JiNanShi",(Long)firstMap.get("JiNanShi")+result.get("num"));
			}
			if ("莱芜市".equals(result.get("CityName"))){
				firstMap.put("JiNanShi",(Long)firstMap.get("JiNanShi")+result.get("num"));
			}
		}
	}



	@Override
	public Map manAndWomenCount(String checkCity,String checkArea) {
		Map<String,String> resultMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.0");
		Calendar calendar = Calendar.getInstance();
		//判断城市是否为空
		if (StringUtils.isNotBlank(checkCity)){
			//判断区县是否为空，如果为空获取当前城市男女患病率
			if (StringUtils.isBlank(checkArea)){
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;

				if (month<=6){
						resultMap = redisTemplate.opsForHash().entries(checkCity+"morbidity");
						if (resultMap.size()==0){
							List<Map<String,Object>> ldataList = redisTemplate.opsForList().range(checkCity+"ldataList",0,-1);
							List<Map<String,Object>> rdataList = redisTemplate.opsForList().range(checkCity+"rdataList",0,-1);
							List<Map<String,Integer>> yuceqianList = redisTemplate.opsForList().range(checkCity+"yuceqianList",0,-1);
							List<Map<String,Integer>> yucehouList = new ArrayList<>();
							if (ldataList.size()==0 || rdataList.size()==0 || yuceqianList.size()==0){
								List<Map<String,Object>> scYuce = studentDao.getScYuCeForProvince(year-1,checkCity, checkArea);
								if (scYuce.size()==0){
									resultMap.put("jxfabinglv","0");
									resultMap.put("lcfabinglv","0");
									resultMap.put("zxfabinglv","0");
									redisTemplate.opsForHash().putAll(checkCity+"morbidity",resultMap);
									return resultMap;
								}
								for (Map<String, Object> stuMap : scYuce) {
									Double dioAL = 0.0;
									Double dioSL = 0.0;
									Double dioCL = 0.0;
									Double dioAR = 0.0;
									Double dioSR = 0.0;
									Double dioCR = 0.0;
									Double corML = 0.0;
									Double corDL = 0.0;
									Double corMR = 0.0;
									Double corDR = 0.0;
									Double eyeaxisOd = 0.0;
									Double eyeaxisOs = 0.0;
									Double eyeaxis_corneal_L = 0.0;
									Double eyeaxis_corneal_R = 0.0;
									String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
									String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
									Double dxqjl = 0.0;
									Double dxqjr = 0.0;
									try {
										NakedOs = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOs);
										NakedOd = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOd);
										dxqjl= Double.parseDouble(stuMap.get("DXQJL").toString());
										dxqjr= Double.parseDouble(stuMap.get("DXQJR").toString());
									} catch (Exception e) {
										continue;
									}


									Map<String,Object> lMap = new HashMap<>();
									Map<String,Object> rMap = new HashMap<>();
									Integer id = (Integer) stuMap.get("id");
									Integer optId = studentDao.getOptId(id);
									List<Map<String,Object>> diopterData = studentDao.getDiopterData(optId);

									for (Map<String, Object> dioMap : diopterData) {
										String ifRl = dioMap.get("ifRL").toString();
										if ("L".equals(ifRl)){
											dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
											dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
											dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
										}

										if ("R".equals(ifRl)){
											dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
											dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
											dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
										}
									}
									List<Map<String,Object>> corData = studentDao.getCornealData(optId);
									for (Map<String, Object> corMap : corData) {
										String ifRL = corMap.get("ifRL").toString();
										if ("L".equals(ifRL)){
											corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
											corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
										}
										if ("R".equals(ifRL)){
											corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
											corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
										}
									}

									Map<String,Double> eyeaxisData = studentDao.getEyeAxisData(id);
									if (eyeaxisData!=null){
										eyeaxisOd = eyeaxisData.get("od");
										eyeaxisOs = eyeaxisData.get("os");
									}

									if (eyeaxisOd == 0.0 || corMR ==0.0){
										eyeaxis_corneal_R = 0.0;
									}else {
										eyeaxis_corneal_R = eyeaxisOd/corMR;
									}
									if (eyeaxisOs == 0.0 || corML==0.0){
										eyeaxis_corneal_L = 0.0;
									}else {
										eyeaxis_corneal_L = eyeaxisOs/corML;
									}
									Double age = Double.parseDouble(stuMap.get("age").toString());

									lMap.put("student",id+"|L");
									lMap.put("life_farvision",0);
									lMap.put("naked_farvision",Double.parseDouble(NakedOs));
									lMap.put("diopter_s1",dioSL);
									lMap.put("diopter_c1",dioCL);
									lMap.put("diopter_a1",dioAL);
									lMap.put("y1",dxqjl);
									lMap.put("corneal_mm",corML);
									lMap.put("corneal_d",corDL);
									lMap.put("eyeaxis",eyeaxisOs);
									lMap.put("age",age);
									lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);


									rMap.put("student",id+"|R");
									rMap.put("life_farvision",0);
									rMap.put("naked_farvision",Double.parseDouble(NakedOd));
									rMap.put("diopter_s1",dioSR);
									rMap.put("diopter_c1",dioCR);
									rMap.put("diopter_a1",dioAR);
									rMap.put("y1",dxqjr);
									rMap.put("corneal_mm",corMR);
									rMap.put("corneal_d",corDR);
									rMap.put("eyeaxis",eyeaxisOd);
									rMap.put("age",age);
									rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
									ldataList.add(lMap);
									rdataList.add(rMap);

									NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
									NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

									Double naked_Farvision = Double.parseDouble(NakedOs)>Double.parseDouble(NakedOd)?Double.parseDouble(NakedOd):Double.parseDouble(NakedOs);
									Double dxqj = Double.parseDouble(stuMap.get("DXQJL").toString())>Double.parseDouble(stuMap.get("DXQJR").toString())?Double.parseDouble(stuMap.get("DXQJR").toString()):Double.parseDouble(stuMap.get("DXQJL").toString());
									Map<String,Integer> yuceqianMap = new HashMap<>();
									if (dxqj > 0.75){
										yuceqianMap.put("type",1);
										yuceqianMap.put("id",id);
										yuceqianList.add(yuceqianMap);
										continue;
									}
									if (dxqj >= -0.5 && dxqj <= 0.75){
										yuceqianMap.put("type",2);
										yuceqianMap.put("id",id);
										yuceqianList.add(yuceqianMap);
										continue;
									}
									if (naked_Farvision >= 5.0 && dxqj<-0.5){
										yuceqianMap.put("type",3);
										yuceqianMap.put("id",id);
										yuceqianList.add(yuceqianMap);
										continue;
									}
									if (naked_Farvision < 5.0 && dxqj<-0.5){
										yuceqianMap.put("type",4);
										yuceqianMap.put("id",id);
										yuceqianList.add(yuceqianMap);
										continue;
									}
								}
								redisTemplate.opsForList().rightPushAll(checkCity+"ldataList",ldataList);
								redisTemplate.opsForList().rightPushAll(checkCity+"rdataList",rdataList);
								redisTemplate.opsForList().rightPushAll(checkCity+"yuceqianList",yuceqianList);
							}




							List<Map<String,Object>> lAllData = new ArrayList<>();
							List<Map<String,Object>> rAllData = new ArrayList<>();
							int lsize = ldataList.size();
							int rsize = rdataList.size();
							int ltoIndex = 2000;
							int rtoIndex = 2000;
							for (int i = 0; i < ldataList.size(); i+=2000) {
								if (i+2000>lsize){
									ltoIndex=lsize-i;
								}
								List<Map<String, Object>> subList = ldataList.subList(i, i+ltoIndex);
								HttpHeaders httpHeaders = new HttpHeaders();
								httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
								HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(subList, httpHeaders);
								ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entity, String.class);
								String response = responseEntity.getBody();
								lAllData .addAll(JSON.parseObject(response, List.class));
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							for (int i = 0; i < rdataList.size(); i+=2000) {
								if (i+2000>rsize){
									rtoIndex=rsize-i;
								}
								List<Map<String, Object>> subList = rdataList.subList(i, i+rtoIndex);
								HttpHeaders httpHeaders = new HttpHeaders();
								httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
								HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(subList, httpHeaders);
								ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entity, String.class);
								String response = responseEntity.getBody();
								rAllData .addAll(JSON.parseObject(response, List.class));
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}


							for (Map<String, Object> lMap : lAllData) {
								for (Map<String, Object> rMap : rAllData) {
									int indexL = lMap.get("student").toString().indexOf("|");
									int indexR = rMap.get("student").toString().indexOf("|");
									String idL = lMap.get("student").toString().substring(0, indexL);
									String idR = rMap.get("student").toString().substring(0, indexR);
									if (idL.equals(idR)){
										Map<String,Integer> yucehouMap = new HashMap<>();
										int ltype = Integer.parseInt(lMap.get("type").toString());
										int rtype = Integer.parseInt(rMap.get("type").toString());
										int type = ltype>rtype?ltype:rtype;
										yucehouMap.put("type",type);
										yucehouMap.put("id",Integer.parseInt(idL));
										yucehouList.add(yucehouMap);
									}
								}
							}
							Integer fzcNum = 0;
							Integer flcNum = 0;
							Integer fjxNum = 0;

							Integer zcTolc = 0;
							Integer zcTojx = 0;
							Integer zcTozx = 0;

							Integer lcTojx = 0;
							Integer lcTozx = 0;

							Integer jxTolc = 0;
							Integer jxTozx = 0;

							for (Map<String, Integer> fMap : yuceqianList) {
								for (Map<String, Integer> sMap : yucehouList) {
									if (fMap.get("id").equals(sMap.get("id"))) {
										int ftype = Integer.parseInt(fMap.get("type").toString());
										int stype = Integer.parseInt(sMap.get("type").toString());
										if (1==ftype){
											fzcNum++;
											if (2==stype){
												zcTolc++;
											}
											if (3==stype){
												zcTojx++;
											}
											if (stype>=4){
												zcTozx++;
											}
										}
										if (2==ftype){
											flcNum++;
											if (3==stype){
												lcTojx++;
											}
											if (stype>=4){
												lcTozx++;
											}
										}
										if (3==ftype){
											fjxNum++;
											if (2==stype){
												jxTolc++;
											}
											if (stype>=4){
												jxTozx++;
											}
										}
									}
								}
							}

							int jxfbnum = zcTojx + lcTojx;
							int lcfbnum = zcTolc + jxTolc;
							int zxfbnum = zcTozx + lcTozx + jxTozx;

							BigDecimal bg = new BigDecimal((float) jxfbnum / (fzcNum + flcNum));
							String jxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
							bg = new BigDecimal((float)lcfbnum/(fzcNum+fjxNum));
							String lcfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

							bg = new BigDecimal((float)zxfbnum/(fzcNum+fjxNum+flcNum));
							String zxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

							resultMap.put("jxfabinglv",jxfabinglv);
							resultMap.put("lcfabinglv",lcfabinglv);
							resultMap.put("zxfabinglv",zxfabinglv);

							redisTemplate.opsForHash().putAll(checkCity+"morbidity",resultMap);
							return resultMap;
						}
					}
				if (month>6){
						int date = calendar.get(Calendar.DATE);
						resultMap = redisTemplate.opsForHash().entries(checkCity+month+date+"nextYearMorbidity");
						if (resultMap.size()==0){
							List<Map<String,Object>> ldataList = new ArrayList<>();
							List<Map<String,Object>> rdataList = new ArrayList<>();
							List<Map<String,Integer>> yuceqianList = new ArrayList<>();
							List<Map<String,Integer>> yucehouList = new ArrayList<>();
							List<Map<String,Object>> scYuce = studentDao.getScYuCeForProvince(year,checkCity, checkArea);

							for (Map<String, Object> stuMap : scYuce) {
								Double dioAL = 0.0;
								Double dioSL = 0.0;
								Double dioCL = 0.0;
								Double dioAR = 0.0;
								Double dioSR = 0.0;
								Double dioCR = 0.0;
								Double corML = 0.0;
								Double corDL = 0.0;
								Double corMR = 0.0;
								Double corDR = 0.0;
								Double eyeaxisOd = 0.0;
								Double eyeaxisOs = 0.0;
								Double eyeaxis_corneal_L = 0.0;
								Double eyeaxis_corneal_R = 0.0;
								String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
								String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
								Double dxqjl = 0.0;
								Double dxqjr = 0.0;
								try {
									NakedOs = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOs);
									NakedOd = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOd);
									dxqjl= Double.parseDouble(stuMap.get("DXQJL").toString());
									dxqjr= Double.parseDouble(stuMap.get("DXQJR").toString());
								} catch (Exception e) {
									continue;
								}


								Map<String,Object> lMap = new HashMap<>();
								Map<String,Object> rMap = new HashMap<>();
								Integer id = (Integer) stuMap.get("id");
								Integer optId = studentDao.getOptId(id);
								List<Map<String,Object>> diopterData = studentDao.getDiopterData(optId);

								for (Map<String, Object> dioMap : diopterData) {
									String ifRl = dioMap.get("ifRL").toString();
									if ("L".equals(ifRl)){
										dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
										dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
										dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
									}

									if ("R".equals(ifRl)){
										dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
										dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
										dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
									}
								}
								List<Map<String,Object>> corData = studentDao.getCornealData(optId);
								for (Map<String, Object> corMap : corData) {
									String ifRL = corMap.get("ifRL").toString();
									if ("L".equals(ifRL)){
										corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
										corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
									}
									if ("R".equals(ifRL)){
										corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
										corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
									}
								}

								Map<String,Double> eyeaxisData = studentDao.getEyeAxisData(id);
								if (eyeaxisData!=null){
									eyeaxisOd = eyeaxisData.get("od");
									eyeaxisOs = eyeaxisData.get("os");
								}

								if (eyeaxisOd == 0.0 || corMR ==0.0){
									eyeaxis_corneal_R = 0.0;
								}else {
									eyeaxis_corneal_R = eyeaxisOd/corMR;
								}
								if (eyeaxisOs == 0.0 || corML==0.0){
									eyeaxis_corneal_L = 0.0;
								}else {
									eyeaxis_corneal_L = eyeaxisOs/corML;
								}
								Double age = Double.parseDouble(stuMap.get("age").toString());

								lMap.put("student",id+"|L");
								lMap.put("life_farvision",0);
								lMap.put("naked_farvision",Double.parseDouble(NakedOs));
								lMap.put("diopter_s1",dioSL);
								lMap.put("diopter_c1",dioCL);
								lMap.put("diopter_a1",dioAL);
								lMap.put("y1",dxqjl);
								lMap.put("corneal_mm",corML);
								lMap.put("corneal_d",corDL);
								lMap.put("eyeaxis",eyeaxisOs);
								lMap.put("age",age);
								lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);


								rMap.put("student",id+"|R");
								rMap.put("life_farvision",0);
								rMap.put("naked_farvision",Double.parseDouble(NakedOd));
								rMap.put("diopter_s1",dioSR);
								rMap.put("diopter_c1",dioCR);
								rMap.put("diopter_a1",dioAR);
								rMap.put("y1",dxqjr);
								rMap.put("corneal_mm",corMR);
								rMap.put("corneal_d",corDR);
								rMap.put("eyeaxis",eyeaxisOd);
								rMap.put("age",age);
								rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
								ldataList.add(lMap);
								rdataList.add(rMap);

								NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
								NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

								Double naked_Farvision = Double.parseDouble(NakedOs)>Double.parseDouble(NakedOd)?Double.parseDouble(NakedOd):Double.parseDouble(NakedOs);
								Double dxqj = Double.parseDouble(stuMap.get("DXQJL").toString())>Double.parseDouble(stuMap.get("DXQJR").toString())?Double.parseDouble(stuMap.get("DXQJR").toString()):Double.parseDouble(stuMap.get("DXQJL").toString());
								Map<String,Integer> yuceqianMap = new HashMap<>();
								if (dxqj > 0.75){
									yuceqianMap.put("type",1);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
								if (dxqj >= -0.5 && dxqj <= 0.75){
									yuceqianMap.put("type",2);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
								if (naked_Farvision >= 5.0 && dxqj<-0.5){
									yuceqianMap.put("type",3);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
								if (naked_Farvision < 5.0 && dxqj<-0.5){
									yuceqianMap.put("type",4);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
							}
							List<Map<String,Object>> lAllData = new ArrayList<>();
							List<Map<String,Object>> rAllData = new ArrayList<>();
							HttpHeaders httpHeadersL = new HttpHeaders();
							httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
							HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(ldataList, httpHeadersL);
							ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entityL, String.class);
							String responseL = responseEntityL.getBody();
							lAllData = JSON.parseObject(responseL, List.class);

							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							HttpHeaders httpHeadersR = new HttpHeaders();
							httpHeadersR.setContentType(MediaType.APPLICATION_JSON_UTF8);
							HttpEntity<List<Map<String, Object>>> entityR = new HttpEntity<>(rdataList, httpHeadersR);
							ResponseEntity<String> responseEntityR = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entityR, String.class);
							String responseR = responseEntityR.getBody();
							rAllData = JSON.parseObject(responseR, List.class);

							for (Map<String, Object> lMap : lAllData) {
								for (Map<String, Object> rMap : rAllData) {
									int indexL = lMap.get("student").toString().indexOf("|");
									int indexR = rMap.get("student").toString().indexOf("|");
									String idL = lMap.get("student").toString().substring(0, indexL);
									String idR = rMap.get("student").toString().substring(0, indexR);
									if (idL.equals(idR)){
										Map<String,Integer> yucehouMap = new HashMap<>();
										int ltype = Integer.parseInt(lMap.get("type").toString());
										int rtype = Integer.parseInt(rMap.get("type").toString());
										int type = ltype>rtype?ltype:rtype;
										yucehouMap.put("type",type);
										yucehouMap.put("id",Integer.parseInt(idL));
										yucehouList.add(yucehouMap);
									}
								}
							}
							Integer fzcNum = 0;
							Integer flcNum = 0;
							Integer fjxNum = 0;

							Integer zcTolc = 0;
							Integer zcTojx = 0;
							Integer zcTozx = 0;

							Integer lcTojx = 0;
							Integer lcTozx = 0;

							Integer jxTolc = 0;
							Integer jxTozx = 0;

							for (Map<String, Integer> fMap : yuceqianList) {
								for (Map<String, Integer> sMap : yucehouList) {
									if (fMap.get("id").equals(sMap.get("id"))) {
										int ftype = Integer.parseInt(fMap.get("type").toString());
										int stype = Integer.parseInt(sMap.get("type").toString());
										if (1==ftype){
											fzcNum++;
											if (2==stype){
												zcTolc++;
											}
											if (3==stype){
												zcTojx++;
											}
											if (stype>=4){
												zcTozx++;
											}
										}
										if (2==ftype){
											flcNum++;
											if (3==stype){
												lcTojx++;
											}
											if (stype>=4){
												lcTozx++;
											}
										}
										if (3==ftype){
											fjxNum++;
											if (2==stype){
												jxTolc++;
											}
											if (stype>=4){
												jxTozx++;
											}
										}
									}
								}
							}

							int jxfbnum = zcTojx + lcTojx;
							int lcfbnum = zcTolc + jxTolc;
							int zxfbnum = zcTozx + lcTozx + jxTozx;

							BigDecimal bg = new BigDecimal((float) jxfbnum / (fzcNum + flcNum));
							String jxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
							bg = new BigDecimal((float)lcfbnum/(fzcNum+fjxNum));
							String lcfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

							bg = new BigDecimal((float)zxfbnum/(fzcNum+fjxNum+flcNum));
							String zxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

							resultMap.put("jxfabinglv",jxfabinglv);
							resultMap.put("lcfabinglv",lcfabinglv);
							resultMap.put("zxfabinglv",zxfabinglv);
							redisTemplate.opsForHash().putAll(checkCity+month+date+"nextYearMorbidity",resultMap);
							return resultMap;
						}
					}
			}
			//区县如果不为空，获取当前城市的区县的男女患病率
			if (StringUtils.isNotBlank(checkArea)){
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;
				if (month<=6){
					resultMap = redisTemplate.opsForHash().entries(checkCity+checkArea+"thisYearMorbidity");
					if (resultMap.size()==0){
						List<Map<String,Object>> ldataList = redisTemplate.opsForList().range(checkCity+checkArea+"ldataList",0,-1);
						List<Map<String,Object>> rdataList = redisTemplate.opsForList().range(checkCity+checkArea+"rdataList",0,-1);
						List<Map<String,Integer>> yuceqianList = redisTemplate.opsForList().range(checkCity+checkArea+"yuceqianList",0,-1);
						List<Map<String,Integer>> yucehouList = new ArrayList<>();
						if (ldataList.size()==0 || rdataList.size()==0 || yuceqianList.size()==0){
							List<Map<String,Object>> scYuce = studentDao.getScYuCeForProvince(year-1,checkCity, checkArea);
							for (Map<String, Object> stuMap : scYuce) {
								Double dioAL = 0.0;
								Double dioSL = 0.0;
								Double dioCL = 0.0;
								Double dioAR = 0.0;
								Double dioSR = 0.0;
								Double dioCR = 0.0;
								Double corML = 0.0;
								Double corDL = 0.0;
								Double corMR = 0.0;
								Double corDR = 0.0;
								Double eyeaxisOd = 0.0;
								Double eyeaxisOs = 0.0;
								Double eyeaxis_corneal_L = 0.0;
								Double eyeaxis_corneal_R = 0.0;
								String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
								String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
								Double dxqjl = 0.0;
								Double dxqjr = 0.0;
								try {
									NakedOs = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOs);
									NakedOd = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOd);
									dxqjl= Double.parseDouble(stuMap.get("DXQJL").toString());
									dxqjr= Double.parseDouble(stuMap.get("DXQJR").toString());
								} catch (Exception e) {
									continue;
								}


								Map<String,Object> lMap = new HashMap<>();
								Map<String,Object> rMap = new HashMap<>();
								Integer id = (Integer) stuMap.get("id");
								Integer optId = studentDao.getOptId(id);
								List<Map<String,Object>> diopterData = studentDao.getDiopterData(optId);

								for (Map<String, Object> dioMap : diopterData) {
									String ifRl = dioMap.get("ifRL").toString();
									if ("L".equals(ifRl)){
										dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
										dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
										dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
									}

									if ("R".equals(ifRl)){
										dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
										dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
										dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
									}
								}
								List<Map<String,Object>> corData = studentDao.getCornealData(optId);
								for (Map<String, Object> corMap : corData) {
									String ifRL = corMap.get("ifRL").toString();
									if ("L".equals(ifRL)){
										corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
										corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
									}
									if ("R".equals(ifRL)){
										corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
										corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
									}
								}

								Map<String,Double> eyeaxisData = studentDao.getEyeAxisData(id);
								if (eyeaxisData!=null){
									eyeaxisOd = eyeaxisData.get("od");
									eyeaxisOs = eyeaxisData.get("os");
								}

								if (eyeaxisOd == 0.0 || corMR ==0.0){
									eyeaxis_corneal_R = 0.0;
								}else {
									eyeaxis_corneal_R = eyeaxisOd/corMR;
								}
								if (eyeaxisOs == 0.0 || corML==0.0){
									eyeaxis_corneal_L = 0.0;
								}else {
									eyeaxis_corneal_L = eyeaxisOs/corML;
								}
								Double age = Double.parseDouble(stuMap.get("age").toString());

								lMap.put("student",id+"|L");
								lMap.put("life_farvision",0);
								lMap.put("naked_farvision",Double.parseDouble(NakedOs));
								lMap.put("diopter_s1",dioSL);
								lMap.put("diopter_c1",dioCL);
								lMap.put("diopter_a1",dioAL);
								lMap.put("y1",dxqjl);
								lMap.put("corneal_mm",corML);
								lMap.put("corneal_d",corDL);
								lMap.put("eyeaxis",eyeaxisOs);
								lMap.put("age",age);
								lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);


								rMap.put("student",id+"|R");
								rMap.put("life_farvision",0);
								rMap.put("naked_farvision",Double.parseDouble(NakedOd));
								rMap.put("diopter_s1",dioSR);
								rMap.put("diopter_c1",dioCR);
								rMap.put("diopter_a1",dioAR);
								rMap.put("y1",dxqjr);
								rMap.put("corneal_mm",corMR);
								rMap.put("corneal_d",corDR);
								rMap.put("eyeaxis",eyeaxisOd);
								rMap.put("age",age);
								rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
								ldataList.add(lMap);
								rdataList.add(rMap);

								NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
								NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

								Double naked_Farvision = Double.parseDouble(NakedOs)>Double.parseDouble(NakedOd)?Double.parseDouble(NakedOd):Double.parseDouble(NakedOs);
								Double dxqj = Double.parseDouble(stuMap.get("DXQJL").toString())>Double.parseDouble(stuMap.get("DXQJR").toString())?Double.parseDouble(stuMap.get("DXQJR").toString()):Double.parseDouble(stuMap.get("DXQJL").toString());
								Map<String,Integer> yuceqianMap = new HashMap<>();
								if (dxqj > 0.75){
									yuceqianMap.put("type",1);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
								if (dxqj >= -0.5 && dxqj <= 0.75){
									yuceqianMap.put("type",2);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
								if (naked_Farvision >= 5.0 && dxqj<-0.5){
									yuceqianMap.put("type",3);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
								if (naked_Farvision < 5.0 && dxqj<-0.5){
									yuceqianMap.put("type",4);
									yuceqianMap.put("id",id);
									yuceqianList.add(yuceqianMap);
									continue;
								}
							}
							redisTemplate.opsForList().rightPushAll(checkCity+checkArea+"ldataList",ldataList);
							redisTemplate.opsForList().rightPushAll(checkCity+checkArea+"rdataList",rdataList);
							redisTemplate.opsForList().rightPushAll(checkCity+checkArea+"yuceqianList",yuceqianList);
						}




						List<Map<String,Object>> lAllData = new ArrayList<>();
						List<Map<String,Object>> rAllData = new ArrayList<>();
						int lsize = ldataList.size();
						int rsize = rdataList.size();
						int ltoIndex = 2000;
						int rtoIndex = 2000;
						for (int i = 0; i < ldataList.size(); i+=2000) {
							if (i+2000>lsize){
								ltoIndex=lsize-i;
							}
							List<Map<String, Object>> subList = ldataList.subList(i, i+ltoIndex);
							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
							HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(subList, httpHeaders);
							ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entity, String.class);
							String response = responseEntity.getBody();
							lAllData .addAll(JSON.parseObject(response, List.class));
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						for (int i = 0; i < rdataList.size(); i+=2000) {
							if (i+2000>rsize){
								rtoIndex=rsize-i;
							}
							List<Map<String, Object>> subList = rdataList.subList(i, i+rtoIndex);
							HttpHeaders httpHeaders = new HttpHeaders();
							httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
							HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(subList, httpHeaders);
							ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entity, String.class);
							String response = responseEntity.getBody();
							rAllData .addAll(JSON.parseObject(response, List.class));
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}


						for (Map<String, Object> lMap : lAllData) {
							for (Map<String, Object> rMap : rAllData) {
								int indexL = lMap.get("student").toString().indexOf("|");
								int indexR = rMap.get("student").toString().indexOf("|");
								String idL = lMap.get("student").toString().substring(0, indexL);
								String idR = rMap.get("student").toString().substring(0, indexR);
								if (idL.equals(idR)){
									Map<String,Integer> yucehouMap = new HashMap<>();
									int ltype = Integer.parseInt(lMap.get("type").toString());
									int rtype = Integer.parseInt(rMap.get("type").toString());
									int type = ltype>rtype?ltype:rtype;
									yucehouMap.put("type",type);
									yucehouMap.put("id",Integer.parseInt(idL));
									yucehouList.add(yucehouMap);
								}
							}
						}
						Integer fzcNum = 0;
						Integer flcNum = 0;
						Integer fjxNum = 0;

						Integer zcTolc = 0;
						Integer zcTojx = 0;
						Integer zcTozx = 0;

						Integer lcTojx = 0;
						Integer lcTozx = 0;

						Integer jxTolc = 0;
						Integer jxTozx = 0;

						for (Map<String, Integer> fMap : yuceqianList) {
							for (Map<String, Integer> sMap : yucehouList) {
								if (fMap.get("id").equals(sMap.get("id"))) {
									int ftype = Integer.parseInt(fMap.get("type").toString());
									int stype = Integer.parseInt(sMap.get("type").toString());
									if (1==ftype){
										fzcNum++;
										if (2==stype){
											zcTolc++;
										}
										if (3==stype){
											zcTojx++;
										}
										if (stype>=4){
											zcTozx++;
										}
									}
									if (2==ftype){
										flcNum++;
										if (3==stype){
											lcTojx++;
										}
										if (stype>=4){
											lcTozx++;
										}
									}
									if (3==ftype){
										fjxNum++;
										if (2==stype){
											jxTolc++;
										}
										if (stype>=4){
											jxTozx++;
										}
									}
								}
							}
						}

						int jxfbnum = zcTojx + lcTojx;
						int lcfbnum = zcTolc + jxTolc;
						int zxfbnum = zcTozx + lcTozx + jxTozx;

						BigDecimal bg = new BigDecimal((float) jxfbnum / (fzcNum + flcNum));
						String jxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
						bg = new BigDecimal((float)lcfbnum/(fzcNum+fjxNum));
						String lcfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

						bg = new BigDecimal((float)zxfbnum/(fzcNum+fjxNum+flcNum));
						String zxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

						resultMap.put("jxfabinglv",jxfabinglv);
						resultMap.put("lcfabinglv",lcfabinglv);
						resultMap.put("zxfabinglv",zxfabinglv);

						redisTemplate.opsForHash().putAll(checkCity+checkArea+"thisYearMorbidity",resultMap);
						return resultMap;
					}
				}
				if (month>6){
					int date = calendar.get(Calendar.DATE);
					resultMap = redisTemplate.opsForHash().entries(checkCity+checkArea+month+date+"nextYearMorbidity");
					if (resultMap.size()==0){
						List<Map<String,Object>> ldataList = new ArrayList<>();
						List<Map<String,Object>> rdataList = new ArrayList<>();
						List<Map<String,Integer>> yuceqianList = new ArrayList<>();
						List<Map<String,Integer>> yucehouList = new ArrayList<>();
						List<Map<String,Object>> scYuce = studentDao.getScYuCeForProvince(year,checkCity, checkArea);

						for (Map<String, Object> stuMap : scYuce) {
							Double dioAL = 0.0;
							Double dioSL = 0.0;
							Double dioCL = 0.0;
							Double dioAR = 0.0;
							Double dioSR = 0.0;
							Double dioCR = 0.0;
							Double corML = 0.0;
							Double corDL = 0.0;
							Double corMR = 0.0;
							Double corDR = 0.0;
							Double eyeaxisOd = 0.0;
							Double eyeaxisOs = 0.0;
							Double eyeaxis_corneal_L = 0.0;
							Double eyeaxis_corneal_R = 0.0;
							String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
							String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
							Double dxqjl = 0.0;
							Double dxqjr = 0.0;
							try {
								NakedOs = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOs);
								NakedOd = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOd);
								dxqjl= Double.parseDouble(stuMap.get("DXQJL").toString());
								dxqjr= Double.parseDouble(stuMap.get("DXQJR").toString());
							} catch (Exception e) {
								continue;
							}


							Map<String,Object> lMap = new HashMap<>();
							Map<String,Object> rMap = new HashMap<>();
							Integer id = (Integer) stuMap.get("id");
							Integer optId = studentDao.getOptId(id);
							List<Map<String,Object>> diopterData = studentDao.getDiopterData(optId);

							for (Map<String, Object> dioMap : diopterData) {
								String ifRl = dioMap.get("ifRL").toString();
								if ("L".equals(ifRl)){
									dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
									dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
									dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
								}

								if ("R".equals(ifRl)){
									dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
									dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
									dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
								}
							}
							List<Map<String,Object>> corData = studentDao.getCornealData(optId);
							for (Map<String, Object> corMap : corData) {
								String ifRL = corMap.get("ifRL").toString();
								if ("L".equals(ifRL)){
									corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
									corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
								}
								if ("R".equals(ifRL)){
									corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
									corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
								}
							}

							Map<String,Double> eyeaxisData = studentDao.getEyeAxisData(id);
							if (eyeaxisData!=null){
								eyeaxisOd = eyeaxisData.get("od");
								eyeaxisOs = eyeaxisData.get("os");
							}

							if (eyeaxisOd == 0.0 || corMR ==0.0){
								eyeaxis_corneal_R = 0.0;
							}else {
								eyeaxis_corneal_R = eyeaxisOd/corMR;
							}
							if (eyeaxisOs == 0.0 || corML==0.0){
								eyeaxis_corneal_L = 0.0;
							}else {
								eyeaxis_corneal_L = eyeaxisOs/corML;
							}
							Double age = Double.parseDouble(stuMap.get("age").toString());

							lMap.put("student",id+"|L");
							lMap.put("life_farvision",0);
							lMap.put("naked_farvision",Double.parseDouble(NakedOs));
							lMap.put("diopter_s1",dioSL);
							lMap.put("diopter_c1",dioCL);
							lMap.put("diopter_a1",dioAL);
							lMap.put("y1",dxqjl);
							lMap.put("corneal_mm",corML);
							lMap.put("corneal_d",corDL);
							lMap.put("eyeaxis",eyeaxisOs);
							lMap.put("age",age);
							lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);


							rMap.put("student",id+"|R");
							rMap.put("life_farvision",0);
							rMap.put("naked_farvision",Double.parseDouble(NakedOd));
							rMap.put("diopter_s1",dioSR);
							rMap.put("diopter_c1",dioCR);
							rMap.put("diopter_a1",dioAR);
							rMap.put("y1",dxqjr);
							rMap.put("corneal_mm",corMR);
							rMap.put("corneal_d",corDR);
							rMap.put("eyeaxis",eyeaxisOd);
							rMap.put("age",age);
							rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
							ldataList.add(lMap);
							rdataList.add(rMap);

							NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
							NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

							Double naked_Farvision = Double.parseDouble(NakedOs)>Double.parseDouble(NakedOd)?Double.parseDouble(NakedOd):Double.parseDouble(NakedOs);
							Double dxqj = Double.parseDouble(stuMap.get("DXQJL").toString())>Double.parseDouble(stuMap.get("DXQJR").toString())?Double.parseDouble(stuMap.get("DXQJR").toString()):Double.parseDouble(stuMap.get("DXQJL").toString());
							Map<String,Integer> yuceqianMap = new HashMap<>();
							if (dxqj > 0.75){
								yuceqianMap.put("type",1);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
							if (dxqj >= -0.5 && dxqj <= 0.75){
								yuceqianMap.put("type",2);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
							if (naked_Farvision >= 5.0 && dxqj<-0.5){
								yuceqianMap.put("type",3);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
							if (naked_Farvision < 5.0 && dxqj<-0.5){
								yuceqianMap.put("type",4);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
						}
						List<Map<String,Object>> lAllData = new ArrayList<>();
						List<Map<String,Object>> rAllData = new ArrayList<>();
						HttpHeaders httpHeadersL = new HttpHeaders();
						httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
						HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(ldataList, httpHeadersL);
						ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entityL, String.class);
						String responseL = responseEntityL.getBody();
						lAllData = JSON.parseObject(responseL, List.class);

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						HttpHeaders httpHeadersR = new HttpHeaders();
						httpHeadersR.setContentType(MediaType.APPLICATION_JSON_UTF8);
						HttpEntity<List<Map<String, Object>>> entityR = new HttpEntity<>(rdataList, httpHeadersR);
						ResponseEntity<String> responseEntityR = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entityR, String.class);
						String responseR = responseEntityR.getBody();
						rAllData = JSON.parseObject(responseR, List.class);

						for (Map<String, Object> lMap : lAllData) {
							for (Map<String, Object> rMap : rAllData) {
								int indexL = lMap.get("student").toString().indexOf("|");
								int indexR = rMap.get("student").toString().indexOf("|");
								String idL = lMap.get("student").toString().substring(0, indexL);
								String idR = rMap.get("student").toString().substring(0, indexR);
								if (idL.equals(idR)){
									Map<String,Integer> yucehouMap = new HashMap<>();
									int ltype = Integer.parseInt(lMap.get("type").toString());
									int rtype = Integer.parseInt(rMap.get("type").toString());
									int type = ltype>rtype?ltype:rtype;
									yucehouMap.put("type",type);
									yucehouMap.put("id",Integer.parseInt(idL));
									yucehouList.add(yucehouMap);
								}
							}
						}
						Integer fzcNum = 0;
						Integer flcNum = 0;
						Integer fjxNum = 0;

						Integer zcTolc = 0;
						Integer zcTojx = 0;
						Integer zcTozx = 0;

						Integer lcTojx = 0;
						Integer lcTozx = 0;

						Integer jxTolc = 0;
						Integer jxTozx = 0;

						for (Map<String, Integer> fMap : yuceqianList) {
							for (Map<String, Integer> sMap : yucehouList) {
								if (fMap.get("id").equals(sMap.get("id"))) {
									int ftype = Integer.parseInt(fMap.get("type").toString());
									int stype = Integer.parseInt(sMap.get("type").toString());
									if (1==ftype){
										fzcNum++;
										if (2==stype){
											zcTolc++;
										}
										if (3==stype){
											zcTojx++;
										}
										if (stype>=4){
											zcTozx++;
										}
									}
									if (2==ftype){
										flcNum++;
										if (3==stype){
											lcTojx++;
										}
										if (stype>=4){
											lcTozx++;
										}
									}
									if (3==ftype){
										fjxNum++;
										if (2==stype){
											jxTolc++;
										}
										if (stype>=4){
											jxTozx++;
										}
									}
								}
							}
						}

						int jxfbnum = zcTojx + lcTojx;
						int lcfbnum = zcTolc + jxTolc;
						int zxfbnum = zcTozx + lcTozx + jxTozx;

						BigDecimal bg = new BigDecimal((float) jxfbnum / (fzcNum + flcNum));
						String jxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
						bg = new BigDecimal((float)lcfbnum/(fzcNum+fjxNum));
						String lcfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

						bg = new BigDecimal((float)zxfbnum/(fzcNum+fjxNum+flcNum));
						String zxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

						resultMap.put("jxfabinglv",jxfabinglv);
						resultMap.put("lcfabinglv",lcfabinglv);
						resultMap.put("zxfabinglv",zxfabinglv);
						redisTemplate.opsForHash().putAll(checkCity+checkArea+month+date+"nextYearMorbidity",resultMap);
					}
				}
			}
		}

		//城市为空，获取省份发病率
		if (StringUtils.isBlank(checkCity)){
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;

			if (month<=6){
				resultMap = redisTemplate.opsForHash().entries("thisYearMorbidity");
				if (resultMap.size()==0){
					List<Map<String,Object>> ldataList = redisTemplate.opsForList().range(year-1+"ldataList",0,-1);
					List<Map<String,Object>> rdataList = redisTemplate.opsForList().range(year-1+"rdataList",0,-1);
					List<Map<String,Integer>> yuceqianList = redisTemplate.opsForList().range(year-1+"yuceqianList",0,-1);
					List<Map<String,Integer>> yucehouList = new ArrayList<>();
					if (ldataList.size()==0 || rdataList.size()==0 || yuceqianList.size()==0){
						List<Map<String,Object>> scYuce = studentDao.getScYuCeForProvince(year-1,checkCity, checkArea);
						for (Map<String, Object> stuMap : scYuce) {
							Double dioAL = 0.0;
							Double dioSL = 0.0;
							Double dioCL = 0.0;
							Double dioAR = 0.0;
							Double dioSR = 0.0;
							Double dioCR = 0.0;
							Double corML = 0.0;
							Double corDL = 0.0;
							Double corMR = 0.0;
							Double corDR = 0.0;
							Double eyeaxisOd = 0.0;
							Double eyeaxisOs = 0.0;
							Double eyeaxis_corneal_L = 0.0;
							Double eyeaxis_corneal_R = 0.0;
							String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
							String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
							Double dxqjl = 0.0;
							Double dxqjr = 0.0;
							try {
								NakedOs = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOs);
								NakedOd = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOd);
								dxqjl= Double.parseDouble(stuMap.get("DXQJL").toString());
								dxqjr= Double.parseDouble(stuMap.get("DXQJR").toString());
							} catch (Exception e) {
								continue;
							}


							Map<String,Object> lMap = new HashMap<>();
							Map<String,Object> rMap = new HashMap<>();
							Integer id = (Integer) stuMap.get("id");
							Integer optId = studentDao.getOptId(id);
							List<Map<String,Object>> diopterData = studentDao.getDiopterData(optId);

							for (Map<String, Object> dioMap : diopterData) {
								String ifRl = dioMap.get("ifRL").toString();
								if ("L".equals(ifRl)){
									dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
									dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
									dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
								}

								if ("R".equals(ifRl)){
									dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
									dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
									dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
								}
							}
							List<Map<String,Object>> corData = studentDao.getCornealData(optId);
							for (Map<String, Object> corMap : corData) {
								String ifRL = corMap.get("ifRL").toString();
								if ("L".equals(ifRL)){
									corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
									corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
								}
								if ("R".equals(ifRL)){
									corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
									corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
								}
							}

							Map<String,Double> eyeaxisData = studentDao.getEyeAxisData(id);
							if (eyeaxisData!=null){
								eyeaxisOd = eyeaxisData.get("od");
								eyeaxisOs = eyeaxisData.get("os");
							}

							if (eyeaxisOd == 0.0 || corMR ==0.0){
								eyeaxis_corneal_R = 0.0;
							}else {
								eyeaxis_corneal_R = eyeaxisOd/corMR;
							}
							if (eyeaxisOs == 0.0 || corML==0.0){
								eyeaxis_corneal_L = 0.0;
							}else {
								eyeaxis_corneal_L = eyeaxisOs/corML;
							}
							Double age = Double.parseDouble(stuMap.get("age").toString());

							lMap.put("student",id+"|L");
							lMap.put("life_farvision",0);
							lMap.put("naked_farvision",Double.parseDouble(NakedOs));
							lMap.put("diopter_s1",dioSL);
							lMap.put("diopter_c1",dioCL);
							lMap.put("diopter_a1",dioAL);
							lMap.put("y1",dxqjl);
							lMap.put("corneal_mm",corML);
							lMap.put("corneal_d",corDL);
							lMap.put("eyeaxis",eyeaxisOs);
							lMap.put("age",age);
							lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);


							rMap.put("student",id+"|R");
							rMap.put("life_farvision",0);
							rMap.put("naked_farvision",Double.parseDouble(NakedOd));
							rMap.put("diopter_s1",dioSR);
							rMap.put("diopter_c1",dioCR);
							rMap.put("diopter_a1",dioAR);
							rMap.put("y1",dxqjr);
							rMap.put("corneal_mm",corMR);
							rMap.put("corneal_d",corDR);
							rMap.put("eyeaxis",eyeaxisOd);
							rMap.put("age",age);
							rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
							ldataList.add(lMap);
							rdataList.add(rMap);

							NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
							NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

							Double naked_Farvision = Double.parseDouble(NakedOs)>Double.parseDouble(NakedOd)?Double.parseDouble(NakedOd):Double.parseDouble(NakedOs);
							Double dxqj = Double.parseDouble(stuMap.get("DXQJL").toString())>Double.parseDouble(stuMap.get("DXQJR").toString())?Double.parseDouble(stuMap.get("DXQJR").toString()):Double.parseDouble(stuMap.get("DXQJL").toString());
							Map<String,Integer> yuceqianMap = new HashMap<>();
							if (dxqj > 0.75){
								yuceqianMap.put("type",1);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
							if (dxqj >= -0.5 && dxqj <= 0.75){
								yuceqianMap.put("type",2);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
							if (naked_Farvision >= 5.0 && dxqj<-0.5){
								yuceqianMap.put("type",3);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
							if (naked_Farvision < 5.0 && dxqj<-0.5){
								yuceqianMap.put("type",4);
								yuceqianMap.put("id",id);
								yuceqianList.add(yuceqianMap);
								continue;
							}
						}
						redisTemplate.opsForList().rightPushAll(year-1+"ldataList",ldataList);
						redisTemplate.opsForList().rightPushAll(year-1+"rdataList",rdataList);
						redisTemplate.opsForList().rightPushAll(year-1+"yuceqianList",yuceqianList);
					}




					List<Map<String,Object>> lAllData = new ArrayList<>();
					List<Map<String,Object>> rAllData = new ArrayList<>();
					int lsize = ldataList.size();
					int rsize = rdataList.size();
					int ltoIndex = 2000;
					int rtoIndex = 2000;
					for (int i = 0; i < ldataList.size(); i+=2000) {
						if (i+2000>lsize){
							ltoIndex=lsize-i;
						}
						List<Map<String, Object>> subList = ldataList.subList(i, i+ltoIndex);
						HttpHeaders httpHeaders = new HttpHeaders();
						httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
						HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(subList, httpHeaders);
						ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entity, String.class);
						String response = responseEntity.getBody();
						lAllData .addAll(JSON.parseObject(response, List.class));
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					for (int i = 0; i < rdataList.size(); i+=2000) {
						if (i+2000>rsize){
							rtoIndex=rsize-i;
						}
						List<Map<String, Object>> subList = rdataList.subList(i, i+rtoIndex);
						HttpHeaders httpHeaders = new HttpHeaders();
						httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
						HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(subList, httpHeaders);
						ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entity, String.class);
						String response = responseEntity.getBody();
						rAllData .addAll(JSON.parseObject(response, List.class));
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}


					for (Map<String, Object> lMap : lAllData) {
						for (Map<String, Object> rMap : rAllData) {
							int indexL = lMap.get("student").toString().indexOf("|");
							int indexR = rMap.get("student").toString().indexOf("|");
							String idL = lMap.get("student").toString().substring(0, indexL);
							String idR = rMap.get("student").toString().substring(0, indexR);
							if (idL.equals(idR)){
								Map<String,Integer> yucehouMap = new HashMap<>();
								int ltype = Integer.parseInt(lMap.get("type").toString());
								int rtype = Integer.parseInt(rMap.get("type").toString());
								int type = ltype>rtype?ltype:rtype;
								yucehouMap.put("type",type);
								yucehouMap.put("id",Integer.parseInt(idL));
								yucehouList.add(yucehouMap);
							}
						}
					}
					Integer fzcNum = 0;
					Integer flcNum = 0;
					Integer fjxNum = 0;

					Integer zcTolc = 0;
					Integer zcTojx = 0;
					Integer zcTozx = 0;

					Integer lcTojx = 0;
					Integer lcTozx = 0;

					Integer jxTolc = 0;
					Integer jxTozx = 0;

					for (Map<String, Integer> fMap : yuceqianList) {
						for (Map<String, Integer> sMap : yucehouList) {
							if (fMap.get("id").equals(sMap.get("id"))) {
								int ftype = Integer.parseInt(fMap.get("type").toString());
								int stype = Integer.parseInt(sMap.get("type").toString());
								if (1==ftype){
									fzcNum++;
									if (2==stype){
										zcTolc++;
									}
									if (3==stype){
										zcTojx++;
									}
									if (stype>=4){
										zcTozx++;
									}
								}
								if (2==ftype){
									flcNum++;
									if (3==stype){
										lcTojx++;
									}
									if (stype>=4){
										lcTozx++;
									}
								}
								if (3==ftype){
									fjxNum++;
									if (2==stype){
										jxTolc++;
									}
									if (stype>=4){
										jxTozx++;
									}
								}
							}
						}
					}

					int jxfbnum = zcTojx + lcTojx;
					int lcfbnum = zcTolc + jxTolc;
					int zxfbnum = zcTozx + lcTozx + jxTozx;

					BigDecimal bg = new BigDecimal((float) jxfbnum / (fzcNum + flcNum));
					String jxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
					bg = new BigDecimal((float)lcfbnum/(fzcNum+fjxNum));
					String lcfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

					bg = new BigDecimal((float)zxfbnum/(fzcNum+fjxNum+flcNum));
					String zxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

					resultMap.put("jxfabinglv",jxfabinglv);
					resultMap.put("lcfabinglv",lcfabinglv);
					resultMap.put("zxfabinglv",zxfabinglv);

					redisTemplate.opsForHash().putAll("thisYearMorbidity",resultMap);
				}
			}
			if (month>6){
				int date = calendar.get(Calendar.DATE);
				resultMap = redisTemplate.opsForHash().entries(month+date+"nextYearMorbidity");
				if (resultMap.size()==0){
					List<Map<String,Object>> ldataList = new ArrayList<>();
					List<Map<String,Object>> rdataList = new ArrayList<>();
					List<Map<String,Integer>> yuceqianList = new ArrayList<>();
					List<Map<String,Integer>> yucehouList = new ArrayList<>();
					List<Map<String,Object>> scYuce = studentDao.getScYuCeForProvince(year,checkCity, checkArea);

					for (Map<String, Object> stuMap : scYuce) {
						Double dioAL = 0.0;
						Double dioSL = 0.0;
						Double dioCL = 0.0;
						Double dioAR = 0.0;
						Double dioSR = 0.0;
						Double dioCR = 0.0;
						Double corML = 0.0;
						Double corDL = 0.0;
						Double corMR = 0.0;
						Double corDR = 0.0;
						Double eyeaxisOd = 0.0;
						Double eyeaxisOs = 0.0;
						Double eyeaxis_corneal_L = 0.0;
						Double eyeaxis_corneal_R = 0.0;
						String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
						String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
						Double dxqjl = 0.0;
						Double dxqjr = 0.0;
						try {
							NakedOs = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOs);
							NakedOd = ShiLiZhuanHuanUtils.zhuanhuanshiliForSc(NakedOd);
							dxqjl= Double.parseDouble(stuMap.get("DXQJL").toString());
							dxqjr= Double.parseDouble(stuMap.get("DXQJR").toString());
						} catch (Exception e) {
							continue;
						}


						Map<String,Object> lMap = new HashMap<>();
						Map<String,Object> rMap = new HashMap<>();
						Integer id = (Integer) stuMap.get("id");
						Integer optId = studentDao.getOptId(id);
						List<Map<String,Object>> diopterData = studentDao.getDiopterData(optId);

						for (Map<String, Object> dioMap : diopterData) {
							String ifRl = dioMap.get("ifRL").toString();
							if ("L".equals(ifRl)){
								dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
								dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
								dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
							}

							if ("R".equals(ifRl)){
								dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
								dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
								dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
							}
						}
						List<Map<String,Object>> corData = studentDao.getCornealData(optId);
						for (Map<String, Object> corMap : corData) {
							String ifRL = corMap.get("ifRL").toString();
							if ("L".equals(ifRL)){
								corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
								corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
							}
							if ("R".equals(ifRL)){
								corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
								corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
							}
						}

						Map<String,Double> eyeaxisData = studentDao.getEyeAxisData(id);
						if (eyeaxisData!=null){
							eyeaxisOd = eyeaxisData.get("od");
							eyeaxisOs = eyeaxisData.get("os");
						}

						if (eyeaxisOd == 0.0 || corMR ==0.0){
							eyeaxis_corneal_R = 0.0;
						}else {
							eyeaxis_corneal_R = eyeaxisOd/corMR;
						}
						if (eyeaxisOs == 0.0 || corML==0.0){
							eyeaxis_corneal_L = 0.0;
						}else {
							eyeaxis_corneal_L = eyeaxisOs/corML;
						}
						Double age = Double.parseDouble(stuMap.get("age").toString());

						lMap.put("student",id+"|L");
						lMap.put("life_farvision",0);
						lMap.put("naked_farvision",Double.parseDouble(NakedOs));
						lMap.put("diopter_s1",dioSL);
						lMap.put("diopter_c1",dioCL);
						lMap.put("diopter_a1",dioAL);
						lMap.put("y1",dxqjl);
						lMap.put("corneal_mm",corML);
						lMap.put("corneal_d",corDL);
						lMap.put("eyeaxis",eyeaxisOs);
						lMap.put("age",age);
						lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);


						rMap.put("student",id+"|R");
						rMap.put("life_farvision",0);
						rMap.put("naked_farvision",Double.parseDouble(NakedOd));
						rMap.put("diopter_s1",dioSR);
						rMap.put("diopter_c1",dioCR);
						rMap.put("diopter_a1",dioAR);
						rMap.put("y1",dxqjr);
						rMap.put("corneal_mm",corMR);
						rMap.put("corneal_d",corDR);
						rMap.put("eyeaxis",eyeaxisOd);
						rMap.put("age",age);
						rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
						ldataList.add(lMap);
						rdataList.add(rMap);

						NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
						NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

						Double naked_Farvision = Double.parseDouble(NakedOs)>Double.parseDouble(NakedOd)?Double.parseDouble(NakedOd):Double.parseDouble(NakedOs);
						Double dxqj = Double.parseDouble(stuMap.get("DXQJL").toString())>Double.parseDouble(stuMap.get("DXQJR").toString())?Double.parseDouble(stuMap.get("DXQJR").toString()):Double.parseDouble(stuMap.get("DXQJL").toString());
						Map<String,Integer> yuceqianMap = new HashMap<>();
						if (dxqj > 0.75){
							yuceqianMap.put("type",1);
							yuceqianMap.put("id",id);
							yuceqianList.add(yuceqianMap);
							continue;
						}
						if (dxqj >= -0.5 && dxqj <= 0.75){
							yuceqianMap.put("type",2);
							yuceqianMap.put("id",id);
							yuceqianList.add(yuceqianMap);
							continue;
						}
						if (naked_Farvision >= 5.0 && dxqj<-0.5){
							yuceqianMap.put("type",3);
							yuceqianMap.put("id",id);
							yuceqianList.add(yuceqianMap);
							continue;
						}
						if (naked_Farvision < 5.0 && dxqj<-0.5){
							yuceqianMap.put("type",4);
							yuceqianMap.put("id",id);
							yuceqianList.add(yuceqianMap);
							continue;
						}
					}
					List<Map<String,Object>> lAllData = new ArrayList<>();
					List<Map<String,Object>> rAllData = new ArrayList<>();
					HttpHeaders httpHeadersL = new HttpHeaders();
					httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
					HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(ldataList, httpHeadersL);
					ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entityL, String.class);
					String responseL = responseEntityL.getBody();
					lAllData = JSON.parseObject(responseL, List.class);

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					HttpHeaders httpHeadersR = new HttpHeaders();
					httpHeadersR.setContentType(MediaType.APPLICATION_JSON_UTF8);
					HttpEntity<List<Map<String, Object>>> entityR = new HttpEntity<>(rdataList, httpHeadersR);
					ResponseEntity<String> responseEntityR = restTemplate.postForEntity("http://121.36.21.238:5000/shaicha_model", entityR, String.class);
					String responseR = responseEntityR.getBody();
					rAllData = JSON.parseObject(responseR, List.class);

					for (Map<String, Object> lMap : lAllData) {
						for (Map<String, Object> rMap : rAllData) {
							int indexL = lMap.get("student").toString().indexOf("|");
							int indexR = rMap.get("student").toString().indexOf("|");
							String idL = lMap.get("student").toString().substring(0, indexL);
							String idR = rMap.get("student").toString().substring(0, indexR);
							if (idL.equals(idR)){
								Map<String,Integer> yucehouMap = new HashMap<>();
								int ltype = Integer.parseInt(lMap.get("type").toString());
								int rtype = Integer.parseInt(rMap.get("type").toString());
								int type = ltype>rtype?ltype:rtype;
								yucehouMap.put("type",type);
								yucehouMap.put("id",Integer.parseInt(idL));
								yucehouList.add(yucehouMap);
							}
						}
					}
					Integer fzcNum = 0;
					Integer flcNum = 0;
					Integer fjxNum = 0;

					Integer zcTolc = 0;
					Integer zcTojx = 0;
					Integer zcTozx = 0;

					Integer lcTojx = 0;
					Integer lcTozx = 0;

					Integer jxTolc = 0;
					Integer jxTozx = 0;

					for (Map<String, Integer> fMap : yuceqianList) {
						for (Map<String, Integer> sMap : yucehouList) {
							if (fMap.get("id").equals(sMap.get("id"))) {
								int ftype = Integer.parseInt(fMap.get("type").toString());
								int stype = Integer.parseInt(sMap.get("type").toString());
								if (1==ftype){
									fzcNum++;
									if (2==stype){
										zcTolc++;
									}
									if (3==stype){
										zcTojx++;
									}
									if (stype>=4){
										zcTozx++;
									}
								}
								if (2==ftype){
									flcNum++;
									if (3==stype){
										lcTojx++;
									}
									if (stype>=4){
										lcTozx++;
									}
								}
								if (3==ftype){
									fjxNum++;
									if (2==stype){
										jxTolc++;
									}
									if (stype>=4){
										jxTozx++;
									}
								}
							}
						}
					}

					int jxfbnum = zcTojx + lcTojx;
					int lcfbnum = zcTolc + jxTolc;
					int zxfbnum = zcTozx + lcTozx + jxTozx;

					BigDecimal bg = new BigDecimal((float) jxfbnum / (fzcNum + flcNum));
					String jxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
					bg = new BigDecimal((float)lcfbnum/(fzcNum+fjxNum));
					String lcfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

					bg = new BigDecimal((float)zxfbnum/(fzcNum+fjxNum+flcNum));
					String zxfabinglv = df.format(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);

					resultMap.put("jxfabinglv",jxfabinglv);
					resultMap.put("lcfabinglv",lcfabinglv);
					resultMap.put("zxfabinglv",zxfabinglv);
					redisTemplate.opsForHash().putAll(month+date+"nextYearMorbidity",resultMap);
				}
			}

		}

		return resultMap;
	}

	@Override
	public Map huanBingNum(String checkCity,String checkArea) {
		Map<String,Object> resultMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("0.0");
		if (StringUtils.isBlank(checkArea)){
			resultMap = redisTemplate.opsForHash().entries(checkCity+"HuanBingLv");
			if (resultMap.size()==0){
				Long num = studentDao.getCityOrAreaNum(checkCity,checkArea);
				Long SLDXnum = studentDao.getCityOrAreaSLDXNum(checkCity,checkArea);
				Long YSnum = studentDao.getCityOrAreaYSNum(checkCity,checkArea);
				Long LCQQnum = studentDao.getCityOrAreaLCQQNum(checkCity,checkArea);
				Long JXJSnum = studentDao.getCityOrAreaJXJSNum(checkCity,checkArea);
				Long ZXJSnum = studentDao.getCityOrAreaZXJSNum(checkCity,checkArea);
				BigDecimal bg = new BigDecimal((float) SLDXnum / num);
				String SLDX = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) YSnum / num);
				String YS = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) LCQQnum / num);
				String LCQQ = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) JXJSnum / num);
				String JXJS = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) ZXJSnum / num);
				String ZXJS = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				resultMap.put("SLDX",SLDX);
				resultMap.put("YS",YS);
				resultMap.put("LCQQ",LCQQ);
				resultMap.put("JXJS",JXJS);
				resultMap.put("ZXJS",ZXJS);
				redisTemplate.opsForHash().putAll(checkCity+"HuanBingLv",resultMap);
			}
			return resultMap;
		}
		if (StringUtils.isNotBlank(checkArea)){
			resultMap = redisTemplate.opsForHash().entries(checkCity+checkArea+"HuanBingLv");
			if (resultMap.size()==0){
				Long num = studentDao.getCityOrAreaNum(checkCity,checkArea);
				Long SLDXnum = studentDao.getCityOrAreaSLDXNum(checkCity,checkArea);
				Long YSnum = studentDao.getCityOrAreaYSNum(checkCity,checkArea);
				Long LCQQnum = studentDao.getCityOrAreaLCQQNum(checkCity,checkArea);
				Long JXJSnum = studentDao.getCityOrAreaJXJSNum(checkCity,checkArea);
				Long ZXJSnum = studentDao.getCityOrAreaZXJSNum(checkCity,checkArea);
				BigDecimal bg = new BigDecimal((float) SLDXnum / num);
				String SLDX = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) YSnum / num);
				String YS = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) LCQQnum / num);
				String LCQQ = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) JXJSnum / num);
				String JXJS = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) ZXJSnum / num);
				String ZXJS = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				resultMap.put("SLDX",SLDX);
				resultMap.put("YS",YS);
				resultMap.put("LCQQ",LCQQ);
				resultMap.put("JXJS",JXJS);
				resultMap.put("ZXJS",ZXJS);
				redisTemplate.opsForHash().putAll(checkCity+checkArea+"HuanBingLv",resultMap);
			}
			return resultMap;
		}
		return resultMap;
	}

	/*@Autowired
	private RestTemplate restTemplate;
*/
	@Override
	public Map developmentAndWarning(String checkType) {
		Map<String, List<Double>> resultMap = new HashMap<>();
			List<Double> jxList= new ArrayList<>();
			List<Double> lcList= new ArrayList<>();
			List<Double> zxList= new ArrayList<>();

			List<Map<String,Object>> shaichaEveryYearCount = studentDao.getEveryYearCount();

			List<Map<String,Object>> liudiaoEveryYearCount = liuDiaoDao.getEveryYearCount();
			Map<String,Integer> EveryYearCount = hebingEveryYearCount(shaichaEveryYearCount,liudiaoEveryYearCount);
			List<Map<String,Object>> checkDataIn2018 =studentDao.get2018CheckData();
			Map<String,Integer> sc2018num=getHuanBingRenShu(checkDataIn2018);

			List<Map<String,Object>> checkDataIn2019 =studentDao.get2019CheckData();
			Map<String,Integer> sc2019num=getHuanBingRenShu(checkDataIn2019);
			List<Map<String,Object>> checkDataIn2020 =studentDao.get2020CheckData();
			Map<String,Integer> sc2020num=getHuanBingRenShu(checkDataIn2020);

			List<Map<String,Object>> checkDataIn2012FL = liuDiaoDao.get2012CheckDataFL();
			List<Map<String,Object>> checkDataIn2012FR = liuDiaoDao.get2012CheckDataFR();
			List<Map<String,Object>> checkDataIn2012SL = liuDiaoDao.get2012CheckDataSL();
			List<Map<String,Object>> checkDataIn2012SR = liuDiaoDao.get2012CheckDataSR();
			List<Map<String,Object>> FDXQJ2012 = jisuanDXQJ(checkDataIn2012FL,checkDataIn2012FR);
			List<Map<String,Object>> SDXQJ2012 = jisuanDXQJ(checkDataIn2012SL,checkDataIn2012SR);
			Map<String, Integer> ld2012num = getHuanBingRenShuForLiuDiao(FDXQJ2012,SDXQJ2012);

			List<Map<String,Object>> checkDataIn2013FL = liuDiaoDao.get2013CheckDataFL();
			List<Map<String,Object>> checkDataIn2013FR = liuDiaoDao.get2013CheckDataFR();
			List<Map<String,Object>> checkDataIn2013SL = liuDiaoDao.get2013CheckDataSL();
			List<Map<String,Object>> checkDataIn2013SR = liuDiaoDao.get2013CheckDataSR();
			List<Map<String,Object>> FDXQJ2013 = jisuanDXQJ(checkDataIn2013FL,checkDataIn2013FR);
			List<Map<String,Object>> SDXQJ2013 = jisuanDXQJ(checkDataIn2013SL,checkDataIn2013SR);
			Map<String, Integer> ld2013num = getHuanBingRenShuForLiuDiao(FDXQJ2013,SDXQJ2013);

			List<Map<String,Object>> checkDataIn2014FL = liuDiaoDao.get2014CheckDataFL();
			List<Map<String,Object>> checkDataIn2014FR = liuDiaoDao.get2014CheckDataFR();
			List<Map<String,Object>> checkDataIn2014SL = liuDiaoDao.get2014CheckDataSL();
			List<Map<String,Object>> checkDataIn2014SR = liuDiaoDao.get2014CheckDataSR();
			List<Map<String,Object>> FDXQJ2014 = jisuanDXQJ(checkDataIn2014FL,checkDataIn2014FR);
			List<Map<String,Object>> SDXQJ2014 = jisuanDXQJ(checkDataIn2014SL,checkDataIn2014SR);
			Map<String, Integer> ld2014num = getHuanBingRenShuForLiuDiao(FDXQJ2014,SDXQJ2014);

			List<Map<String,Object>> checkDataIn2015FL = liuDiaoDao.get2015CheckDataFL();
			List<Map<String,Object>> checkDataIn2015FR = liuDiaoDao.get2015CheckDataFR();
			List<Map<String,Object>> checkDataIn2015SL = liuDiaoDao.get2015CheckDataSL();
			List<Map<String,Object>> checkDataIn2015SR = liuDiaoDao.get2015CheckDataSR();
			List<Map<String,Object>> FDXQJ2015 = jisuanDXQJ(checkDataIn2015FL,checkDataIn2015FR);
			List<Map<String,Object>> SDXQJ2015 = jisuanDXQJ(checkDataIn2015SL,checkDataIn2015SR);
			Map<String, Integer> ld2015num = getHuanBingRenShuForLiuDiao(FDXQJ2015,SDXQJ2015);

			List<Map<String,Object>> checkDataIn2018FL = liuDiaoDao.get2018CheckDataFL();
			List<Map<String,Object>> checkDataIn2018FR = liuDiaoDao.get2018CheckDataFR();
			List<Map<String,Object>> checkDataIn2018SL = liuDiaoDao.get2018CheckDataSL();
			List<Map<String,Object>> checkDataIn2018SR = liuDiaoDao.get2018CheckDataSR();
			List<Map<String,Object>> FDXQJ2018 = jisuanDXQJ(checkDataIn2018FL,checkDataIn2018FR);
			List<Map<String,Object>> SDXQJ2018 = jisuanDXQJ(checkDataIn2018SL,checkDataIn2018SR);
			Map<String, Integer> ld2018num = getHuanBingRenShuForLiuDiao(FDXQJ2018,SDXQJ2018);

			List<Map<String,Object>> checkDataIn2019FL = liuDiaoDao.get2019CheckDataFL();
			List<Map<String,Object>> checkDataIn2019FR = liuDiaoDao.get2019CheckDataFR();
			List<Map<String,Object>> checkDataIn2019SL = liuDiaoDao.get2019CheckDataSL();
			List<Map<String,Object>> checkDataIn2019SR = liuDiaoDao.get2019CheckDataSR();
			List<Map<String,Object>> FDXQJ2019 = jisuanDXQJ(checkDataIn2019FL,checkDataIn2019FR);
			List<Map<String,Object>> SDXQJ2019 = jisuanDXQJ(checkDataIn2019SL,checkDataIn2019SR);
			Map<String, Integer> ld2019num = getHuanBingRenShuForLiuDiao(FDXQJ2019,SDXQJ2019);

			List<Map<String,Object>> checkDataIn2020FL = liuDiaoDao.get2020CheckDataFL();
			List<Map<String,Object>> checkDataIn2020FR = liuDiaoDao.get2020CheckDataFR();
			List<Map<String,Object>> checkDataIn2020SL = liuDiaoDao.get2020CheckDataSL();
			List<Map<String,Object>> checkDataIn2020SR = liuDiaoDao.get2020CheckDataSR();
			List<Map<String,Object>> FDXQJ2020 = jisuanDXQJ(checkDataIn2020FL,checkDataIn2020FR);
			List<Map<String,Object>> SDXQJ2020 = jisuanDXQJ(checkDataIn2020SL,checkDataIn2020SR);
			Map<String, Integer> ld2020num = getHuanBingRenShuForLiuDiao(FDXQJ2020,SDXQJ2020);
			ld2018num.put("lc",sc2018num.get("lc")+ld2018num.get("lc"));
			ld2018num.put("jx",sc2018num.get("jx")+ld2018num.get("jx"));
			ld2018num.put("zx",sc2018num.get("zx")+ld2018num.get("zx"));

			ld2019num.put("lc",sc2019num.get("lc")+ld2019num.get("lc"));
			ld2019num.put("jx",sc2019num.get("jx")+ld2019num.get("jx"));
			ld2019num.put("zx",sc2019num.get("zx")+ld2019num.get("zx"));

			ld2020num.put("lc",sc2020num.get("lc")+ld2020num.get("lc"));
			ld2020num.put("jx",sc2020num.get("jx")+ld2020num.get("jx"));
			ld2020num.put("zx",sc2020num.get("zx")+ld2020num.get("zx"));


			BigDecimal lc = new BigDecimal((float) ld2012num.get("lc") / EveryYearCount.get("in2012"));
			BigDecimal jx = new BigDecimal((float) ld2012num.get("jx") / EveryYearCount.get("in2012"));
			BigDecimal zx = new BigDecimal((float) ld2012num.get("zx") / EveryYearCount.get("in2012"));

			double lc2012lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2012lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2012lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			lc = new BigDecimal((float) ld2013num.get("lc") / EveryYearCount.get("in2013"));
			jx = new BigDecimal((float) ld2013num.get("jx") / EveryYearCount.get("in2013"));
			zx = new BigDecimal((float) ld2013num.get("zx") / EveryYearCount.get("in2013"));

			double lc2013lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2013lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2013lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			lc = new BigDecimal((float) ld2014num.get("lc") / EveryYearCount.get("in2014"));
			jx = new BigDecimal((float) ld2014num.get("jx") / EveryYearCount.get("in2014"));
			zx = new BigDecimal((float) ld2014num.get("zx") / EveryYearCount.get("in2014"));

			double lc2014lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2014lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2014lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			lc = new BigDecimal((float) ld2015num.get("lc") / EveryYearCount.get("in2015"));
			jx = new BigDecimal((float) ld2015num.get("jx") / EveryYearCount.get("in2015"));
			zx = new BigDecimal((float) ld2015num.get("zx") / EveryYearCount.get("in2015"));

			double lc2015lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2015lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2015lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			lc = new BigDecimal((float) ld2018num.get("lc") / EveryYearCount.get("in2018"));
			jx = new BigDecimal((float) ld2018num.get("jx") / EveryYearCount.get("in2018"));
			zx = new BigDecimal((float) ld2018num.get("zx") / EveryYearCount.get("in2018"));

			double lc2018lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2018lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2018lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			lc = new BigDecimal((float) ld2019num.get("lc") / EveryYearCount.get("in2019"));
			jx = new BigDecimal((float) ld2019num.get("jx") / EveryYearCount.get("in2019"));
			zx = new BigDecimal((float) ld2019num.get("zx") / EveryYearCount.get("in2019"));

			double lc2019lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2019lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2019lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			lc = new BigDecimal((float) ld2020num.get("lc") / EveryYearCount.get("in2020"));
			jx = new BigDecimal((float) ld2020num.get("jx") / EveryYearCount.get("in2020"));
			zx = new BigDecimal((float) ld2020num.get("zx") / EveryYearCount.get("in2020"));

			double lc2020lv = lc.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double jx2020lv = jx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double zx2020lv = zx.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			lcList.add(lc2012lv);
			lcList.add(lc2013lv);
			lcList.add(lc2014lv);
			lcList.add(lc2015lv);
			lcList.add(lc2018lv);
			lcList.add(lc2019lv);
			lcList.add(lc2020lv);

			jxList.add(jx2012lv);
			jxList.add(jx2013lv);
			jxList.add(jx2014lv);
			jxList.add(jx2015lv);
			jxList.add(jx2018lv);
			jxList.add(jx2019lv);
			jxList.add(jx2020lv);

			zxList.add(zx2012lv);
			zxList.add(zx2013lv);
			zxList.add(zx2014lv);
			zxList.add(zx2015lv);
			zxList.add(zx2018lv);
			zxList.add(zx2019lv);
			zxList.add(zx2020lv);
			resultMap.put("lc",lcList);
			resultMap.put("jx",jxList);
			resultMap.put("zx",zxList);
		return resultMap;
	}

	private Map<String, Integer> getHuanBingRenShuForLiuDiao(List<Map<String, Object>> FDXQJ, List<Map<String, Object>> SDXQJ) {
		Map<String,Integer> result = new HashMap<>();
		Integer jiaxingjinshi = 0;
		Integer linchuangqianqi = 0;
		Integer zhenxingjinshi = 0;
		for (Map<String, Object> Fmap : FDXQJ) {
			for (Map<String, Object> Smap : SDXQJ) {
				if (Fmap.get("id").equals(Smap.get("id"))){
					Double Fdxqj = (Double) Fmap.get("DXQJ");
					Double Sdxqj = (Double) Smap.get("DXQJ");
					if (Fdxqj<-0.5 && Sdxqj>-0.5 && Sdxqj-Fdxqj>0.5){
						jiaxingjinshi++;
					}
					if (Sdxqj>-0.5 && Sdxqj<=0.75){
						linchuangqianqi++;
					}
					if (Sdxqj<=-0.5){
						zhenxingjinshi++;
					}
				}
			}
		}
		result.put("lc",linchuangqianqi);
		result.put("jx",jiaxingjinshi);
		result.put("zx",zhenxingjinshi);
		return result;
	}

	private List<Map<String, Object>> jisuanDXQJ(List<Map<String, Object>> checkDataIn2012L, List<Map<String, Object>> checkDataIn2012R) {
		List<Map<String,Object>> FDXQJ =new ArrayList<>();
		List<Object> idList = new ArrayList<>();
		for (Map<String, Object> Lmap : checkDataIn2012L) {
			for (Map<String, Object> Rmap : checkDataIn2012R) {
				if (Lmap.get("id").equals(Rmap.get("id"))){
					Double DXQJ = 0.0;
					if (Lmap.get("DXQJ")==null && Rmap.get("DXQJ")!=null){
						if (!idList.contains(Lmap.get("id"))){
							idList.add(Rmap.get("id"));
							HashMap<String, Object> map = new HashMap<>();
							map.put("id",Lmap.get("id"));
							map.put("DXQJ",Rmap.get("DXQJ"));
							FDXQJ.add(map);
						}

						continue;
					}
					if (Lmap.get("DXQJ")!=null && Rmap.get("DXQJ")==null){
						if (!idList.contains(Lmap.get("id"))){
							idList.add(Lmap.get("id"));
							HashMap<String, Object> map = new HashMap<>();
							map.put("id",Lmap.get("id"));
							map.put("DXQJ",Lmap.get("DXQJ"));
							FDXQJ.add(map);
						}

						continue;
					}
					if (Lmap.get("DXQJ")==null && Rmap.get("DXQJ")==null){
						continue;
					}
					if (!idList.contains(Lmap.get("id"))){
						DXQJ = (Double) ((Double)Lmap.get("DXQJ")>(Double)Rmap.get("DXQJ")?Lmap.get("DXQJ"):Rmap.get("DXQJ"));
						idList.add(Lmap.get("id"));
						HashMap<String, Object> map = new HashMap<>();
						map.put("id",Lmap.get("id"));
						map.put("DXQJ",DXQJ);
						FDXQJ.add(map);
					}
				}
			}
		}
		return FDXQJ;
	}

	private Map<String, Integer> getHuanBingRenShu(List<Map<String, Object>> checkDataIn2018) {
		Map<String,Integer> result = new HashMap<>();
		Integer lc=0;
		Integer jx=0;
		Integer zx=0;
		for (Map<String, Object> map : checkDataIn2018) {
			Double luoyanshili = 0.0;
			Double dengxiaoqiujing = 0.0;
			if (map==null){
				continue;
			}
			String nakeOd = (String) map.get("NakeOd");
			String nakeOs = (String) map.get("NakeOs");
			if (nakeOd ==null || nakeOs==null){
				continue;
			}
			if (!nakeOd.matches("-?[0-9]+.?[0-9]*")){
				continue;
			}
			if (!nakeOs.matches("-?[0-9]+.?[0-9]*")){
				continue;
			}
			nakeOd = nakeOd.compareTo(nakeOs)>0?nakeOs:nakeOd;
			if (!StringUtils.isBlank(nakeOd)){
				if ("10/10".equals(nakeOd)){
					nakeOd="5.0";
				}
				luoyanshili=Double.parseDouble(nakeOd);
				Double dr = (Double) map.get("DR");
				Double dl = (Double) map.get("DL");
				if (dr == null || dl == null){
					continue;
				}
				dengxiaoqiujing =dr>dl?dl:dr;
				if (luoyanshili>=5.0 && dengxiaoqiujing<-0.5){
					jx++;
				}
				if (luoyanshili<5.0 && dengxiaoqiujing<-0.5){
					zx++;
				}
				if (luoyanshili>=5.0 && -0.5<dengxiaoqiujing && dengxiaoqiujing <0.75){
					lc++;
				}
			}
		}
		result.put("lc",lc);
		result.put("jx",jx);
		result.put("zx",zx);
		return result;
	}

	private Map<String, Integer> hebingEveryYearCount(List<Map<String, Object>> shaichaEveryYearCount, List<Map<String, Object>> liudiaoEveryYearCount) {
		Map<String, Integer> result = new HashMap<>();
		result.put("in2012",0);
		result.put("in2013",0);
		result.put("in2014",0);
		result.put("in2015",0);
		result.put("in2018",0);
		result.put("in2019",0);
		result.put("in2020",0);
		for (Map<String, Object> map : shaichaEveryYearCount) {
			if (map.get("year").equals("2012")){
				result.put("in2012",result.get("in2012")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2013")){
				result.put("in2013",result.get("in2013")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2014")){
				result.put("in2014",result.get("in2014")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2015")){
				result.put("in2015",result.get("in2015")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2018")){
				result.put("in2018",result.get("in2018")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2019")){
				result.put("in2019",result.get("in2019")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2020")){
				result.put("in2020",result.get("in2020")+((Long) map.get("num")).intValue());
			}
		}
		for (Map<String, Object> map : liudiaoEveryYearCount) {
			if (map.get("year").equals("2012")){
				result.put("in2012",result.get("in2012")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2013")){
				result.put("in2013",result.get("in2013")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2014")){
				result.put("in2014",result.get("in2014")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2015")){
				result.put("in2015",result.get("in2015")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2018")){
				result.put("in2018",result.get("in2018")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2019")){
				result.put("in2019",result.get("in2019")+((Long) map.get("num")).intValue());
			}
			if (map.get("year").equals("2020")){
				result.put("in2020",result.get("in2020")+((Long) map.get("num")).intValue());
			}
		}
		return result;
	}

	@Override
	public Map getEveryYearCountForCity(String cityName,String checkType) {
		Map<String,Long> resultMap = new HashMap<>();
		resultMap.put("in2012",0L);
		resultMap.put("in2013",0L);
		resultMap.put("in2014",0L);
		resultMap.put("in2015",0L);
		resultMap.put("in2018",0L);
		resultMap.put("in2019",0L);
		resultMap.put("in2020",0L);
		if ("all".equals(checkType)){
			Long shaiChaIn2020 = studentDao.getShaiChaNewEveryYearCountForCity(cityName);
			resultMap.put("in2020",resultMap.get("in2020")+shaiChaIn2020);
			List<Map<String,Long>> shaiChaOldData = studentDao.getShaiChaOldEveryYearCountForCity(cityName);
			addEveryYearCount(resultMap,shaiChaOldData);
			Long liuDiaoIn2020 = liuDiaoDao.getLiuDiaoNewEveryYearCountForCity(cityName);
			resultMap.put("in2020",resultMap.get("in2020")+liuDiaoIn2020);
			List<Map<String,Long>> liuDiaoOldData = liuDiaoDao.getLiuDiaoOldEveryYearCountForCity(cityName);
			addEveryYearCount(resultMap,liuDiaoOldData);
		}
		if ("shaiCha".equals(checkType)){
			Long shaiChaIn2020 = studentDao.getShaiChaNewEveryYearCountForCity(cityName);
			resultMap.put("in2020",resultMap.get("in2020")+shaiChaIn2020);
			List<Map<String,Long>> shaiChaOldData = studentDao.getShaiChaOldEveryYearCountForCity(cityName);
			addEveryYearCount(resultMap,shaiChaOldData);
		}
		if ("liuDiao".equals(checkType)){
			Long liuDiaoIn2020 = liuDiaoDao.getLiuDiaoNewEveryYearCountForCity(cityName);
			resultMap.put("in2020",resultMap.get("in2020")+liuDiaoIn2020);
			List<Map<String,Long>> liuDiaoOldData = liuDiaoDao.getLiuDiaoOldEveryYearCountForCity(cityName);
			addEveryYearCount(resultMap,liuDiaoOldData);
		}
		return resultMap;
	}

	@Override
	public Map developmentAndWarningForCity(String cityName,String checkType) {
		Map<String, List<Integer>> resultMap = new HashMap<>();
		if ("all".equals(checkType)){
			List<Map<String,Object>> shaiChaiNewData = studentDao.getDevelopmentAndWarningDataForNewForCity(cityName);
			List<Map<String,Object>> shaiChaiOldData =studentDao.getDevelopmentAndWarningDataForOldForCity(cityName);


			List<Map<String,Object>> liuDiaoNewData =liuDiaoDao.getDevelopmentAndWarningDataForNewForCity(cityName);
			List<Map<String,Object>> liuDIaoOldData =liuDiaoDao.getDevelopmentAndWarningDataForOldForCity(cityName);
			Map<String, List<Integer>> map1 = processTheDataForShaiCha(shaiChaiNewData);
			Map<String, List<Integer>> map2 = processTheDataForShaiCha(shaiChaiOldData);
			Map<String, List<Integer>> map3 = processTheDataForLiuDiao(liuDiaoNewData);
			Map<String, List<Integer>> map4 = processTheDataForLiuDiao(liuDIaoOldData);
			resultMap = processTheDataForMap(map1, map2, map3, map4);
		}
		if ("shaiCha".equals(checkType)){
			List<Map<String,Object>> shaiChaiNewData = studentDao.getDevelopmentAndWarningDataForNewForCity(cityName);
			List<Map<String,Object>> shaiChaiOldData =studentDao.getDevelopmentAndWarningDataForOldForCity(cityName);
			Map<String, List<Integer>> map1 = processTheDataForShaiCha(shaiChaiNewData);
			Map<String, List<Integer>> map2 = processTheDataForShaiCha(shaiChaiOldData);
			resultMap = processTheDataForMap(map1, map2);
		}
		if ("liuDiao".equals(checkType)){
			List<Map<String,Object>> liuDiaoNewData =liuDiaoDao.getDevelopmentAndWarningDataForNewForCity(cityName);
			List<Map<String,Object>> liuDIaoOldData =liuDiaoDao.getDevelopmentAndWarningDataForOldForCity(cityName);
			Map<String, List<Integer>> map3 = processTheDataForLiuDiao(liuDiaoNewData);
			Map<String, List<Integer>> map4 = processTheDataForLiuDiao(liuDIaoOldData);
			resultMap = processTheDataForMap(map3, map4);
		}

		return resultMap;
	}



	@Override
	public Map<String, Object> getAreaCount(String checkCity, String[] areaList) {
		Map<String,Object> areaMap = new HashMap<>();
		areaMap = redisTemplate.opsForHash().entries(checkCity + "AreaData");
		if (areaMap.size()==0){
			for (String area : areaList) {
				areaMap.put(area,0L);
			}

			List<Map<String,Long>> shaichaOldAreaCount = studentDao.getOldAreaCount(checkCity);
			List<Map<String,Long>> shaichaNewAreaCount = studentDao.getNewAreaCount(checkCity);

			List<Map<String,Long>> liudiaoOldAreaCount = liuDiaoDao.getOldAreaCount(checkCity);
			List<Map<String,Long>> liudiaoNewAreaCount = liuDiaoDao.getNewAreaCount(checkCity);

			AddAreaCount(areaMap,shaichaOldAreaCount);
			AddAreaCount(areaMap,shaichaNewAreaCount);
			AddAreaCount(areaMap,liudiaoOldAreaCount);
			AddAreaCount(areaMap,liudiaoNewAreaCount);

			Calendar instance = Calendar.getInstance();
			int year = instance.get(Calendar.YEAR);

			 getThisYearCheck(areaMap,checkCity,areaList,year);


			redisTemplate.opsForHash().putAll(checkCity+"AreaData",areaMap);
		}
		return areaMap;
	}

	private void getThisYearCheck(Map<String, Object> areaMap,String checkCity, String[] areaList, int year) {
		for (String areaName : areaList) {
			Long thisYearCountSc = studentDao.getThisYearCheckCountForArea(checkCity,areaName,year);
			Long thisYearCountld = liuDiaoDao.getThisYearCheckCountForArea(checkCity, areaName, year);
			long thisYearCount = thisYearCountSc + thisYearCountld;
			if (thisYearCount!=0){
				long scJsCount = 0;
				long ldJsCount = 0;
				if (thisYearCountSc!=0){
					scJsCount = studentDao.getThisYearJinShiCountForArea(checkCity,areaName,year);
				}
				if (thisYearCountld!=0){
					ldJsCount = liuDiaoDao.getThisYearJinShiCountForArea(checkCity,areaName,year);
				}
				long thisYearJsCount = scJsCount + ldJsCount;
				if (thisYearJsCount!=0){
					BigDecimal bg = new BigDecimal((float) thisYearJsCount / thisYearCount);
					double jsl = (bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100;
					Long scQNCheckNum = studentDao.getThisYearCheckCountForArea(checkCity, areaName, year - 1);
					Long ldQNCheckNum = liuDiaoDao.getThisYearCheckCountForArea(checkCity, areaName, year - 1);
					long qnCheckNum = scQNCheckNum + ldQNCheckNum;
					if (qnCheckNum!=0){
						long scQNJsCount = 0;
						long ldQNJsCount = 0;
						if (scQNCheckNum!=0){
							scQNJsCount = studentDao.getThisYearJinShiCountForArea(checkCity,areaName,year-1);
						}
						if (ldQNCheckNum!=0){
							ldQNJsCount = liuDiaoDao.getThisYearJinShiCountForArea(checkCity,areaName,year-1);
						}
						long qnJsNum = scQNJsCount + ldQNJsCount;
						if (qnJsNum!=0){
							bg = new BigDecimal((float)qnJsNum/qnCheckNum);
							double qnjsl = (bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()) * 100;
							if (jsl-qnjsl>0){
								areaMap.put(areaName+"JS",2);
							}
							if (jsl-qnjsl<0 && jsl-qnjsl<-1){
								areaMap.put(areaName+"JS",0);
							}
							if (jsl-qnjsl<0 && jsl-qnjsl>=-1){
								areaMap.put(areaName+"JS",1);
							}
						}else {
							areaMap.put(areaName+"JS",0);
						}
					}else {
						areaMap.put(areaName+"JS",0);
					}
				}else {
					areaMap.put(areaName+"JS",0);
				}
			}else {
				areaMap.put(areaName+"JS",1);
			}
		}


	}

	@Override
	public Map<String, Object> getAreaCountForShaiCha(String checkCity, String[] areaList) {
		Map<String,Object> areaMap = new HashMap<>();
		for (String area : areaList) {
			areaMap.put(area,0L);
		}
		List<Map<String,Long>> shaichaOldAreaCount = studentDao.getOldAreaCount(checkCity);
		List<Map<String,Long>> shaichaNewAreaCount = studentDao.getNewAreaCount(checkCity);
		AddAreaCount(areaMap,shaichaOldAreaCount);
		AddAreaCount(areaMap,shaichaNewAreaCount);
		return areaMap;
	}

	@Override
	public Map<String, Object> getAreaCountForLiuDiao(String checkCity, String[] areaList) {
		Map<String,Object> areaMap = new HashMap<>();
		for (String area : areaList) {
			areaMap.put(area,0L);
		}
		List<Map<String,Long>> liudiaoOldAreaCount = liuDiaoDao.getOldAreaCount(checkCity);
		List<Map<String,Long>> liudiaoNewAreaCount = liuDiaoDao.getNewAreaCount(checkCity);
		AddAreaCount(areaMap,liudiaoOldAreaCount);
		AddAreaCount(areaMap,liudiaoNewAreaCount);
		return areaMap;
	}

	@Override
	public List<Map<String, Object>> getAreaSchoolData(String checkCity, String checkArea) {
		DecimalFormat df = new DecimalFormat("0.0");
		List<Map<String,Object>> resultList = new ArrayList<>();
		Boolean flag = redisTemplate.hasKey(checkCity + checkArea + "SchoolData");
		if (!flag){
			resultList = studentDao.getSchoolNum(checkCity,checkArea);
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> map = resultList.get(i);
				String check_time = (String) map.get("check_time");
				String school = (String) map.get("school");
				Long lcNum = studentDao.getSchoolLCNum(checkCity,checkArea,school,check_time);
				Long zxNum = studentDao.getSchoolZXNum(checkCity,checkArea,school,check_time);
				Long jxNum = studentDao.getSchoolJXNum(checkCity,checkArea,school,check_time);
				Long num = (Long) map.get("num");
				BigDecimal bg = new BigDecimal((float) lcNum / num);
				String lcLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) zxNum / num);
				String zxLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				bg = new BigDecimal((float) jxNum / num);
				String jxLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
				map.put("sclcLv",lcLv);
				map.put("sczxLv",jxLv);
				map.put("scjxLv",zxLv);
				map.put("ldNum",0);
				map.put("ldlcLv",0);
				map.put("ldzxLv",0);
				map.put("ldjxLv",0);
				map.put("checkType","sc");
				resultList.set(i,map);
			}
			List<Map<String,Object>> liuDiaoSchool=  liuDiaoDao.getSchoolNum(checkCity,checkArea);
			if (liuDiaoSchool.size()==0){
				redisTemplate.opsForList().rightPushAll(checkCity + checkArea + "SchoolData",resultList);
				return resultList;
			}
			if (liuDiaoSchool.size()>0){
				for (int i = 0; i < liuDiaoSchool.size(); i++) {
					Map<String, Object> map = liuDiaoSchool.get(i);
					String check_time = (String) map.get("check_time");
					String school = (String) map.get("school");
					Long lcNum = liuDiaoDao.getSchoolLCNum(checkCity,checkArea,school,check_time);
					Long zxNum = liuDiaoDao.getSchoolZXNum(checkCity,checkArea,school,check_time);
					Long jxNum = liuDiaoDao.getSchoolJXNum(checkCity,checkArea,school,check_time);
					Long num = (Long) map.get("num");
					BigDecimal bg = new BigDecimal((float) lcNum / num);
					String lcLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
					bg = new BigDecimal((float) zxNum / num);
					String zxLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
					bg = new BigDecimal((float) jxNum / num);
					String jxLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
					map.put("sclcLv",0);
					map.put("sczxLv",0);
					map.put("scjxLv",0);
					map.put("num",0);
					map.put("ldNum",num);
					map.put("ldlcLv",lcLv);
					map.put("ldzxLv",zxLv);
					map.put("ldjxLv",jxLv);
					map.put("checkType","ld");
					liuDiaoSchool.set(i,map);
				}
				resultList.addAll(liuDiaoSchool);
				redisTemplate.opsForList().rightPushAll(checkCity + checkArea + "SchoolData",resultList);
				return resultList;
			}

		}
		if (flag){
			resultList = redisTemplate.opsForList().range(checkCity + checkArea + "SchoolData",0,-1);
			return resultList;
		}

		return  resultList;
	}

	@Override
	public Map<String, Object> getAreaSchoolDataForShaiCha(String checkCity, String checkArea) {
		return null;
	}

	@Override
	public Map<String, Object> getAreaSchoolDataForLiuDiao(String checkCity, String checkArea) {
		return null;
	}

	private void AddAreaCount(Map<String, Object> areaMap, List<Map<String, Long>> areaCount) {
		for (String area : areaMap.keySet()) {
			for (Map<String, Long> map : areaCount) {
				if (area.equals(map.get("AreaName"))){
					areaMap.put(area,(Long)areaMap.get(area)+map.get("num"));
				}
			}
		}
	}

	static Map<String,List<Integer>> processTheDataForShaiCha(List<Map<String,Object>> checkDatas){
		Map<String,List<Integer>> resultMap = new HashMap<>();
		List<Integer> jiaxingjinshi = new ArrayList<>();
		List<Integer> zhenxingjinshi = new ArrayList<>();
		List<Integer> linchuangqianqi = new ArrayList<>();
		Map<String,Integer> jiaxingjinshiMap = new HashMap<>();
		jiaxingjinshiMap.put("in2012",0);
		jiaxingjinshiMap.put("in2013",0);
		jiaxingjinshiMap.put("in2014",0);
		jiaxingjinshiMap.put("in2015",0);
		jiaxingjinshiMap.put("in2018",0);
		jiaxingjinshiMap.put("in2019",0);
		jiaxingjinshiMap.put("in2020",0);
		Map<String,Integer> zhenxingjinshiMap = new HashMap<>();
		zhenxingjinshiMap.put("in2012",0);
		zhenxingjinshiMap.put("in2013",0);
		zhenxingjinshiMap.put("in2014",0);
		zhenxingjinshiMap.put("in2015",0);
		zhenxingjinshiMap.put("in2018",0);
		zhenxingjinshiMap.put("in2019",0);
		zhenxingjinshiMap.put("in2020",0);
		Map<String,Integer> linchuangqianqiMap = new HashMap<>();
		linchuangqianqiMap.put("in2012",0);
		linchuangqianqiMap.put("in2013",0);
		linchuangqianqiMap.put("in2014",0);
		linchuangqianqiMap.put("in2015",0);
		linchuangqianqiMap.put("in2018",0);
		linchuangqianqiMap.put("in2019",0);
		linchuangqianqiMap.put("in2020",0);
		for (Map<String, Object> result : checkDatas) {
			Object year = result.get("year");

			Double luoyanshili = 0.0;
			Double dengxiaoqiujing = 0.0;
			if (result==null){
				continue;
			}
			String nakeOd = (String) result.get("NakeOd");
			String nakeOs = (String) result.get("NakeOs");
			if (nakeOd ==null || nakeOs==null){
				continue;
			}
			if (!nakeOd.matches("-?[0-9]+.?[0-9]*")){
				continue;
			}
			if (!nakeOs.matches("-?[0-9]+.?[0-9]*")){
				continue;
			}
			nakeOd = nakeOd.compareTo(nakeOs)>0?nakeOs:nakeOd;
			if (!StringUtils.isBlank(nakeOd)){
				if ("10/10".equals(nakeOd)){
					nakeOd="5.0";
				}
				luoyanshili=Double.parseDouble(nakeOd);
				Double dr = (Double) result.get("DR");
				Double dl = (Double) result.get("DL");
				if (dr == null || dl == null){
					continue;
				}
				dengxiaoqiujing =dr>dl?dl:dr;
				if (luoyanshili>=5.0 && dengxiaoqiujing<=-0.5){//假性近视
					if ("2012".equals(year)){
						jiaxingjinshiMap.put("in2012",jiaxingjinshiMap.get("in2012")+1);
					}
					if ("2013".equals(year)){
						jiaxingjinshiMap.put("in2013",jiaxingjinshiMap.get("in2013")+1);
					}
					if ("2014".equals(year)){
						jiaxingjinshiMap.put("in2014",jiaxingjinshiMap.get("in2014")+1);
					}
					if ("2015".equals(year)){
						jiaxingjinshiMap.put("in2015",jiaxingjinshiMap.get("in2015")+1);
					}
					if ("2018".equals(year)){
						jiaxingjinshiMap.put("in2018",jiaxingjinshiMap.get("in2018")+1);
					}
					if ("2019".equals(year)){
						jiaxingjinshiMap.put("in2019",jiaxingjinshiMap.get("in2019")+1);
					}
					if ("2020".equals(year)){
						jiaxingjinshiMap.put("in2020",jiaxingjinshiMap.get("in2020")+1);
					}
				}
				if (luoyanshili<5.0 && dengxiaoqiujing<-0.5){//真性近视
					if ("2012".equals(year)){
						zhenxingjinshiMap.put("in2012",zhenxingjinshiMap.get("in2012")+1);
					}
					if ("2013".equals(year)){
						zhenxingjinshiMap.put("in2013",zhenxingjinshiMap.get("in2013")+1);
					}
					if ("2014".equals(year)){
						zhenxingjinshiMap.put("in2014",zhenxingjinshiMap.get("in2014")+1);
					}
					if ("2015".equals(year)){
						zhenxingjinshiMap.put("in2015",zhenxingjinshiMap.get("in2015")+1);
					}
					if ("2018".equals(year)){
						zhenxingjinshiMap.put("in2018",zhenxingjinshiMap.get("in2018")+1);
					}
					if ("2019".equals(year)){
						zhenxingjinshiMap.put("in2019",zhenxingjinshiMap.get("in2019")+1);
					}
					if ("2020".equals(year)){
						zhenxingjinshiMap.put("in2020",zhenxingjinshiMap.get("in2020")+1);
					}
				}
				if (luoyanshili>=5.0 && dengxiaoqiujing>=-0.5 && dengxiaoqiujing <0.75){//临床前期
					if ("2012".equals(year)){
						linchuangqianqiMap.put("in2012",linchuangqianqiMap.get("in2012")+1);
					}
					if ("2013".equals(year)){
						linchuangqianqiMap.put("in2013",linchuangqianqiMap.get("in2013")+1);
					}
					if ("2014".equals(year)){
						linchuangqianqiMap.put("in2014",linchuangqianqiMap.get("in2014")+1);
					}
					if ("2015".equals(year)){
						linchuangqianqiMap.put("in2015",linchuangqianqiMap.get("in2015")+1);
					}
					if ("2018".equals(year)){
						linchuangqianqiMap.put("in2018",linchuangqianqiMap.get("in2018")+1);
					}
					if ("2019".equals(year)){
						linchuangqianqiMap.put("in2019",linchuangqianqiMap.get("in2019")+1);
					}
					if ("2020".equals(year)){
						linchuangqianqiMap.put("in2020",linchuangqianqiMap.get("in2020")+1);
					}
				}
			}

		}
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2012"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2013"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2014"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2015"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2018"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2019"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2020"));

		zhenxingjinshi.add(zhenxingjinshiMap.get("in2012"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2013"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2014"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2015"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2018"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2019"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2020"));

		linchuangqianqi.add(linchuangqianqiMap.get("in2012"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2013"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2014"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2015"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2018"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2019"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2020"));

		resultMap.put("jiaxingjinshi",jiaxingjinshi);
		resultMap.put("zhenxingjinshi",zhenxingjinshi);
		resultMap.put("linchuangqianqi",linchuangqianqi);
		return resultMap;
	}
	static Map<String,List<Integer>> processTheDataForLiuDiao(List<Map<String,Object>> checkDatas){
		Map<String,List<Integer>> resultMap = new HashMap<>();
		List<Integer> jiaxingjinshi = new ArrayList<>();
		List<Integer> zhenxingjinshi = new ArrayList<>();
		List<Integer> linchuangqianqi = new ArrayList<>();
		Map<String,Integer> jiaxingjinshiMap = new HashMap<>();
		jiaxingjinshiMap.put("in2012",0);
		jiaxingjinshiMap.put("in2013",0);
		jiaxingjinshiMap.put("in2014",0);
		jiaxingjinshiMap.put("in2015",0);
		jiaxingjinshiMap.put("in2018",0);
		jiaxingjinshiMap.put("in2019",0);
		jiaxingjinshiMap.put("in2020",0);
		Map<String,Integer> zhenxingjinshiMap = new HashMap<>();
		zhenxingjinshiMap.put("in2012",0);
		zhenxingjinshiMap.put("in2013",0);
		zhenxingjinshiMap.put("in2014",0);
		zhenxingjinshiMap.put("in2015",0);
		zhenxingjinshiMap.put("in2018",0);
		zhenxingjinshiMap.put("in2019",0);
		zhenxingjinshiMap.put("in2020",0);
		Map<String,Integer> linchuangqianqiMap = new HashMap<>();
		linchuangqianqiMap.put("in2012",0);
		linchuangqianqiMap.put("in2013",0);
		linchuangqianqiMap.put("in2014",0);
		linchuangqianqiMap.put("in2015",0);
		linchuangqianqiMap.put("in2018",0);
		linchuangqianqiMap.put("in2019",0);
		linchuangqianqiMap.put("in2020",0);

		List<Map<String,Object>> LMapList = new ArrayList<>();
		List<Map<String,Object>> RMapList = new ArrayList<>();
		for (Map<String, Object> checkData : checkDatas) {
			String isSecond = (String) checkData.get("isSecond");
			if ("SECOND_CHECK".equals(isSecond)){
				String ifRL = (String) checkData.get("ifRL");
				if ("L".equals(ifRL)){
					List<Object> nameList = new ArrayList<>();
					for (Map<String, Object> lMap : LMapList) {
						nameList.add(lMap.get("stuName"));
					}
					if (nameList.contains(checkData.get("stuName"))){
						continue;
					}
					Map<String,Object> LMap = new HashMap<>();
					LMap.put("stuName",checkData.get("stuName"));
					LMap.put("year",checkData.get("year"));
					LMap.put("DXQJ",checkData.get("DXQJ"));
					LMapList.add(LMap);
				}else {
					List<Object> nameList = new ArrayList<>();
					for (Map<String, Object> rMap : RMapList) {
						nameList.add(rMap.get("stuName"));
					}
					if (nameList.contains(checkData.get("stuName"))){
						continue;
					}
					Map<String,Object> RMap = new HashMap<>();
					RMap.put("stuName",checkData.get("stuName"));
					RMap.put("DXQJ",checkData.get("DXQJ"));
					RMapList.add(RMap);
				}
			}else {
				String ifRL = (String) checkData.get("ifRL");
				if ("L".equals(ifRL)){
					if (LMapList.isEmpty()){
						Map<String,Object> LMap = new HashMap<>();
						LMap.put("stuName",checkData.get("stuName"));
						LMap.put("year",checkData.get("year"));
						LMap.put("DXQJ",checkData.get("DXQJ"));
						LMapList.add(LMap);
					}
					List<Object> nameList = new ArrayList<>();
					for (Map<String, Object> lMap : LMapList) {
						nameList.add(lMap.get("stuName"));
					}
					if (nameList.contains(checkData.get("stuName"))){
						continue;
					}else {
						Map<String,Object> LMap = new HashMap<>();
						LMap.put("stuName",checkData.get("stuName"));
						LMap.put("year",checkData.get("year"));
						LMap.put("DXQJ",checkData.get("DXQJ"));
						LMapList.add(LMap);
					}

				}else {
					if (RMapList.isEmpty()){
						Map<String,Object> RMap = new HashMap<>();
						RMap.put("stuName",checkData.get("stuName"));
						RMap.put("DXQJ",checkData.get("DXQJ"));
						RMapList.add(RMap);
					}
					List<Object> nameList = new ArrayList<>();
					for (Map<String, Object> rMap : RMapList) {
						nameList.add(rMap.get("stuName"));
					}
					if (nameList.contains(checkData.get("stuName"))){
						continue;
					}else {
						Map<String,Object> RMap = new HashMap<>();
						RMap.put("stuName",checkData.get("stuName"));
						RMap.put("DXQJ",checkData.get("DXQJ"));
						RMapList.add(RMap);
					}

				}
			}
		}
		for (Map<String, Object> lMap : LMapList) {
			for (Map<String, Object> rMap : RMapList) {
				Object year = lMap.get("year");
				String name1 = (String) lMap.get("stuName");
				String name2 = (String) rMap.get("stuName");
				if (name1.equals(name2)){
					Double dengxiaoqiujing = 0.0;
						Double dl = (Double) lMap.get("DXQJ");
						Double dr = (Double) rMap.get("DXQJ");
						if (dr == null || dl == null){
							continue;
						}
						dengxiaoqiujing =dr>dl?dl:dr;
						if (dengxiaoqiujing>-0.5){//假性近视
							if ("2012".equals(year)){
								jiaxingjinshiMap.put("in2012",jiaxingjinshiMap.get("in2012")+1);
							}
							if ("2013".equals(year)){
								jiaxingjinshiMap.put("in2013",jiaxingjinshiMap.get("in2013")+1);
							}
							if ("2014".equals(year)){
								jiaxingjinshiMap.put("in2014",jiaxingjinshiMap.get("in2014")+1);
							}
							if ("2015".equals(year)){
								jiaxingjinshiMap.put("in2015",jiaxingjinshiMap.get("in2015")+1);
							}
							if ("2018".equals(year)){
								jiaxingjinshiMap.put("in2018",jiaxingjinshiMap.get("in2018")+1);
							}
							if ("2019".equals(year)){
								jiaxingjinshiMap.put("in2019",jiaxingjinshiMap.get("in2019")+1);
							}
							if ("2020".equals(year)){
								jiaxingjinshiMap.put("in2020",jiaxingjinshiMap.get("in2020")+1);
							}
						}
						if (dengxiaoqiujing>-0.5 && dengxiaoqiujing<0.75){//临床前期
							if ("2012".equals(year)){
								linchuangqianqiMap.put("in2012",linchuangqianqiMap.get("in2012")+1);
							}
							if ("2013".equals(year)){
								linchuangqianqiMap.put("in2013",linchuangqianqiMap.get("in2013")+1);
							}
							if ("2014".equals(year)){
								linchuangqianqiMap.put("in2014",linchuangqianqiMap.get("in2014")+1);
							}
							if ("2015".equals(year)){
								linchuangqianqiMap.put("in2015",linchuangqianqiMap.get("in2015")+1);
							}
							if ("2018".equals(year)){
								linchuangqianqiMap.put("in2018",linchuangqianqiMap.get("in2018")+1);
							}
							if ("2019".equals(year)){
								linchuangqianqiMap.put("in2019",linchuangqianqiMap.get("in2019")+1);
							}
							if ("2020".equals(year)){
								linchuangqianqiMap.put("in2020",linchuangqianqiMap.get("in2020")+1);
							}
						}
						if (dengxiaoqiujing<=-0.5){//真性近视
							if ("2012".equals(year)){
								zhenxingjinshiMap.put("in2012",zhenxingjinshiMap.get("in2012")+1);
							}
							if ("2013".equals(year)){
								zhenxingjinshiMap.put("in2013",zhenxingjinshiMap.get("in2013")+1);
							}
							if ("2014".equals(year)){
								zhenxingjinshiMap.put("in2014",zhenxingjinshiMap.get("in2014")+1);
							}
							if ("2015".equals(year)){
								zhenxingjinshiMap.put("in2015",zhenxingjinshiMap.get("in2015")+1);
							}
							if ("2018".equals(year)){
								zhenxingjinshiMap.put("in2018",zhenxingjinshiMap.get("in2018")+1);
							}
							if ("2019".equals(year)){
								zhenxingjinshiMap.put("in2019",zhenxingjinshiMap.get("in2019")+1);
							}
							if ("2020".equals(year)){
								zhenxingjinshiMap.put("in2020",zhenxingjinshiMap.get("in2020")+1);
							}
						}
					}
				}
			}
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2012"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2013"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2014"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2015"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2018"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2019"));
		jiaxingjinshi.add(jiaxingjinshiMap.get("in2020"));

		zhenxingjinshi.add(zhenxingjinshiMap.get("in2012"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2013"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2014"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2015"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2018"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2019"));
		zhenxingjinshi.add(zhenxingjinshiMap.get("in2020"));

		linchuangqianqi.add(linchuangqianqiMap.get("in2012"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2013"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2014"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2015"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2018"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2019"));
		linchuangqianqi.add(linchuangqianqiMap.get("in2020"));

		resultMap.put("jiaxingjinshi",jiaxingjinshi);
		resultMap.put("zhenxingjinshi",zhenxingjinshi);
		resultMap.put("linchuangqianqi",linchuangqianqi);
		return resultMap;
	}

	static Map<String,List<Integer>> processTheDataForMap(Map<String,List<Integer>> map1,Map<String, List<Integer>> map2,Map<String, List<Integer>> map3,Map<String, List<Integer>> map4){
		Map<String,List<Integer>> resultMap = new HashMap<>();
		List<Integer> jiaxingjinshi = new ArrayList<>();
		List<Integer> zhenxingjinshi = new ArrayList<>();
		List<Integer> linchuangqianqi = new ArrayList<>();

		List<Integer> jiaxingjinshi1  = map1.get("jiaxingjinshi");
		List<Integer> zhenxingjinshi1  = map1.get("zhenxingjinshi");
		List<Integer> linchuangqianqi1 = map1.get("linchuangqianqi");

		List<Integer> jiaxingjinshi2  = map2.get("jiaxingjinshi");
		List<Integer> zhenxingjinshi2  = map2.get("zhenxingjinshi");
		List<Integer> linchuangqianqi2 = map2.get("linchuangqianqi");

		List<Integer> jiaxingjinshi3  = map3.get("jiaxingjinshi");
		List<Integer> zhenxingjinshi3 = map3.get("zhenxingjinshi");
		List<Integer> linchuangqianqi3 = map3.get("linchuangqianqi");

		List<Integer> jiaxingjinshi4  = map4.get("jiaxingjinshi");
		List<Integer> zhenxingjinshi4 = map4.get("zhenxingjinshi");
		List<Integer> linchuangqianqi4 = map4.get("linchuangqianqi");

		jiaxingjinshi.add(jiaxingjinshi1.get(0)+jiaxingjinshi2.get(0)+jiaxingjinshi3.get(0)+jiaxingjinshi4.get(0));
		jiaxingjinshi.add(jiaxingjinshi1.get(1)+jiaxingjinshi2.get(1)+jiaxingjinshi3.get(1)+jiaxingjinshi4.get(1));
		jiaxingjinshi.add(jiaxingjinshi1.get(2)+jiaxingjinshi2.get(2)+jiaxingjinshi3.get(2)+jiaxingjinshi4.get(2));
		jiaxingjinshi.add(jiaxingjinshi1.get(3)+jiaxingjinshi2.get(3)+jiaxingjinshi3.get(3)+jiaxingjinshi4.get(3));
		jiaxingjinshi.add(jiaxingjinshi1.get(4)+jiaxingjinshi2.get(4)+jiaxingjinshi3.get(4)+jiaxingjinshi4.get(4));
		jiaxingjinshi.add(jiaxingjinshi1.get(5)+jiaxingjinshi2.get(5)+jiaxingjinshi3.get(5)+jiaxingjinshi4.get(5));
		jiaxingjinshi.add(jiaxingjinshi1.get(6)+jiaxingjinshi2.get(6)+jiaxingjinshi3.get(6)+jiaxingjinshi4.get(6));

		zhenxingjinshi.add(zhenxingjinshi1.get(0)+zhenxingjinshi2.get(0)+zhenxingjinshi3.get(0)+zhenxingjinshi4.get(0));
		zhenxingjinshi.add(zhenxingjinshi1.get(1)+zhenxingjinshi2.get(1)+zhenxingjinshi3.get(1)+zhenxingjinshi4.get(1));
		zhenxingjinshi.add(zhenxingjinshi1.get(2)+zhenxingjinshi2.get(2)+zhenxingjinshi3.get(2)+zhenxingjinshi4.get(2));
		zhenxingjinshi.add(zhenxingjinshi1.get(3)+zhenxingjinshi2.get(3)+zhenxingjinshi3.get(3)+zhenxingjinshi4.get(3));
		zhenxingjinshi.add(zhenxingjinshi1.get(4)+zhenxingjinshi2.get(4)+zhenxingjinshi3.get(4)+zhenxingjinshi4.get(4));
		zhenxingjinshi.add(zhenxingjinshi1.get(5)+zhenxingjinshi2.get(5)+zhenxingjinshi3.get(5)+zhenxingjinshi4.get(5));
		zhenxingjinshi.add(zhenxingjinshi1.get(6)+zhenxingjinshi2.get(6)+zhenxingjinshi3.get(6)+zhenxingjinshi4.get(6));

		linchuangqianqi.add(linchuangqianqi1.get(0)+linchuangqianqi2.get(0)+linchuangqianqi3.get(0)+linchuangqianqi4.get(0));
		linchuangqianqi.add(linchuangqianqi1.get(1)+linchuangqianqi2.get(1)+linchuangqianqi3.get(1)+linchuangqianqi4.get(1));
		linchuangqianqi.add(linchuangqianqi1.get(2)+linchuangqianqi2.get(2)+linchuangqianqi3.get(2)+linchuangqianqi4.get(2));
		linchuangqianqi.add(linchuangqianqi1.get(3)+linchuangqianqi2.get(3)+linchuangqianqi3.get(3)+linchuangqianqi4.get(3));
		linchuangqianqi.add(linchuangqianqi1.get(4)+linchuangqianqi2.get(4)+linchuangqianqi3.get(4)+linchuangqianqi4.get(4));
		linchuangqianqi.add(linchuangqianqi1.get(5)+linchuangqianqi2.get(5)+linchuangqianqi3.get(5)+linchuangqianqi4.get(5));
		linchuangqianqi.add(linchuangqianqi1.get(6)+linchuangqianqi2.get(6)+linchuangqianqi3.get(6)+linchuangqianqi4.get(6));

		resultMap.put("jiaxingjinshi",jiaxingjinshi);
		resultMap.put("zhenxingjinshi",zhenxingjinshi);
		resultMap.put("linchuangqianqi",linchuangqianqi);

		return resultMap;
	}
	static Map<String,List<Integer>> processTheDataForMap(Map<String,List<Integer>> map1,Map<String, List<Integer>> map2){
		Map<String,List<Integer>> resultMap = new HashMap<>();
		List<Integer> jiaxingjinshi = new ArrayList<>();
		List<Integer> zhenxingjinshi = new ArrayList<>();
		List<Integer> linchuangqianqi = new ArrayList<>();

		List<Integer> jiaxingjinshi1  = map1.get("jiaxingjinshi");
		List<Integer> zhenxingjinshi1  = map1.get("zhenxingjinshi");
		List<Integer> linchuangqianqi1 = map1.get("linchuangqianqi");

		List<Integer> jiaxingjinshi2  = map2.get("jiaxingjinshi");
		List<Integer> zhenxingjinshi2  = map2.get("zhenxingjinshi");
		List<Integer> linchuangqianqi2 = map2.get("linchuangqianqi");



		jiaxingjinshi.add(jiaxingjinshi1.get(0)+jiaxingjinshi2.get(0));
		jiaxingjinshi.add(jiaxingjinshi1.get(1)+jiaxingjinshi2.get(1));
		jiaxingjinshi.add(jiaxingjinshi1.get(2)+jiaxingjinshi2.get(2));
		jiaxingjinshi.add(jiaxingjinshi1.get(3)+jiaxingjinshi2.get(3));
		jiaxingjinshi.add(jiaxingjinshi1.get(4)+jiaxingjinshi2.get(4));
		jiaxingjinshi.add(jiaxingjinshi1.get(5)+jiaxingjinshi2.get(5));
		jiaxingjinshi.add(jiaxingjinshi1.get(6)+jiaxingjinshi2.get(6));

		zhenxingjinshi.add(zhenxingjinshi1.get(0)+zhenxingjinshi2.get(0));
		zhenxingjinshi.add(zhenxingjinshi1.get(1)+zhenxingjinshi2.get(1));
		zhenxingjinshi.add(zhenxingjinshi1.get(2)+zhenxingjinshi2.get(2));
		zhenxingjinshi.add(zhenxingjinshi1.get(3)+zhenxingjinshi2.get(3));
		zhenxingjinshi.add(zhenxingjinshi1.get(4)+zhenxingjinshi2.get(4));
		zhenxingjinshi.add(zhenxingjinshi1.get(5)+zhenxingjinshi2.get(5));
		zhenxingjinshi.add(zhenxingjinshi1.get(6)+zhenxingjinshi2.get(6));

		linchuangqianqi.add(linchuangqianqi1.get(0)+linchuangqianqi2.get(0));
		linchuangqianqi.add(linchuangqianqi1.get(1)+linchuangqianqi2.get(1));
		linchuangqianqi.add(linchuangqianqi1.get(2)+linchuangqianqi2.get(2));
		linchuangqianqi.add(linchuangqianqi1.get(3)+linchuangqianqi2.get(3));
		linchuangqianqi.add(linchuangqianqi1.get(4)+linchuangqianqi2.get(4));
		linchuangqianqi.add(linchuangqianqi1.get(5)+linchuangqianqi2.get(5));
		linchuangqianqi.add(linchuangqianqi1.get(6)+linchuangqianqi2.get(6));

		resultMap.put("jiaxingjinshi",jiaxingjinshi);
		resultMap.put("zhenxingjinshi",zhenxingjinshi);
		resultMap.put("linchuangqianqi",linchuangqianqi);

		return resultMap;
	}


}





