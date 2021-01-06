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
        redisTemplate.opsForHash().putAll(school+AreaName+checkdate+"dataTwo",listMap);
        return listMap;
    }

    @Override
    public List<Map> dataThree(String school, String CityName, String AreaName, String checkdate,String checkType) {
        List<Map> studentList = shaichaStudentDao.studentList(school, CityName, AreaName, checkdate);
        String xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        for (int a = 0; a<studentList.size();a++){
            Map student = studentList.get(a);
            String grade = student.get("grade")==null ? "" :xueBu(xueBu,student.get("grade").toString());
            studentList.get(a).put("grade",grade);
            studentList.get(a).put("checkdate",checkdate);
            studentList.get(a).put("checkType",checkType);
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

    @Override
    public List<Map> dataSix(String school, String CityName, String AreaName, String checkdate) {
        String xueBu = shaichaStudentDao.getXueBu(school, CityName, AreaName, checkdate);
        List<Map> gradeNumberList = shaichaStudentDao.gradeNumber(school, CityName, AreaName, checkdate);
        for(int s = 0;s<gradeNumberList.size();s++){
            Map map = gradeNumberList.get(s);
            gradeNumberList.get(s).put("name",map.get("name")==null ? "" :xueBu(xueBu,map.get("name").toString()));
        }
        redisTemplate.opsForList().rightPushAll(school+AreaName+checkdate+"dataSix",gradeNumberList);
        return gradeNumberList;
    }

    @Override
    public Map gerenAdvice(String name, String idCard, String checkdate) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map advice = shaichaStudentDao.advice(name, idCard, checkdate);
        Double luoyanr = Double.parseDouble(advice.get("luoyanr").toString());
        Double luoyanl = Double.parseDouble(advice.get("luoyanl").toString());
        Double daijingr = Double.parseDouble(advice.get("daijingr").toString());
        Double daijingl = Double.parseDouble(advice.get("daijingl").toString());
        Double dengxiaoqiujingr = (double)advice.get("dengxiaoqiujingr");
        Double dengxiaoqiujingl = (double)advice.get("dengxiaoqiujingl");
        Double zhujingr = (double)advice.get("zhujingr");
        Double zhujingl = (double)advice.get("zhujingl");
        advice(params,luoyanr,luoyanl,dengxiaoqiujingr,dengxiaoqiujingl,zhujingr,zhujingl,daijingr,daijingl);
        return params;
    }

    @Override
    public List<Map> table(String name, String idCard) {
        List<Map> table = shaichaStudentDao.table(name, idCard);
        return table;
    }
    @Override
    public Map report(String name, String idCard, String checkdate) {
        return null;
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
}
