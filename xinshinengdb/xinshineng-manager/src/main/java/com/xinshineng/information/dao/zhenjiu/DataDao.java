package com.xinshineng.information.dao.zhenjiu;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.zhenjiu.DataDO;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-03-29 17:51:43
 */
@Mapper
public interface DataDao {

	DataDO get(Integer id);
	
	List<DataDO> list(Map<String,Object> map);
	
	List<DataDO> selectByMonth(Map<String, Object> params);
	
	int count(Map<String,Object> map);
	
	int save(DataDO data);
	
	int update(DataDO data);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	List<DataDO> selectByWeek(Map<String, Object> params);

	List<DataDO> selectByDay(Map<String, Object> params);
	
	List<DataDO> selectUsername(Map<String, Object> params);

	List<DataDO> selectBytime(Map<String, Object> params);

	List<DataDO> lists(Integer id);

	List<Map<String, Object>> exeList(Map<String, Object> map);

	List<Map<String, Object>> getData(@Param("name") String name,@Param("idCard") String idCard);
}
