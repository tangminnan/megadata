package com.xinshineng.information.service.zhenjiu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinshineng.information.dao.zhenjiu.DataDao;
import com.xinshineng.information.domain.zhenjiu.DataDO;
import com.xinshineng.information.service.zhenjiu.service.DataService;





@Service
public class DataServiceImpl implements DataService {
	@Autowired
	private DataDao dataDao;
	
	@Override
	public DataDO get(Integer id){
		return dataDao.get(id);
	}
	
	@Override
	public List<DataDO> list(Map<String, Object> map){
		return dataDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dataDao.count(map);
	}
	
	@Override
	public int save(DataDO data){
		return dataDao.save(data);
	}
	
	@Override
	public int update(DataDO data){
		return dataDao.update(data);
	}
	
	@Override
	public int remove(Integer id){
		return dataDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return dataDao.batchRemove(ids);
	}

	@Override
	public List<DataDO> selectByMonth(Map<String, Object> params) {
		
		return dataDao.selectByMonth(params);
	}

	@Override
	public List<DataDO> selectByWeek(Map<String, Object> params) {
		return  dataDao.selectByWeek(params);
	}

	@Override
	public List<DataDO> selectByDay(Map<String, Object> params) {
		return  dataDao.selectByDay(params);
	}


	@Override
	public List<DataDO> selectUsername(Map<String, Object> params) {
		return null;
	}
	
	@Override
	public List<DataDO> selectBytime(Map<String, Object> params) {
		return dataDao.selectBytime(params);
	}

	@Override
	public List<DataDO> lists(Integer id) {
		return dataDao.lists(id);
	}

	@Override
	public List<Map<String, Object>> exeList(Map<String, Object> map) {
		
		return dataDao.exeList(map);
	}

	@Override
	public List<Map<String,Object>> getData(String name, String idCard) {
		List<Map<String,Object>> dataList = dataDao.getData(name,idCard);
		if (dataList.size()==0){
			return null;
		}
		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = dataList.get(i);
			map.put("phone",map.get("phone")==null?"":map.get("phone"));
			int treat_waveform = (int) map.get("treat_waveform");
			if (0==treat_waveform){
				map.put("treat_waveform","");
			}
			if (1==treat_waveform){
				map.put("treat_waveform","连续波");
			}
			if (2==treat_waveform){
				map.put("treat_waveform","断续波");
			}
			if (3==treat_waveform){
				map.put("treat_waveform","疏密波");
			}
			int treat_workmethod = (int) map.get("treat_workmethod");
			if (0==treat_workmethod){
				map.put("treat_workmethod","");
			}
			if (1==treat_workmethod){
				map.put("treat_workmethod","同时");
			}
			if (2==treat_workmethod){
				map.put("treat_workmethod","轮巡");
			}
			map.put("treat_electrode","");
			dataList.set(i,map);
		}
		return dataList;
	}

}
