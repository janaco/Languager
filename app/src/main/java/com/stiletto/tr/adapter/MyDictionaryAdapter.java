package com.stiletto.tr.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.translator.yandex.Translation;
import com.stiletto.tr.translator.yandex.model.DictionaryWord;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yana on 26.02.17.
 */

public class MyDictionaryAdapter extends RecyclerView.Adapter<MyDictionaryAdapter.ViewHolder> {

    private List<Translation> list;

    public MyDictionaryAdapter(List<Translation> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Translation translation = list.get(position);

        holder.itemOrigin.setText(translation.getOrigin());
        holder.itemTranslated.setText(translation.getTranslationAsString());

    }

    public void addTranslation(Translation translation){
        list.add(translation);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemOrigin;
        private TextView itemTranslated;

        ViewHolder( View view) {
            super(view);

            itemOrigin = (TextView) view.findViewById(R.id.item_origin);
            itemTranslated = (TextView) view.findViewById(R.id.item_translation);
        }
    }
}
