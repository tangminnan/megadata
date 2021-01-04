package com.xinshineng.information.service.yanke.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.yanke.ResultCornealDao;
import com.xinshineng.information.domain.yanke.ResultCornealDO;
import com.xinshineng.information.service.yanke.service.ResultCornealService;

import java.util.List;
import java.util.Map;




@Service
public class ResultCornealServiceImpl implements ResultCornealService {
	@Autowired
	private ResultCornealDao resultCornealDao;
	
	@Override
	public ResultCornealDO get(Integer tCornealId){
		return resultCornealDao.get(tCornealId);
	}
	
	@Override
	public List<ResultCornealDO> list(Map<String, Object> map){
		return resultCornealDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultCornealDao.count(map);
	}
	
	@Override
	public int save(ResultCornealDO resultCorneal){
		return resultCornealDao.save(resultCorneal);
	}
	
	@Override
	public int update(ResultCornealDO resultCorneal){
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
	public List<ResultCornealDO> queryByToptometryTd(Integer tOptometryId) {
		return resultCornealDao.queryByToptometryTd(tOptometryId);
	}
	
}
