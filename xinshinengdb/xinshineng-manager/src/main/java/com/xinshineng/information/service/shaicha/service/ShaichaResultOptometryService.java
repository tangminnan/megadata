package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.shaicha.ShaichaResultOptometryDO;


/**
 * 验光数据表
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
public interface ShaichaResultOptometryService {
	
	ShaichaResultOptometryDO get(Integer tOptometryId);
	
	List<ShaichaResultOptometryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ShaichaResultOptometryDO resultOptometry);
	
	int update(ShaichaResultOptometryDO resultOptometry);
	
	int remove(Integer tOptometryId);
	
	int batchRemove(Integer[] tOptometryIds);
	
	List<ShaichaResultOptometryDO> getCheckDate(String identityCard);
}
