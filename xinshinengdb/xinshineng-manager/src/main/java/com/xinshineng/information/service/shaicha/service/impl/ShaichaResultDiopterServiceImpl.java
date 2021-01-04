package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultDiopterDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultDiopterDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultDiopterService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultDiopterServiceImpl implements ShaichaResultDiopterService {
	@Autowired
	private ShaichaResultDiopterDao resultDiopterDao;
	
	@Override
	public ShaichaResultDiopterDO get(Integer tDiopterId){
		return resultDiopterDao.get(tDiopterId);
	}
	
	@Override
	public List<ShaichaResultDiopterDO> list(Map<String, Object> map){
		return resultDiopterDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultDiopterDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultDiopterDO resultDiopter){
		return resultDiopterDao.save(resultDiopter);
	}
	
	@Override
	public int update(ShaichaResultDiopterDO resultDiopter){
		return resultDiopterDao.update(resultDiopter);
	}
	
	@Override
	public int remove(Integer tDiopterId){
		return resultDiopterDao.remove(tDiopterId);
	}
	
	@Override
	public int batchRemove(Integer[] tDiopterIds){
		return resultDiopterDao.batchRemove(tDiopterIds);
	}

	@Override
	public List<ShaichaResultDiopterDO> getByToptometryId(Integer tOptometryId) {
		return resultDiopterDao.getByToptometryId(tOptometryId);
	}
	
}
