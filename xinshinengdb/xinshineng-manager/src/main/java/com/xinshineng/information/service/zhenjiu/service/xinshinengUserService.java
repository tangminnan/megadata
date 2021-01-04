package com.xinshineng.information.service.zhenjiu.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;
import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;

public interface xinshinengUserService {
	
	List<zhenjiuUserDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	zhenjiuUserDO get(Integer id);

}
