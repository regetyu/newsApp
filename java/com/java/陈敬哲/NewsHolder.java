package com.java.陈敬哲;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

class NewsHolder extends BaseViewHolder {
    View keyView;
    TextView name;
    public NewsHolder(View view){
        super(view);
        keyView=view;
        name=(TextView) view.findViewById(R.id.key_name);
    }
}