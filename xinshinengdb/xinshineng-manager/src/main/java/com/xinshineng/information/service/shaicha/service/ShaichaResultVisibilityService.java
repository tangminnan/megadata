package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.shaicha.ShaichaResultVisibilityDO;


/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ShaichaResultVisibilityService {
	
	ShaichaResultVisibilityDO get(Integer tVisibilityId);
	
	List<ShaichaResultVisibilityDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ShaichaResultVisibilityDO resultVisibility);
	
	int update(ShaichaResultVisibilityDO resultVisibility);
	
	int remove(Integer tVisibilityId);
	
	int batchRemove(Integer[] tVisibilityIds);
}
