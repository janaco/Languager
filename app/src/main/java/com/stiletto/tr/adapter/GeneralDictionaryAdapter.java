package com.stiletto.tr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.DictionaryItem;

import java.util.List;

/**
 * Created by yana on 08.03.17.
 */

public class GeneralDictionaryAdapter
        extends RecyclerView.Adapter<GeneralDictionaryAdapter.ViewHolder> {

    private List<DictionaryItem> list;

    public GeneralDictionaryAdapter(List<DictionaryItem> list) {
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_general_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DictionaryItem item = list.get(position);
//
//        String origin = item.getOrigin();
//        String transcription = item.hasTranscription() ? "[" + item.getTranscription() + "]" : "";
//        String partOfSpeech = item.hasPartOfSpeech() ? "(" + item.getPartOfSpeech().toString() + ")" : "";
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

        holder.itemOrigin.setText(item.getOrigin());
        holder.itemTranslation.setText(item.getTranslation());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private TextView itemOrigin;
        private TextView itemTranslation;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemOrigin = (TextView) itemView.findViewById(R.id.item_text);
            itemTranslation = (TextView) itemView.findViewById(R.id.item_translation);
        }
    }
}
