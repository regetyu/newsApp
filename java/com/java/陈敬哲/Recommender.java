package com.java.陈敬哲;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.*;

/**
 * 用于推荐新闻
 */
public class Recommender {
    private static String keyword = "";
    private static File recommenderFile = new File(Environment.getExternalStorageDirectory(), "newRecommmender");;

    public static void refresh(NewsBean.DataBean news){
        keyword = news.getKeywords().get(0).getWord();
        save();
    }
    /**
     * 获取推荐页面的新闻
     * 尚未完成
     * @return 推荐的新闻
     */
    public static NewsBean getRecommendNews(int page){
        Date currentDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return NewsBean.getNewsList(5, page, "", ft.format(currentDate), keyword, "");
    }

    public static void save(){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(recommenderFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(keyword);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void load(){
        try{
            FileInputStream fileInputStream = new FileInputStream(recommenderFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            keyword = (String) objectInputStream.readObject();
        }catch (Exception e){
            keyword = "";
            save();
        }
    }
}
