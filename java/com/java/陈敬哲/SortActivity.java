package com.java.陈敬哲;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SortActivity extends AppCompatActivity {
    private List<String> in=new ArrayList<>();
    private List<String> out=new ArrayList<>();

    ChoiceAdapter adapter_s0 = new ChoiceAdapter(in);
    ChoiceAdapter adapter_s1 = new ChoiceAdapter(out);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        final RecyclerView rv0=(RecyclerView) findViewById(R.id.exist_sort);
        final RecyclerView rv1=(RecyclerView) findViewById(R.id.delete_sort);

        LinearLayoutManager layoutManager0 = new LinearLayoutManager(SortActivity.this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(SortActivity.this);
        rv1.setLayoutManager(layoutManager1);
        rv0.setLayoutManager(layoutManager0);
        ItemTouchHelper helper0 = new ItemTouchHelper(new MyItemTouchCallback(adapter_s0,adapter_s1,this));
        helper0.attachToRecyclerView(rv0);
        ItemTouchHelper helper1 = new ItemTouchHelper(new MyItemTouchCallback(adapter_s1,adapter_s0,this));
        helper1.attachToRecyclerView(rv1);
        new Thread(){
            public void run(){
                in.addAll(CategoryList.getInList());
                out.addAll(CategoryList.getOutList());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter_s0.notifyDataSetChanged();
                        adapter_s1.notifyDataSetChanged();
                    }
                });
                rv0.setAdapter(adapter_s0);
                rv1.setAdapter(adapter_s1);
            }
        }.start();

        LinearLayout layout1=findViewById(R.id.layout1);
        if(Data.theme==false){
            layout1.setBackgroundColor(Color.BLACK);
            TextView t1=findViewById(R.id.t1);
            TextView t2=findViewById(R.id.t2);
            t1.setTextColor(Color.GRAY);
            t2.setTextColor(Color.GRAY);
        }
    }
}
