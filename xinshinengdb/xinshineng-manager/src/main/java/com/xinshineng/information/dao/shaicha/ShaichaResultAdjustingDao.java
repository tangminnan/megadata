package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.ShaichaResultAdjustingDO;


/**
 * 调节灵敏度
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ShaichaResultAdjustingDao {

	ShaichaResultAdjustingDO get(Integer tAdjustingId);
	
	List<ShaichaResultAdjustingDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaResultAdjustingDO resultAdjusting);
	
	int update(ShaichaResultAdjustingDO resultAdjusting);
	
	int remove(Integer t_adjusting__id);
	
	int batchRemove(Integer[] tAdjustingIds);
}
