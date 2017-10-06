package com.nandy.reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nandy.reader.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 16.07.17.
 */

public class DictionariesListAdapter extends RecyclerView.Adapter<DictionariesListAdapter.ViewHolder> {

    private final List<Item> list = new ArrayList<>();
    private OnItemClickListener onListItemClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, parent, false));
    }

    public void add(Item item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final Item item = list.get(position);
        String originLanguage = new Locale(item.langOrigin).getDisplayLanguage();
        String translationLanguage = new Locale(item.langTranslation).getDisplayLanguage();
        holder.setLanguages(originLanguage + " - " + translationLanguage);
        holder.setWordsCount(item.wordsCount);
        holder.setInfo(item.wordsCount == item.unknownWordsCount ? "No learned hasWords" : item.unknownWordsCount + " hasWords to learn");
        holder.mainLayout.setOnClickListener(v -> onListItemClickListener.onListItemClick(item, position));
        holder.btnMenu.setOnClickListener(v -> onListItemClickListener.onItemMenuClick(item, position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnListItemClickListener(OnItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.languages)
        TextView viewLanguages;
        @Bind(R.id.words_count)
        TextView viewWordsCount;
        @Bind(R.id.words_info)
        TextView viewInfo;
        @Bind(R.id.menu)
        ImageView btnMenu;
        @Bind(R.id.layout_main)
        LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        void setLanguages(String languages) {
            viewLanguages.setText(languages);
        }

        void setWordsCount(int count) {
            viewWordsCount.setText(count + " hasWords");
        }

        void setInfo(String text) {
            viewInfo.setText(text);
        }
    }

    public static class Item {

        public final String langOrigin;
        public final String langTranslation;
        private int wordsCount;
        private int unknownWordsCount;

        public Item(String langOrigin, String langTranslation, int wordsCount, int unknownWordsCount) {
            this.langOrigin = langOrigin;
            this.langTranslation = langTranslation;
            this.wordsCount = wordsCount;
            this.unknownWordsCount = unknownWordsCount;
        }
    }

    public interface OnItemClickListener {

        void onListItemClick(Item item, int position);

        void onItemMenuClick(Item item, int position);
    }
}
