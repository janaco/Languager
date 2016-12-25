package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;

import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class PagesListAdapter extends RecyclerView.Adapter<PagesListAdapter.ViewHolder>{

    private List<String> list;

    public PagesListAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String content = list.get(position);
        holder.itemContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

     static class ViewHolder extends RecyclerView.ViewHolder{

        TextView itemContent;

         ViewHolder(View itemView) {
            super(itemView);

             itemContent = (TextView) itemView.findViewById(R.id.item_content);
        }
    }
}
