package com.xinshineng.common.utils;

public class ShiLiZhuanHuanUtils {
    
    public static String zhuanhuanshiliForSc(String shili){

        if ("4.0".equals(shili)){
            return shili="0.1";
        }

        if ("4.1".equals(shili)){
            return shili="0.12";
        }
        if ("4.2".equals(shili)){
            return shili="0.15";
        }
        if ("4.3".equals(shili)){
            return shili="0.2";
        }
        if ("4.4".equals(shili)){
            return shili="0.25";
        }
        if ("4.5".equals(shili)){
            return shili="0.3";
        }
        if ("4.6".equals(shili)){
            return shili="0.4";
        }
        if ("4.7".equals(shili)){
            return shili="0.5";
        }
        if ("4.8".equals(shili)){
            return shili="0.6";
        }
        if ("4.9".equals(shili)){
            return shili="0.8";
        }
        if ("5.0".equals(shili)){
            return shili="1.0";
        }

        if (Double.parseDouble(shili)<4.0){
            return shili="0.1";
        }
        if (Double.parseDouble(shili)>5.0){
            return shili="1.0";
        }
        return shili;
    }

    public static String zhuanhuanshiliForLdFenShu(String shili){
        if ("10/100".equals(shili)){
            return shili="0.1";
        }
        if ("10/80".equals(shili)){
            return shili="0.12";
        }
        if ("10/60".equals(shili)){
            return  shili="0.15";
        }
        if ("10/50".equals(shili)){
            return  shili="0.2";
        }
        if ("10/40".equals(shili)){
            return  shili="0.25";
        }
        if ("10/30".equals(shili)){
            return shili="0.3";
        }
        if ("10/25".equals(shili)){
            return  shili="0.4";
        }
        if ("10/20".equals(shili)){
            return shili="0.5";
        }
        if ("10/15".equals(shili)){
            return shili="0.6";
        }
        if ("10/12.5".equals(shili)){
            return shili="0.8";
        }
        if ("10/10".equals(shili)){
            return shili="1.0";
        }
        return shili;
    }


    public static Double zhuanshuanshiliForLd(Double shili){
        if (shili<0.1){
            return 4.0;
        }
        if (shili>1.0){
            return 5.0;
        }
        if (shili==0.1){
            return 4.0;
        }
        if (shili==0.12){
            return 4.1;
        }
        if (shili==0.15){
            return 4.2;
        }
        if (shili==0.2){
            return 4.3;
        }
        if (shili==0.25){
            return 4.4;
        }
        if (shili==0.3){
            return 4.5;
        }
        if (shili == 0.4){
            return 4.6;
        }
        if (shili==0.5){
            return 4.7;
        }
        if (shili==0.6){
            return 4.8;
        }
        if (shili==0.8){
            return 4.9;
        }
        if (shili==1.0){
            return 5.0;
        }
        if (shili>0.1 && shili<0.12){
            return (shili-0.1)>(0.12-shili)?4.1:4.0;
        }
        if (shili>0.12 && shili<0.15){
            return (shili-0.12)>(0.15-shili)?4.2:4.1;
        }
        if (shili>0.15 && shili<0.2){
            return (shili-0.15)>(0.2-shili)?4.3:4.2;
        }
        if (shili>0.2 && shili<0.25){
            return (shili-0.2)>(0.25-shili)?4.4:4.3;
        }
        if (shili>0.25 && shili<0.3){
            return (shili-0.25)>(0.3-shili)?4.5:4.4;
        }
        if (shili>0.3 && shili<0.4){
            return (shili-0.3)>(0.4-shili)?4.6:4.5;
        }
        if (shili>0.4 && shili<0.5){
            return (shili-0.4)>(0.5-shili)?4.7:4.6;
        }
        if (shili>0.5 && shili<0.6){
            return (shili-0.5)>(0.6-shili)?4.8:4.7;
        }
        if (shili>0.6 && shili<0.8){
            return (shili-0.6)>(0.8-shili)?4.9:4.8;
        }
        if (shili>0.8 && shili<1.0){
            return (shili-0.8)>(1.0-shili)?5.0:4.9;
        }
        return shili;
    }

}
