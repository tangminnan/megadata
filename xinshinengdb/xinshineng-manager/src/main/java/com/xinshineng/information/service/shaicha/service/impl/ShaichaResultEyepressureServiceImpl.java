package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultEyepressureDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultEyepressureDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultEyepressureService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultEyepressureServiceImpl implements ShaichaResultEyepressureService {
	@Autowired
	private ShaichaResultEyepressureDao resultEyepressureDao;
	
	@Override
	public ShaichaResultEyepressureDO get(Integer tEyepressureId){
		return resultEyepressureDao.get(tEyepressureId);
	}
	
	@Override
	public List<ShaichaResultEyepressureDO> list(Map<String, Object> map){
		return resultEyepressureDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultEyepressureDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultEyepressureDO resultEyepressure){
		return resultEyepressureDao.save(resultEyepressure);
	}
	
	@Override
	public int update(ShaichaResultEyepressureDO resultEyepressure){
		return resultEyepressureDao.update(resultEyepressure);
	}
	
	@Override
	public int remove(Integer tEyepressureId){
		return resultEyepressureDao.remove(tEyepressureId);
	}
	
	@Override
	public int batchRemove(Integer[] tEyepressureIds){
		return resultEyepressureDao.batchRemove(tEyepressureIds);
	}
	
}
