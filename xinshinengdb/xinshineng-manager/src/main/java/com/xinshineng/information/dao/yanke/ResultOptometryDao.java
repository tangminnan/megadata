package com.xinshineng.information.dao.yanke;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xinshineng.information.domain.yanke.ResultOptometryDO;

/**
 * 验光数据表
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ResultOptometryDao {

	ResultOptometryDO get(Integer tOptometryId);
	
	List<ResultOptometryDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ResultOptometryDO resultOptometry);
	
	int update(ResultOptometryDO resultOptometry);
	
	int remove(Integer t_optometry_id);
	
	int batchRemove(Integer[] tOptometryIds);
}
