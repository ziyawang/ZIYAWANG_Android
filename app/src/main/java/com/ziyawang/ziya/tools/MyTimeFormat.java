package com.ziyawang.ziya.tools;

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
public class MyTimeFormat {

    public static String changeTimeToDHMS(long time) {

        //将时间戳改变
        int day = (int)time / 86400 ;
        int hour = (int)(time % 86400 ) / 3600 ;
        int min = (int) (time % 3600) / 60 ;
        int seconds = (int )(time % 60 ) ;
        return day+"" +hour + "" + min +"" +seconds+"" ;

    }

    public static String changeTimeMS(int time){

        int temTime = time / 1000 ;

        String a = "00" ;
        String b = "00" ;

        int min = temTime / 60 ;
        int second = (temTime % 60 ) ;
        if (min < 10 ){
            a= "0" + min ;
        }else {
            a= "" + min ;
        }

        if (second < 10 ){
            b= "0" + second ;
        }else {
            b = "" + second ;
        }

        return a +":" + b ;
    }
}


