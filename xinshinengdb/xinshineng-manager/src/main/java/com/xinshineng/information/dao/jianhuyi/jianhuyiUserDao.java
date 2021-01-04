package com.xinshineng.information.dao.jianhuyi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.xinshineng.information.domain.jianhuyi.jianhuyiUserDO;

/**
 * 用户信息表
 * @author wjl
 * @email bushuo@163.com
 * @date 2018-09-27 10:18:38
 */
@Mapper
public interface jianhuyiUserDao {

	jianhuyiUserDO get(Integer id);
	
	jianhuyiUserDO getidbyphone(String phone);
	
	List<jianhuyiUserDO> listjianhuyi(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(jianhuyiUserDO user);
	
	int update(jianhuyiUserDO user);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

}
