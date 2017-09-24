package com.nandy.reader.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.model.word.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yana on 21.05.17.
 */

public class BaseDictionaryAdapter  extends RecyclerView.Adapter<BaseDictionaryAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(String key, Word word, int position);
    }

    private final List<Word> list = new ArrayList<>();
    private OnItemClickListener onListItemClickListener;


    public void setOnListItemClickListener(OnItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_general_dictionary, null, false));
    }

    public void addItem(Word word){
        list.add(word);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {

        final Word word = list.get(position);

        final String origin = word.getText();
        String translation = " - " + word.getTranslationsAsString();

        SpannableString text = new SpannableString(origin + translation);

        text.setSpan(new StyleSpan(Typeface.BOLD), 0, origin.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int from = origin.length();
        int to = text.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorSecondaryText)), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(0.8f), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.itemText.setText(text);

        holder.view.setOnClickListener(view -> onListItemClickListener.onItemClick(origin, word, holder.getAdapterPosition()));


        if (position > 0){
            Word prev = list.get(position-1);
            String prevIndex = prev.getText().substring(0, 1);
            String currentIndex = word.getText().substring(0, 1);

            if (prevIndex.equalsIgnoreCase(currentIndex)){
                holder.itemDelimiter.setVisibility(View.GONE);
                holder.itemKey.setVisibility(View.GONE);
            }else {
                holder.itemDelimiter.setVisibility(View.VISIBLE);
                holder.itemKey.setText(currentIndex);
                holder.itemKey.setVisibility(View.VISIBLE);
            }
        }else {
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

    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemText;
        private TextView itemDelimiter;
        private
        TextView itemKey;

        private View view;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            context = itemView.getContext();
            itemText = (TextView) itemView.findViewById(R.id.item_text);
            itemDelimiter = (TextView) itemView.findViewById(R.id.item_delimiter);
            itemKey = (TextView) itemView.findViewById(R.id.item_key);
        }
    }
}