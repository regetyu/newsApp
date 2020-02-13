package com.java.陈敬哲;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.widget.VideoView;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsBean.DataBean> newslist;
    private Context context;
    private int normalType=0;  //正常的news
    private int footType = 1;       // 第二种ViewType，底部的提示View

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示

    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler
    private ImageView myImg;

    static class NewsHolder extends RecyclerView.ViewHolder{
        View newsView;
        TextView txt0;//title
        ImageView img0;
        TextView publisher;
        TextView time;

        public NewsHolder(View view){
            super(view);
            newsView=view;
            txt0=(TextView) view.findViewById(R.id.main_text);
            //img0=(ImageView) view.findViewById(R.id.main_img);
            publisher=(TextView) view.findViewById(R.id.publisher);
            time=(TextView) view.findViewById(R.id.publish_time);
        }
    }
    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public NewsAdapter(List<NewsBean.DataBean> lists, Context context, boolean hasMore){
        newslist=lists;
        this.context=context;
        this.hasMore=hasMore;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==footType)
        {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item,parent,false);
        final NewsHolder vh2=new NewsHolder(view);
        vh2.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position =vh2.getAdapterPosition();
                final NewsBean.DataBean s=newslist.get(position);
                if(!SavedSet.contains(s.getNewsID())) {
                    new Thread(){
                        public void run(){
                            s.save();
                            if(context instanceof MainActivity)
                                ((MainActivity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                });
                            else
                                ((SearchedActivity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                });
                        }
                    }.start();
                }

                Intent intent=new Intent(context,DetailsActivity.class);
                Data.reading=s;
                context.startActivity(intent);
            }
        });
        //还可写vh2.name.setOnClick....
        return vh2;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh2, final int position) {
        if(vh2 instanceof NewsHolder)
        {
            //myImg=((NewsHolder) vh2).img0;
            final NewsBean.DataBean now=newslist.get(position);
            ((NewsHolder) vh2).txt0.setText(now.getTitle());
            ((NewsHolder) vh2).time.setText(now.getPublishTime());
            ((NewsHolder) vh2).publisher.setText(now.getPublisher());
            ((NewsHolder) vh2).txt0.setTextColor(0xff636363);
            ((NewsHolder) vh2).time.setTextColor(0xff636363);
            ((NewsHolder) vh2).publisher.setTextColor(0xff636363);
            if(SavedSet.contains(now.getNewsID()))
            {
                System.out.println("gray"+now.getTitle());
                ((NewsHolder) vh2).txt0.setTextColor(0xffCCCCCC);
                ((NewsHolder) vh2).time.setTextColor(0xffCCCCCC);
                ((NewsHolder) vh2).publisher.setTextColor(0xffCCCCCC);
            }
        }
        else
        {
            ((FootHolder) vh2).tips.setVisibility(View.VISIBLE);
            // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
            if (hasMore == true) {
                // 不隐藏footView提示
                fadeTips = false;
                if (newslist.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    ((FootHolder) vh2).tips.setText("正在加载更多...");
                }
            } else {
                if (newslist.size() > 0) {
                    // 如果查询数据发现并没有增加时，就显示没有更多数据了
                    ((FootHolder) vh2).tips.setText("没有更多数据了");
                    final FootHolder h=((FootHolder) vh2);
                    // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            h.tips.setVisibility(View.GONE);
                            // 将fadeTips设置true
                            fadeTips = true;
                            // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                            hasMore = true;
                        }
                    }, 500);
                }
            }
            if(Data.theme==false)
                ((FootHolder) vh2).tips.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1)
            return footType;
        return normalType;
    }

    @Override
    public int getItemCount() {
        //return news.size();
        return newslist.size()+1; //+footview
    }
    public boolean isFadeTips() {
        return fadeTips;
    }

    // 暴露接口，下拉刷新时，通过暴露方法将数据源置为空
    public void resetDatas() {
        newslist.clear();
    }

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<NewsBean.DataBean> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            newslist.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
    public void updateListAsFirst(List<NewsBean.DataBean> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            List<NewsBean.DataBean> pre=new ArrayList<>();
            pre.addAll(newslist);
            newslist.clear();
            newslist.addAll(newDatas);
            newslist.addAll(pre);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
}