package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultEyesightDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultEyesightDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultEyesightService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultEyesightServiceImpl implements ShaichaResultEyesightService {
	@Autowired
	private ShaichaResultEyesightDao resultEyesightDao;
	
	@Override
	public ShaichaResultEyesightDO get(Integer tEyesightId){
		return resultEyesightDao.get(tEyesightId);
	}
	
	@Override
	public List<ShaichaResultEyesightDO> list(Map<String, Object> map){
		return resultEyesightDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultEyesightDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultEyesightDO resultEyesight){
		return resultEyesightDao.save(resultEyesight);
	}
	
	@Override
	public int update(ShaichaResultEyesightDO resultEyesight){
		return resultEyesightDao.update(resultEyesight);
	}
	
	@Override
	public int remove(Integer tEyesightId){
		return resultEyesightDao.remove(tEyesightId);
	}
	
	@Override
	public int batchRemove(Integer[] tEyesightIds){
		return resultEyesightDao.batchRemove(tEyesightIds);
	}
	
}
