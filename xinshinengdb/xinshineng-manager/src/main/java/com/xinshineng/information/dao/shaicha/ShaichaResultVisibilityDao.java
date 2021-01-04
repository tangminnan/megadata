package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.ShaichaResultVisibilityDO;


/**
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ShaichaResultVisibilityDao {

	ShaichaResultVisibilityDO get(Integer tVisibilityId);
	
	List<ShaichaResultVisibilityDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaResultVisibilityDO resultVisibility);
	
	int update(ShaichaResultVisibilityDO resultVisibility);
	
	int remove(Integer t_visibility_id);
	
	int batchRemove(Integer[] tVisibilityIds);
}
