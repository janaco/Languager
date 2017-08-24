package com.nandy.reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nandy.reader.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by yana on 16.07.17.
 */

public class DictionariesListAdapter extends RecyclerView.Adapter<DictionariesListAdapter.ViewHolder> {

    private List<Item> list;
    private OnItemClickListener onListItemClickListener;

    public DictionariesListAdapter(List<Item> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Item item = list.get(position);
        String originLanguage = new Locale(item.langOrigin).getDisplayLanguage();
        String translationLanguage = new Locale(item.langTranslation).getDisplayLanguage();

        holder.viewLanguages.setText(originLanguage + " - " + translationLanguage);
        holder.viewWordsCount.setText(item.wordsCount + " words");

        if (item.wordsCount == item.unknownWordsCount) {
            holder.viewInfo.setText("No learned words");
        } else {
            holder.viewInfo.setText(item.unknownWordsCount + " words to learn");
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onListItemClick(item, position);
            }
        });

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onItemMenuClick(item, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnListItemClickListener(OnItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView viewLanguages;
        private TextView viewWordsCount;
        private TextView viewInfo;
        private ImageView btnMenu;
        private LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            viewLanguages = (TextView) itemView.findViewById(R.id.languages);
            viewWordsCount = (TextView) itemView.findViewById(R.id.words_count);
            viewInfo = (TextView) itemView.findViewById(R.id.words_info);
            btnMenu = (ImageView) itemView.findViewById(R.id.menu);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.layout_main);
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

    public interface OnItemClickListener{

        void onListItemClick(Item item, int position);

        void onItemMenuClick(Item item, int position);
    }
}