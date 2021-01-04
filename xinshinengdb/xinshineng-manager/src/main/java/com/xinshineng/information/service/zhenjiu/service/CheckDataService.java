package com.xinshineng.information.service.zhenjiu.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.xinshineng.information.domain.zhenjiu.CheckDataDO;

/**
 * 
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-11-23 18:14:30
 */
public interface CheckDataService {
	
	CheckDataDO get(Integer id);
	
	List<CheckDataDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CheckDataDO checkData);
	
	int update(CheckDataDO checkData);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
		
	void downloadExportExcel(Integer id,HttpServletResponse response);
}
