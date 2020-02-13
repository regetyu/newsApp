package com.java.陈敬哲;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.service.autofill.CustomDescription;
import android.util.Log;
import android.view.View;

//import com.chad.library.adapter_n.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.graphics.Color;

//import com.chad.library.adapter_n.base.BaseQuickAdapter;
//import com.chad.library.adapter_n.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;




public class MainActivity extends AppCompatActivity implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener {
    private Button button_add;
    private Button button_search;
    private List<String> choices=new ArrayList<>();
    private List<NewsBean.DataBean> currentnews =new ArrayList<>();  //总的
    private ArrayList<String> keys=new ArrayList<>();  //用过的搜索词
    private RecyclerView rv;
    private RecyclerView nv; //新闻列表
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout layout1;
    private int loadMore=0;
    private int count=0;
    private NewsBean now;
    private NewsAdapter adapter_n=new NewsAdapter(currentnews,MainActivity.this,true);
    private SortAdapter adapter_s=new SortAdapter(choices,this,currentnews,adapter_n,refreshLayout);
    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 5;
    private GridLayoutManager mLayoutManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private final static String TAG="MainActivity";//输出调试用
    private Button button_change;
    private Button button_log;


    private List<NewsBean.DataBean> newDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_add = (Button) findViewById(R.id.add);
        button_add.setOnClickListener(this);  //所有button都要设置啊
        button_search = (Button) findViewById(R.id.to_search);
        button_search.setOnClickListener(this);
        button_log = (Button) findViewById(R.id.login);
        button_log.setOnClickListener(this);
        button_change = (Button) findViewById(R.id.change);
        button_change.setOnClickListener(this);
        button_change.setText("非夜间模式");
        refreshLayout = findViewById(R.id.swipeRefresLayout);
        refreshLayout.setOnRefreshListener(this);
        layout1=findViewById(R.id.layout1);

        new Thread(){
            public void run(){
                CategoryList.load();
                CollectedSet.load();
                History.load();
                keys=History.get();
                SavedSet.load();
                Recommender.load();
                Data.sort=CategoryList.getInList().get(0);
                now=NewsBean.getLatestNews("娱乐",3);
                if(now.getTotal()!=0)
                    currentnews.addAll(now.getData());
                Data.total_pages=now.getTotal()/5;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter_n.notifyDataSetChanged();
                    }
                });
            }
        }.start();
        System.out.println("Thread terminate!!!");
        nv=(RecyclerView) findViewById(R.id.main_list);
        mLayoutManager = new GridLayoutManager(this, 1);
        nv.setLayoutManager(mLayoutManager);
        nv.setItemAnimator(new DefaultItemAnimator());
        nv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView nv, int newState) {
                super.onScrollStateChanged(nv, newState);
                if(Data.sort=="收藏"||Data.sort=="历史记录")
                    return;
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1，自己可以算一下
                    if (adapter_n.isFadeTips() == false && lastVisibleItem + 1 == adapter_n.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 然后调用updateRecyclerview方法更新RecyclerView
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
        rv=(RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter_s);
        button_log.getBackground().setAlpha(100);//0~255透明度值
        button_change.getBackground().setAlpha(100);
        button_search.getBackground().setAlpha(100);
        button_add.getBackground().setAlpha(100);
    }


    private void updateRecyclerView() {
        if (Data.total_pages >Data.page_high) {
            new Thread(){
                public void run(){
                    now=NewsBean.getLatestNews(Data.sort,Data.page_high);
                    if(now.getTotal()>5) {
                        newDatas=now.getData();
                        Data.page_high++;
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
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter_n.updateList(null, false);
                }
            });
        }
    }
    private void updateRecyclerViewAsFirst() {
        System.out.println(Data.page_low);
        if (Data.page_low>0) {
            new Thread(){
                public void run(){
                    now=NewsBean.getLatestNews(Data.sort,Data.page_low);
                    if(now.getTotal()>5) {
                        newDatas=now.getData();
                        Data.page_low--;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter_n.updateListAsFirst(newDatas, true);
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
    public void onRefresh() {
        if(Data.sort=="收藏"||Data.sort=="历史记录"){
            refreshLayout.setRefreshing(false);
            return;
        }
        refreshLayout.setRefreshing(true);
        //adapter_n.resetDatas();
        updateRecyclerViewAsFirst();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter_n.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }, 1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        choices.clear();
        choices.add("历史记录");
        choices.add("收藏");
        choices.add("推荐");
        if(CategoryList.getInList()!=null)
            choices.addAll(CategoryList.getInList());
        if(choices.size()>3)
            Data.sort=choices.get(3);
        else
            Data.sort="推荐";
        System.out.println("back");
        adapter_s.notifyDataSetChanged();
        new Thread(){//重新loadset
            public void run(){
                CollectedSet.load();
                History.load();
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)
            keys = data.getExtras().getStringArrayList("keylist");
    }



    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.to_search:
                Intent intent1=new Intent(MainActivity.this,SearchActivity.class);
                intent1.putStringArrayListExtra("infoList", keys);
                //startActivity(intent2);
                startActivityForResult(intent1, 1);
                break;
            case R.id.add:
                Intent intent2=new Intent(MainActivity.this,SortActivity.class);
                //intent2.putExtra("adapter", adapter_s);
                startActivity(intent2);
                //startActivityForResult(intent2, 2);
                break;
            case R.id.login:
                Intent intent3=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent3);
                break;
            case R.id.change:

                if(Data.theme==false){
                    Data.theme=true;
                    button_change.setText("非夜间模式");
                    layout1.setBackgroundColor(Color.WHITE);
                    //button_change.setBackgroundColor(Color.WHITE);
                    //button_search.setBackgroundColor(Color.WHITE);
                   // button_add.setBackgroundColor(Color.WHITE);
                   // button_change.setBackgroundColor(Color.WHITE);
                }
                else{
                    Data.theme=false;
                    button_change.setText("夜间模式");
                    layout1.setBackgroundColor(Color.BLACK);


                }
                break;
            default:
        }
    }

}