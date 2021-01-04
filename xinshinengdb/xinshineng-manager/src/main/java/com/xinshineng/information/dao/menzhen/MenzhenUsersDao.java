package com.xinshineng.information.dao.menzhen;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinshineng.information.domain.menzhen.MenzhenUsersDO;


/**
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-04-24 16:22:03
 */
@Mapper
public interface MenzhenUsersDao {

	MenzhenUsersDO get(Long uId);
	
	List<MenzhenUsersDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(MenzhenUsersDO user);
	
	int update(MenzhenUsersDO user);
	
	int remove(Long u_id);
	
	int batchRemove(Long[] uIds);

	List<MenzhenUsersDO> lists();

	List<MenzhenUsersDO> getFileByid(Long uid);

	List<MenzhenUsersDO> getfileByname(Map<String, Object> map);

	MenzhenUsersDO getNameByimg(String uimg);

	int removeByimg(String uimg);

	List<Map<String, Object>> exeList(Map<String, Object> map);

	List<MenzhenUsersDO> selectlist(Map<String, Object> map);

	int updateUser(Long uid);
}
