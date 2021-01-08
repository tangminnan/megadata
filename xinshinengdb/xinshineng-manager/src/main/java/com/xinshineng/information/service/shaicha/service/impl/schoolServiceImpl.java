package com.xinshineng.information.service.shaicha.service.impl;

import com.xinshineng.common.utils.ShiroUtils;
import com.xinshineng.common.utils.TimeUtils;
import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.domain.yanke.ResultDiopterDO;
import com.xinshineng.information.service.shaicha.service.schoolService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class schoolServiceImpl implements schoolService {
    @Autowired
    private ShaichaStudentDao shaichaStudentDao;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, String> dataOne(String school, String CityName, String AreaName, String checkdate,String checkType) {
        Map<String, String> map = new HashMap<>();
        Integer schoolAllNumber,schoolThisNumber,illNumber;
        Integer manNumber,wumanNumber;
        if ("sc".equals(checkType)) {
            schoolAllNumber = shaichaStudentDao.schoolAllNumber(school, CityName, AreaName);
            schoolThisNumber = shaichaStudentDao.schoolThisNumber(school, CityName, AreaName, checkdate);
            illNumber = shaichaStudentDao.illNumber(school, CityName, AreaName, checkdate);
            manNumber = shaichaStudentDao.sexThisNumber(school, CityName, AreaName, checkdate, 1);
            wumanNumber = shaichaStudentDao.sexThisNumber(school, CityName, AreaName, checkdate, 2);
        }else {
            schoolAllNumber = shaichaStudentDao.schoolAllNumberld(school, CityName, AreaName);
            schoolThisNumber = shaichaStudentDao.schoolThisNumberld(school, CityName, AreaName, checkdate);
            illNumber = shaichaStudentDao.illNumberld(school, CityName, AreaName, checkdate);
            manNumber = shaichaStudentDao.sexThisNumberld(school, CityName, AreaName, checkdate, 1);
            wumanNumber = shaichaStudentDao.sexThisNumberld(school, CityName, AreaName, checkdate, 2);
        }
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
        map.put("illLv",format(illLv));
        if (manNumber>wumanNumber){
            map.put("man",bl.get(1).toString() +" : "+ bl.get(0).toString());
        }else {
            map.put("man",bl.get(0).toString() +":"+bl.get(1).toString());
        }
        map.put("school",school);
        map.put("checkdate",checkdate);
        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+checkType+"dataOne",map);
        return map;
    }

    @Override
    public Map<String, List> dataTwo(String school, String CityName, String AreaName,String checkdate,String checkType) {
        Map<String, List> listMap = new HashMap<>();
        List zxjsLv = new ArrayList();
        List everyTime = new ArrayList();
        List myopia = new ArrayList();
        List lcqqLv = new ArrayList();
        List jxjsLv = new ArrayList();
        List<Map> everyCheck;
        if ("sc".equals(checkType)){
            everyCheck = shaichaStudentDao.everyCheck(school, CityName, AreaName);
        }else {
            everyCheck = shaichaStudentDao.everyCheckld(school, CityName, AreaName);
        }
        //轻度，中度，高度，正常
        Integer mildNumber,moderateNumber,highlyNumber,thisNumber1;
        if ("sc".equals(checkType)){
            mildNumber = shaichaStudentDao.mildNumber(school, CityName, AreaName, checkdate);
            moderateNumber = shaichaStudentDao.moderateNumber(school, CityName, AreaName, checkdate);
            highlyNumber = shaichaStudentDao.highlyNumber(school, CityName, AreaName, checkdate);
            thisNumber1 = shaichaStudentDao.schoolThisNumber(school, CityName, AreaName, checkdate);
        }else {
            mildNumber = shaichaStudentDao.mildNumberld(school, CityName, AreaName, checkdate);
            moderateNumber = shaichaStudentDao.moderateNumberld(school, CityName, AreaName, checkdate);
            highlyNumber = shaichaStudentDao.highlyNumberld(school, CityName, AreaName, checkdate);
            thisNumber1 = shaichaStudentDao.schoolThisNumberld(school, CityName, AreaName, checkdate);
        }
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
            Integer thisNumber,lcqqNumber,jxjsNumber;
            if ("sc".equals(checkType)){
                thisNumber = shaichaStudentDao.illNumber(school, CityName, AreaName, checkTime);
                lcqqNumber = shaichaStudentDao.lcqqNumber(school, CityName, AreaName, checkTime);
                jxjsNumber = shaichaStudentDao.jxjsNumber(school, CityName, AreaName, checkTime);
            }else {
                thisNumber = shaichaStudentDao.illNumberld(school, CityName, AreaName, checkTime);
                lcqqNumber = shaichaStudentDao.lcqqNumberld(school, CityName, AreaName, checkTime);
                jxjsNumber = shaichaStudentDao.jxjsNumberld(school, CityName, AreaName, checkTime);
            }
            jxjsLv.add(format(getLv(jxjsNumber,Integer.parseInt(allNumber))));
            zxjsLv.add(format(getLv(thisNumber,Integer.parseInt(allNumber))));
            lcqqLv.add(format(getLv(lcqqNumber,Integer.parseInt(allNumber))));
        }
        //年级，年级人数
        List grade = new ArrayList();
        List gradeNumber = new ArrayList();
        String xueBu;
        List<Map> gradeNumberList;
        if ("sc".equals(checkType)){
            xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
            gradeNumberList = shaichaStudentDao.gradeNumber(school, CityName, AreaName, checkdate);
        }else {
            xueBu = shaichaStudentDao.getXueBuld(school, CityName, AreaName, checkdate);
            gradeNumberList = shaichaStudentDao.gradeNumberld(school, CityName, AreaName, checkdate);
        }
        for(Map map : gradeNumberList){
            grade.add(map.get("name")==null ? "" :xueBu(xueBu,map.get("name").toString()));
            gradeNumber.add(map.get("value"));
        }

        listMap.put("grade",grade);
        listMap.put("gradeNumber",gradeNumber);
        listMap.put("zxjsLv",zxjsLv);
        listMap.put("lcqqLv",lcqqLv);
        listMap.put("jxjsLv",jxjsLv);
        listMap.put("myopia",myopia);
        listMap.put("everyTime",everyTime);
        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+checkType+"dataTwo",listMap);
        return listMap;
    }

    @Override
    public List<Map> dataThree(String school, String CityName, String AreaName, String checkdate,String checkType) {
        List<Map> studentList;
        String xueBu;
        if ("sc".equals(checkType)) {
            studentList = shaichaStudentDao.studentList(school, CityName, AreaName, checkdate);
            xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        }else {
            studentList = shaichaStudentDao.studentListld(school, CityName, AreaName, checkdate);
            xueBu = shaichaStudentDao.getXueBuld(school, CityName, AreaName, checkdate);
        }
        for (int a = 0; a<studentList.size();a++){
            Map student = studentList.get(a);
            String grade = student.get("grade")==null ? "" :xueBu(xueBu,student.get("grade").toString());
            studentList.get(a).put("grade",grade);
            studentList.get(a).put("checkdate",checkdate);
            studentList.get(a).put("checkType",checkType);
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+checkType+"dataThree",studentList);
        return studentList;
    }

    @Override
    public List<Map> dataFour(String school, String CityName, String AreaName, String checkdate,String checkType) {
        String xueBu;
        List<Map> paiMing;
        if ("sc".equals(checkType)) {
            xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
            paiMing = shaichaStudentDao.paiMing(school, CityName, AreaName, checkdate);
        }else {
            xueBu = shaichaStudentDao.getXueBuld(school, CityName, AreaName, checkdate);
            paiMing = shaichaStudentDao.paiMingld(school, CityName, AreaName, checkdate);
        }
        for (int b = 0;b<paiMing.size();b++){
            Map gradeClass = paiMing.get(b);
            String grade = gradeClass.get("年级")==null?"":xueBu(xueBu,gradeClass.get("年级").toString());
            paiMing.get(b).put("gradeClass",grade+gradeClass.get("班级"));
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+checkType+"dataFour",paiMing);
        return paiMing;
    }

    @Override
    public List<Map> dataFive(String school, String CityName, String AreaName, String checkdate,String checkType) {
        String xueBu;
        List<Map> gradeLv;
        if ("sc".equals(checkType)) {
            xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
            gradeLv = shaichaStudentDao.gradeLv(school, CityName, AreaName, checkdate);
        }else {
            xueBu = shaichaStudentDao.getXueBuld(school, CityName, AreaName, checkdate);
            gradeLv = shaichaStudentDao.gradeLvld(school, CityName, AreaName, checkdate);
        }
        for (int i = 0; i <gradeLv.size() ; i++) {
            Map map = gradeLv.get(i);
            gradeLv.get(i).put("grade",map.get("grade")==null?"":xueBu(xueBu,map.get("grade").toString()));
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+checkType+"dataFive",gradeLv);
        return gradeLv;
    }

    @Override
    public List<Map> dataSix(String school, String CityName, String AreaName, String checkdate,String checkType) {
        String xueBu;
        List<Map> gradeNumberList;
        if ("sc".equals(checkType)) {
            xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
            gradeNumberList = shaichaStudentDao.gradeNumber(school, CityName, AreaName, checkdate);
        }else {
            xueBu = shaichaStudentDao.getXueBuld(school, CityName, AreaName, checkdate);
            gradeNumberList = shaichaStudentDao.gradeNumberld(school, CityName, AreaName, checkdate);
        }
        for(int s = 0;s<gradeNumberList.size();s++){
            Map map = gradeNumberList.get(s);
            gradeNumberList.get(s).put("name",map.get("name")==null ? "" :xueBu(xueBu,map.get("name").toString()));
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+checkType+"dataSix",gradeNumberList);
        return gradeNumberList;
    }

    @Override
    public Map gerenAdvice(String name, String idCard, String checkdate,String age,String checkType) {
        Map<String, Object> params = new HashMap<String, Object>();
        if ("sc".equals(checkType)) {
            String maxcheckdate = shaichaStudentDao.maxcheckdate(name, idCard);
            Map advice = shaichaStudentDao.advice(name, idCard, maxcheckdate);
            Double luoyanr = Double.parseDouble(advice.get("luoyanr").toString());
            Double luoyanl = Double.parseDouble(advice.get("luoyanl").toString());
            Double daijingr = Double.parseDouble(advice.get("daijingr") == null ? "0.0" : advice.get("daijingr").toString());
            Double daijingl = Double.parseDouble(advice.get("daijingl") == null ? "0.0" : advice.get("daijingl").toString());
            Double dengxiaoqiujingr = (double) advice.get("dengxiaoqiujingr");
            Double dengxiaoqiujingl = (double) advice.get("dengxiaoqiujingl");
            Double zhujingr = advice.get("zhujingr") == null ? 0.0 : (double) advice.get("zhujingr");
            Double zhujingl = advice.get("zhujingl") == null ? 0.0 : (double) advice.get("zhujingl");
            advice(params, luoyanr, luoyanl, dengxiaoqiujingr, dengxiaoqiujingl, zhujingr, zhujingl, daijingr, daijingl);
            return params;
        }else {
            String maxcheckdate = shaichaStudentDao.maxcheckdateld(name, idCard);
            Map advice = shaichaStudentDao.adviceld(name, idCard, maxcheckdate);
            Double daijingr = Double.parseDouble(advice.get("daijingr") == null ? "0.0" : advice.get("daijingr").toString());
            Double daijingl = Double.parseDouble(advice.get("daijingl") == null ? "0.0" : advice.get("daijingl").toString());
            Double dengxiaoqiujingr = (double) advice.get("dengxiaoqiujingr");
            Double dengxiaoqiujingl = (double) advice.get("dengxiaoqiujingl");
            Double zhujingr = advice.get("zhujingr") == null ? 0.0 : (double) advice.get("zhujingr");
            Double zhujingl = advice.get("zhujingl") == null ? 0.0 : (double) advice.get("zhujingl");
            adviceld(params,Integer.parseInt(age),dengxiaoqiujingr,dengxiaoqiujingl,zhujingr,zhujingl,daijingr,daijingl);
            return params;
        }
    }

    @Override
    public List<Map> table(String name, String idCard,String checkType,String age) {
        List<Map> table;
        if ("sc".equals(checkType)) {
            table = shaichaStudentDao.table(name, idCard);
        }else {
            table = shaichaStudentDao.tableld(name, idCard);
        }
        for (int i = 0;i<table.size();i++) {
            table.get(i).put("checkType",checkType);
            table.get(i).put("age",age);
        }
        return table;
    }

    @Override
    public Map report(String name, String idCard, String checkdate,String checkType) {
        Map report = shaichaStudentDao.report(name, idCard, checkdate);
        Map<String, Object> params = new HashMap<String, Object>();
        //基本信息获取
        params.put("school", report.get("school"));
        params.put("grade",report.get("grade"));
        params.put("studentClass",report.get("student_class"));
        params.put("studentName",report.get("student_name"));
        params.put("studentSex", report.get("student_sex")==null?"":(int)report.get("student_sex")==1? "男":"女");
        params.put("lastCheckTime", new SimpleDateFormat("yyyy-MM-dd").format(report.get("last_check_time")));
        DecimalFormat df = new DecimalFormat("0.00");

        //视力检查结果获取
        String nakedFarvisionOd="";
        String nakedFarvisionOs="";
        String correctionFarvisionOd="5.0";
        String correctionFarvisionOs="5.0";

        nakedFarvisionOd=report.get("naked_farvision_od")==null?"":report.get("naked_farvision_od").toString();
        nakedFarvisionOs=report.get("naked_farvision_os")==null?"":report.get("naked_farvision_os").toString();
        correctionFarvisionOd=report.get("correction_farvision_od")==null?"":report.get("correction_farvision_od").toString();
        correctionFarvisionOs=report.get("correction_farvision_os")==null?"":report.get("correction_farvision_os").toString();

        params.put("nakedFarvisionOd",zhuanhuan1(nakedFarvisionOd)==""?"":zhuanhuan1(nakedFarvisionOd));
        params.put("nakedFarvisionOs",zhuanhuan1(nakedFarvisionOs)==""?"":zhuanhuan1(nakedFarvisionOs));
        params.put("glassvisionOd",zhuanhuan1(correctionFarvisionOd)==""?"":zhuanhuan1(correctionFarvisionOd));
        params.put("glassvisionOs",zhuanhuan1(correctionFarvisionOd)==""?"":zhuanhuan1(correctionFarvisionOs));

        //电脑验光结果(左眼)
        double dengxiaoqiujingL = 0.0,dengxiaoqiujingR=0.0;
        String diopterSL="";
        if(report.get("diopter_s_os")!=null){
            diopterSL = df.format(zhuanhuan(report.get("diopter_s_os").toString()));
            if(Double.valueOf(diopterSL)>0){
                diopterSL="+"+diopterSL;
            }
        }
        params.put("diopterSL",diopterSL);
        params.put("diopterCL",report.get("diopter_c_os")==null?"":df.format(zhuanhuan(report.get("diopter_c_os").toString())));
        params.put("diopterAL",report.get("diopter_a_os")==null?"":zhuanhuan(report.get("diopter_a_os").toString()));
        dengxiaoqiujingL=report.get("dengxiaoqiujingl")==null?0.0:(double)report.get("dengxiaoqiujingl");
        double zhujingqL = report.get("diopter_c_os") == null ? 0.0 : (double)report.get("diopter_c_os");
        //电脑验光结果(右眼)
        String diopterSR="";
        if(report.get("diopter_s_od")!=null){
            diopterSR = df.format(zhuanhuan(report.get("diopter_s_od").toString()));
            if(Double.valueOf(diopterSR)>0){
                diopterSR="+"+diopterSR;
            }
        }
        params.put("diopterSR",diopterSR);
        params.put("diopterCR",report.get("diopter_c_od")==null?"":df.format(zhuanhuan(report.get("diopter_c_od").toString())));
        params.put("diopterAR",report.get("diopter_a_od")==null?"":zhuanhuan(report.get("diopter_a_od").toString()));
        dengxiaoqiujingR=report.get("dengxiaoqiujingr")==null?0.0:(double)report.get("dengxiaoqiujingr");
        double zhujingqR = report.get("diopter_c_od") == null ? 0.0 : (double)report.get("diopter_c_od");

        //眼轴长度数据拼装
        params.put("secondCheckOd",report.get("first_check_od")==null?"":zhuanhuan(report.get("first_check_od").toString()));
        params.put("secondCheckOs", report.get("first_check_os")==null?"":zhuanhuan(report.get("first_check_os").toString()));

        //角膜验光拼装
        params.put("cornealMmr1R",report.get("corneal_mm_od_k1")==null?"0":zhuanhuan(report.get("corneal_mm_od_k1")));
        params.put("cornealDr1R", report.get("corneal_d_od_k1")==null?"0":report.get("corneal_d_od_k1"));
        params.put("cornealMmr2R",report.get("orneal_mm_od_k2")==null?"0":zhuanhuan(report.get("orneal_mm_od_k2")));
        params.put("cornealDr2R", report.get("corneal_d_od_k2")==null?"0":report.get("corneal_d_od_k2"));
        params.put("cornealMmr1L",report.get("corneal_mm_os_k1")==null?"0":zhuanhuan(report.get("corneal_mm_os_k1")));
        params.put("cornealDr1L", report.get("corneal_d_os_k1")==null?"0":report.get("corneal_d_os_k1"));
        params.put("cornealMmr2L",report.get("corneal_mm_os_k2")==null?"0":zhuanhuan(report.get("corneal_mm_os_k2")));
        params.put("cornealDr2L", report.get("corneal_d_os_k2")==null?"0":report.get("corneal_d_os_k2"));
        double od=0.0,os=0.0;
        if(!StringUtils.isBlank(nakedFarvisionOd) && isDouble(nakedFarvisionOd)){
            od=Double.parseDouble(nakedFarvisionOd);
        }
        if(!StringUtils.isBlank(nakedFarvisionOs) && isDouble(nakedFarvisionOs)){
            os=Double.parseDouble(nakedFarvisionOs);
        }
        double yuanjingshiliL=0,yuanjingshiliR=0;//原镜视力
        if(!StringUtils.isBlank(correctionFarvisionOd) && isDouble(correctionFarvisionOd)){
            yuanjingshiliR=Double.parseDouble(correctionFarvisionOd);
        }
        if(!StringUtils.isBlank(correctionFarvisionOs) && isDouble(correctionFarvisionOs)){
            yuanjingshiliL=Double.parseDouble(correctionFarvisionOs);
        }
        advice(params,od,os,dengxiaoqiujingR,dengxiaoqiujingL,zhujingqR,zhujingqL,yuanjingshiliR,yuanjingshiliL);
        return params;
    }

    @Override
    public Map reportld(String name, String idCard, String checkdate, String checkType,String age) {
        Map report = shaichaStudentDao.reportld(name, idCard, checkdate);
        Map<String, Object> params = new HashMap<String, Object>();
        //基本信息获取
        params.put("school", report.get("school"));
        params.put("grade",report.get("grade"));
        params.put("studentClass",report.get("student_class"));
        params.put("studentName",report.get("student_name"));
        params.put("studentSex", report.get("student_sex")==null?"":(int)report.get("student_sex")==1? "男":"女");
        params.put("lastCheckTime", new SimpleDateFormat("yyyy-MM-dd").format(report.get("last_check_time")));
        params.put("height",report.get("height")==null?"":report.get("height"));
        params.put("weight",report.get("weight")==null?"":report.get("weight"));
        DecimalFormat df = new DecimalFormat("0.00");

        //视力检查结果获取
        String nakedFarvisionOd="";
        String nakedFarvisionOs="";
        String correctionFarvisionOd="5.0";
        String correctionFarvisionOs="5.0";

        nakedFarvisionOd=report.get("naked_farvision_od")==null?"":report.get("naked_farvision_od").toString();
        nakedFarvisionOs=report.get("naked_farvision_os")==null?"":report.get("naked_farvision_os").toString();
        correctionFarvisionOd=report.get("correction_farvision_od")==null?"":report.get("correction_farvision_od").toString();
        correctionFarvisionOs=report.get("correction_farvision_os")==null?"":report.get("correction_farvision_os").toString();

        params.put("nakedFarvisionOd",zhuanhuan1(nakedFarvisionOd)==""?"":zhuanhuan1(nakedFarvisionOd));
        params.put("nakedFarvisionOs",zhuanhuan1(nakedFarvisionOs)==""?"":zhuanhuan1(nakedFarvisionOs));
        params.put("glassvisionOd",zhuanhuan1(correctionFarvisionOd)==""?"":zhuanhuan1(correctionFarvisionOd));
        params.put("glassvisionOs",zhuanhuan1(correctionFarvisionOd)==""?"":zhuanhuan1(correctionFarvisionOs));

        //眼轴长度数据拼装
        params.put("secondCheckOd",report.get("first_check_od")==null?"":zhuanhuan(report.get("first_check_od").toString()));
        params.put("secondCheckOs", report.get("first_check_os")==null?"":zhuanhuan(report.get("first_check_os").toString()));

        //散瞳前电脑验光结果(左眼)
        String diopterSL="";
        if(report.get("diopter_s_os_first")!=null){
            diopterSL = df.format(zhuanhuan(report.get("diopter_s_os_first").toString()));
            if(Double.valueOf(diopterSL)>0){
                diopterSL="+"+diopterSL;
            }
        }
        params.put("diopterSL",diopterSL);
        params.put("diopterCL",report.get("diopter_c_os_first")==null?"":df.format(zhuanhuan(report.get("diopter_c_os_first").toString())));
        params.put("diopterAL",report.get("diopter_a_os_first")==null?"":zhuanhuan(report.get("diopter_a_os_first").toString()));
        double zhujingqL = report.get("diopter_c_os_first") == null ? 0.0 : (double)report.get("diopter_c_os_first");
        //散瞳前电脑验光结果(右眼)
        String diopterSR="";
        if(report.get("diopter_s_od_first")!=null){
            diopterSR = df.format(zhuanhuan(report.get("diopter_s_od_first").toString()));
            if(Double.valueOf(diopterSR)>0){
                diopterSR="+"+diopterSR;
            }
        }
        params.put("diopterSR",diopterSR);
        params.put("diopterCR",report.get("diopter_c_od_first")==null?"":df.format(zhuanhuan(report.get("diopter_c_od_first").toString())));
        params.put("diopterAR",report.get("diopter_a_od_first")==null?"":zhuanhuan(report.get("diopter_a_od_first").toString()));
        double zhujingqR = report.get("diopter_c_od_first") == null ? 0.0 : (double)report.get("diopter_c_od_first");
        /**
         * 散瞳后(球镜、柱镜、轴位) 数据获取
         */
        String hdiopterSR="";
        if(report.get("diopter_s_od_second")!=null){
            hdiopterSR = df.format(zhuanhuan(report.get("diopter_s_od_second").toString()));
            if(Double.valueOf(hdiopterSR)>0){
                hdiopterSR="+"+hdiopterSR;
            }
        }
        String hdiopterSL="";
        if(report.get("diopter_s_os_second")!=null){
            hdiopterSL = df.format(zhuanhuan(report.get("diopter_s_os_second").toString()));
            if(Double.valueOf(hdiopterSL)>0){
                hdiopterSL="+"+hdiopterSL;
            }
        }
        double hdengxiaoqiujingL = 0.0, hdengxiaoqiujingR = 0.0;//散瞳后验光等效球镜
        params.put("hdiopterSL", hdiopterSL);
        params.put("hdiopterCL", report.get("diopter_c_os_second")==null?"":df.format(zhuanhuan(report.get("diopter_c_os_second").toString())));
        params.put("hdiopterAL", report.get("diopter_a_os_second")==null?"":zhuanhuan(report.get("diopter_a_os_second").toString()));
        hdengxiaoqiujingL = report.get("dengxiaoqiujing_os_second") == null ? 0.0 : (double)report.get("dengxiaoqiujing_os_second");

        params.put("hdiopterSR", hdiopterSR);
        params.put("hdiopterCR", report.get("diopter_c_od_second")==null?"":df.format(zhuanhuan(report.get("diopter_c_od_second").toString())));
        params.put("hdiopterAR", report.get("diopter_a_od_second")==null?"":zhuanhuan(report.get("diopter_a_od_second").toString()));
        hdengxiaoqiujingR = report.get("dengxiaoqiujing_od_second") == null ? 0.0 : (double)report.get("dengxiaoqiujing_od_second");
        /**
         *  散瞳前（K1、K1轴位、K2、K2轴位) 数据获取
         */
        params.put("cornealMmr1R",report.get("corneal_mm_od_first_K1")==null?"0":zhuanhuan(report.get("corneal_mm_od_first_K1")));
        params.put("cornealDr1R", report.get("corneal_d_od_first_K1")==null?"0":report.get("corneal_d_od_first_K1"));
        params.put("cornealMmr2R",report.get("corneal_mm_od_first_K2")==null?"0":zhuanhuan(report.get("corneal_mm_od_first_K2")));
        params.put("cornealDr2R", report.get("corneal_d_od_first_K2")==null?"0":report.get("corneal_d_od_first_K2"));
        params.put("cornealMmr1L",report.get("corneal_mm_os_first_K1")==null?"0":zhuanhuan(report.get("corneal_mm_os_first_K1")));
        params.put("cornealDr1L", report.get("corneal_d_os_first_K1")==null?"0":report.get("corneal_d_os_first_K1"));
        params.put("cornealMmr2L",report.get("corneal_mm_os_first_K2")==null?"0":zhuanhuan(report.get("corneal_mm_os_first_K2")));
        params.put("cornealDr2L", report.get("corneal_d_os_first_K2")==null?"0":report.get("corneal_d_os_first_K2"));

        double yuanjingshiliL=0,yuanjingshiliR=0;//原镜视力
        if(!StringUtils.isBlank(correctionFarvisionOd) && isDouble(correctionFarvisionOd)){
            yuanjingshiliR=Double.parseDouble(correctionFarvisionOd);
        }
        if(!StringUtils.isBlank(correctionFarvisionOs) && isDouble(correctionFarvisionOs)){
            yuanjingshiliL=Double.parseDouble(correctionFarvisionOs);
        }
        adviceld(params,Integer.parseInt(age),hdengxiaoqiujingR,hdengxiaoqiujingL,zhujingqR,zhujingqL,yuanjingshiliR,yuanjingshiliL);
        return params;
    }

    @Override
    public Map<String,List> echarts(String name, String idCard,String checkType) {
        List<Map> yanzhou,luoyan,dengxiaoqiujing;
        if ("sc".equals(checkType)) {
            yanzhou = shaichaStudentDao.yanzhou(name, idCard);
            luoyan = shaichaStudentDao.luoyan(name, idCard);
            dengxiaoqiujing = shaichaStudentDao.dengxiaoqiujing(name, idCard);
        }else {
            yanzhou = shaichaStudentDao.yanzhould(name, idCard);
            luoyan = shaichaStudentDao.luoyanld(name, idCard);
            dengxiaoqiujing = shaichaStudentDao.dengxiaoqiujingld(name, idCard);
        }
        fixList(yanzhou);
        fixList(luoyan);
        fixList(dengxiaoqiujing);
        Map<String,List> map = new HashMap<>();
        List yanzhour = new ArrayList();
        List yanzhoul = new ArrayList();
        List yanzhoutime = new ArrayList();
        List luoyanr = new ArrayList();
        List luoyanl = new ArrayList();
        List luoyantime = new ArrayList();
        List dengxiaoqiujingr = new ArrayList();
        List dengxiaoqiujingl = new ArrayList();
        List dengxiaoqiujingtime = new ArrayList();
        for (int i = 3; i >=0 ; i--) {
            luoyanr.add(luoyan.get(i).get("youyan"));
            luoyanl.add(luoyan.get(i).get("zuoyan"));
            luoyantime.add(luoyan.get(i).get("checkdate"));
        }
        for (int i = 3; i >=0 ; i--) {
            yanzhour.add(yanzhou.get(i).get("youyan"));
            yanzhoul.add(yanzhou.get(i).get("zuoyan"));
            yanzhoutime.add(yanzhou.get(i).get("checkdate"));
        }
        for (int i = 3; i >=0 ; i--) {
            dengxiaoqiujingr.add(dengxiaoqiujing.get(i).get("youyan"));
            dengxiaoqiujingl.add(dengxiaoqiujing.get(i).get("zuoyan"));
            dengxiaoqiujingtime.add(dengxiaoqiujing.get(i).get("checkdate"));
        }
        map.put("yanzhour",yanzhour);
        map.put("yanzhoul",yanzhoul);
        map.put("yanzhoutime",yanzhoutime);
        map.put("dengxiaoqiujingr",dengxiaoqiujingr);
        map.put("dengxiaoqiujingl",dengxiaoqiujingl);
        map.put("dengxiaoqiujingtime",dengxiaoqiujingtime);
        map.put("luoyanr",luoyanr);
        map.put("luoyanl",luoyanl);
        map.put("luoyantime",luoyantime);
        return map;
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

    /**
     * 散瞳前输出医生建议
     * @param params
     * @param od
     * @param os
     * @param dengxiaoqiujingR
     * @param dengxiaoqiujingL
     * @param zhujingqr
     * @param zhujingqL
     * @param yuanjingshiliR
     * @param yuanjingshiliL
     */
    public static void advice (Map<String, Object> params,Double od,Double os,Double dengxiaoqiujingR,Double dengxiaoqiujingL,Double zhujingqr,Double zhujingqL,Double yuanjingshiliR,Double yuanjingshiliL){
        if (od >= 5.0 && dengxiaoqiujingR > 0.75 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "视力目前正常，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }
        if (os >= 5.0 && dengxiaoqiujingL > 0.75 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "视力目前正常，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }

        if (od >= 5.0 && dengxiaoqiujingR > 0.75 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "视力目前正常，散光，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }
        if (os >= 5.0 && dengxiaoqiujingL > 0.75 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "视力目前正常，散光，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }

        if (od >= 5.0 && dengxiaoqiujingR >= -0.5 && dengxiaoqiujingR <= 0.75 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "视力目前正常，近视临床前期。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }
        if (os >= 5.0 && dengxiaoqiujingL >= -0.5 && dengxiaoqiujingL <= 0.75 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "视力目前正常，近视临床前期。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }

        if (od >= 5.0 && dengxiaoqiujingR >= -0.5 && dengxiaoqiujingR <= 0.75 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "视力目前正常，散光，近视临床前期。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }
        if (os >= 5.0 && dengxiaoqiujingL >= -0.5 && dengxiaoqiujingL <= 0.75 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "视力目前正常，散光，近视临床前期。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }

        if (od >= 5.0 && dengxiaoqiujingR < -0.5 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "视力目前正常，属于假性近视。根据目前检查结果，有发生真性近视的可能。建议您到医院进行进一步散瞳检查，以确定是否近期可能发展为近视，并请严格注意用眼卫生，避免长时间近距离持续用眼，采用科学的防控措施，多参加户外活动，建立完善的视觉健康档案，避免假性近视发展为真性近视。");
        }
        if (os >= 5.0 && dengxiaoqiujingL < -0.5 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "视力目前正常，属于假性近视。根据目前检查结果，有发生真性近视的可能。建议您到医院进行进一步散瞳检查，以确定是否近期可能发展为近视，并请严格注意用眼卫生，避免长时间近距离持续用眼，采用科学的防控措施，多参加户外活动，建立完善的视觉健康档案，避免假性近视发展为真性近视。");
        }

        if (od >= 5.0 && dengxiaoqiujingR < -0.5 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "视力目前正常，属于假性近视，散光。根据目前检查结果，有发生真性近视的可能。建议您到医院进行进一步散瞳检查，以确定是否近期可能发展为近视，并请严格注意用眼卫生，避免长时间近距离持续用眼，采用科学的防控措施，多参加户外活动，建立完善的视觉健康档案，避免假性近视发展为真性近视。");
        }
        if (os >= 5.0 && dengxiaoqiujingL < -0.5 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "视力目前正常，属于假性近视，散光。根据目前检查结果，有发生真性近视的可能。建议您到医院进行进一步散瞳检查，以确定是否近期可能发展为近视，并请严格注意用眼卫生，避免长时间近距离持续用眼，采用科学的防控措施，多参加户外活动，建立完善的视觉健康档案，避免假性近视发展为真性近视。");
        }
        if (od < 5.0 && dengxiaoqiujingR >= -0.5 && yuanjingshiliR >= 5.0 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "戴原镜视力正常。请继续佩戴原来的眼镜，遵医嘱定期复查。合理卫生用眼，建议到医院进一步检查，了解远视储备值情况，预防近视的发生。");
        }
        if (os < 5.0 && dengxiaoqiujingL >= -0.5 && yuanjingshiliL >= 5.0 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "戴原镜视力正常。请继续佩戴原来的眼镜，遵医嘱定期复查。合理卫生用眼，建议到医院进一步检查，了解远视储备值情况，预防近视的发生。");
        }

        if (od < 5.0 && dengxiaoqiujingR >= -0.5 && yuanjingshiliR >= 5.0 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "散光，戴原镜视力正常。请继续佩戴原来的眼镜，遵医嘱定期复查。合理卫生用眼，建议到医院进一步检查，了解远视储备值情况，预防近视的发生。");
        }
        if (os < 5.0 && dengxiaoqiujingL >= -0.5 && yuanjingshiliL >= 5.0 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "散光，戴原镜视力正常。请继续佩戴原来的眼镜，遵医嘱定期复查。合理卫生用眼，建议到医院进一步检查，了解远视储备值情况，预防近视的发生。");
        }

        if (od < 5.0 && dengxiaoqiujingR < -0.5 && yuanjingshiliR >= 5.0 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "戴原镜视力正常，近视。请继续佩戴原来的眼镜，遵医嘱定期复查。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展；建议到正规医院，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }
        if (os < 5.0 && dengxiaoqiujingL < -0.5 && yuanjingshiliL >= 5.0 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "戴原镜视力正常，近视。请继续佩戴原来的眼镜，遵医嘱定期复查。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展；建议到正规医院，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }

        if (od < 5.0 && dengxiaoqiujingR < -0.5 && yuanjingshiliR >= 5.0 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "戴原镜视力正常，近视，散光。请继续佩戴原来的眼镜，遵医嘱定期复查。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展；建议到正规医院，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }
        if (os < 5.0 && dengxiaoqiujingL < -0.5 && yuanjingshiliL >= 5.0 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "戴原镜视力正常，近视，散光。请继续佩戴原来的眼镜，遵医嘱定期复查。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展；建议到正规医院，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }

        if (od < 5.0 && dengxiaoqiujingR >= -0.5 && yuanjingshiliR < 5.0 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "视力异常。建议及时到正规医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼部疾病，并及时采取相应治疗措施；若已明确诊断，请遵医嘱及时定期复查，进行科学规范的治疗。");
        }
        if (os < 5.0 && dengxiaoqiujingL >= -0.5 && yuanjingshiliL < 5.0 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "视力异常。建议及时到正规医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼部疾病，并及时采取相应治疗措施；若已明确诊断，请遵医嘱及时定期复查，进行科学规范的治疗。");
        }

        if (od < 5.0 && dengxiaoqiujingR >= -0.5 && yuanjingshiliR < 5.0 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "视力异常，散光。建议及时到正规医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼部疾病，并及时采取相应治疗措施；若已明确诊断，请遵医嘱及时定期复查，进行科学规范的治疗。");
        }
        if (os < 5.0 && dengxiaoqiujingL >= -0.5 && yuanjingshiliL < 5.0 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "视力异常，散光。建议及时到正规医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼部疾病，并及时采取相应治疗措施；若已明确诊断，请遵医嘱及时定期复查，进行科学规范的治疗。");
        }

        if (od < 5.0 && dengxiaoqiujingR < -0.5 && yuanjingshiliR < 5.0 && zhujingqr > -1.0) {
            params.put("ydoctorchubu", "视力下降，近视。建议及时到医院接受近视的详细检查，通过散瞳明确近视的程度并排除其他眼病；若已进行近视矫治，根据检查结果提示，眼镜度数可能需要调整，请及时到医院进行复查，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展。");
        }
        if (os < 5.0 && dengxiaoqiujingL < -0.5 && yuanjingshiliL < 5.0 && zhujingqL > -1.0) {
            params.put("zdoctorchubu", "视力下降，近视。建议及时到医院接受近视的详细检查，通过散瞳明确近视的程度并排除其他眼病；若已进行近视矫治，根据检查结果提示，眼镜度数可能需要调整，请及时到医院进行复查，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展。");
        }

        if (od < 5.0 && dengxiaoqiujingR < -0.5 && yuanjingshiliR < 5.0 && zhujingqr <= -1.0) {
            params.put("ydoctorchubu", "视力下降，近视，散光。建议及时到医院接受近视的详细检查，通过散瞳明确近视的程度并排除其他眼病；若已进行近视矫治，根据检查结果提示，眼镜度数可能需要调整，请及时到医院进行复查，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展。");
        }
        if (os < 5.0 && dengxiaoqiujingL < -0.5 && yuanjingshiliL < 5.0 && zhujingqL <= -1.0) {
            params.put("zdoctorchubu", "视力下降，近视，散光。建议及时到医院接受近视的详细检查，通过散瞳明确近视的程度并排除其他眼病；若已进行近视矫治，根据检查结果提示，眼镜度数可能需要调整，请及时到医院进行复查，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。并请严格注意用眼卫生，避免长时间近距离持续用眼，多参加户外活动，建立完善的视觉健康档案，延缓近视的进展。");
        }
    }

    /**
     * 散瞳后输出医生建议
     * @param resultMap
     * @param birth
     * @param hdengxiaoqiujingR
     * @param hdengxiaoqiujingL
     * @param zhujingqr
     * @param zhujingqL
     * @param yuanjingshiliR
     * @param yuanjingshiliL
     */
    public static void adviceld (Map<String, Object> resultMap,Integer birth,Double hdengxiaoqiujingR,Double hdengxiaoqiujingL,Double zhujingqr,Double zhujingqL,Double yuanjingshiliR,Double yuanjingshiliL){
        if (hdengxiaoqiujingR < -6.0 && zhujingqr > -1.0) {
            resultMap.put("ydoctorchubu", "高度近视。建议及时到医院接受近视的详细检查，进一步详尽眼底检查，排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，减少高度近视的并发症发生。");
        }
        if (hdengxiaoqiujingL < -6.0 && zhujingqL > -1.0) {
            resultMap.put("zdoctorchubu", "高度近视 。建议及时到医院接受近视的详细检查，进一步详尽眼底检查，排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，减少高度近视的并发症发生。");
        }

        if (hdengxiaoqiujingR < -6.0 && zhujingqr <= -1.0) {
            resultMap.put("ydoctorchubu", "高度近视，散光。建议及时到医院接受近视的详细检查，进一步详尽眼底检查，排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，减少高度近视的并发症发生。");
        }
        if (hdengxiaoqiujingL < -6.0 && zhujingqL <= -1.0) {
            resultMap.put("zdoctorchubu", "高度近视，散光。建议及时到医院接受近视的详细检查，进一步详尽眼底检查，排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，减少高度近视的并发症发生。");
        }

        if (hdengxiaoqiujingR >= -6.0 && hdengxiaoqiujingR <= -0.5 && zhujingqr > -1.0) {
            resultMap.put("ydoctorchubu", "近视。 建议及时到医院接受近视的详细检查，进一步明确近视的程度并排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }
        if (hdengxiaoqiujingL >= -6.0 && hdengxiaoqiujingL <= -0.5 && zhujingqL > -1.0) {
            resultMap.put("zdoctorchubu", "近视。 建议及时到医院接受近视的详细检查，进一步明确近视的程度并排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }

        if (hdengxiaoqiujingR >= -6.0 && hdengxiaoqiujingR <= -0.5 && zhujingqr <= -1.0) {
            resultMap.put("ydoctorchubu", "近视，散光。建议及时到医院接受近视的详细检查，进一步明确近视的程度并排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }
        if (hdengxiaoqiujingL >= -6.0 && hdengxiaoqiujingL <= -0.5 && zhujingqL <= -1.0) {
            resultMap.put("zdoctorchubu", "近视，散光。建议及时到医院接受近视的详细检查，进一步明确近视的程度并排除其他眼病，采取科学的方法进行近视的防控或采取相应眼病治疗措施，避免低度近视发展为中度近视，避免中度近视发展为高度近视，减少高度近视的并发症发生。");
        }

        if (hdengxiaoqiujingR > -0.5 && hdengxiaoqiujingR <= 0.75 && zhujingqr > -1.0) {
            resultMap.put("ydoctorchubu", "近视临床前期。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }
        if (hdengxiaoqiujingL > -0.5 && hdengxiaoqiujingL <= 0.75 && zhujingqL > -1.0) {
            resultMap.put("zdoctorchubu", " 近视临床前期。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }

        if (hdengxiaoqiujingR > -0.5 && hdengxiaoqiujingR <= 0.75 && zhujingqr <= -1.0) {
            resultMap.put("ydoctorchubu", "近视临床前期，散光。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }
        if (hdengxiaoqiujingL > -0.5 && hdengxiaoqiujingL <= 0.75 && zhujingqL <= -1.0) {
            resultMap.put("zdoctorchubu", "近视临床前期，散光。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，防控近视的发生，更好地进行近视发生的预警。");
        }

        if (hdengxiaoqiujingR > 0.75 && zhujingqr > -1.0 && ((birth == 3 && yuanjingshiliR >= 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliR >= 4.8) || (birth == 6 && yuanjingshiliR >= 4.9) || (birth >= 7 && yuanjingshiliR >= 5.0))) {
            resultMap.put("ydoctorchubu", "远视，视力达到相应年龄段水平，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }
        if (hdengxiaoqiujingL > 0.75 && zhujingqL > -1.0 && ((birth == 3 && yuanjingshiliL >= 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliL >= 4.8) || (birth == 6 && yuanjingshiliL >= 4.9) || (birth >= 7 && yuanjingshiliL >= 5.0))) {
            resultMap.put("zdoctorchubu", "远视，视力达到相应年龄段水平，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }

        if (hdengxiaoqiujingR > 0.75 && zhujingqr <= -1.0 && ((birth == 3 && yuanjingshiliR >= 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliR >= 4.8) || (birth == 6 && yuanjingshiliR >= 4.9) || (birth >= 7 && yuanjingshiliR >= 5.0))) {
            resultMap.put("ydoctorchubu", "远视，散光，视力达到相应年龄段水平，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }
        if (hdengxiaoqiujingL > 0.75 && zhujingqL <= -1.0 && ((birth == 3 && yuanjingshiliL >= 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliL >= 4.8) || (birth == 6 && yuanjingshiliL >= 4.9) || (birth >= 7 && yuanjingshiliL >= 5.0))) {
            resultMap.put("zdoctorchubu", "远视，散光，视力达到相应年龄段水平，无近视发生。请注意卫生用眼，避免长时间近距离持续用眼，多参加户外活动，建议建立完善的视觉健康档案，更好地进行近视发生的预警。");
        }

        if (hdengxiaoqiujingR > 0.75 && zhujingqr > -1.0 && ((birth == 3 && yuanjingshiliR < 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliR < 4.8) || (birth == 6 && yuanjingshiliR < 4.9) || (birth >= 7 && yuanjingshiliR < 5.0))) {
            resultMap.put("ydoctorchubu", "远视，视力异常。建议及时到医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼病，并及时采取相应治疗措施。");
        }
        if (hdengxiaoqiujingL > 0.75 && zhujingqL > -1.0 && ((birth == 3 && yuanjingshiliL < 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliL < 4.8) || (birth == 6 && yuanjingshiliL < 4.9) || (birth >= 7 && yuanjingshiliL < 5.0))) {
            resultMap.put("zdoctorchubu", "远视，视力异常。建议及时到医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼病，并及时采取相应治疗措施。");
        }

        if (hdengxiaoqiujingR > 0.75 && zhujingqr <= -1.0 && ((birth == 3 && yuanjingshiliR < 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliR < 4.8) || (birth == 6 && yuanjingshiliR < 4.9) || (birth >= 7 && yuanjingshiliR < 5.0))) {
            resultMap.put("ydoctorchubu", "远视，散光。视力异常。建议及时到医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼病，并及时采取相应治疗措施。");
        }
        if (hdengxiaoqiujingL > 0.75 && zhujingqL <= -1.0 && ((birth == 3 && yuanjingshiliL < 4.7) || (birth >= 4 && birth <= 5 && yuanjingshiliL < 4.8) || (birth == 6 && yuanjingshiliL < 4.9) || (birth >= 7 && yuanjingshiliL < 5.0))) {
            resultMap.put("zdoctorchubu", "远视，散光。视力异常。建议及时到医院接受详细检查，明确诊断是否为屈光不正、弱视、斜视、视功能异常以及其他眼病，并及时采取相应治疗措施。");
        }
    }
    /**
     * 将集合补全为4个
     * @param param
     */
    public static void fixList (List<Map> param){
        if (param.size()<4){
            for (int i = param.size();i<4;i++){
                Map map = new HashMap();
                map.put("youyan",0);
                map.put("zuoyan",0);
                SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM");
                try {
                    Date date = sdFormat.parse(param.get(i-1).get("checkdate").toString());
                    date.setMonth(date.getMonth()-6);
                    String format = sdFormat.format(date);
                    map.put("checkdate",format);
                    param.add(i,map);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Object zhuanhuan1(Object s){
        DecimalFormat df1 = new DecimalFormat("0.0");
        String d=null;
        if(s instanceof String){
            if("".equals((String)s))
                return "";
            d = (String)s;
        }
        if(s instanceof Double)
            d = df1.format(s);
        return d;
    }
    private static Object zhuanhuan(Object s){
        Double d=null;
        if(s instanceof String){
            if("".equals((String)s))
                return "";
            d = Double.parseDouble((String)s);
        }
        if(s instanceof Double)
            d = (Double)s;
        if(Math.floor(d)==d)
            return d.intValue();
        return d;
    }
    public boolean isDouble( String s ){

        boolean matches = s.matches("-?[0-9]+.*[0-9]*");

        return matches;

    }
}
