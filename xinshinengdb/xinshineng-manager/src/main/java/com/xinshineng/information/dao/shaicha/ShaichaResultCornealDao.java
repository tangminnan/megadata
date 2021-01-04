package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.ShaichaResultCornealDO;


/**
 * 角膜曲率详情
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ShaichaResultCornealDao {

	ShaichaResultCornealDO get(Integer tCornealId);
	
	List<ShaichaResultCornealDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaResultCornealDO resultCorneal);
	
	int update(ShaichaResultCornealDO resultCorneal);
	
	int remove(Integer t_corneal_id);
	
	int batchRemove(Integer[] tCornealIds);
	
	List<ShaichaResultCornealDO> queryByToptometryTd(Integer tOptometryId);
}
