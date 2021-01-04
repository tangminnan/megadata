package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.shaicha.ShaichaResultCornealDO;


/**
 * 角膜曲率详情
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ShaichaResultCornealService {
	
	ShaichaResultCornealDO get(Integer tCornealId);
	
	List<ShaichaResultCornealDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ShaichaResultCornealDO resultCorneal);
	
	int update(ShaichaResultCornealDO resultCorneal);
	
	int remove(Integer tCornealId);
	
	int batchRemove(Integer[] tCornealIds);
	
	List<ShaichaResultCornealDO> queryByToptometryTd(Integer tOptometryId);
}
