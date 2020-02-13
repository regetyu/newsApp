package com.java.陈敬哲;

import android.os.Environment;

import java.io.*;
import java.util.*;

/**
 * 这个类用于完成评分标准中的分类列表功能
 * 像今日头条那样, 上面显示已经添加的分类, 下面显示未添加的分类
 *
 */
public class CategoryList implements Serializable {
    private static ArrayList<String> inList;
    private static ArrayList<String> outList;
    private static File inListFile = new File(Environment.getExternalStorageDirectory(), "inList");
    private static File outListFile = new File(Environment.getExternalStorageDirectory(), "outList");
    private CategoryList(){}

    /**
     * 获取已经添加的分类列表
     * 用于已添加的分类列表的显示
     * 初始化和分类列表进行修改后调用
     * @return 已经添加的分类列表
     */
    public static ArrayList<String> getInList() {
        return inList;
    }

    /**
     * 获取未添加的分类列表
     * 用于未添加的分类列表的显示
     * 初始化和分类列表进行修改后调用
     * @return 未添加的分类列表
     */
    public static ArrayList<String> getOutList(){
        return outList;
    }

    /**
     * 添加分类
     * 用户添加某一项到分类列表后调用
     * 调用后自动保存配置文件到本地
     * @param category 要添加的分类
     */
    public static void add(String category){
        inList.add(category);
        outList.remove(category);
        save();
    }

    /**
     * 删除分类
     * 用户删除某一项分类列表后调用
     * 调用后自动保存配置文件到本地
     * @param category 要删除的分类
     */
    public static void remove(String category){
        inList.remove(category);
        outList.add(category);
        save();
    }

    /**
     * 不需要主动调用这个
     */
    public static void save(){
        try{
            FileOutputStream fileOutputStream1 = new FileOutputStream(inListFile);
            FileOutputStream fileOutputStream2 = new FileOutputStream(outListFile);
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream1);
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(fileOutputStream2);
            objectOutputStream1.writeObject(inList);
            objectOutputStream2.writeObject(outList);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * 读取本地配置
     * 初始化的时候调用
     */
    public static void load(){
        try{
            FileInputStream fileInputStream1 = new FileInputStream(inListFile);
            FileInputStream fileInputStream2 = new FileInputStream(outListFile);
            ObjectInputStream objectInputStream1 = new ObjectInputStream(fileInputStream1);
            ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream2);
            inList = (ArrayList<String>) objectInputStream1.readObject();
            outList = (ArrayList<String>) objectInputStream2.readObject();

        }catch (Exception e){
            System.out.println(e);
            inList = new ArrayList<>();
            outList = new ArrayList<>();
            inList.add("娱乐");
            inList.add("军事");
            inList.add("教育");
            inList.add("文化");
            inList.add("健康");
            outList.add("财经");
            outList.add("体育");
            outList.add("汽车");
            outList.add("科技");
            outList.add("社会");
            save();
        }
    }
}