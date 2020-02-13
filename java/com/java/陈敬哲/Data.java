package com.java.陈敬哲;


import android.app.Application;

public class Data extends Application {
    static String sort;
    static int page_high=4,page_low=2;
    static int total_pages=0;
    static NewsBean.DataBean reading;
    static boolean theme=true;//true表示非夜间模式
    static boolean login=false;//false表示未登录
    static boolean connected=false;//是否连接过一次
    static String user;
}