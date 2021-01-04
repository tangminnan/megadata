package com.xinshineng.information.service.yanke.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.schemas.office.visio.x2012.main.CellType;
import com.xinshineng.common.config.BootdoConfig;
import com.xinshineng.common.utils.R;
import com.xinshineng.information.dao.yanke.StudentDao;
import com.xinshineng.information.domain.yanke.AnswerResultDO;
import com.xinshineng.information.domain.yanke.StudentDO;
import com.xinshineng.information.service.yanke.service.StudentService;
import com.xinshineng.system.config.ExcelUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;




@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private BootdoConfig bootdoConfig;
	
	@Override
	public StudentDO get(Integer id){
		return studentDao.get(id);
	}
	
	@Override
	public List<StudentDO> list(Map<String, Object> map){
		return studentDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return studentDao.count(map);
	}
	
	@Override
	public int save(StudentDO student){
		return studentDao.save(student);
	}
	
	@Override
	public int update(StudentDO student){
		return studentDao.update(student);
	}
	
	@Override
	public int remove(Integer id){
		return studentDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return studentDao.batchRemove(ids);
	}

	
	@Override
	public List<StudentDO> getList() {
		return studentDao.getList();
	}
	
	@Override
	public List<AnswerResultDO> listDati(Map<String, Object> map) {
		return studentDao.listDati(map);
	}

	@Override
	public int countDati(Map<String, Object> map) {
		return studentDao.countDati(map);
	}

	@Override
	public R importMember(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void downloadErweima(Integer[] ids, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public R daorudatijiguo(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}
}
