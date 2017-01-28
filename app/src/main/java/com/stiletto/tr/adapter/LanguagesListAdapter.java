package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.translator.yandex.Language;

import java.util.List;
import java.util.Locale;

/**
 * Created by yana on 28.01.17.
 */

public class LanguagesListAdapter extends RecyclerView.Adapter<LanguagesListAdapter.ViewHolder>{

    private List<Language> list;
    private OnListItemClickListener<Language>  onItemClickListener;

    public LanguagesListAdapter(List<Language> languages) {
        this.list = languages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Language language = list.get(position);

        String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
        holder.textView.setText(displayLanguage);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onListItemClick(language, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return list.size();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setOnItemClickListener(OnListItemClickListener<Language> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        ViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.item_name);
        }
    }
}
