package com.java.陈敬哲;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.annotation.RequiresPermission;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类包含了一次搜索结果的全部信息
 * @author 陈敬哲
 */

public class NewsBean {

    private static File searchResultCacheFile = new File(Environment.getExternalStorageDirectory(), "searchResultCache");;
    private int pageSize;
    private int total;
    private int currentPage;
    private List<DataBean> data;

    /**
     * 获取当前页面的新闻数
     * @return 当前页面的新闻数(似乎目前就是搜索的条数?)
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 获取总新闻数
     * @return 总新闻数
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取当前所在的页数
     * @return 当前所在的页数(似乎总是1)
     */
    public int getCurrentPage() {
        return currentPage;
    }


    /**
     * 获取新闻中最主要的信息
     * @return 新闻中最主要的信息
     */
    public List<DataBean> getData() {
        return data;
    }

    /**
     * 这个类包含了一条新闻中的全部信息
     */
    public static class DataBean implements Serializable{

        private String image;
        private String publishTime;
        private String language;
        private String video;
        private String title;
        private String content;
        private String url;
        private String newsID;
        private String publisher;
        private String category;
        private List<KeywordsBean> keywords;
        private List<WhenBean> when;
        private List<PersonsBean> persons;
        private List<OrganizationsBean> organizations;
        private List<LocationsBean> locations;
        private List<WhereBean> where;
        private List<WhoBean> who;

        /**
         * 获取图片信息
         * @return 图片信息
         */
        public String getImage() {
            return image;
        }

        /**
         * 获取时间
         * @return 时间
         */
        public String getPublishTime() {
            return publishTime;
        }

        /**
         * 获取语言
         * @return 语言
         */
        public String getLanguage() {
            return language;
        }

        /**
         * 获取视频
         * @return 视频
         */
        public String getVideo() {
            return video;
        }

        /**
         * 获取标题
         * @return 标题
         */
        public String getTitle() {
            return title;
        }

        /**
         * 获取正文
         * @return 正文
         */
        public String getContent() {
            return content;
        }

        /**
         * 获取原始网址
         * @return 原始网址
         */
        public String getUrl() {
            return url;
        }

        /**
         * 获取新闻ID
         * @return 新闻ID
         */
        public String getNewsID() {
            return newsID;
        }

        /**
         * 获取发布者
         * @return 发布者
         */
        public String getPublisher() {
            return publisher;
        }

        /**
         * 获取新闻类型
         * @return 新闻类型
         */
        public String getCategory() {
            return category;
        }

        /**
         * 获取关键词信息
         * @return 关键词信息
         */
        public List<KeywordsBean> getKeywords() {
            return keywords;
        }

        /**
         * 获取时间信息
         * @return 时间信息
         */
        public List<WhenBean> getWhen() {
            return when;
        }

        /**
         * 获取人物信息
         * @return 人物信息
         */
        public List<PersonsBean> getPersons() {
            return persons;
        }

        /**
         * 获取组织信息
         * @return 组织信息
         */
        public List<OrganizationsBean> getOrganizations() {
            return organizations;
        }

        /**
         * 获取位置信息
         * @return 位置信息
         */
        public List<LocationsBean> getLocations() {
            return locations;
        }

        /**
         * 获取位置信息2
         * @return 位置信息2
         */
        public List<WhereBean> getWhere() {
            return where;
        }

        /**
         * 获取人物信息2
         * @return 人物信息2
         */
        public List<WhoBean> getWho() {
            return who;
        }

        /**
         * 这个类包含了一条新闻的关键词信息
         */
        public static class KeywordsBean implements Serializable{

            private double score;
            private String word;

            /**
             * 获取关键词的权重
             * @return 关键词的权重
             */
            public double getScore() {
                return score;
            }

            /**
             * 获取关键词
             * @return 关键词
             */
            public String getWord() {
                return word;
            }
        }

        /**
         * 这个类包含了一条新闻的时间信息
         */
        public static class WhenBean implements Serializable{

            private double score;
            private String word;

            /**
             * 获取时间信息的权重
             * @return 时间信息的权重
             */
            public double getScore() {
                return score;
            }

