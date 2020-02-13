package com.java.陈敬哲;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

/**
 * 保存的新闻id构成的一个集合
 */
public class SavedSet implements Serializable {
    private static HashSet <String> idSet;
    private static File savedSetFile = new File(Environment.getExternalStorageDirectory(), "savedSet");;
    private SavedSet(){}
    /**
     * 返回保存的id列表
     * 好像不需要调用
     * @return 保存的id列表
     */
    public static HashSet<String> get() {
        return idSet;
    }

    /**
     * 不需要主动调用
     */
    public static void add(String newsId){
        idSet.add(newsId);
        save();
    }

    /**
     * 删除保存
     * 实际上不需要删除
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
            FileInputStream fileInputStream = new FileInputStream(savedSetFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            idSet = (HashSet <String>) objectInputStream.readObject();
        }catch (Exception e){
            idSet = new HashSet<>();
            save();
        }
    }

    /**
     * 不需要主动调用
     */
    public static void save(){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(savedSetFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(idSet);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * 判断新闻ID是否在保存列表中
     * @param newsID 新闻ID
     * @return 是否在保存列表中
     */
    public static boolean contains(String newsID){
        return idSet.contains(newsID);
    }
}
