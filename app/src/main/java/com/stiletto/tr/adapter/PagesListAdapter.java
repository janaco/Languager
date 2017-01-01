package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.widget.ClickableTextView;

import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class PagesListAdapter extends RecyclerView.Adapter<PagesListAdapter.ViewHolder> {

    private List<CharSequence> list;
    private OnListItemClickListener<CharSequence> onClickListener;

    public PagesListAdapter(List<CharSequence> list) {
        this.list = list;
    }

    public void setOnListItemClick(OnListItemClickListener<CharSequence> onClickListener){
        this.onClickListener = onClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final CharSequence content = list.get(position);
        holder.itemContent.setText(content);
//        holder.itemContent.setOnWordClickListener(new ClickableTextView.OnWordClickListener() {
//            @Override
//            public void onClick(String word) {
//                Toast.makeText(holder.itemContent.getContext(), word, Toast.LENGTH_SHORT).show();
//            }
//        });


        holder.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LIST_", "onPageClick: " + position);
                onClickListener.onListItemClick(content, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ClickableTextView itemContent;

        private ViewHolder(View itemView) {
            super(itemView);

            itemContent = (ClickableTextView) itemView.findViewById(R.id.item_content);
        }
    }
}
