package com.xinshineng.common.controller;

import org.springframework.stereotype.Controller;

import com.xinshineng.common.utils.ShiroUtils;
import com.xinshineng.system.domain.UserDO;
import com.xinshineng.system.domain.UserToken;

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}