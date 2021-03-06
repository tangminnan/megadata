package com.xinshineng.information.dao.yanke;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.yanke.ResultCornealDO;

/**
 * 角膜曲率详情
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ResultCornealDao {

	ResultCornealDO get(Integer tCornealId);
	
	List<ResultCornealDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ResultCornealDO resultCorneal);
	
	int update(ResultCornealDO resultCorneal);
	
	int remove(Integer t_corneal_id);
	
	int batchRemove(Integer[] tCornealIds);
	
	List<ResultCornealDO> queryByToptometryTd(Integer tOptometryId);
}
