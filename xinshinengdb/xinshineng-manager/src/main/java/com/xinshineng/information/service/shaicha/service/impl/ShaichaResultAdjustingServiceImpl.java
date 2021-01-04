package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultAdjustingDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultAdjustingDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultAdjustingService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultAdjustingServiceImpl implements ShaichaResultAdjustingService {
	@Autowired
	private ShaichaResultAdjustingDao resultAdjustingDao;
	
	@Override
	public ShaichaResultAdjustingDO get(Integer tAdjustingId){
		return resultAdjustingDao.get(tAdjustingId);
	}
	
	@Override
	public List<ShaichaResultAdjustingDO> list(Map<String, Object> map){
		return resultAdjustingDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultAdjustingDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultAdjustingDO resultAdjusting){
		return resultAdjustingDao.save(resultAdjusting);
	}
	
	@Override
	public int update(ShaichaResultAdjustingDO resultAdjusting){
		return resultAdjustingDao.update(resultAdjusting);
	}
	
	@Override
	public int remove(Integer tAdjustingId){
		return resultAdjustingDao.remove(tAdjustingId);
	}
	
	@Override
	public int batchRemove(Integer[] tAdjustingIds){
		return resultAdjustingDao.batchRemove(tAdjustingIds);
	}
	
}
