package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultEyeaxisDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultEyeaxisDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultEyeaxisService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultEyeaxisServiceImpl implements ShaichaResultEyeaxisService {
	@Autowired
	private ShaichaResultEyeaxisDao resultEyeaxisDao;
	
	@Override
	public ShaichaResultEyeaxisDO get(Integer tEyeaxisId){
		return resultEyeaxisDao.get(tEyeaxisId);
	}
	
	@Override
	public List<ShaichaResultEyeaxisDO> list(Map<String, Object> map){
		return resultEyeaxisDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultEyeaxisDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultEyeaxisDO resultEyeaxis){
		return resultEyeaxisDao.save(resultEyeaxis);
	}
	
	@Override
	public int update(ShaichaResultEyeaxisDO resultEyeaxis){
		return resultEyeaxisDao.update(resultEyeaxis);
	}
	
	@Override
	public int remove(Integer tEyeaxisId){
		return resultEyeaxisDao.remove(tEyeaxisId);
	}
	
	@Override
	public int batchRemove(Integer[] tEyeaxisIds){
		return resultEyeaxisDao.batchRemove(tEyeaxisIds);
	}
	
}
