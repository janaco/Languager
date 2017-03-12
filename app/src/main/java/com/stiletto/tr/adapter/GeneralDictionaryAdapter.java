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
        String translation = " - " + DictionaryItem.getTranslation(items);

        SpannableString text = new SpannableString(origin + translation);

        text.setSpan(new UnderlineSpan(), 0, origin.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, origin.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void cleanAll(){
        list.clear();
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemText;

        private View view;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.context = view.getContext();
            itemText = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
