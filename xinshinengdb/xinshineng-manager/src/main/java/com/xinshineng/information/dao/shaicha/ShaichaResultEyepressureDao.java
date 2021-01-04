package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.ShaichaResultEyepressureDO;


/**
 * 眼内压
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ShaichaResultEyepressureDao {

	ShaichaResultEyepressureDO get(Integer tEyepressureId);
	
	List<ShaichaResultEyepressureDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaResultEyepressureDO resultEyepressure);
	
	int update(ShaichaResultEyepressureDO resultEyepressure);
	
	int remove(Integer t_eyepressure_id);
	
	int batchRemove(Integer[] tEyepressureIds);
}
