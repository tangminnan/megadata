package com.xinshineng.information.dao.zhenjiu;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xinshineng.information.domain.zhenjiu.zhenjiuUserDO;

/**
 * 用户信息表
 * @author wjl
 * @email bushuo@163.com
 * @date 2018-09-27 10:18:38
 */
@Mapper
public interface zhenjiuUserDao {

	zhenjiuUserDO get(Integer id);
	
	zhenjiuUserDO getidbyphone(String phone);
	
	List<zhenjiuUserDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(zhenjiuUserDO user);
	
	int update(zhenjiuUserDO user);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

}
