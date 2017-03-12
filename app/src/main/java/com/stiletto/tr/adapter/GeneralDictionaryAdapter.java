package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 08.03.17.
 */

public class GeneralDictionaryAdapter
        extends RecyclerView.Adapter<GeneralDictionaryAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(String key, Word word, int position);
    }

    //    private List<DictionaryItem> list;
    private List<Word> list;
    private OnItemClickListener onListItemClickListener;


    public GeneralDictionaryAdapter(List<Word> list) {
        this.list = list;
    }

    public void setOnListItemClickListener(OnItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_general_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Word word = list.get(position);
        final ArrayList<DictionaryItem> items = word.getDictionaryItems();

        final String origin = word.getText();
        String translation = DictionaryItem.getTranslation(items);


        holder.itemOrigin.setText(origin);
        holder.itemTranslation.setText(translation);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TOOLBAR_", "onItemClick: " + origin);
                onListItemClickListener.onItemClick(origin, word, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemOrigin;
        private TextView itemTranslation;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            itemOrigin = (TextView) itemView.findViewById(R.id.item_text);
            itemTranslation = (TextView) itemView.findViewById(R.id.item_translation);
        }
    }
}
