package com.xinshineng.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xinshineng.common.domain.LogDO;
import com.xinshineng.common.domain.PageDO;
import com.xinshineng.common.utils.Query;
@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
