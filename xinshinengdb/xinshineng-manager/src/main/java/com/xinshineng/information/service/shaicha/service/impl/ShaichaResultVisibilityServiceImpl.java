package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultVisibilityDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultVisibilityDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultVisibilityService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultVisibilityServiceImpl implements ShaichaResultVisibilityService {
	@Autowired
	private ShaichaResultVisibilityDao resultVisibilityDao;
	
	@Override
	public ShaichaResultVisibilityDO get(Integer tVisibilityId){
		return resultVisibilityDao.get(tVisibilityId);
	}
	
	@Override
	public List<ShaichaResultVisibilityDO> list(Map<String, Object> map){
		return resultVisibilityDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultVisibilityDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultVisibilityDO resultVisibility){
		return resultVisibilityDao.save(resultVisibility);
	}
	
	@Override
	public int update(ShaichaResultVisibilityDO resultVisibility){
		return resultVisibilityDao.update(resultVisibility);
	}
	
	@Override
	public int remove(Integer tVisibilityId){
		return resultVisibilityDao.remove(tVisibilityId);
	}
	
	@Override
	public int batchRemove(Integer[] tVisibilityIds){
		return resultVisibilityDao.batchRemove(tVisibilityIds);
	}
	
}
