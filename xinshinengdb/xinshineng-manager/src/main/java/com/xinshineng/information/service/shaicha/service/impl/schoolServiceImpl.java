package com.xinshineng.information.service.shaicha.service.impl;

import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.service.shaicha.service.schoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class schoolServiceImpl implements schoolService {
    @Autowired
    private ShaichaStudentDao shaichaStudentDao;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, String> dataOne(String school, String CityName, String AreaName, String checkdate) {
        Map<String, String> map = new HashMap<>();
        Integer schoolAllNumber = shaichaStudentDao.schoolAllNumber(school,CityName,AreaName);
        Integer schoolThisNumber = shaichaStudentDao.schoolThisNumber(school, CityName, AreaName, checkdate);
        Integer illNumber = shaichaStudentDao.illNumber(school, CityName, AreaName, checkdate);
        Integer manNumber = shaichaStudentDao.sexThisNumber(school, CityName, AreaName, checkdate, 1);
        Integer wumanNumber = shaichaStudentDao.sexThisNumber(school, CityName, AreaName, checkdate, 2);
        Double illLv = getLv(illNumber,schoolThisNumber);
        BigDecimal le = null;
        if(manNumber>wumanNumber){
            le = new BigDecimal((float) wumanNumber / manNumber);
        }else{
            le = new BigDecimal((float) manNumber / wumanNumber);
        }
        Double humanLv = le.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List bl = BL(humanLv);
        map.put("schoolAllNumber",schoolAllNumber.toString());
        map.put("schoolThisNumber",schoolThisNumber.toString());
        map.put("illLv",illLv.toString());
        if (manNumber>wumanNumber){
            map.put("man",bl.get(1).toString() +" : "+ bl.get(0).toString());
//            map.put("wuman",bl.get(0).toString());
        }else {
            map.put("man",bl.get(0).toString() +":"+bl.get(1).toString());
//            map.put("wuman",bl.get(1).toString());
        }
        map.put("school",school);
        map.put("checkdate",checkdate);
        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+"dataOne",map);
        return map;
    }

    @Override
    public Map<String, List> dataTwo(String school, String CityName, String AreaName,String checkdate) {
        Map<String, List> listMap = new HashMap<>();
        List zxjsLv = new ArrayList();
        List everyTime = new ArrayList();
        List myopia = new ArrayList();
        List lcqqLv = new ArrayList();
        List jxjsLv = new ArrayList();
        List<Map> everyCheck = shaichaStudentDao.everyCheck(school, CityName, AreaName);
        //轻度，中度，高度，正常
        Integer mildNumber = shaichaStudentDao.mildNumber(school, CityName, AreaName, checkdate);
        Integer moderateNumber = shaichaStudentDao.moderateNumber(school, CityName, AreaName, checkdate);
        Integer highlyNumber = shaichaStudentDao.highlyNumber(school, CityName, AreaName, checkdate);
        Integer thisNumber1 = shaichaStudentDao.schoolThisNumber(school, CityName, AreaName, checkdate);
        Integer normal = thisNumber1-mildNumber-moderateNumber-highlyNumber;
        myopia.add(normal);
        myopia.add(mildNumber);
        myopia.add(moderateNumber);
        myopia.add(highlyNumber);
        //循环获得历年患病率
        for (Map map : everyCheck){
            String checkTime = map.get("checkTime").toString();
            String allNumber = map.get("checkNumber").toString();
            everyTime.add(checkTime);
            Integer thisNumber = shaichaStudentDao.illNumber(school, CityName, AreaName, checkTime);
            zxjsLv.add(format(getLv(thisNumber,Integer.parseInt(allNumber))));
            Integer lcqqNumber = shaichaStudentDao.lcqqNumber(school, CityName, AreaName, checkTime);
            lcqqLv.add(format(getLv(lcqqNumber,Integer.parseInt(allNumber))));
            Integer jxjsNumber = shaichaStudentDao.jxjsNumber(school, CityName, AreaName, checkTime);
            jxjsLv.add(format(getLv(jxjsNumber,Integer.parseInt(allNumber))));

        }
        //年级，年级人数
        List grade = new ArrayList();
        List gradeNumber = new ArrayList();
        String xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        List<Map> gradeNumberList = shaichaStudentDao.gradeNumber(school, CityName, AreaName, checkdate);
        for(Map map : gradeNumberList){
            grade.add(map.get("grade")==null ? "" :xueBu(xueBu,map.get("grade").toString()));
            gradeNumber.add(map.get("num"));
        }

        listMap.put("grade",grade);
        listMap.put("gradeNumber",gradeNumber);
        listMap.put("zxjsLv",zxjsLv);
        listMap.put("lcqqLv",lcqqLv);
        listMap.put("jxjsLv",jxjsLv);
        listMap.put("myopia",myopia);
        listMap.put("everyTime",everyTime);
        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+"dataTwo",listMap);
        return listMap;
    }

    @Override
    public List<Map> dataThree(String school, String CityName, String AreaName, String checkdate) {
        List<Map> studentList = shaichaStudentDao.studentList(school, CityName, AreaName, checkdate);
        String xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        for (int a = 0; a<studentList.size();a++){
            Map student = studentList.get(a);
            String grade = student.get("grade")==null ? "" :xueBu(xueBu,student.get("grade").toString());
            studentList.get(a).put("grade",grade);
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataThree",studentList);
        return studentList;
    }

    @Override
    public List<Map> dataFour(String school, String CityName, String AreaName, String checkdate) {
        String xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        List<Map> paiMing = shaichaStudentDao.paiMing(school, CityName, AreaName, checkdate);
        for (int b = 0;b<paiMing.size();b++){
            Map gradeClass = paiMing.get(b);
            String grade = gradeClass.get("年级")==null?"":xueBu(xueBu,gradeClass.get("年级").toString());
            paiMing.get(b).put("gradeClass",grade+gradeClass.get("班级"));
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataFour",paiMing);
        return paiMing;
    }

    @Override
    public List<Map> dataFive(String school, String CityName, String AreaName, String checkdate) {
        String xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        List<Map> gradeLv = shaichaStudentDao.gradeLv(school, CityName, AreaName, checkdate);
        for (int i = 0; i <gradeLv.size() ; i++) {
            Map map = gradeLv.get(i);
            gradeLv.get(i).put("grade",map.get("grade")==null?"":xueBu(xueBu,map.get("grade").toString()));
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataFive",gradeLv);
        return gradeLv;
    }


    /**
     * 求率，保留3位小数  a除以b
     * @param a
     * @param b
     * @return
     */
    public static Double getLv (Integer a,Integer b){
        BigDecimal lc = new BigDecimal((float) a / b);
        return lc.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()*100;
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

    /**
     * 判断 学部 年级
     * @param xueBu
     * @param grade
     * @return
     */
    public static String xueBu(String xueBu,String grade){
        switch (xueBu){
            case "小学" :
                return xiaoXue(grade);
            case "初中" :
                return chuZhong(grade);
        }
        return grade;
    }
    /**
     * 判断 小学部 年级
     * @param grade
     * @return
     */
    public static String xiaoXue (String grade){
        switch (grade){
            case "小学2019级" :
                return "二年级";
            case "小学2020级" :
                return "一年级";
            case "2015级" :
                return "六年级";
            case "2016级" :
                return "五年级";
            case "2017级" :
                return "四年级";
            case "2018级" :
                return "三年级";
            case "2019级" :
                return "二年级";
            case "2020级" :
                return "一年级";
        }
        return grade;
    }

    /**
     * 判断 初中部 年级
     * @param grade
     * @return
     */
    public static String chuZhong (String grade){
        switch (grade){
            case "初中2019级" :
                return "初一";
            case "初中2018级" :
                return "初二";
            case "初中2017级" :
                return "初三";
            case "九年级" :
                return "初三";
            case "八年级" :
                return "初二";
            case "七年级" :
                return "初一";
        }
        return grade;
    }

    /**
     * 保留小数
     * @param ff
     * @return
     */
    public static String format(Double ff){
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(ff);
    }
}
