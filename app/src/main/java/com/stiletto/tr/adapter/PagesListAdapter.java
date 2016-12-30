package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.widget.ClickableTextView;

import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class PagesListAdapter extends RecyclerView.Adapter<PagesListAdapter.ViewHolder> {

    private List<CharSequence> list;

    private  int width;
    private int height;

    public PagesListAdapter(List<CharSequence> list, int width, int heigth) {
        this.list = list;
        this.width = width;
        this.height = heigth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, null),
                width, height);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        CharSequence content = list.get(position);
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
        return list != null ? list.size() : 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ClickableTextView itemContent;

        public ViewHolder(View itemView, int width, int heigth) {
            super(itemView);

            itemContent = (ClickableTextView) itemView.findViewById(R.id.item_content);
            itemContent.setWidth(width);
            itemContent.setHeight(heigth);
        }
    }
}
