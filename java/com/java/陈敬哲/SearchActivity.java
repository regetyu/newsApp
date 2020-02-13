package com.java.陈敬哲;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et;
    private ArrayList<String> used;
    private KeyAdapter adapter_k;
    private Intent data=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et=(EditText) findViewById(R.id.search_text);
        Button get = (Button) findViewById(R.id.search_button);
        get.setOnClickListener(this);
        get.getBackground().setAlpha(100);

        used = getIntent().getStringArrayListExtra("infoList");
        data.putExtra("keylist",used);
        setResult(1, data);
        RecyclerView rv=(RecyclerView) findViewById(R.id.used_key);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        rv.setLayoutManager(layoutManager);
        adapter_k = new KeyAdapter(used,SearchActivity.this);
        rv.setAdapter(adapter_k);

        LinearLayout layout1=findViewById(R.id.layout1);
        if(Data.theme==false){
            layout1.setBackgroundColor(Color.BLACK);
        }
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.search_button:
                String searchText =et.getText().toString();
                toSearch(searchText);
                break;

            default:
        }
    }
    public void toSearch(String key){
        used.add(key);
        adapter_k.notifyDataSetChanged();
        data.putExtra("keylist",used);
        setResult(1, data);
        Intent intent=new Intent(SearchActivity.this,SearchedActivity.class);
        intent.putExtra("key",key);
        startActivity(intent);
    }

}
