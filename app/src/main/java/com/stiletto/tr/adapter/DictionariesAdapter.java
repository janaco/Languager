package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.model.word.WordInfo;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by yana on 16.07.17.
 */

public class DictionariesAdapter extends RecyclerView.Adapter<DictionariesAdapter.ViewHolder> {

    private List<WordInfo> list;
    private OnListItemClickListener<WordInfo> onListItemClickListener;

    public DictionariesAdapter(List<WordInfo> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final WordInfo info = list.get(position);

        holder.viewName.setText(info.getOriginLanguage() + " - " + info.getTranslationLanguage());
        holder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onListItemClick(info, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnListItemClickListener(OnListItemClickListener<WordInfo> onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView viewName;

        public ViewHolder(View itemView) {
            super(itemView);

            viewName = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
