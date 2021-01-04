package com.xinshineng.information.dao.zhenjiu;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.zhenjiu.CheckDataDO;

/**
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-11-23 18:14:30
 */
@Mapper
public interface CheckDataDao {

	CheckDataDO get(Integer id);
	
	List<CheckDataDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CheckDataDO checkData);
	
	int update(CheckDataDO checkData);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	List<CheckDataDO> getStudentId(Integer studentId);
}
