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

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder> {
    private List<String> choices;
    private List<NewsBean.DataBean> load=new ArrayList<>();

    public List<String> getDataList() {
        return choices;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View choiceView;
        TextView name;
        public ViewHolder(View view){
            super(view);
            choiceView=view;
            name=(TextView) view.findViewById(R.id.choice_name);
        }
    }
    public ChoiceAdapter(List<String> lists){
        choices=lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choice_item,parent,false);
        final ViewHolder vh2=new ViewHolder(view);
        if(Data.theme==false)
            vh2.name.setTextColor(Color.WHITE);
        vh2.choiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int position =vh2.getAdapterPosition();
                final String s=choices.get(position);
            }
        });
        return vh2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh2, int position) {
        String s=choices.get(position);
        vh2.name.setText(s);
    }


    @Override
    public int getItemCount() {
        return choices.size();
    }
}
