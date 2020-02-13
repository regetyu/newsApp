package com.java.陈敬哲;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * 搜索历史
 */
public class History implements Serializable {
    private static ArrayList<String> list;
    private static File historyFile = new File(Environment.getExternalStorageDirectory(), "history");;
    private History(){}

    /**
     * 获取搜索历史记录
     * 显示历史记录的时候调用
     * @return 搜索历史记录
     */
    public static ArrayList<String> get(){
        return list;
    }

    /**
     * 不需要主动调用
     */
    public static void add(String keyword){
        list.add(keyword);
        save();
    }

    /**
     * 删除搜索历史记录
     * 用户删除历史记录的时候调用
     * @param keyword 搜索关键词
     */
    public static void remove(String keyword){
        list.remove(keyword);
        save();
    }

    /**
     * 读取本地配置
     * 初始化的时候调用
     */
    public static void load(){
        try{
            FileInputStream fileInputStream = new FileInputStream(historyFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            list = (ArrayList<String>) objectInputStream.readObject();
        }catch (Exception e){
            list = new ArrayList<>();
            save();
        }
    }

    /**
     * 不需要主动调用
     */
    public static void save(){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(historyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void save(Object object){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(historyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * 清空历史记录
     * 用户清空历史记录的时候调用
     */
    public static void clear(){
        list.clear();
        save();
    }

}
