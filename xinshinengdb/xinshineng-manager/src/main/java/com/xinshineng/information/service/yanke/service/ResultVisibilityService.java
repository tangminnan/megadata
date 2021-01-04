package com.xinshineng.information.service.yanke.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.yanke.ResultVisibilityDO;

/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ResultVisibilityService {
	
	ResultVisibilityDO get(Integer tVisibilityId);
	
	List<ResultVisibilityDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ResultVisibilityDO resultVisibility);
	
	int update(ResultVisibilityDO resultVisibility);
	
	int remove(Integer tVisibilityId);
	
	int batchRemove(Integer[] tVisibilityIds);
}
