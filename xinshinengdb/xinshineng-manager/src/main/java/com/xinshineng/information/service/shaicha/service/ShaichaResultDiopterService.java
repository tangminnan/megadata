package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.shaicha.ShaichaResultDiopterDO;


/**
 * 曲光度详情
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ShaichaResultDiopterService {
	
	ShaichaResultDiopterDO get(Integer tDiopterId);
	
	List<ShaichaResultDiopterDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ShaichaResultDiopterDO resultDiopter);
	
	int update(ShaichaResultDiopterDO resultDiopter);
	
	int remove(Integer tDiopterId);
	
	int batchRemove(Integer[] tDiopterIds);
	
	List<ShaichaResultDiopterDO> getByToptometryId(Integer tOptometryId);
}