            /**
             * 获取时间信息
             * @return 时间信息
             */
            public String getWord() {
                return word;
            }
        }

        /**
         * 这个类包含了一条新闻的人物信息
         */
        public static class PersonsBean implements Serializable{

            private double count;
            private String linkedURL;
            private String mention;

            /**
             * 获取人物的出现次数
             * @return 人物的出现次数
             */
            public double getCount() {
                return count;
            }

            /**
             * 获取与人物相关的url
             * @return 与人物相关的url
             */
            public String getLinkedURL() {
                return linkedURL;
            }

            /**
             * 获取人物名称
             * @return 人物名称
             */
            public String getMention() {
                return mention;
            }

        }

        /**
         * 这个类包含了一条新闻的组织信息
         */
        public static class OrganizationsBean implements Serializable{

            private int count;
            private String linkedURL;
            private String mention;

            /**
             * 获取组织出现的次数
             * @return 组织出现的次数
             */
            public int getCount() {
                return count;
            }

            /**
             * 获取与组织相关的url
             * @return 与组织相关的url
             */
            public String getLinkedURL() {
                return linkedURL;
            }

            /**
             * 获取组织名称
             * @return 组织名称
             */
            public String getMention() {
                return mention;
            }
        }

        /**
         * 这个类包含了一条新闻的位置信息
         */
        public static class LocationsBean implements Serializable{

            private double lng;
            private int count;
            private String linkedURL;
            private double lat;
            private String mention;

            /**
             * 获取经度
             * @return 经度
             */
            public double getLng() {
                return lng;
            }

            /**
             * 获取位置出现次数
             * @return 位置出现次数
             */
            public int getCount() {
                return count;
            }

            /**
             * 获取与位置相关的url
             * @return 与位置相关的url
             */
            public String getLinkedURL() {
                return linkedURL;
            }

            /**
             * 获取纬度
             * @return 纬度
             */
            public double getLat() {
                return lat;
            }

            /**
             * 获取位置名称
             * @return 位置名称
             */
            public String getMention() {
                return mention;
            }
        }

        /**
         * 这个类包含了一条新闻的位置信息2
         */
        public static class WhereBean implements Serializable{

            private double score;
            private String word;

            /**
             * 获取位置信息的权重
             * @return 位置信息的权重
             */
            public double getScore() {
                return score;
            }

            /**
             * 获取位置信息的名称
             * @return 位置信息的名称
             */
            public String getWord() {
                return word;
            }
        }

        /**
         * 这个类包含了一条新闻的人物信息2
         */
        public static class WhoBean implements Serializable{

            private double score;
            private String word;

            /**
             * 获取人物信息的权重
             * @return 人物信息的权重
             */
            public double getScore() {
                return score;
            }

            /**
             * 获取人物的名称
             * @return 人物的名称
             */
            public String getWord() {
                return word;
            }
        }

        /**
         * 读取本地的新闻
         * 离线打开新闻的时候调用
         * @param newsID 新闻的ID
         * @return 本地存储的新闻的信息
         */
        public static DataBean getLocalNews(String newsID){
            try{
                FileInputStream fileInputStream = new FileInputStream(new File(Environment.getExternalStorageDirectory(), newsID));
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                return (DataBean) objectInputStream.readObject();
            }catch (Exception e){
                System.out.println(e);
                return new DataBean();
            }
        }

