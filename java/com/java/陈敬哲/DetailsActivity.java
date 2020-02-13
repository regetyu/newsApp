package com.java.陈敬哲;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private TextView content;
    private TextView time;
    private TextView publisher;
    final private ImageView[] img=new ImageView[4];
    private Button collect;
    private Button share;
    private NewsBean.DataBean now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title = findViewById(R.id.details_title);
        content = findViewById(R.id.details_content);
        time = findViewById(R.id.details_time);
        publisher = findViewById(R.id.details_publisher);
        img[0]=findViewById(R.id.details_img0);
        img[1]=findViewById(R.id.details_img1);
        img[2]=findViewById(R.id.details_img2);
        img[3]=findViewById(R.id.details_img3);
        now=Data.reading;
        title.setText(now.getTitle());
        content.setText(now.getContent());
        time.setText(now.getPublishTime());
        publisher.setText(now.getPublisher());
        final VideoView videoView=findViewById(R.id.videoview);
        CoordinatorLayout layout1=findViewById(R.id.layout1);
        if(Data.theme==false){
            layout1.setBackgroundColor(Color.BLACK);
            title.setTextColor(Color.GRAY);
            content.setTextColor(Color.GRAY);
            time.setTextColor(Color.GRAY);
            publisher.setTextColor(Color.GRAY);
        }

        new Thread(){
            public void run() {
                final ArrayList<Bitmap> bitmaps = NewsBean.DataBean.getBmp(now.getImage());
                for(int i=0;i<4;i++)
                if(bitmaps.size()<i+1)
                {
                    final int j=i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img[j].setVisibility(View.GONE);
                        }
                    });
                }
                else
                {
                    final int j=i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img[j].setImageBitmap(bitmaps.get(j));
                        }
                    });
                }
            }
        }.start();
        if(!now.getVideo().equals(""))
            new Thread(){
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoView.setMediaController(new MediaController(DetailsActivity.this));
                            System.out.println("video"+now.getVideo());
                            Uri uri = Uri.parse(now.getVideo());
                            videoView.setVideoURI(uri);
                            videoView.start();
                            videoView.requestFocus();

                        }
                    });
                }
            }.start();
        else
            videoView.setVisibility(View.GONE);
        collect=(Button) findViewById(R.id.like);
        collect.setOnClickListener(this);
        share=(Button) findViewById(R.id.share);
        share.setOnClickListener(this);
        /*share_pic=(Button) findViewById(R.id.share_pic);
        share_pic.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.like){
            now.collect();
        }
        else if(view.getId()==R.id.share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, now.getContent()+" "+now.getUrl());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,"share"));
        }/*
        else if(view.getId()==R.id.share_pic){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, now.getContent());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,"share"));
        }*/

    }
}
