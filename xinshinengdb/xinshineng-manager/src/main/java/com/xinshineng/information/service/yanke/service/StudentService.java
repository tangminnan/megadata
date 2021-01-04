package com.xinshineng.information.service.yanke.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.xinshineng.common.utils.R;
import com.xinshineng.information.domain.yanke.AnswerResultDO;
import com.xinshineng.information.domain.yanke.StudentDO;


/**
 * 学生表
 * 
 * @author wjl
 * @email bushuo@163.com
 * @date 2019-08-14 17:21:00
 */
public interface StudentService {
	
	StudentDO get(Integer id);
	
	List<StudentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StudentDO student);
	
	int update(StudentDO student);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
	
	R importMember(MultipartFile file);
	
	List<StudentDO> getList();

	void downloadErweima(Integer[] ids,HttpServletResponse response);

	R daorudatijiguo(MultipartFile file);

	List<AnswerResultDO> listDati(Map<String, Object> map);

	int countDati(Map<String,Object> map);
}
