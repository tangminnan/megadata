package com.xinshineng.information.service.jianhuyi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.jianhuyi.UseJianhuyiLogDao;
import com.xinshineng.information.domain.jianhuyi.UseJianhuyiLogDO;
import com.xinshineng.information.service.jianhuyi.service.UseJianhuyiLogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service
public class UseJianhuyiLogServiceImpl implements UseJianhuyiLogService {
	@Autowired
	private UseJianhuyiLogDao useJianhuyiLogDao;
	
	@Override
	public UseJianhuyiLogDO get(Integer id){
		return useJianhuyiLogDao.get(id);
	}
	
	@Override
	public List<UseJianhuyiLogDO> list(Map<String, Object> map){
		return useJianhuyiLogDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return useJianhuyiLogDao.count(map);
	}
	
	@Override
	public int save(UseJianhuyiLogDO useJianhuyiLog){
		return useJianhuyiLogDao.save(useJianhuyiLog);
	}
	
	@Override
	public int update(UseJianhuyiLogDO useJianhuyiLog){
		return useJianhuyiLogDao.update(useJianhuyiLog);
	}
	
	@Override
	public int remove(Integer id){
		return useJianhuyiLogDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return useJianhuyiLogDao.batchRemove(ids);
	}

	@Override
	public Map getData(String name, String idCard) {
		Map<String,Double> result = new HashMap<>();
		List<Map> userData = useJianhuyiLogDao.getData(name,idCard);
		if (userData.size()==0){
			return null;
		}
		double read=0.00;
		double outdoors=0.00;
		double distance_least=0.00;
		double light_least=0.00;
		double look_phone=0.00;
		double look_tv=0.00;
		double tilt=0.00;
		double use_jianhuyi=0.00;
		for (Map map : userData) {
			read += map.get("read_duration") == null ? 0.00 : (Double) map.get("read_duration");
			outdoors += map.get("outdoors_duration") == null ? 0.00 : (Double) map.get("outdoors_duration");
			double distance = map.get("read_distance") == null ? 0.00 : (Double) map.get("read_distance");
			distance_least=distance_least>distance?distance:distance_least;
			double read_light = map.get("read_light") == null ? 0.00 : (Double) map.get("read_light");
			light_least = light_least > read_light?read_light:read_light;
			look_phone += map.get("look_phone_duration") == null ? 0.00 : (Double) map.get("look_phone_duration");
			look_tv += map.get("look_tv_computer_duration") == null ? 0.00 : (Double) map.get("look_tv_computer_duration");
			double sit_tilt = map.get("read_duration") == null ? 0.00 : (Double) map.get("sit_tilt");
			tilt = tilt > sit_tilt ? sit_tilt : tilt;
			use_jianhuyi += map.get("read_duration") == null ? 0.00 : (Double) map.get("use_jianhuyi_duration");
		}
		result.put("read",read);
		result.put("outdoors",outdoors);
		result.put("distance_least",distance_least);
		result.put("light_least",light_least);
		result.put("look_phone",look_phone);
		result.put("look_tv",look_tv);
		result.put("tilte",tilt);
		result.put("use_jianhuyi",use_jianhuyi);

		return result;
	}

}
