package com.java.陈敬哲;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private List<String> sorts;
    private NewsBean news; //引用
    private NewsAdapter newsAdapter;
    private List<NewsBean.DataBean> load=new ArrayList<>();
    private Context context;
    private SwipeRefreshLayout refreshLayout;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View sortView;
        TextView name;
        public ViewHolder(View view){
            super(view);
            sortView=view;
            name=(TextView) view.findViewById(R.id.sort_name);
        }
    }
    public SortAdapter(List<String> lists, Context context,
                       List<NewsBean.DataBean> sub, NewsAdapter newsAdapter,
                       SwipeRefreshLayout refreshLayout){
        sorts=lists;
        Log.d("adapter",lists.size()+"");
        this.newsAdapter=newsAdapter;
        this.refreshLayout=refreshLayout;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sort,parent,false);
        final ViewHolder vh2=new ViewHolder(view);
        vh2.sortView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int position =vh2.getAdapterPosition();
                final String s=sorts.get(position);
                Data.sort = s;
                new Thread() {
                    public void run() {
                        if(s=="收藏"||s=="历史记录"){
                            HashSet<String> set;
                            load.clear();
                            if(s=="收藏")
                                set=CollectedSet.get();
                            else
                                set=SavedSet.get();
                            NewsBean.DataBean loadnews;
                            for(String loadnewsid:set){
                                loadnews=NewsBean.DataBean.getLocalNews(loadnewsid);
                                load.add(loadnews);
                            }

                            ((MainActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    newsAdapter.resetDatas();
                                    newsAdapter.updateList(load, false);
                                }
                            });

                        }
                        else {
                            news = NewsBean.getLatestNews(s, 3);
                            if(news.getPageSize()>1){
                                Data.total_pages = news.getTotal() / 5;
                                Data.page_high = 4;
                                Data.page_low = 2;
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        newsAdapter.resetDatas();
                                        newsAdapter.updateList(news.getData(), true);
                                    }
                                });
                            }

                        }

                    }

                }.start();
            }
        });

        //还可写vh2.name.setOnClick....
        return vh2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh2, int position) {
        String s=sorts.get(position);
        vh2.name.setText(s);
    }


    @Override
    public int getItemCount() {
        return sorts.size();
    }
}
