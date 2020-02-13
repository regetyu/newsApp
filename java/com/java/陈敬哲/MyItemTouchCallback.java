package com.java.陈敬哲;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.lang.Runnable;

public class MyItemTouchCallback extends ItemTouchHelper.Callback {

    private final ChoiceAdapter adapter;
    private final ChoiceAdapter adapter2;
    private final Context context;
    public MyItemTouchCallback(ChoiceAdapter adapter,ChoiceAdapter adapter2,Context c) {
        this.adapter = adapter;
        this.adapter2=adapter2;
        this.context=c;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(adapter.getDataList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(adapter.getDataList(), i, i - 1);
            }
        }
        recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        System.out.println("onswipe");
        if (direction == ItemTouchHelper.END) {
            final String s = adapter.getDataList().get(position);
            adapter2.getDataList().add(s);
            adapter.getDataList().remove(s);
            if (CategoryList.getInList().contains(s)) {
                CategoryList.getInList().remove(s);
                CategoryList.getOutList().add(s);
            } else {
                CategoryList.getOutList().remove(s);
                CategoryList.getInList().add(s);
            }
            adapter.notifyDataSetChanged();
            adapter2.notifyDataSetChanged();
        }
    }


}