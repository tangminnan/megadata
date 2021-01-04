package com.xinshineng.information.service.yanke.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.yanke.ResultEyesightDao;
import com.xinshineng.information.domain.yanke.ResultEyesightDO;
import com.xinshineng.information.service.yanke.service.ResultEyesightService;

import java.util.List;
import java.util.Map;




@Service
public class ResultEyesightServiceImpl implements ResultEyesightService {
	@Autowired
	private ResultEyesightDao resultEyesightDao;
	
	@Override
	public ResultEyesightDO get(Integer tEyesightId){
		return resultEyesightDao.get(tEyesightId);
	}
	
	@Override
	public List<ResultEyesightDO> list(Map<String, Object> map){
		return resultEyesightDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultEyesightDao.count(map);
	}
	
	@Override
	public int save(ResultEyesightDO resultEyesight){
		return resultEyesightDao.save(resultEyesight);
	}
	
	@Override
	public int update(ResultEyesightDO resultEyesight){
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
