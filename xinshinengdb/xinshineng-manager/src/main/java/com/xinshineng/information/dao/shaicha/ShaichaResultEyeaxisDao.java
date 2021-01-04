package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.ShaichaResultEyeaxisDO;

/**
 * 眼轴长度
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ShaichaResultEyeaxisDao {

	ShaichaResultEyeaxisDO get(Integer tEyeaxisId);
	
	List<ShaichaResultEyeaxisDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ShaichaResultEyeaxisDO resultEyeaxis);
	
	int update(ShaichaResultEyeaxisDO resultEyeaxis);
	
	int remove(Integer t_eyeaxis_id);
	
	int batchRemove(Integer[] tEyeaxisIds);
}
