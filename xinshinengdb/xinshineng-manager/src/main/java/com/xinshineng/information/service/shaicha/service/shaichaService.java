package com.xinshineng.information.service.shaicha.service;

import java.util.List;
import java.util.Map;


import com.xinshineng.information.domain.shaicha.shaiChaUserDo;

public interface shaichaService {

	List<shaiChaUserDo> listShaiCha(Map<String,Object> map);
	
	int count(Map<String,Object> map);
		
}
