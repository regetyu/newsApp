package com.java.陈敬哲;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * 收藏的新闻的id组成的一个集合
 */
public class CollectedSet implements Serializable {
    private static LinkedHashSet<String> idSet;
    private static File collectedSetFile = new File(Environment.getExternalStorageDirectory(), "collectedSet");
    private CollectedSet(){}

    /**
     * 获取收藏的id列表
     * 用于显示收藏的新闻内容
     * @return 收藏的id列表
     */
    public static LinkedHashSet<String> get() {
        return idSet;
    }

    /**
     * 添加收藏
     * 用户收藏某个新闻的时候调用
     * 自动保存配置文件到本地
     */
    public static void add(String newsId){
        idSet.add(newsId);
        save();
    }

    /**
     * 删除收藏
     * 用户取消收藏某个新闻的时候调用
     * 自动保存配置文件到本地
     */
    public static void remove(String newsId){
        idSet.remove(newsId);
        save();
    }

    /**
     * 读取本地配置
     * 初始化的时候调用
     */
    public static void load(){
        try{
            FileInputStream fileInputStream = new FileInputStream(collectedSetFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            idSet = (LinkedHashSet<String>) objectInputStream.readObject();
        }catch (Exception e){
            idSet = new LinkedHashSet<>();
            save();
        }
    }

    /**
     * 不需要主动调用
     */
    public static void save(){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(collectedSetFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(idSet);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void save(Object object){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(collectedSetFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * 判断新闻ID是否在收藏夹中
     * @param newsID 新闻ID
     * @return 是否在收藏夹中
     */
    public static boolean contains(String newsID){
        return idSet.contains(newsID);
    }
}
