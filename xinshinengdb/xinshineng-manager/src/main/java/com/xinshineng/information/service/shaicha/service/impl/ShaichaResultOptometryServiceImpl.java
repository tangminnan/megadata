package com.xinshineng.information.service.shaicha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.ShaichaResultOptometryDao;
import com.xinshineng.information.domain.shaicha.ShaichaResultOptometryDO;
import com.xinshineng.information.service.shaicha.service.ShaichaResultOptometryService;

import java.util.List;
import java.util.Map;



@Service
public class ShaichaResultOptometryServiceImpl implements ShaichaResultOptometryService {
	@Autowired
	private ShaichaResultOptometryDao resultOptometryDao;
	
	@Override
	public ShaichaResultOptometryDO get(Integer tOptometryId){
		return resultOptometryDao.get(tOptometryId);
	}
	
	@Override
	public List<ShaichaResultOptometryDO> list(Map<String, Object> map){
		return resultOptometryDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resultOptometryDao.count(map);
	}
	
	@Override
	public int save(ShaichaResultOptometryDO resultOptometry){
		return resultOptometryDao.save(resultOptometry);
	}
	
	@Override
	public int update(ShaichaResultOptometryDO resultOptometry){
		return resultOptometryDao.update(resultOptometry);
	}
	
	@Override
	public int remove(Integer tOptometryId){
		return resultOptometryDao.remove(tOptometryId);
	}
	
	@Override
	public int batchRemove(Integer[] tOptometryIds){
		return resultOptometryDao.batchRemove(tOptometryIds);
	}

	@Override
	public List<ShaichaResultOptometryDO> getCheckDate(String identityCard) {
		// TODO Auto-generated method stub
		return resultOptometryDao.getCheckDate(identityCard);
	}
	
}
