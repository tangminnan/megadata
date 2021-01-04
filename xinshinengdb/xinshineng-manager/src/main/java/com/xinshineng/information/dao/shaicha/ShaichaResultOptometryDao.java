package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.ShaichaResultOptometryDO;

/**
 * 验光数据表
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ShaichaResultOptometryDao {

	ShaichaResultOptometryDO get(Integer tOptometryId);
	
	List<ShaichaResultOptometryDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaResultOptometryDO resultOptometry);
	
	int update(ShaichaResultOptometryDO resultOptometry);
	
	int remove(Integer t_optometry_id);
	
	int batchRemove(Integer[] tOptometryIds);
	
	List<ShaichaResultOptometryDO> getCheckDate(String identityCard);
}
