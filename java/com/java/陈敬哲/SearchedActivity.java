package com.java.陈敬哲;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchedActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    private List<NewsBean.DataBean> currentnews =new ArrayList<>();  //总的
    private RecyclerView nv; //新闻列表
    private SwipeRefreshLayout refreshLayout;
    private NewsAdapter adapter_n= new NewsAdapter(currentnews,SearchedActivity.this,true);;
    private int lastVisibleItem = 0;
    private GridLayoutManager mLayoutManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private NewsBean now;
    private int page_high=1,all_pages;
    private String sort;
    private List<NewsBean.DataBean> newDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        sort=getIntent().getStringExtra("key");
        History.add(sort);
        refreshLayout = findViewById(R.id.swipeRefresLayout0);
        refreshLayout.setOnRefreshListener(this);
        new Thread(){
            public void run(){
                now=NewsBean.getNewsList(5,page_high,"","",sort,"");
                if(now.getPageSize()>1)
                    currentnews.addAll(now.getData());
                all_pages=now.getTotal()/5;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter_n.notifyDataSetChanged();
                    }
                });
            }
        }.start();
        nv=(RecyclerView) findViewById(R.id.searched_list);
        mLayoutManager = new GridLayoutManager(this, 1);
        nv.setLayoutManager(mLayoutManager);
        nv.setItemAnimator(new DefaultItemAnimator());
        nv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView nv, int newState) {
                super.onScrollStateChanged(nv, newState);
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1，自己可以算一下
                    if (adapter_n.isFadeTips() == false && lastVisibleItem + 1 == adapter_n.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView();
                            }
                        }, 5);
                    }

                    // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
                    if (adapter_n.isFadeTips() == true && lastVisibleItem + 2 == adapter_n.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView();
                            }
                        }, 5);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView nv, int dx, int dy) {
                super.onScrolled(nv, dx, dy);
                // 在滑动完成后，拿到最后一个可见的item的位置
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        nv.setAdapter(adapter_n);
        onRefresh();
        ConstraintLayout layout1=findViewById(R.id.layout1);
        if(Data.theme==false){
            layout1.setBackgroundColor(Color.BLACK);
        }
    }

    private void updateRecyclerView() {
        if (all_pages >page_high) {
            new Thread(){
                public void run(){
                    page_high++;
                    now=NewsBean.getNewsList(5,page_high,"","",sort,"");
                    if(now.getPageSize()>1) {
                        newDatas=now.getData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter_n.updateList(newDatas, true);
                            }
                        });
                    }
                    else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter_n.updateList(null, false);
                        }
                    });

                }
            }.start();

        } else
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter_n.updateList(null, false);
                }
            });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }
}
