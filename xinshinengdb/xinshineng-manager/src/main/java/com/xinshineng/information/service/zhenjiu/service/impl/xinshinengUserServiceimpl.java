package com.xinshineng.information.service.zhenjiu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinshineng.information.dao.jianhuyi.jianhuyiUserDao;
import com.xinshineng.information.dao.zhenjiu.zhenjiuUserDao;
import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;
import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;
import com.xinshineng.information.service.zhenjiu.service.xinshinengUserService;


@Service
public class xinshinengUserServiceimpl implements xinshinengUserService{
	
	@Autowired
	private zhenjiuUserDao zhenjiuUserDao;
	
	@Override
//	@Transactional
	public List<zhenjiuUserDO> list(Map<String, Object> map) {
		return zhenjiuUserDao.list(map);
	}
	@Override
	public int count(Map<String, Object> map) {
		return zhenjiuUserDao.count(map);
	}
	@Override
	public zhenjiuUserDO get(Integer id) {
		
		return zhenjiuUserDao.get(id);
	}

}
