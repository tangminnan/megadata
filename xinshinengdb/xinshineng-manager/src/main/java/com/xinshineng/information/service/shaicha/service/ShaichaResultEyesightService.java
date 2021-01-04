package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.shaicha.ShaichaResultEyesightDO;


/**
 * 视力检查
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ShaichaResultEyesightService {
	
	ShaichaResultEyesightDO get(Integer tEyesightId);
	
	List<ShaichaResultEyesightDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ShaichaResultEyesightDO resultEyesight);
	
	int update(ShaichaResultEyesightDO resultEyesight);
	
	int remove(Integer tEyesightId);
	
	int batchRemove(Integer[] tEyesightIds);
}
