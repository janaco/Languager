package com.stiletto.tr.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter to organize list of all words learned by user.
 * Used on GeneralDictionary.
 * <p>
 * Created by yana on 08.03.17.
 */

public class GeneralDictionaryAdapter extends RecyclerView.Adapter<GeneralDictionaryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String key, Word word, int position);
    }

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
        String translation = " - " + DictionaryItem.getTranslation(items);

        SpannableString text = new SpannableString(origin + translation);

        //highlight origin word
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, origin.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //and its translation
        int from = origin.length();
        int to = text.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorSecondaryText)), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(0.8f), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.itemText.setText(text);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListItemClickListener.onItemClick(origin, word, position);
            }
        });


    }

    private void showDelimiter(ViewHolder holder, Word word, int position) {
        if (position > 0) {
            Word prev = list.get(position - 1);
            String prevIndex = prev.getText().substring(0, 1);
            String currentIndex = word.getText().substring(0, 1);

            //if previous and current items starts from
            //different letters, we will highlight new list view section
            if (prevIndex.equalsIgnoreCase(currentIndex)) {
                holder.itemDelimiter.setVisibility(View.GONE);
                holder.itemKey.setVisibility(View.GONE);
            } else {
                holder.itemDelimiter.setVisibility(View.VISIBLE);
                holder.itemKey.setText(currentIndex);
                holder.itemKey.setVisibility(View.VISIBLE);
            }
        } else {
            //list view section is always show before first item
            String currentIndex = word.getText().substring(0, 1);
            holder.itemDelimiter.setVisibility(View.VISIBLE);
            holder.itemKey.setText(currentIndex);
            holder.itemKey.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void cleanAll() {
        list.clear();
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemText;
        private TextView itemDelimiter;
        private TextView itemKey;

        private View view;
        private Context context;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            context = itemView.getContext();
            itemText = (TextView) itemView.findViewById(R.id.item_text);
            itemDelimiter = (TextView) itemView.findViewById(R.id.item_delimiter);
            itemKey = (TextView) itemView.findViewById(R.id.item_key);
        }
    }
}
