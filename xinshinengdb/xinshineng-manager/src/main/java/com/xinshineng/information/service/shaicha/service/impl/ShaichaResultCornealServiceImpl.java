package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultCornealDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultCornealDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultCornealService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultCornealServiceImpl implements ShaichaResultCornealService {
	@Autowired
	private ShaichaResultCornealDao resultCornealDao;
	
	@Override
	public ShaichaResultCornealDO get(Integer tCornealId){
		return resultCornealDao.get(tCornealId);
	}
	
	@Override
	public List<ShaichaResultCornealDO> list(Map<String, Object> map){
		return resultCornealDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultCornealDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultCornealDO resultCorneal){
		return resultCornealDao.save(resultCorneal);
	}
	
	@Override
	public int update(ShaichaResultCornealDO resultCorneal){
		return resultCornealDao.update(resultCorneal);
	}
	
	@Override
	public int remove(Integer tCornealId){
		return resultCornealDao.remove(tCornealId);
	}
	
	@Override
	public int batchRemove(Integer[] tCornealIds){
		return resultCornealDao.batchRemove(tCornealIds);
	}

	@Override
	public List<ShaichaResultCornealDO> queryByToptometryTd(Integer tOptometryId) {
		return resultCornealDao.queryByToptometryTd(tOptometryId);
	}
	
}
