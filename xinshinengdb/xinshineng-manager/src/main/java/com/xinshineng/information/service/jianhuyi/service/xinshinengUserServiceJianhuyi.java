package com.xinshineng.information.service.jianhuyi.service;

import java.util.List;
import java.util.Map;

import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;
import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;

public interface xinshinengUserServiceJianhuyi {
	
	List<jianhuyiUserDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

}