        public static ArrayList <Bitmap> getBmp(String image){
//            Bitmap bmp;
//            try{
//                if (image.charAt(1) == ']'){
//                    System.out.println("gen!!!");
//                    return null;
//                }
//                else{
//                    int size = image.length();
//                    int index = 0;
//                    for (int i = 0; i < size; i++){
//                        char ch = image.charAt(i);
//                        if (ch == ']' || ch == ','){
//                            index = i;
//                            break;
//                        }
//                    }
//                    String str = image.substring(1, index);
//                    System.out.println("gen!!! " + str);
//                    URL bmpURL = new URL(str);
//                    URLConnection urlConnection = bmpURL.openConnection();
//                    InputStream inputStream = urlConnection.getInputStream();
//                    bmp = BitmapFactory.decodeStream(inputStream);
//                    return bmp;
//                }
//            }catch (Exception e){
//                System.out.println(e);
//                return null;
//            }
            ArrayList <Bitmap> bitmaps = new ArrayList<>();
            try{
                Pattern pattern = Pattern.compile("http[^,\\]]*");
                Matcher matcher = pattern.matcher(image);
                while (matcher.find()){
                    String str = matcher.group();
                    System.out.println("BMP!!!" + str);
                    URL bmpURL = new URL(str);
                    URLConnection urlConnection = bmpURL.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    bitmaps.add(BitmapFactory.decodeStream(inputStream));
                }
            } catch (Exception e){
                System.out.println(e);
            }
            return bitmaps;
        }
        /**
         * 保存新闻到本地
         * 更新推荐信息
         * 在点开一个新闻后调用
         */
        public void save(){
            if (!SavedSet.contains(newsID)){
                try{
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), newsID));
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(this);
                    SavedSet.add(newsID);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            Recommender.refresh(this);
        }

        /**
         * 收藏的时候调用
         */
        public void collect(){
            CollectedSet.add(newsID);
        }

        /**
         * 取消收藏的时候调用
         */
        public void unCollect(){
            CollectedSet.remove(newsID);
        }
    }

    /**
     * 获取搜索结果
     * 自动保存搜索历史记录
     * @param size 新闻篇数, 未填写请传递负值
     * @param startDate 开始时间, 格式为yyyy-mm-dd hh-mm-ss, 未填写请传递空字符串
     * @param endDate 结束时间, 格式为yyyy-mm-dd hh-mm-ss, 未填写请传递空字符串
     * @param words 关键词, 未填写请传递空字符串
     * @param categories 类型, 参数可以是：娱乐、军事、教育、文化、健康、财经、体育、汽车、科技、社会, 未填写请传递空字符串
     * @return 搜索结果
     */
    public static NewsBean getSearchResult(int size, int page, String startDate, String endDate, String words, String categories){
        NewsBean newsBean = getNewsList(size, page, startDate, endDate, words, categories);
        History.add(words);
        return newsBean;
    }

    /**
     * 不需要主动调用
     */

    public static NewsBean getNewsList(int size, int page, String startDate, String endDate, String words, String categories){
        startDate = startDate.replace(" ", "%20");
        endDate = endDate.replace(" ", "%20");
        words = URLEncoder.encode(words);
        categories = URLEncoder.encode(categories);
        String _size;
        if (size < 0)
            _size = "";
        else
            _size = String.valueOf(size);
        try{
            URL url = new URL("https://api2.newsminer.net/svc/news/queryNewsList?size=" + _size + "&page=" + page + "&startDate=" + startDate + "&endDate=" + endDate + "&words=" + words + "&categories=" + categories);
            System.out.println(url);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String inputLine;
            PrintStream printStream = new PrintStream(searchResultCacheFile);
            while ((inputLine = bufferedReader.readLine()) != null)
                printStream.println(inputLine);
            bufferedReader.close();
            FileInputStream in = new FileInputStream(searchResultCacheFile);
            Reader reader = new InputStreamReader(in, "UTF-8");
            Gson gson = new GsonBuilder().create();
            NewsBean newsBean = gson.fromJson(reader, NewsBean.class);
            return newsBean;
        }catch (Exception e){
            return new NewsBean();
        }
    }

    public static Bitmap getFirstImage(String image){
        Bitmap bitmap = null;
        try{
            Pattern pattern = Pattern.compile("http[^,\\]]*");
            Matcher matcher = pattern.matcher(image);
            if (matcher.find()){
                String str = matcher.group();
                System.out.println("BMP!!!" + str);
                URL bmpURL = new URL(str);
                URLConnection urlConnection = bmpURL.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                bitmap = (BitmapFactory.decodeStream(inputStream));
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return bitmap;
    }

    /**
     * 默认进入某一个分类界面显示的新闻
     * @param categories 新闻的分类
     * @return 该分类的最新新闻
     */
    public static NewsBean getLatestNews(String categories, int page){
        if (categories.equals("推荐"))
            return Recommender.getRecommendNews(page);
        Date currentDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return getNewsList(5, page, "", ft.format(currentDate), "", categories);
    }
}