package com.xinshineng.information.dao.shaicha;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.shaicha.shaiChaUserDo;


@Mapper
public interface shaiChaUserDao {

	List<shaiChaUserDo> listShaiCha(Map<String,Object> map);
	
	int count(Map<String,Object> map);
		
}