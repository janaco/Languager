package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.view.text.ClickableTextView;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String content = list.get(position);
        holder.itemContent.setText(content);
        holder.itemContent.setOnWordClickListener(new ClickableTextView.OnWordClickListener() {
            @Override
            public void onClick(String word) {
                Toast.makeText(holder.itemContent.getContext(), word, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

     static class ViewHolder extends RecyclerView.ViewHolder{

        ClickableTextView itemContent;

         ViewHolder(View itemView) {
            super(itemView);

             itemContent = (ClickableTextView) itemView.findViewById(R.id.item_content);
        }
    }
}
