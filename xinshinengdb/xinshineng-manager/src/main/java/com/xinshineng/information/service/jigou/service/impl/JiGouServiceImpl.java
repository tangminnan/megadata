package com.xinshineng.information.service.jigou.service.impl;

import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.dao.yanke.StudentDao;
import com.xinshineng.information.service.jigou.service.JiGouService;
import com.xinshineng.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiGouServiceImpl implements JiGouService {

    @Autowired
    private ShaichaStudentDao shaichaStudentDao;

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private UserDao userDao;
    @Override
    public Map getOneData(Integer sys_id) {
        Map resultMap = new HashMap();
        DecimalFormat df = new DecimalFormat("0.0");
        Integer checkNum = shaichaStudentDao.getCheckNum(sys_id);
        resultMap.put("checkNum",checkNum);
        Integer JsNum = shaichaStudentDao.getJiGouJSNum(sys_id);
        BigDecimal bg = new BigDecimal((float) JsNum / checkNum);
        String JiGouJsLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
        resultMap.put("JiGouJsLv",JiGouJsLv);
        Integer manNum = shaichaStudentDao.getJiGouManNum(sys_id);
        Integer womenNum = shaichaStudentDao.getJiGouWomenNum(sys_id);
        if (manNum>womenNum){
            bg = new BigDecimal((float) womenNum / manNum);
        }else {
            bg = new BigDecimal((float) manNum / womenNum);
        }
        double humanLv = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List bl = BL(humanLv);
        if (manNum>womenNum){
            resultMap.put("man",bl.get(1).toString()+" : "+ bl.get(0).toString());
        }else {
            resultMap.put("man",bl.get(0).toString() +" : "+bl.get(1).toString());
        }
        if (resultMap.get("man").equals("1 : 0") || resultMap.get("man").equals("0 : 1")){
            resultMap.put("man","1 : 1");
        }
        Integer JiGouLcNum = shaichaStudentDao.getJiGouLcNum(sys_id);
        bg =new BigDecimal((float)JiGouLcNum/checkNum);
        String JiGouLcLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
        resultMap.put("JiGouLcLv",JiGouLcLv);
        Integer JiGouJxNum = shaichaStudentDao.getJiGouJxNum(sys_id);
        bg = new BigDecimal((float)JiGouJxNum/checkNum);
        String JiGouJxLv = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
        resultMap.put("JiGouJxLv",JiGouJxLv);
        String name = userDao.getName(sys_id);
        resultMap.put("name",name);
        return resultMap;
    }

    @Override
    public Map getTwoData(Integer sys_id) {
        DecimalFormat df = new DecimalFormat("0.0");
        Map<String,List> result = new HashMap();
        List<Map<String,Object>> everyCheckNum = shaichaStudentDao.getEveryCheckNum(sys_id);
        List<String> checkTimeList = new ArrayList<>();
        List<Long> numList= new ArrayList<>();
        List<String> everyCheckLcList = new ArrayList<>();
        List<String> everyCheckZxList = new ArrayList<>();
        List<String> everyCheckJxList = new ArrayList<>();
        for (Map<String, Object> map1 : everyCheckNum) {
            String check_time = (String) map1.get("check_time");
            checkTimeList.add(check_time);
            Long num = (Long) map1.get("num");
            numList.add(num);
            Integer everyCheckLcNum = shaichaStudentDao.getEveryCheckLcNum(sys_id,check_time);
            Integer everyCheckZxNum = shaichaStudentDao.getEveryCheckZxNum(sys_id,check_time);
            Integer everyCheckJxNum = shaichaStudentDao.getEveryCheckJxNum(sys_id,check_time);
            BigDecimal bg = new BigDecimal((float) everyCheckLcNum / num);
            String Lc = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            everyCheckLcList.add(Lc);
            bg = new BigDecimal((float)everyCheckZxNum/num);
            String zx = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            everyCheckZxList.add(zx);
            bg = new BigDecimal((float)everyCheckJxNum/num);
            String jx = df.format(bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            everyCheckJxList.add(jx);
        }
        result.put("checkTimeList",checkTimeList);
        result.put("numList",numList);
        result.put("everyCheckLcList",everyCheckLcList);
        result.put("everyCheckZxList",everyCheckZxList);
        result.put("everyCheckJxList",everyCheckJxList);

        return result;
    }

    @Override
    public Map getThreeData(Integer sys_id) {
        Map<String,Integer> JsMap = new HashMap<>();
        Integer checkNum = shaichaStudentDao.getCheckNum(sys_id);
        Integer mildNumber = shaichaStudentDao.mildNumberForJG(sys_id);
        Integer moderateNumber = shaichaStudentDao.moderateNumberForJG(sys_id);
        Integer highlyNumber = shaichaStudentDao.highlyNumberForJG(sys_id);
        Integer normal = checkNum-mildNumber-moderateNumber-highlyNumber;
        JsMap.put("normal",normal);
        JsMap.put("mildNumber",mildNumber);
        JsMap.put("moderateNumber",moderateNumber);
        JsMap.put("highlyNumber",highlyNumber);
        return JsMap;
    }

    @Override
    public Map getFourData(Integer sys_id) {
        Map<String,Integer> ageMap = new HashMap<>();
        Integer lt6Num = shaichaStudentDao.getJiGouLt6Num(sys_id);
        Integer to12Num = shaichaStudentDao.getJiGou6To12Num(sys_id);
        Integer to15Num = shaichaStudentDao.getJiGou13To15Num(sys_id);
        Integer to18Num = shaichaStudentDao.getJiGou16To18Num(sys_id);
        Integer gt18Num = shaichaStudentDao.getJiGouGt18Num(sys_id);
        ageMap.put("lt6Num",lt6Num);
        ageMap.put("to12Num",to12Num);
        ageMap.put("to15Num",to15Num);
        ageMap.put("to18Num",to18Num);
        ageMap.put("gt18Num",gt18Num);
        return ageMap;
    }

    @Override
    public List<Map> getSchoolList(Integer sys_id) {
        List<Map> SchoolList = shaichaStudentDao.getJGSchoolList(sys_id);
        return SchoolList;
    }

    @Override
    public List<Map> getGerenList(Integer sys_id) {
        List<Map> result = new ArrayList<>();
        List<Map> gerenList = shaichaStudentDao.getGeRenList(sys_id);
        for (int i = 0; i < gerenList.size(); i++) {
            Map toAddMap = new HashMap();
            Map map = gerenList.get(i);
            Double NAKED = Double.parseDouble(map.get("NAKED").toString());
            Double DXQJ = Double.parseDouble(map.get("DXQJ").toString());
            if (DXQJ>=-0.5 && DXQJ <=0.75){
                toAddMap.put("name",map.get("NAME"));
                toAddMap.put("eyeCondition","临床前期");
                result.add(toAddMap);
                continue;
            }
            if (NAKED<5.0 && DXQJ<-0.5){
                toAddMap.put("name",map.get("NAME"));
                toAddMap.put("eyeCondition","真性近视");
                result.add(toAddMap);
                continue;
            }
            if (NAKED>=5.0 && DXQJ<-0.5){
                toAddMap.put("name",map.get("NAME"));
                toAddMap.put("eyeCondition","假性近视");
                result.add(toAddMap);
                continue;
            }
            toAddMap.put("name",map.get("NAME"));
            toAddMap.put("eyeCondition","正常");
            result.add(toAddMap);
        }
        return result;

    }

    @Override
    public List<Map> getEyeaxisList(Integer sys_id) {
        List<Map> eyeaxisList = shaichaStudentDao.getJGEyeaxisList(sys_id);
        return eyeaxisList;
    }

    @Override
    public List<Map> getCornealList(Integer sys_id) {
        List<Map> cornealList = shaichaStudentDao.getJGCornealList(4);
        return cornealList;
    }


    /**
     * 实现男女比例,小数转变为最简分数  z为整数部分，c为分子，b为分母
     * @param n
     * @return
     */
    public static List BL (double n){
        List list = new ArrayList();
        int z,l,b,c;
        double x;
        z=(int)n;
        BigDecimal p=new BigDecimal(n);
        BigDecimal q=new BigDecimal(z);
        BigDecimal y=p.subtract(q);
        x=y.setScale(8,   BigDecimal.ROUND_HALF_UP).doubleValue();
        String a=""+x;
        l=a.length()-2;
        b=(int)Math.pow(10,l);
        c=(int)(b*x);
        if(c==0) {
            list.add(0, 0);
            list.add(1, 1);
        }else if(c==1) {
            list.add(0, c);
            list.add(1, b);
        }else if(c!=0)
        {
            for(int i=c;i>1;i--)
            {
                if(b%i==0&&c%i==0)
                {
                    b=b/i;
                    c=c/i;
                    list.add(0, c);
                    list.add(1, b);
                    break;
                }
                else if((b%i!=0||c%i!=0)&&i==2)
                {
                    list.add(0, c);
                    list.add(1, b);
                    break;
                }
                else
                    continue;
            }
        }
        return list;
    }
}
