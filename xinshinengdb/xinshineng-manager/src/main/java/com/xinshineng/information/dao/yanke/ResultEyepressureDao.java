package com.xinshineng.information.dao.yanke;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xinshineng.information.domain.yanke.ResultEyepressureDO;

/**
 * 眼内压
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ResultEyepressureDao {

	ResultEyepressureDO get(Integer tEyepressureId);
	
	List<ResultEyepressureDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ResultEyepressureDO resultEyepressure);
	
	int update(ResultEyepressureDO resultEyepressure);
	
	int remove(Integer t_eyepressure_id);
	
	int batchRemove(Integer[] tEyepressureIds);
}
