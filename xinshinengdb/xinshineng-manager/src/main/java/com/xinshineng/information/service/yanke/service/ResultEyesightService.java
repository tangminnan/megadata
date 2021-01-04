package com.xinshineng.information.service.yanke.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.yanke.ResultEyesightDO;

/**
 * 视力检查
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ResultEyesightService {
	
	ResultEyesightDO get(Integer tEyesightId);
	
	List<ResultEyesightDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ResultEyesightDO resultEyesight);
	
	int update(ResultEyesightDO resultEyesight);
	
	int remove(Integer tEyesightId);
	
	int batchRemove(Integer[] tEyesightIds);
}
