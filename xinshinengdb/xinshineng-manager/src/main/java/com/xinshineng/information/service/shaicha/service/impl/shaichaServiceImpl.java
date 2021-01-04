package com.xinshineng.information.service.shaicha.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.shaicha.shaiChaUserDao;
import com.xinshineng.information.domain.shaicha.shaiChaUserDo;
import com.xinshineng.information.service.shaicha.service.shaichaService;

@Service
public class shaichaServiceImpl implements shaichaService {

	@Autowired
	private shaiChaUserDao scUserdao;
	

	@Override
	public int count(Map<String, Object> map) {
		return scUserdao.count(map);
	}
	
	@Override
	public List<shaiChaUserDo> listShaiCha(Map<String, Object> map) {
		return scUserdao.listShaiCha(map);
	}


}
