package com.xinshineng.information.service.jianhuyi.service.impl;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.jianhuyi.jianhuyiUserDao;
import com.xinshineng.information.dao.zhenjiu.zhenjiuUserDao;
import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;
import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;
import com.xinshineng.information.service.jianhuyi.service.xinshinengUserServiceJianhuyi;
import com.xinshineng.information.service.zhenjiu.service.xinshinengUserService;


@Service
public class xinshinengUserServiceimplJianhuyi implements xinshinengUserServiceJianhuyi{
	
	@Autowired
	private jianhuyiUserDao jianhuyiUserDao;
	
	

	@Override
	public List<jianhuyiUserDO> list(Map<String, Object> map) {
		return jianhuyiUserDao.listjianhuyi(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return jianhuyiUserDao.count(map);
	}
	

}
