package com.xinshineng.information.service.jianhuyi.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.jianhuyi.UseJianhuyiLogDO;

/**
 * 检测记录表
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-10-11 18:54:34
 */
public interface UseJianhuyiLogService {
	
	UseJianhuyiLogDO get(Integer id);
	
	List<UseJianhuyiLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UseJianhuyiLogDO useJianhuyiLog);
	
	int update(UseJianhuyiLogDO useJianhuyiLog);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
