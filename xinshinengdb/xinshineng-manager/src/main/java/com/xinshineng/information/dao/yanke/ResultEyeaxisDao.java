package com.xinshineng.information.dao.yanke;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xinshineng.information.domain.yanke.ResultEyeaxisDO;

/**
 * 眼轴长度
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-16 14:08:23
 */
@Mapper
public interface ResultEyeaxisDao {

	ResultEyeaxisDO get(Integer tEyeaxisId);
	
	List<ResultEyeaxisDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ResultEyeaxisDO resultEyeaxis);
	
	int update(ResultEyeaxisDO resultEyeaxis);
	
	int remove(Integer t_eyeaxis_id);
	
	int batchRemove(Integer[] tEyeaxisIds);
}
