package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.translator.yandex.SimpleTranslation;

import java.util.List;

/**
 * Created by yana on 26.02.17.
 */

public class MyDictionaryAdapter extends RecyclerView.Adapter<MyDictionaryAdapter.ViewHolder> {

    private List<SimpleTranslation> list;

    public MyDictionaryAdapter(List<SimpleTranslation> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SimpleTranslation translation = list.get(position);

        holder.itemOrigin.setText(translation.getOrigin());
        holder.itemTranslated.setText(translation.getTranslationAsString());

    }

    public void addTranslation(SimpleTranslation translation){
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
