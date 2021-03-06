package com.xinshineng.information.service.yanke.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.yanke.ResultVisibilityDao;
import com.xinshineng.information.domain.yanke.ResultVisibilityDO;
import com.xinshineng.information.service.yanke.service.ResultVisibilityService;

import java.util.List;
import java.util.Map;




@Service
public class ResultVisibilityServiceImpl implements ResultVisibilityService {
	@Autowired
	private ResultVisibilityDao resultVisibilityDao;
	
	@Override
	public ResultVisibilityDO get(Integer tVisibilityId){
		return resultVisibilityDao.get(tVisibilityId);
	}
	
	@Override
	public List<ResultVisibilityDO> list(Map<String, Object> map){
		return resultVisibilityDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultVisibilityDao.count(map);
	}
	
	@Override
	public int save(ResultVisibilityDO resultVisibility){
		return resultVisibilityDao.save(resultVisibility);
	}
	
	@Override
	public int update(ResultVisibilityDO resultVisibility){
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
