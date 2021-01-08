package com.xinshineng.information.service.zhenjiu.service;


import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.zhenjiu.DataDO;

/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-03-29 17:51:43
 */
public interface DataService {
	
	DataDO get(Integer id);
	
	List<DataDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DataDO data);
	
	int update(DataDO data);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	List<DataDO> selectByMonth(Map<String, Object> params);

	List<DataDO> selectByWeek(Map<String, Object> params);

	List<DataDO> selectByDay(Map<String, Object> params);
	
	List<DataDO> selectUsername(Map<String, Object> params);
	
	List<DataDO> selectBytime(Map<String, Object> params);

	List<DataDO> lists(Integer id);

	List<Map<String, Object>> exeList(Map<String, Object> map);

    List<Map<String,Object>> getData(String name, String idCard);
}
