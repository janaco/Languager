package com.stiletto.tr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.DictionaryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yana on 08.03.17.
 */

public class GeneralDictionaryAdapter
        extends RecyclerView.Adapter<GeneralDictionaryAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(String key, ArrayList<DictionaryItem> items, int position);
    }

    //    private List<DictionaryItem> list;
    private Map<Integer, Map.Entry<String, ArrayList<DictionaryItem>>> map;
    private OnItemClickListener onListItemClickListener;


    public GeneralDictionaryAdapter(Map<String, ArrayList<DictionaryItem>> map) {
        this.map = new HashMap<>();

        int index = 0;

        for (Map.Entry<String, ArrayList<DictionaryItem>> entry : map.entrySet()) {
            this.map.put(index++, entry);
        }

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

        Map.Entry<String, ArrayList<DictionaryItem>> entry = map.get(position);
        final ArrayList<DictionaryItem> items = entry.getValue();

        final String origin = entry.getKey();
        Log.d("TOOLBAR_", "entry.getKey: " + origin);
        String translation = DictionaryItem.getTranslation(items);


        holder.itemOrigin.setText(origin);
        holder.itemTranslation.setText(translation);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TOOLBAR_", "onItemClick: " + origin);
                onListItemClickListener.onItemClick(origin, items, position);
            }
        });

//        final DictionaryItem item = list.get(position);
//
//        String origin = item.getOriginText();
//        String transcription = item.hasTranscription() ? "[" + item.getTranscription() + "]" : "";
//        String partOfSpeech = item.isKnownPartOfSpeech() ? "(" + item.getPartOfSpeech() + ")" : "";
//
//        int indexFrom = 0;
//        int indexTo = origin.length();
//        SpannableString text = new SpannableString(origin + partOfSpeech + transcription);
//        text.setSpan(new UnderlineSpan(), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new StyleSpan(Typeface.BOLD),indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        indexFrom = origin.length();
//        indexTo = indexFrom + partOfSpeech.length();
//        text.setSpan(new RelativeSizeSpan(0.5f), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new StyleSpan(Typeface.ITALIC), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorSecondaryText)),
//                indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//
//        indexFrom = indexTo;
//        indexTo = indexFrom + transcription.length();
//        text.setSpan(new RelativeSizeSpan(0.85f), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorSecondaryText)),
//                indexFrom, indexTo,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        holder.itemOrigin.setText(text);
//        holder.itemTranslation.setText(item.getTranslationsAsString());


    }

    @Override
    public int getItemCount() {
        return map.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private TextView itemOrigin;
        private TextView itemTranslation;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            context = itemView.getContext();
            itemOrigin = (TextView) itemView.findViewById(R.id.item_text);
            itemTranslation = (TextView) itemView.findViewById(R.id.item_translation);
        }
    }
}
