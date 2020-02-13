package com.java.陈敬哲;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.ViewHolder> {
    private List<String> keys;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View keyView;
        TextView name;
        public ViewHolder(View view){
            super(view);
            keyView=view;
            name=(TextView) view.findViewById(R.id.key_name);
        }
    }
    public KeyAdapter(List<String> lists,Context context){
        keys=lists;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.key_item,parent,false);
        final ViewHolder vh2=new ViewHolder(view);
        if(Data.theme==false)
            vh2.name.setTextColor(Color.WHITE);
        vh2.keyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position =vh2.getAdapterPosition();
                String s=keys.get(position);
                keys.add(s);
                Intent intent=new Intent(context,SearchedActivity.class);
                intent.putExtra("key",s);
                context.startActivity(intent);
            }
        });
        return vh2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh2, int position) {
        String s=keys.get(position);
        vh2.name.setText(s);
    }


    @Override
    public int getItemCount() {
        return keys.size();
    }
}
