package com.xinshineng.information.service.shaicha.service.impl;

import com.alibaba.fastjson.JSON;
import com.xinshineng.common.utils.JSONUtils;
import com.xinshineng.common.utils.ShiroUtils;
import com.xinshineng.common.utils.TimeUtils;
import com.xinshineng.information.dao.shaicha.ShaichaStudentDao;
import com.xinshineng.information.dao.yanke.StudentDao;
import com.xinshineng.information.domain.yanke.ResultDiopterDO;
import com.xinshineng.information.service.shaicha.service.schoolService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        BigDecimal le;
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
            map.put("man",bl.get(0).toString() +" : "+bl.get(1).toString());
        }
        if ("1 : 0".equals(map.get("man"))) map.put("man","1:1");
        if ("0 : 1".equals(map.get("man"))) map.put("man","1:1");
        map.put("school",school);
        map.put("checkdate",checkdate);
        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+checkType+"dataOne",map);
        return map;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StudentDao liuDiaoDao;
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


//        Map map = everyCheck.get(everyCheck.size() - 1);
/*        List zxycList = new ArrayList();
        List jxycList = new ArrayList();
        List lcycList = new ArrayList();
        for (Map map : everyCheck) {
            String checkTime =  map.get("checkTime").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date date = null;
            try {
                date = sdf.parse(checkTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int month = date.getMonth();
            int winter = 0;
            if (month==9 || month == 10 || month == 11 || month == 0){
                winter=1;
            }else {
                winter=0;
            }


            Integer checkNumber = Integer.parseInt(map.get("checkNumber").toString());
            List<Map<String,Object>> ldataList = new ArrayList<>();
            List<Map<String,Object>> rdataList = new ArrayList<>();
            if ("sc".equals(checkType)){
                if (date.getYear()<2020){
                    List<Map<String,Object>> scYuce = shaichaStudentDao.getScYuCeForOld(checkTime,school, CityName, AreaName);
                    Double dioAL = 0.0;
                    Double dioSL = 0.0;
                    Double dioCL = 0.0;
                    Double dioAR = 0.0;
                    Double dioSR = 0.0;
                    Double dioCR = 0.0;
                    Double corML = 0.0;
                    Double corDL = 0.0;
                    Double corMR = 0.0;
                    Double corDR = 0.0;
                    Double eyeaxisOd = 0.0;
                    Double eyeaxisOs = 0.0;
                    Double preOd = 0.0;
                    Double preOs = 0.0;
                    Double eyeaxis_corneal_L = 0.0;
                    Double eyeaxis_corneal_R = 0.0;
                    for (Map<String, Object> stuMap : scYuce) {
                        if ("塑形镜".equals(stuMap.get("NakedOs"))||"塑形镜".equals(stuMap.get("NakedOd"))){
                            continue;
                        }
                        Map<String,Object> lMap = new HashMap<>();
                        Map<String,Object> rMap = new HashMap<>();
                        Integer id = (Integer) stuMap.get("id");
                        Integer sex = (Integer) stuMap.get("sex");
                        if (sex == 1){
                            sex=1;
                        }
                        if (sex == 2){
                            sex=0;
                        }
                        String lasy_check_time = stuMap.get("checkTime").toString();
                        Integer optId = shaichaStudentDao.getOptIdForOld(id);
                        List<Map<String,Object>> diopterData = shaichaStudentDao.getDiopterDataForOld(optId);

                        for (Map<String, Object> dioMap : diopterData) {
                            String ifRl = dioMap.get("ifRL").toString();
                            if ("L".equals(ifRl)){
                                dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                            }

                            if ("R".equals(ifRl)){
                                dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                            }
                        }
                        List<Map<String,Object>> corData = shaichaStudentDao.getCornealDataForOld(optId);
                        for (Map<String, Object> corMap : corData) {
                            String ifRL = corMap.get("ifRL").toString();
                            if ("L".equals(ifRL)){
                                corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                                corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                            }
                            if ("R".equals(ifRL)){
                                corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                                corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                            }
                        }

                        Map<String,Double> eyeaxisData = shaichaStudentDao.getEyeAxisDataForOld(id);
                        if (eyeaxisData!=null){
                            eyeaxisOd = eyeaxisData.get("od");
                            eyeaxisOs = eyeaxisData.get("os");
                        }
                        Map<String,Double> eyepressureData = shaichaStudentDao.getEyepressureDataForOld(id);

                        if (eyepressureData != null){
                            preOd = eyepressureData.get("preOd");
                            preOs = eyepressureData.get("preOs");
                        }

                        if (eyeaxisOd == 0.0 || corMR ==0.0){
                            eyeaxis_corneal_R = 0.0;
                        }else {
                            eyeaxis_corneal_R = eyeaxisOd/corMR;
                        }
                        if (eyeaxisOs == 0.0 || corML==0.0){
                            eyeaxis_corneal_L = 0.0;
                        }else {
                            eyeaxis_corneal_L = eyeaxisOs/corML;
                        }
                        String age = stuMap.get("age").toString();
                        String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
                        if ("nlp".equals(NakedOs)||"NLP".equals(NakedOs)){
                            NakedOs="0.0";
                        }

                        if (Double.parseDouble(NakedOs)<4.0){
                            NakedOs="0.1";
                        }
                        if (Double.parseDouble(NakedOs)>5.0){
                            NakedOs="1.0";
                        }
                        if ("4.0".equals(NakedOs)){
                            NakedOs="0.1";
                        }

                        if ("4.1".equals(NakedOs)){
                            NakedOs="0.12";
                        }
                        if ("4.2".equals(NakedOs)){
                            NakedOs="0.15";
                        }
                        if ("4.3".equals(NakedOs)){
                            NakedOs="0.2";
                        }
                        if ("4.4".equals(NakedOs)){
                            NakedOs="0.25";
                        }
                        if ("4.5".equals(NakedOs)){
                            NakedOs="0.3";
                        }
                        if ("4.6".equals(NakedOs)){
                            NakedOs="0.4";
                        }
                        if ("4.7".equals(NakedOs)){
                            NakedOs="0.5";
                        }
                        if ("4.8".equals(NakedOs)){
                            NakedOs="0.6";
                        }
                        if ("4.9".equals(NakedOs)){
                            NakedOs="0.8";
                        }
                        if ("5.0".equals(NakedOs)){
                            NakedOs="1.0";
                        }
                        String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
                        if ("nlp".equals(NakedOd)||"NLP".equals(NakedOd)){
                            NakedOd="0.0";
                        }
                        if (Double.parseDouble(NakedOd)<4.0){
                            NakedOd="0.1";
                        }
                        if (Double.parseDouble(NakedOd)>5.0){
                            NakedOd="1.0";
                        }
                        if ("4.0".equals(NakedOd)){
                            NakedOd="0.1";
                        }

                        if ("4.1".equals(NakedOd)){
                            NakedOd="0.12";
                        }
                        if ("4.2".equals(NakedOd)){
                            NakedOd="0.15";
                        }
                        if ("4.3".equals(NakedOd)){
                            NakedOd="0.2";
                        }
                        if ("4.4".equals(NakedOd)){
                            NakedOd="0.25";
                        }
                        if ("4.5".equals(NakedOd)){
                            NakedOd="0.3";
                        }
                        if ("4.6".equals(NakedOd)){
                            NakedOd="0.4";
                        }
                        if ("4.7".equals(NakedOd)){
                            NakedOd="0.5";
                        }
                        if ("4.8".equals(NakedOd)){
                            NakedOd="0.6";
                        }
                        if ("4.9".equals(NakedOd)){
                            NakedOd="0.8";
                        }
                        if ("5.0".equals(NakedOd)){
                            NakedOd="1.0";
                        }
                        lMap.put("student",id+"|L");
                        lMap.put("lasy_check_time",lasy_check_time);
                        lMap.put("studeny_sex",sex);
                        lMap.put("shifou_tangwo",0);
                        lMap.put("zuizhong_tizhi",0);
                        lMap.put("gzr_sm",0);
                        lMap.put("zm_sm",0);
                        lMap.put("zm_swhd",0);
                        lMap.put("zm_mtgz",0);
                        lMap.put("zx_swhd",0);
                        lMap.put("xw_swhd",0);
                        lMap.put("life_farvision",0);
                        lMap.put("naked_farvision",NakedOs);
                        lMap.put("diopter_s1",dioSL);
                        lMap.put("diopter_c1",dioCL);
                        lMap.put("diopter_a1",dioAL);
                        lMap.put("y1_x",stuMap.get("DXQJL"));
                        lMap.put("diopter_s2",0);
                        lMap.put("diopter_c2",0);
                        lMap.put("diopter_a2",0);
                        lMap.put("y2_x",0);
                        lMap.put("corneal_mm",corML);
                        lMap.put("corneal_d",corDL);
                        lMap.put("eyeaxis",eyeaxisOs);
                        lMap.put("eyepressure",preOs);
                        lMap.put("age",age);
                        lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);
                        lMap.put("winter",winter);
                        lMap.put("flag",2);



                        rMap.put("student",id+"|R");
                        rMap.put("lasy_check_time",lasy_check_time);
                        rMap.put("studeny_sex",sex);
                        rMap.put("shifou_tangwo",0);
                        rMap.put("zuizhong_tizhi",0);
                        rMap.put("gzr_sm",0);
                        rMap.put("zm_sm",0);
                        rMap.put("zm_swhd",0);
                        rMap.put("zm_mtgz",0);
                        rMap.put("zx_swhd",0);
                        rMap.put("xw_swhd",0);
                        rMap.put("life_farvision",0);
                        rMap.put("naked_farvision",NakedOd);
                        rMap.put("diopter_s1",dioSR);
                        rMap.put("diopter_c1",dioCR);
                        rMap.put("diopter_a1",dioAR);
                        rMap.put("y1_x",stuMap.get("DXQJR"));
                        rMap.put("diopter_s2",0);
                        rMap.put("diopter_c2",0);
                        rMap.put("diopter_a2",0);
                        rMap.put("y2_x",0);
                        rMap.put("corneal_mm",corMR);
                        rMap.put("corneal_d",corDR);
                        rMap.put("eyeaxis",eyeaxisOd);
                        rMap.put("eyepressure",preOd);
                        rMap.put("age",age);
                        rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
                        rMap.put("winter",winter);
                        rMap.put("flag",2);
                        ldataList.add(lMap);
                        rdataList.add(rMap);
                        continue;
                    }
                }
                if (date.getYear()==2020 && date.getMonth()<5){
                    List<Map<String,Object>> scYuce = shaichaStudentDao.getScYuCeForOld(checkTime,school, CityName, AreaName);
                    Double dioAL = 0.0;
                    Double dioSL = 0.0;
                    Double dioCL = 0.0;
                    Double dioAR = 0.0;
                    Double dioSR = 0.0;
                    Double dioCR = 0.0;
                    Double corML = 0.0;
                    Double corDL = 0.0;
                    Double corMR = 0.0;
                    Double corDR = 0.0;
                    Double eyeaxisOd = 0.0;
                    Double eyeaxisOs = 0.0;
                    Double preOd = 0.0;
                    Double preOs = 0.0;
                    Double eyeaxis_corneal_L = 0.0;
                    Double eyeaxis_corneal_R = 0.0;
                    for (Map<String, Object> stuMap : scYuce) {
                        if ("塑形镜".equals(stuMap.get("NakedOs"))||"塑形镜".equals(stuMap.get("NakedOd"))){
                            continue;
                        }
                        Map<String,Object> lMap = new HashMap<>();
                        Map<String,Object> rMap = new HashMap<>();
                        Integer id = (Integer) stuMap.get("id");
                        Integer sex = (Integer) stuMap.get("sex");
                        if (sex == 1){
                            sex=1;
                        }
                        if (sex == 2){
                            sex=0;
                        }
                        String lasy_check_time = stuMap.get("checkTime").toString();
                        Integer optId = shaichaStudentDao.getOptIdForOld(id);
                        List<Map<String,Object>> diopterData = shaichaStudentDao.getDiopterDataForOld(optId);

                        for (Map<String, Object> dioMap : diopterData) {
                            String ifRl = dioMap.get("ifRL").toString();
                            if ("L".equals(ifRl)){
                                dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                            }

                            if ("R".equals(ifRl)){
                                dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                            }
                        }
                        List<Map<String,Object>> corData = shaichaStudentDao.getCornealDataForOld(optId);
                        for (Map<String, Object> corMap : corData) {
                            String ifRL = corMap.get("ifRL").toString();
                            if ("L".equals(ifRL)){
                                corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                                corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                            }
                            if ("R".equals(ifRL)){
                                corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                                corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                            }
                        }

                        Map<String,Double> eyeaxisData = shaichaStudentDao.getEyeAxisDataForOld(id);
                        if (eyeaxisData!=null){
                            eyeaxisOd = eyeaxisData.get("od");
                            eyeaxisOs = eyeaxisData.get("os");
                        }
                        Map<String,Double> eyepressureData = shaichaStudentDao.getEyepressureDataForOld(id);

                        if (eyepressureData != null){
                            preOd = eyepressureData.get("preOd");
                            preOs = eyepressureData.get("preOs");
                        }

                        if (eyeaxisOd == 0.0 || corMR ==0.0){
                            eyeaxis_corneal_R = 0.0;
                        }else {
                            eyeaxis_corneal_R = eyeaxisOd/corMR;
                        }
                        if (eyeaxisOs == 0.0 || corML==0.0){
                            eyeaxis_corneal_L = 0.0;
                        }else {
                            eyeaxis_corneal_L = eyeaxisOs/corML;
                        }
                        String age = stuMap.get("age").toString();
                        String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();

                        if (Double.parseDouble(NakedOs)<4.0){
                            NakedOs="0.1";
                        }
                        if (Double.parseDouble(NakedOs)>5.0){
                            NakedOs="1.0";
                        }
                        if ("4.0".equals(NakedOs)){
                            NakedOs="0.1";
                        }

                        if ("4.1".equals(NakedOs)){
                            NakedOs="0.12";
                        }
                        if ("4.2".equals(NakedOs)){
                            NakedOs="0.15";
                        }
                        if ("4.3".equals(NakedOs)){
                            NakedOs="0.2";
                        }
                        if ("4.4".equals(NakedOs)){
                            NakedOs="0.25";
                        }
                        if ("4.5".equals(NakedOs)){
                            NakedOs="0.3";
                        }
                        if ("4.6".equals(NakedOs)){
                            NakedOs="0.4";
                        }
                        if ("4.7".equals(NakedOs)){
                            NakedOs="0.5";
                        }
                        if ("4.8".equals(NakedOs)){
                            NakedOs="0.6";
                        }
                        if ("4.9".equals(NakedOs)){
                            NakedOs="0.8";
                        }
                        if ("5.0".equals(NakedOs)){
                            NakedOs="1.0";
                        }
                        String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();

                        if (Double.parseDouble(NakedOd)<4.0){
                            NakedOd="0.1";
                        }
                        if (Double.parseDouble(NakedOd)>5.0){
                            NakedOd="1.0";
                        }
                        if ("4.0".equals(NakedOd)){
                            NakedOd="0.1";
                        }

                        if ("4.1".equals(NakedOd)){
                            NakedOd="0.12";
                        }
                        if ("4.2".equals(NakedOd)){
                            NakedOd="0.15";
                        }
                        if ("4.3".equals(NakedOd)){
                            NakedOd="0.2";
                        }
                        if ("4.4".equals(NakedOd)){
                            NakedOd="0.25";
                        }
                        if ("4.5".equals(NakedOd)){
                            NakedOd="0.3";
                        }
                        if ("4.6".equals(NakedOd)){
                            NakedOd="0.4";
                        }
                        if ("4.7".equals(NakedOd)){
                            NakedOd="0.5";
                        }
                        if ("4.8".equals(NakedOd)){
                            NakedOd="0.6";
                        }
                        if ("4.9".equals(NakedOd)){
                            NakedOd="0.8";
                        }
                        if ("5.0".equals(NakedOd)){
                            NakedOd="1.0";
                        }
                        lMap.put("student",id+"|L");
                        lMap.put("lasy_check_time",lasy_check_time);
                        lMap.put("studeny_sex",sex);
                        lMap.put("shifou_tangwo",0);
                        lMap.put("zuizhong_tizhi",0);
                        lMap.put("gzr_sm",0);
                        lMap.put("zm_sm",0);
                        lMap.put("zm_swhd",0);
                        lMap.put("zm_mtgz",0);
                        lMap.put("zx_swhd",0);
                        lMap.put("xw_swhd",0);
                        lMap.put("life_farvision",0);
                        lMap.put("naked_farvision",NakedOs);
                        lMap.put("diopter_s1",dioSL);
                        lMap.put("diopter_c1",dioCL);
                        lMap.put("diopter_a1",dioAL);
                        lMap.put("y1_x",stuMap.get("DXQJL"));
                        lMap.put("diopter_s2",0);
                        lMap.put("diopter_c2",0);
                        lMap.put("diopter_a2",0);
                        lMap.put("y2_x",0);
                        lMap.put("corneal_mm",corML);
                        lMap.put("corneal_d",corDL);
                        lMap.put("eyeaxis",eyeaxisOs);
                        lMap.put("eyepressure",preOs);
                        lMap.put("age",age);
                        lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);
                        lMap.put("winter",winter);
                        lMap.put("flag",2);



                        rMap.put("student",id+"|R");
                        rMap.put("lasy_check_time",lasy_check_time);
                        rMap.put("studeny_sex",sex);
                        rMap.put("shifou_tangwo",0);
                        rMap.put("zuizhong_tizhi",0);
                        rMap.put("gzr_sm",0);
                        rMap.put("zm_sm",0);
                        rMap.put("zm_swhd",0);
                        rMap.put("zm_mtgz",0);
                        rMap.put("zx_swhd",0);
                        rMap.put("xw_swhd",0);
                        rMap.put("life_farvision",0);
                        rMap.put("naked_farvision",NakedOd);
                        rMap.put("diopter_s1",dioSR);
                        rMap.put("diopter_c1",dioCR);
                        rMap.put("diopter_a1",dioAR);
                        rMap.put("y1_x",stuMap.get("DXQJR"));
                        rMap.put("diopter_s2",0);
                        rMap.put("diopter_c2",0);
                        rMap.put("diopter_a2",0);
                        rMap.put("y2_x",0);
                        rMap.put("corneal_mm",corMR);
                        rMap.put("corneal_d",corDR);
                        rMap.put("eyeaxis",eyeaxisOd);
                        rMap.put("eyepressure",preOd);
                        rMap.put("age",age);
                        rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
                        rMap.put("winter",winter);
                        rMap.put("flag",2);
                        ldataList.add(lMap);
                        rdataList.add(rMap);
                        continue;
                    }
                }

                List<Map<String,Object>> scYuce = shaichaStudentDao.getScYuCe(checkTime,school, CityName, AreaName);
                Double dioAL = 0.0;
                Double dioSL = 0.0;
                Double dioCL = 0.0;
                Double dioAR = 0.0;
                Double dioSR = 0.0;
                Double dioCR = 0.0;
                Double corML = 0.0;
                Double corDL = 0.0;
                Double corMR = 0.0;
                Double corDR = 0.0;
                Double eyeaxisOd = 0.0;
                Double eyeaxisOs = 0.0;
                Double preOd = 0.0;
                Double preOs = 0.0;
                Double eyeaxis_corneal_L = 0.0;
                Double eyeaxis_corneal_R = 0.0;
                for (Map<String, Object> stuMap : scYuce) {
                    String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();


                    if ("4.0".equals(NakedOs)){
                        NakedOs="0.1";
                    }

                    if ("4.1".equals(NakedOs)){
                        NakedOs="0.12";
                    }
                    if ("4.2".equals(NakedOs)){
                        NakedOs="0.15";
                    }
                    if ("4.3".equals(NakedOs)){
                        NakedOs="0.2";
                    }
                    if ("4.4".equals(NakedOs)){
                        NakedOs="0.25";
                    }
                    if ("4.5".equals(NakedOs)){
                        NakedOs="0.3";
                    }
                    if ("4.6".equals(NakedOs)){
                        NakedOs="0.4";
                    }
                    if ("4.7".equals(NakedOs)){
                        NakedOs="0.5";
                    }
                    if ("4.8".equals(NakedOs)){
                        NakedOs="0.6";
                    }
                    if ("4.9".equals(NakedOs)){
                        NakedOs="0.8";
                    }
                    if ("5.0".equals(NakedOs)){
                        NakedOs="1.0";
                    }
                    try {
                        if (Double.parseDouble(NakedOs)<4.0){
                            NakedOs="0.1";
                        }
                        if (Double.parseDouble(NakedOs)>5.0){
                            NakedOs="1.0";
                        }
                    }catch (Exception e){
                        continue;
                    }
                    String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();


                    if ("4.0".equals(NakedOd)){
                        NakedOd="0.1";
                    }

                    if ("4.1".equals(NakedOd)){
                        NakedOd="0.12";
                    }
                    if ("4.2".equals(NakedOd)){
                        NakedOd="0.15";
                    }
                    if ("4.3".equals(NakedOd)){
                        NakedOd="0.2";
                    }
                    if ("4.4".equals(NakedOd)){
                        NakedOd="0.25";
                    }
                    if ("4.5".equals(NakedOd)){
                        NakedOd="0.3";
                    }
                    if ("4.6".equals(NakedOd)){
                        NakedOd="0.4";
                    }
                    if ("4.7".equals(NakedOd)){
                        NakedOd="0.5";
                    }
                    if ("4.8".equals(NakedOd)){
                        NakedOd="0.6";
                    }
                    if ("4.9".equals(NakedOd)){
                        NakedOd="0.8";
                    }
                    if ("5.0".equals(NakedOd)){
                        NakedOd="1.0";
                    }
                    try {
                        if (Double.parseDouble(NakedOd)<4.0){
                            NakedOd="0.1";
                        }
                        if (Double.parseDouble(NakedOd)>5.0){
                            NakedOd="1.0";
                        }
                    } catch (Exception e) {
                        continue;
                    }
                    Map<String,Object> lMap = new HashMap<>();
                    Map<String,Object> rMap = new HashMap<>();
                    Integer id = (Integer) stuMap.get("id");
                    Integer sex = (Integer) stuMap.get("sex");
                    if (sex == 1){
                        sex=1;
                    }
                    if (sex == 2){
                        sex=0;
                    }
                    String lasy_check_time = stuMap.get("checkTime").toString();
                    Integer optId = shaichaStudentDao.getOptId(id);
                    List<Map<String,Object>> diopterData = shaichaStudentDao.getDiopterData(optId);

                    for (Map<String, Object> dioMap : diopterData) {
                        String ifRl = dioMap.get("ifRL").toString();
                        if ("L".equals(ifRl)){
                            dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                            dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                            dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                        }

                        if ("R".equals(ifRl)){
                            dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                            dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                            dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                        }
                    }
                    List<Map<String,Object>> corData = shaichaStudentDao.getCornealData(optId);
                    for (Map<String, Object> corMap : corData) {
                        String ifRL = corMap.get("ifRL").toString();
                        if ("L".equals(ifRL)){
                            corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                            corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                        }
                        if ("R".equals(ifRL)){
                            corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                            corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                        }
                    }

                    Map<String,Double> eyeaxisData = shaichaStudentDao.getEyeAxisData(id);
                    if (eyeaxisData!=null){
                        eyeaxisOd = eyeaxisData.get("od");
                        eyeaxisOs = eyeaxisData.get("os");
                    }
                    Map<String,Double> eyepressureData = shaichaStudentDao.getEyepressureData(id);

                    if (eyepressureData != null){
                        preOd = eyepressureData.get("preOd");
                        preOs = eyepressureData.get("preOs");
                    }

                    if (eyeaxisOd == 0.0 || corMR ==0.0){
                        eyeaxis_corneal_R = 0.0;
                    }else {
                        eyeaxis_corneal_R = eyeaxisOd/corMR;
                    }
                    if (eyeaxisOs == 0.0 || corML==0.0){
                        eyeaxis_corneal_L = 0.0;
                    }else {
                        eyeaxis_corneal_L = eyeaxisOs/corML;
                    }
                    String age = stuMap.get("age").toString();

                    lMap.put("student",id+"|L");
                    lMap.put("lasy_check_time",lasy_check_time);
                    lMap.put("studeny_sex",sex);
                    lMap.put("shifou_tangwo",0);
                    lMap.put("zuizhong_tizhi",0);
                    lMap.put("gzr_sm",0);
                    lMap.put("zm_sm",0);
                    lMap.put("zm_swhd",0);
                    lMap.put("zm_mtgz",0);
                    lMap.put("zx_swhd",0);
                    lMap.put("xw_swhd",0);
                    lMap.put("life_farvision",0);
                    lMap.put("naked_farvision",NakedOs);
                    lMap.put("diopter_s1",dioSL);
                    lMap.put("diopter_c1",dioCL);
                    lMap.put("diopter_a1",dioAL);
                    lMap.put("y1_x",stuMap.get("DXQJL"));
                    lMap.put("diopter_s2",0);
                    lMap.put("diopter_c2",0);
                    lMap.put("diopter_a2",0);
                    lMap.put("y2_x",0);
                    lMap.put("corneal_mm",corML);
                    lMap.put("corneal_d",corDL);
                    lMap.put("eyeaxis",eyeaxisOs);
                    lMap.put("eyepressure",preOs);
                    lMap.put("age",age);
                    lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);
                    lMap.put("winter",winter);
                    lMap.put("flag",2);



                    rMap.put("student",id+"|R");
                    rMap.put("lasy_check_time",lasy_check_time);
                    rMap.put("studeny_sex",sex);
                    rMap.put("shifou_tangwo",0);
                    rMap.put("zuizhong_tizhi",0);
                    rMap.put("gzr_sm",0);
                    rMap.put("zm_sm",0);
                    rMap.put("zm_swhd",0);
                    rMap.put("zm_mtgz",0);
                    rMap.put("zx_swhd",0);
                    rMap.put("xw_swhd",0);
                    rMap.put("life_farvision",0);
                    rMap.put("naked_farvision",NakedOd);
                    rMap.put("diopter_s1",dioSR);
                    rMap.put("diopter_c1",dioCR);
                    rMap.put("diopter_a1",dioAR);
                    rMap.put("y1_x",stuMap.get("DXQJR"));
                    rMap.put("diopter_s2",0);
                    rMap.put("diopter_c2",0);
                    rMap.put("diopter_a2",0);
                    rMap.put("y2_x",0);
                    rMap.put("corneal_mm",corMR);
                    rMap.put("corneal_d",corDR);
                    rMap.put("eyeaxis",eyeaxisOd);
                    rMap.put("eyepressure",preOd);
                    rMap.put("age",age);
                    rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
                    rMap.put("winter",winter);
                    rMap.put("flag",2);
                    ldataList.add(lMap);
                    rdataList.add(rMap);
                }
            }
            if ("ld".equals(checkType)){
                List<Map<String,Object>> ldYuce = liuDiaoDao.getLDYuCe(checkTime,school, CityName, AreaName);
                for (Map<String, Object> stuMap : ldYuce) {
                    Map<String,Object> lMap = new HashMap<>();
                    Map<String,Object> rMap = new HashMap<>();
                    Integer id = (Integer) stuMap.get("id");
                    Integer sex = (Integer) stuMap.get("sex");
                    if (sex == 1){
                        sex=1;
                    }
                    if (sex == 2){
                        sex=0;
                    }
                    String lasy_check_time = stuMap.get("checkTime").toString();
                    Map<String,String> eyeSightData = liuDiaoDao.getEyeSightData(id);
                    String nakedOd = eyeSightData.get("nakedOd")==""?"0.0":eyeSightData.get("nakedOd");
                    String nakedOs = eyeSightData.get("nakedOs")==""?"0.0":eyeSightData.get("nakedOs");
                    String lifeOd = eyeSightData.get("lifeOd")==""?"0.0":eyeSightData.get("lifeOd");
                    String lifeOs = eyeSightData.get("lifeOs")==""?"0.0":eyeSightData.get("lifeOs");


                    try {
                        if ("10/100".equals(nakedOd)){
                            nakedOd="0.1";
                        }
                        if ("10/80".equals(nakedOd)){
                            nakedOd="0.12";
                        }
                        if ("10/60".equals(nakedOd)){
                            nakedOd="0.15";
                        }
                        if ("10/50".equals(nakedOd)){
                            nakedOd="0.2";
                        }
                        if ("10/40".equals(nakedOd)){
                            nakedOd="0.25";
                        }
                        if ("10/30".equals(nakedOd)){
                            nakedOd="0.3";
                        }
                        if ("10/25".equals(nakedOd)){
                            nakedOd="0.4";
                        }
                        if ("10/20".equals(nakedOd)){
                            nakedOd="0.5";
                        }
                        if ("10/15".equals(nakedOd)){
                            nakedOd="0.6";
                        }
                        if ("10/12.5".equals(nakedOd)){
                            nakedOd="0.8";
                        }
                        if ("10/10".equals(nakedOd)){
                            nakedOd="1.0";
                        }
                        if (Double.parseDouble(nakedOd)>1.0){
                            nakedOd="1.0";
                        }
                        if (Double.parseDouble(nakedOd)<0.1){
                            nakedOd="0.1";
                        }

                        if ("10/100".equals(nakedOs)){
                            nakedOs="0.1";
                        }
                        if ("10/80".equals(nakedOs)){
                            nakedOs="0.12";
                        }
                        if ("10/60".equals(nakedOs)){
                            nakedOs="0.15";
                        }
                        if ("10/50".equals(nakedOs)){
                            nakedOs="0.2";
                        }
                        if ("10/40".equals(nakedOs)){
                            nakedOs="0.25";
                        }
                        if ("10/30".equals(nakedOs)){
                            nakedOs="0.3";
                        }
                        if ("10/25".equals(nakedOs)){
                            nakedOs="0.4";
                        }
                        if ("10/20".equals(nakedOs)){
                            nakedOs="0.5";
                        }
                        if ("10/15".equals(nakedOs)){
                            nakedOs="0.6";
                        }
                        if ("10/12.5".equals(nakedOs)){
                            nakedOs="0.8";
                        }
                        if ("10/10".equals(nakedOs)){
                            nakedOs="1.0";
                        }
                        if (Double.parseDouble(nakedOs)>1.0){
                            nakedOs="1.0";
                        }
                        if (Double.parseDouble(nakedOs)<0.1){
                            nakedOs="0.1";
                        }

                        if ("10/100".equals(lifeOd)){
                            lifeOd="0.1";
                        }
                        if ("10/80".equals(lifeOd)){
                            lifeOd="0.12";
                        }
                        if ("10/60".equals(lifeOd)){
                            lifeOd="0.15";
                        }
                        if ("10/50".equals(lifeOd)){
                            lifeOd="0.2";
                        }
                        if ("10/40".equals(lifeOd)){
                            lifeOd="0.25";
                        }
                        if ("10/30".equals(lifeOd)){
                            lifeOd="0.3";
                        }
                        if ("10/25".equals(lifeOd)){
                            lifeOd="0.4";
                        }
                        if ("10/20".equals(lifeOd)){
                            lifeOd="0.5";
                        }
                        if ("10/15".equals(lifeOd)){
                            lifeOd="0.6";
                        }
                        if ("10/12.5".equals(lifeOd)){
                            lifeOd="0.8";
                        }
                        if ("10/10".equals(lifeOd)){
                            lifeOd="1.0";
                        }
                        if (Double.parseDouble(lifeOd)>1.0){
                            lifeOd="1.0";
                        }
                        if (Double.parseDouble(lifeOd)<0.1){
                            lifeOd="0.1";
                        }

                        if ("10/100".equals(lifeOs)){
                            lifeOs="0.1";
                        }
                        if ("10/80".equals(lifeOs)){
                            lifeOs="0.12";
                        }
                        if ("10/60".equals(lifeOs)){
                            lifeOs="0.15";
                        }
                        if ("10/50".equals(lifeOs)){
                            lifeOs="0.2";
                        }
                        if ("10/40".equals(lifeOs)){
                            lifeOs="0.25";
                        }
                        if ("10/30".equals(lifeOs)){
                            lifeOs="0.3";
                        }
                        if ("10/25".equals(lifeOs)){
                            lifeOs="0.4";
                        }
                        if ("10/20".equals(lifeOs)){
                            lifeOs="0.5";
                        }
                        if ("10/15".equals(lifeOs)){
                            lifeOs="0.6";
                        }
                        if ("10/12.5".equals(lifeOs)){
                            lifeOs="0.8";
                        }
                        if ("10/10".equals(lifeOs)){
                            lifeOs="1.0";
                        }
                        if (Double.parseDouble(lifeOs)>1.0){
                            lifeOs="1.0";
                        }
                        if (Double.parseDouble(lifeOs)<0.1){
                            lifeOs="0.1";
                        }
                    } catch (Exception e) {
                        continue;
                    }
                    Integer optId = liuDiaoDao.getOptId(id);
                    Double dioAFL = 0.0;
                    Double dioSFL = 0.0;
                    Double dioCFL = 0.0;
                    Double DXQJFL = 0.0;

                    Double dioAFR = 0.0;
                    Double dioSFR = 0.0;
                    Double dioCFR = 0.0;
                    Double DXQJFR = 0.0;

                    Double dioASL = 0.0;
                    Double dioSSL = 0.0;
                    Double dioCSL = 0.0;
                    Double DXQJSL = 0.0;

                    Double dioASR = 0.0;
                    Double dioSSR = 0.0;
                    Double dioCSR = 0.0;
                    Double DXQJSR = 0.0;


                    Double corML = 0.0;
                    Double corDL = 0.0;
                    Double corMR = 0.0;
                    Double corDR = 0.0;

                    Double eyeaxisOd = 0.0;
                    Double eyeaxisOs = 0.0;

                    Double preOd = 0.0;
                    Double preOs = 0.0;

                    Double eyeaxis_corneal_L = 0.0;
                    Double eyeaxis_corneal_R = 0.0;
                    List<Map<String,Object>> diopterData = liuDiaoDao.getDiopterData(optId);
                    for (Map<String, Object> dioMap : diopterData) {
                        String ifRL = dioMap.get("ifRL").toString();
                        if ("L".equals(ifRL)){
                            String first_second = dioMap.get("first_second").toString();
                            if ("FIRST_CHECK".equals(first_second)){
                                dioAFL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSFL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCFL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                                DXQJFL = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                            }
                            if ("SECOND_CHECK".equals(first_second)){
                                dioASL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCSL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                                DXQJSL = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                            }
                        }
                        if ("R".equals(ifRL)){
                            String first_second = dioMap.get("first_second").toString();
                            if ("FIRST_CHECK".equals(first_second)){
                                dioAFR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSFR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCFR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                                DXQJFR = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                            }
                            if ("SECOND_CHECK".equals(first_second)){
                                dioASR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                                dioSSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                                dioCSR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                                DXQJSR = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                            }
                        }
                    }
                    List<Map<String,Object>> corData = liuDiaoDao.getCornealData(optId);
                    for (Map<String, Object> corMap : corData) {
                        String ifRL = corMap.get("ifRL").toString();
                        if ("L".equals(ifRL)){
                            corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                            corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                        }
                        if ("R".equals(ifRL)){
                            corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                            corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                        }
                    }

                    Map<String,Double> eyeaxisData = liuDiaoDao.getEyeAxisData(id);
                    if (eyeaxisData!=null){
                        eyeaxisOd = eyeaxisData.get("od");
                        eyeaxisOs = eyeaxisData.get("os");
                    }
                    Map<String,Double> eyepressureData = liuDiaoDao.getEyepressureData(id);
                    if (eyepressureData != null){
                        preOd = eyepressureData.get("preOd");
                        preOs = eyepressureData.get("preOs");
                    }
                    if (eyeaxisOd == 0.0 || corMR ==0.0){
                        eyeaxis_corneal_R = 0.0;
                    }else {
                        eyeaxis_corneal_R = eyeaxisOd/corMR;
                    }
                    if (eyeaxisOs == 0.0 || corML==0.0){
                        eyeaxis_corneal_L = 0.0;
                    }else {
                        eyeaxis_corneal_L = eyeaxisOs/corML;
                    }
                    String age = stuMap.get("age").toString();

                    lMap.put("student",id+"|L");
                    lMap.put("lasy_check_time",lasy_check_time);
                    lMap.put("studeny_sex",sex);
                    lMap.put("shifou_tangwo",0);
                    lMap.put("zuizhong_tizhi",0);
                    lMap.put("gzr_sm",0);
                    lMap.put("zm_sm",0);
                    lMap.put("zm_swhd",0);
                    lMap.put("zm_mtgz",0);
                    lMap.put("zx_swhd",0);
                    lMap.put("xw_swhd",0);
                    lMap.put("life_farvision",lifeOs);
                    lMap.put("naked_farvision",nakedOs);
                    lMap.put("diopter_s1",dioSFL);
                    lMap.put("diopter_c1",dioCFL);
                    lMap.put("diopter_a1",dioAFL);
                    lMap.put("y1_x",DXQJFL);
                    lMap.put("diopter_s2",dioSSL);
                    lMap.put("diopter_c2",dioCSL);
                    lMap.put("diopter_a2",dioASL);
                    lMap.put("y2_x",DXQJSL);
                    lMap.put("corneal_mm",corML);
                    lMap.put("corneal_d",corDL);
                    lMap.put("eyeaxis",eyeaxisOs);
                    lMap.put("eyepressure",preOs);
                    lMap.put("age",age);
                    lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);
                    lMap.put("winter",winter);
                    lMap.put("flag",1);

                    rMap.put("student",id+"|R");
                    rMap.put("lasy_check_time",lasy_check_time);
                    rMap.put("studeny_sex",sex);
                    rMap.put("shifou_tangwo",0);
                    rMap.put("zuizhong_tizhi",0);
                    rMap.put("gzr_sm",0);
                    rMap.put("zm_sm",0);
                    rMap.put("zm_swhd",0);
                    rMap.put("zm_mtgz",0);
                    rMap.put("zx_swhd",0);
                    rMap.put("xw_swhd",0);
                    rMap.put("life_farvision",lifeOd);
                    rMap.put("naked_farvision",nakedOd);
                    rMap.put("diopter_s1",dioSFR);
                    rMap.put("diopter_c1",dioCFR);
                    rMap.put("diopter_a1",dioAFR);
                    rMap.put("y1_x",DXQJFR);
                    rMap.put("diopter_s2",dioSSR);
                    rMap.put("diopter_c2",dioCSR);
                    rMap.put("diopter_a2",dioASR);
                    rMap.put("y2_x",DXQJSR);
                    rMap.put("corneal_mm",corMR);
                    rMap.put("corneal_d",corDR);
                    rMap.put("eyeaxis",eyeaxisOd);
                    rMap.put("eyepressure",preOd);
                    rMap.put("age",age);
                    rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
                    rMap.put("winter",winter);
                    rMap.put("flag",1);
                    ldataList.add(lMap);
                    rdataList.add(rMap);
                }
            }
            List<Map<String,Object>> lAllData = new ArrayList<>();
            List<Map<String,Object>> rAllData = new ArrayList<>();


            int l = ldataList.size() / 200;
            for (int j = 1; j <= l+1; j++) {
                if (l==0){
                    HttpHeaders httpHeadersL = new HttpHeaders();
                    httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(ldataList, httpHeadersL);
                    ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entityL, String.class);
                    String responseL = responseEntityL.getBody();
                    Map mapData = JSON.parseObject(responseL, Map.class);
                    List<Map<String,Object>> Ldata = (List<Map<String, Object>>) mapData.get("data");
                    lAllData.addAll(Ldata);
                    continue;
                }
                if (j==1){
                    List<Map<String, Object>> mapList = ldataList.subList(j * 200 - 200, j * 200 + 1);
                    HttpHeaders httpHeadersL = new HttpHeaders();
                    httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(mapList, httpHeadersL);
                    ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entityL, String.class);
                    String responseL = responseEntityL.getBody();
                    Map mapData = JSON.parseObject(responseL, Map.class);
                    List<Map<String,Object>> Ldata = (List<Map<String, Object>>) mapData.get("data");
                    lAllData.addAll(Ldata);
                    continue;
                }
                if (j==l+1){
                    List<Map<String, Object>> mapList = ldataList.subList((j * 200 - 200)+1, ldataList.size());
                    HttpHeaders httpHeadersL = new HttpHeaders();
                    httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(mapList, httpHeadersL);
                    ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entityL, String.class);
                    String responseL = responseEntityL.getBody();
                    Map mapData = JSON.parseObject(responseL, Map.class);
                    List<Map<String,Object>> Ldata = (List<Map<String, Object>>) mapData.get("data");
                    lAllData.addAll(Ldata);
                    continue;
                }
                List<Map<String, Object>> mapList = ldataList.subList((j * 200 - 200)+1, j * 200 + 1);
                HttpHeaders httpHeadersL = new HttpHeaders();
                httpHeadersL.setContentType(MediaType.APPLICATION_JSON_UTF8);
                HttpEntity<List<Map<String, Object>>> entityL = new HttpEntity<>(mapList, httpHeadersL);
                ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entityL, String.class);
                String responseL = responseEntityL.getBody();
                Map mapData = JSON.parseObject(responseL, Map.class);
                List<Map<String,Object>> data = (List<Map<String, Object>>) mapData.get("data");
                lAllData.addAll(data);
            }
            int r = rdataList.size() / 200;
            for (int j = 1; j <= r+1; j++) {
                if (r==0){
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(rdataList, httpHeaders);
                    ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entity, String.class);
                    String responseL = responseEntityL.getBody();
                    Map mapData = JSON.parseObject(responseL, Map.class);
                    List<Map<String,Object>> data = (List<Map<String, Object>>) mapData.get("data");
                    rAllData.addAll(data);
                    continue;
                }
                if (j==1){
                    List<Map<String, Object>> mapList = rdataList.subList(j * 200 - 200, j * 200 + 1);
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(mapList, httpHeaders);
                    ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entity, String.class);
                    String responseL = responseEntityL.getBody();
                    Map mapData = JSON.parseObject(responseL, Map.class);
                    List<Map<String,Object>> data = (List<Map<String, Object>>) mapData.get("data");
                    rAllData.addAll(data);
                    continue;
                }
                if (j==r+1){
                    List<Map<String, Object>> mapList = rdataList.subList((j * 200 - 200)+1, ldataList.size());
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                    HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(mapList, httpHeaders);
                    ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entity, String.class);
                    String responseL = responseEntityL.getBody();
                    Map mapData = JSON.parseObject(responseL, Map.class);
                    List<Map<String,Object>> data = (List<Map<String, Object>>) mapData.get("data");
                    rAllData.addAll(data);

                    continue;
                }
                List<Map<String, Object>> mapList = rdataList.subList((j * 200 - 200)+1, j * 200 + 1);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(mapList, httpHeaders);
                ResponseEntity<String> responseEntityL = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entity, String.class);
                String responseL = responseEntityL.getBody();
                Map mapData = JSON.parseObject(responseL, Map.class);
                List<Map<String,Object>> data = (List<Map<String, Object>>) mapData.get("data");
                rAllData.addAll(data);
            }
            Integer zx = 0;
            Integer jx = 0;
            Integer lc = 0;

            for (Map<String, Object> lMap : lAllData) {
                for (Map<String, Object> rMap : rAllData) {
                    int indexL = lMap.get("student").toString().indexOf("|");
                    int indexR = rMap.get("student").toString().indexOf("|");
                    String idL = lMap.get("student").toString().substring(0, indexL);
                    String idR = rMap.get("student").toString().substring(0, indexR);
                    if (idL.equals(idR)){
                        double y1YL = Double.parseDouble(lMap.get("y1Y").toString());
                        double y1YR = Double.parseDouble(rMap.get("y1Y").toString());
                        double y2YL = Double.parseDouble(lMap.get("y2Y").toString());
                        double y2YR = Double.parseDouble(rMap.get("y2Y").toString());
                        *//*double nakedL = Double.parseDouble(lMap.get("nakedFarvisionY").toString());
                        double nakedR = Double.parseDouble(rMap.get("nakedFarvisionY").toString());*//*
                        double dxqjF = y1YL > y1YR ? y1YR:y1YL;
                        double dxqjS = y2YL > y2YR ? y2YR:y2YL;
                        if (dxqjF<-0.5 && dxqjS>-0.5 && dxqjS-dxqjF>0.5){
                            jx++;
                        }
                        if (dxqjS>-0.5 && dxqjS<=0.75){
                            lc++;
                        }
                        if (dxqjS<=-0.5){
                            zx++;
                        }
                    }
                }
            }
            zxycList.add(format(getLv(zx,checkNumber)));
            jxycList.add(format(getLv(jx,checkNumber)));
            lcycList.add(format(getLv(lc,checkNumber)));
        }


        zxycList.add(0, 0);
        jxycList.add(0, 0);
        lcycList.add(0, 0);
        everyTime.add("预测");*/
        fixlist(everyTime);
        fixTheList(zxjsLv);
        fixTheList(lcqqLv);
        fixTheList(jxjsLv);
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
        params.put("ydoctorchubu","");
        params.put("zdoctorchubu","");
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
        params.put("ydoctorchubu","");
        params.put("zdoctorchubu","");
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
        fixList(luoyan);
        fixList(dengxiaoqiujing);
        if (yanzhou.size()==0){
            fixList(yanzhou);
            for (int i = 3; i >=0 ; i--) {
                yanzhour.add(yanzhou.get(i).get("youyan"));
                yanzhoul.add(yanzhou.get(i).get("zuoyan"));
                yanzhoutime.add(luoyan.get(i).get("checkdate"));
            }
        }else {
            fixList(yanzhou);
            for (int i = 3; i >=0 ; i--) {
                yanzhour.add(yanzhou.get(i).get("youyan"));
                yanzhoul.add(yanzhou.get(i).get("zuoyan"));
                yanzhoutime.add(yanzhou.get(i).get("checkdate"));
            }
        }
        for (int i = 3; i >=0 ; i--) {
            luoyanr.add(luoyan.get(i).get("youyan"));
            luoyanl.add(luoyan.get(i).get("zuoyan"));
            luoyantime.add(luoyan.get(i).get("checkdate"));
        }

        for (int i = 3; i >=0 ; i--) {
            dengxiaoqiujingr.add(dengxiaoqiujing.get(i).get("youyan"));
            dengxiaoqiujingl.add(dengxiaoqiujing.get(i).get("zuoyan"));
            dengxiaoqiujingtime.add(dengxiaoqiujing.get(i).get("checkdate"));
        }
        String checkDate = luoyantime.get(luoyantime.size() - 1).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(checkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int month = date.getMonth();
        int winter = 0;
        if (month==9 || month == 10 || month == 11 || month == 0){
            winter=1;
        }else {
            winter=0;
        }
        List<Map<String,Object>> dataList = new ArrayList<>();
        if ("sc".equals(checkType)){
            Map<String,Object> stuMap = shaichaStudentDao.getYuCeForPerson(checkDate,name,idCard);
            Double dioAL = 0.0;
            Double dioSL = 0.0;
            Double dioCL = 0.0;
            Double dioAR = 0.0;
            Double dioSR = 0.0;
            Double dioCR = 0.0;
            Double corML = 0.0;
            Double corDL = 0.0;
            Double corMR = 0.0;
            Double corDR = 0.0;
            Double eyeaxisOd = 0.0;
            Double eyeaxisOs = 0.0;
            Double preOd = 0.0;
            Double preOs = 0.0;
            Double eyeaxis_corneal_L = 0.0;
            Double eyeaxis_corneal_R = 0.0;
            Map<String,Object> lMap = new HashMap<>();
            Map<String,Object> rMap = new HashMap<>();
            Integer id = (Integer) stuMap.get("id");
            Integer sex = (Integer) stuMap.get("sex");
            if (sex == 1){
                sex=1;
            }
            if (sex == 2){
                sex=0;
            }
            String lasy_check_time = stuMap.get("checkTime").toString();
            Integer optId = shaichaStudentDao.getOptId(id);
            List<Map<String,Object>> diopterData = shaichaStudentDao.getDiopterData(optId);

            for (Map<String, Object> dioMap : diopterData) {
                String ifRl = dioMap.get("ifRL").toString();
                if ("L".equals(ifRl)){
                    dioAL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                    dioSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                    dioCL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                }

                if ("R".equals(ifRl)){
                    dioAR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                    dioSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                    dioCR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                }
            }
            List<Map<String,Object>> corData = shaichaStudentDao.getCornealData(optId);
            for (Map<String, Object> corMap : corData) {
                String ifRL = corMap.get("ifRL").toString();
                if ("L".equals(ifRL)){
                    corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                    corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                }
                if ("R".equals(ifRL)){
                    corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                    corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                }
            }

            Map<String,Double> eyeaxisData = shaichaStudentDao.getEyeAxisData(id);
            if (eyeaxisData!=null){
                eyeaxisOd = eyeaxisData.get("od");
                eyeaxisOs = eyeaxisData.get("os");
            }
            Map<String,Double> eyepressureData = shaichaStudentDao.getEyepressureData(id);

            if (eyepressureData != null){
                preOd = eyepressureData.get("preOd");
                preOs = eyepressureData.get("preOs");
            }

            if (eyeaxisOd == 0.0 || corMR ==0.0){
                eyeaxis_corneal_R = 0.0;
            }else {
                eyeaxis_corneal_R = eyeaxisOd/corMR;
            }
            if (eyeaxisOs == 0.0 || corML==0.0){
                eyeaxis_corneal_L = 0.0;
            }else {
                eyeaxis_corneal_L = eyeaxisOs/corML;
            }
            String age = stuMap.get("age").toString();
            String NakedOs = stuMap.get("NakedOs").toString() == "" ? "0" : stuMap.get("NakedOs").toString();
            if (Double.parseDouble(NakedOs)<4.0){
                NakedOs="0.1";
            }
            if (Double.parseDouble(NakedOs)>5.0){
                NakedOs="1.0";
            }
            if ("4.0".equals(NakedOs)){
                NakedOs="0.1";
            }
            if ("4.1".equals(NakedOs)){
                NakedOs="0.12";
            }
            if ("4.2".equals(NakedOs)){
                NakedOs="0.15";
            }
            if ("4.3".equals(NakedOs)){
                NakedOs="0.2";
            }
            if ("4.4".equals(NakedOs)){
                NakedOs="0.25";
            }
            if ("4.5".equals(NakedOs)){
                NakedOs="0.3";
            }
            if ("4.6".equals(NakedOs)){
                NakedOs="0.4";
            }
            if ("4.7".equals(NakedOs)){
                NakedOs="0.5";
            }
            if ("4.8".equals(NakedOs)){
                NakedOs="0.6";
            }
            if ("4.9".equals(NakedOs)){
                NakedOs="0.8";
            }
            if ("5.0".equals(NakedOs)){
                NakedOs="1.0";
            }
            String NakedOd = stuMap.get("NakedOd").toString() == "" ? "0" : stuMap.get("NakedOd").toString();
            if (Double.parseDouble(NakedOd)<4.0){
                NakedOd="0.1";
            }
            if (Double.parseDouble(NakedOd)>5.0){
                NakedOd="1.0";
            }
            if ("4.0".equals(NakedOd)){
                NakedOd="0.1";
            }
            if ("4.1".equals(NakedOd)){
                NakedOd="0.12";
            }
            if ("4.2".equals(NakedOd)){
                NakedOd="0.15";
            }
            if ("4.3".equals(NakedOd)){
                NakedOd="0.2";
            }
            if ("4.4".equals(NakedOd)){
                NakedOd="0.25";
            }
            if ("4.5".equals(NakedOd)){
                NakedOd="0.3";
            }
            if ("4.6".equals(NakedOd)){
                NakedOd="0.4";
            }
            if ("4.7".equals(NakedOd)){
                NakedOd="0.5";
            }
            if ("4.8".equals(NakedOd)){
                NakedOd="0.6";
            }
            if ("4.9".equals(NakedOd)){
                NakedOd="0.8";
            }
            if ("5.0".equals(NakedOd)){
                NakedOd="1.0";
            }
            lMap.put("student",id+"|L");
            lMap.put("lasy_check_time",lasy_check_time);
            lMap.put("studeny_sex",sex);
            lMap.put("shifou_tangwo",0);
            lMap.put("zuizhong_tizhi",0);
            lMap.put("gzr_sm",0);
            lMap.put("zm_sm",0);
            lMap.put("zm_swhd",0);
            lMap.put("zm_mtgz",0);
            lMap.put("zx_swhd",0);
            lMap.put("xw_swhd",0);
            lMap.put("life_farvision",0);
            lMap.put("naked_farvision",NakedOs);
            lMap.put("diopter_s1",dioSL);
            lMap.put("diopter_c1",dioCL);
            lMap.put("diopter_a1",dioAL);
            lMap.put("y1_x",stuMap.get("DXQJL"));
            lMap.put("diopter_s2",0);
            lMap.put("diopter_c2",0);
            lMap.put("diopter_a2",0);
            lMap.put("y2_x",0);
            lMap.put("corneal_mm",corML);
            lMap.put("corneal_d",corDL);
            lMap.put("eyeaxis",eyeaxisOs);
            lMap.put("eyepressure",preOs);
            lMap.put("age",age);
            lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);
            lMap.put("winter",winter);
            lMap.put("flag",2);



            rMap.put("student",id+"|R");
            rMap.put("lasy_check_time",lasy_check_time);
            rMap.put("studeny_sex",sex);
            rMap.put("shifou_tangwo",0);
            rMap.put("zuizhong_tizhi",0);
            rMap.put("gzr_sm",0);
            rMap.put("zm_sm",0);
            rMap.put("zm_swhd",0);
            rMap.put("zm_mtgz",0);
            rMap.put("zx_swhd",0);
            rMap.put("xw_swhd",0);
            rMap.put("life_farvision",0);
            rMap.put("naked_farvision",NakedOd);
            rMap.put("diopter_s1",dioSR);
            rMap.put("diopter_c1",dioCR);
            rMap.put("diopter_a1",dioAR);
            rMap.put("y1_x",stuMap.get("DXQJR"));
            rMap.put("diopter_s2",0);
            rMap.put("diopter_c2",0);
            rMap.put("diopter_a2",0);
            rMap.put("y2_x",0);
            rMap.put("corneal_mm",corMR);
            rMap.put("corneal_d",corDR);
            rMap.put("eyeaxis",eyeaxisOd);
            rMap.put("eyepressure",preOd);
            rMap.put("age",age);
            rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
            rMap.put("winter",winter);
            rMap.put("flag",2);

            dataList.add(lMap);
            dataList.add(rMap);
        }
        if ("ld".equals(checkType)){

            Map<String,Object> stuMap = liuDiaoDao.getYuCeForPerson(checkDate,name,idCard);
            Map<String,Object> lMap = new HashMap<>();
            Map<String,Object> rMap = new HashMap<>();
            Integer id = (Integer) stuMap.get("id");
            Integer sex = (Integer) stuMap.get("sex");
            if (sex == 1){
                sex=1;
            }
            if (sex == 2){
                sex=0;
            }
            String lasy_check_time = stuMap.get("checkTime").toString();
            Map<String,String> eyeSightData = liuDiaoDao.getEyeSightData(id);
            String nakedOd = eyeSightData.get("nakedOd")==""?"0.0":eyeSightData.get("nakedOd");
            String nakedOs = eyeSightData.get("nakedOs")==""?"0.0":eyeSightData.get("nakedOs");
            String lifeOd = eyeSightData.get("lifeOd")==""?"0.0":eyeSightData.get("lifeOd");
            String lifeOs = eyeSightData.get("lifeOs")==""?"0.0":eyeSightData.get("lifeOs");
            if ("10/100".equals(nakedOd)){
                nakedOd="0.1";
            }
            if ("10/80".equals(nakedOd)){
                nakedOd="0.12";
            }
            if ("10/60".equals(nakedOd)){
                nakedOd="0.15";
            }
            if ("10/50".equals(nakedOd)){
                nakedOd="0.2";
            }
            if ("10/40".equals(nakedOd)){
                nakedOd="0.25";
            }
            if ("10/30".equals(nakedOd)){
                nakedOd="0.3";
            }
            if ("10/25".equals(nakedOd)){
                nakedOd="0.4";
            }
            if ("10/20".equals(nakedOd)){
                nakedOd="0.5";
            }
            if ("10/15".equals(nakedOd)){
                nakedOd="0.6";
            }
            if ("10/12.5".equals(nakedOd)){
                nakedOd="0.8";
            }
            if ("10/10".equals(nakedOd)){
                nakedOd="1.0";
            }


            if ("10/100".equals(nakedOs)){
                nakedOs="0.1";
            }
            if ("10/80".equals(nakedOs)){
                nakedOs="0.12";
            }
            if ("10/60".equals(nakedOs)){
                nakedOs="0.15";
            }
            if ("10/50".equals(nakedOs)){
                nakedOs="0.2";
            }
            if ("10/40".equals(nakedOs)){
                nakedOs="0.25";
            }
            if ("10/30".equals(nakedOs)){
                nakedOs="0.3";
            }
            if ("10/25".equals(nakedOs)){
                nakedOs="0.4";
            }
            if ("10/20".equals(nakedOs)){
                nakedOs="0.5";
            }
            if ("10/15".equals(nakedOs)){
                nakedOs="0.6";
            }
            if ("10/12.5".equals(nakedOs)){
                nakedOs="0.8";
            }
            if ("10/10".equals(nakedOs)){
                nakedOs="1.0";
            }


            if ("10/100".equals(lifeOd)){
                lifeOd="0.1";
            }
            if ("10/80".equals(lifeOd)){
                lifeOd="0.12";
            }
            if ("10/60".equals(lifeOd)){
                lifeOd="0.15";
            }
            if ("10/50".equals(lifeOd)){
                lifeOd="0.2";
            }
            if ("10/40".equals(lifeOd)){
                lifeOd="0.25";
            }
            if ("10/30".equals(lifeOd)){
                lifeOd="0.3";
            }
            if ("10/25".equals(lifeOd)){
                lifeOd="0.4";
            }
            if ("10/20".equals(lifeOd)){
                lifeOd="0.5";
            }
            if ("10/15".equals(lifeOd)){
                lifeOd="0.6";
            }
            if ("10/12.5".equals(lifeOd)){
                lifeOd="0.8";
            }
            if ("10/10".equals(lifeOd)){
                lifeOd="1.0";
            }


            if ("10/100".equals(lifeOs)){
                lifeOs="0.1";
            }
            if ("10/80".equals(lifeOs)){
                lifeOs="0.12";
            }
            if ("10/60".equals(lifeOs)){
                lifeOs="0.15";
            }
            if ("10/50".equals(lifeOs)){
                lifeOs="0.2";
            }
            if ("10/40".equals(lifeOs)){
                lifeOs="0.25";
            }
            if ("10/30".equals(lifeOs)){
                lifeOs="0.3";
            }
            if ("10/25".equals(lifeOs)){
                lifeOs="0.4";
            }
            if ("10/20".equals(lifeOs)){
                lifeOs="0.5";
            }
            if ("10/15".equals(lifeOs)){
                lifeOs="0.6";
            }
            if ("10/12.5".equals(lifeOs)){
                lifeOs="0.8";
            }
            if ("10/10".equals(lifeOs)){
                lifeOs="1.0";
            }
            Integer optId = liuDiaoDao.getOptId(id);
            Double dioAFL = 0.0;
            Double dioSFL = 0.0;
            Double dioCFL = 0.0;
            Double DXQJFL = 0.0;

            Double dioAFR = 0.0;
            Double dioSFR = 0.0;
            Double dioCFR = 0.0;
            Double DXQJFR = 0.0;

            Double dioASL = 0.0;
            Double dioSSL = 0.0;
            Double dioCSL = 0.0;
            Double DXQJSL = 0.0;

            Double dioASR = 0.0;
            Double dioSSR = 0.0;
            Double dioCSR = 0.0;
            Double DXQJSR = 0.0;


            Double corML = 0.0;
            Double corDL = 0.0;
            Double corMR = 0.0;
            Double corDR = 0.0;

            Double eyeaxisOd = 0.0;
            Double eyeaxisOs = 0.0;

            Double preOd = 0.0;
            Double preOs = 0.0;

            Double eyeaxis_corneal_L = 0.0;
            Double eyeaxis_corneal_R = 0.0;
            List<Map<String,Object>> diopterData = liuDiaoDao.getDiopterData(optId);
            for (Map<String, Object> dioMap : diopterData) {
                String ifRL = dioMap.get("ifRL").toString();
                if ("L".equals(ifRL)){
                    String first_second = dioMap.get("first_second").toString();
                    if ("FIRST_CHECK".equals(first_second)){
                        dioAFL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                        dioSFL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                        dioCFL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                        DXQJFL = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                    }
                    if ("SECOND_CHECK".equals(first_second)){
                        dioASL = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                        dioSSL = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                        dioCSL = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                        DXQJSL = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                    }
                }
                if ("R".equals(ifRL)){
                    String first_second = dioMap.get("first_second").toString();
                    if ("FIRST_CHECK".equals(first_second)){
                        dioAFR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                        dioSFR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                        dioCFR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                        DXQJFR = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                    }
                    if ("SECOND_CHECK".equals(first_second)){
                        dioASR = Double.parseDouble(dioMap.get("dioA")==null?"0.0":dioMap.get("dioA")==""?"0.0":dioMap.get("dioA").toString());
                        dioSSR = Double.parseDouble(dioMap.get("dioS")==null?"0.0":dioMap.get("dioS")==""?"0.0":dioMap.get("dioS").toString());
                        dioCSR = Double.parseDouble(dioMap.get("dioC")==null?"0.0":dioMap.get("dioC")==""?"0.0":dioMap.get("dioC").toString());
                        DXQJSR = Double.parseDouble(dioMap.get("DXQJ")==null?"0.0":dioMap.get("DXQJ")==""?"0.0":dioMap.get("DXQJ").toString());
                    }
                }
            }
            List<Map<String,Object>> corData = liuDiaoDao.getCornealData(optId);
            for (Map<String, Object> corMap : corData) {
                String ifRL = corMap.get("ifRL").toString();
                if ("L".equals(ifRL)){
                    corML = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                    corDL = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                }
                if ("R".equals(ifRL)){
                    corMR = Double.parseDouble(corMap.get("corM")==null?"0.0":corMap.get("corM")==""?"0.0":corMap.get("corM").toString());
                    corDR = Double.parseDouble(corMap.get("corD")==null?"0.0":corMap.get("corD")==""?"0.0":corMap.get("corD").toString());
                }
            }

            Map<String,Double> eyeaxisData = liuDiaoDao.getEyeAxisData(id);
            if (eyeaxisData!=null){
                eyeaxisOd = eyeaxisData.get("od");
                eyeaxisOs = eyeaxisData.get("os");
            }
            Map<String,Double> eyepressureData = liuDiaoDao.getEyepressureData(id);
            if (eyepressureData != null){
                preOd = eyepressureData.get("preOd");
                preOs = eyepressureData.get("preOs");
            }
            if (eyeaxisOd == 0.0 || corMR ==0.0){
                eyeaxis_corneal_R = 0.0;
            }else {
                eyeaxis_corneal_R = eyeaxisOd/corMR;
            }
            if (eyeaxisOs == 0.0 || corML==0.0){
                eyeaxis_corneal_L = 0.0;
            }else {
                eyeaxis_corneal_L = eyeaxisOs/corML;
            }
            String age = stuMap.get("age").toString();

            lMap.put("student",id+"|L");
            lMap.put("lasy_check_time",lasy_check_time);
            lMap.put("studeny_sex",sex);
            lMap.put("shifou_tangwo",0);
            lMap.put("zuizhong_tizhi",0);
            lMap.put("gzr_sm",0);
            lMap.put("zm_sm",0);
            lMap.put("zm_swhd",0);
            lMap.put("zm_mtgz",0);
            lMap.put("zx_swhd",0);
            lMap.put("xw_swhd",0);
            lMap.put("life_farvision",0);
            lMap.put("naked_farvision",nakedOs);
            lMap.put("diopter_s1",dioSFL);
            lMap.put("diopter_c1",dioCFL);
            lMap.put("diopter_a1",dioAFL);
            lMap.put("y1_x",DXQJFL);
            lMap.put("diopter_s2",dioSSL);
            lMap.put("diopter_c2",dioCSL);
            lMap.put("diopter_a2",dioASL);
            lMap.put("y2_x",DXQJSL);
            lMap.put("corneal_mm",corML);
            lMap.put("corneal_d",corDL);
            lMap.put("eyeaxis",eyeaxisOs);
            lMap.put("eyepressure",preOs);
            lMap.put("age",age);
            lMap.put("eyeaxis_corneal",eyeaxis_corneal_L);
            lMap.put("winter",winter);
            lMap.put("flag",1);

            rMap.put("student",id+"|R");
            rMap.put("lasy_check_time",lasy_check_time);
            rMap.put("studeny_sex",sex);
            rMap.put("shifou_tangwo",0);
            rMap.put("zuizhong_tizhi",0);
            rMap.put("gzr_sm",0);
            rMap.put("zm_sm",0);
            rMap.put("zm_swhd",0);
            rMap.put("zm_mtgz",0);
            rMap.put("zx_swhd",0);
            rMap.put("xw_swhd",0);
            rMap.put("life_farvision",0);
            rMap.put("naked_farvision",nakedOd);
            rMap.put("diopter_s1",dioSFR);
            rMap.put("diopter_c1",dioCFR);
            rMap.put("diopter_a1",dioAFR);
            rMap.put("y1_x",DXQJFR);
            rMap.put("diopter_s2",dioSSR);
            rMap.put("diopter_c2",dioCSR);
            rMap.put("diopter_a2",dioASR);
            rMap.put("y2_x",DXQJSR);
            rMap.put("corneal_mm",corMR);
            rMap.put("corneal_d",corDR);
            rMap.put("eyeaxis",eyeaxisOd);
            rMap.put("eyepressure",preOd);
            rMap.put("age",age);
            rMap.put("eyeaxis_corneal",eyeaxis_corneal_R);
            rMap.put("winter",winter);
            rMap.put("flag",1);
            dataList.add(lMap);
            dataList.add(rMap);
        }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(dataList, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://121.36.21.238:8081/vision_analyze/api/vision/visionAnalyze", entity, String.class);
        String response = responseEntity.getBody();
        Map mapData = JSON.parseObject(response, Map.class);
        List<Map<String,Object>> data = (List<Map<String, Object>>) mapData.get("data");
        for (Map<String, Object> stuMap : data) {
            int index = stuMap.get("student").toString().indexOf("|");
            String ifRl = stuMap.get("student").toString().substring(index + 1);
            if ("L".equals(ifRl)){
                dengxiaoqiujingl.remove(0);
                dengxiaoqiujingl.add(stuMap.get("y1Y"));
                luoyanl.remove(0);
                luoyanl.add(stuMap.get("nakedFarvisionY"));
            }
            if ("R".equals(ifRl)){
                dengxiaoqiujingr.remove(0);
                dengxiaoqiujingr.add(stuMap.get("y1Y"));
                luoyanr.remove(0);
                luoyanr.add(stuMap.get("nakedFarvisionY"));
            }
        }

        dengxiaoqiujingtime.remove(0);
        luoyantime.remove(0);
        dengxiaoqiujingtime.add("预测");
        luoyantime.add("预测");
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
        DecimalFormat df = new DecimalFormat("0.0");
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
        if (param.size()==4) return;
        if (param.size()==0){
            Map<String,Object> map = new HashMap();
            map.put("youyan",0);
            map.put("zuoyan",0);
            Object checkdate = "2020-12";
            map.put("checkdate",checkdate);
            param.add(map);
        }
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
    public static void fixlist(List list){
        if (list.size()==4) return;
        if (list.size()==0){
            Object checkdate = "2020-12";
            list.add(checkdate);
        }
        Collections.reverse(list);
        for (int i = list.size();i<4;i++){
            SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM");
            try {
                Date date = sdFormat.parse(list.get(i-1).toString());
                date.setMonth(date.getMonth()-6);
                String format = sdFormat.format(date);
                list.add(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(list);
    }
    public static void fixTheList(List list){
        if (list.size()==4) return;
        if (list.size()==0) list.add(0);
        Collections.reverse(list);
        for (int i = list.size();i<4;i++){
            list.add(0);
        }
        Collections.reverse(list);
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
