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
import com.stiletto.tr.model.word.DictionaryItem;

import java.util.List;

/**
 * Use it to organize all words with its translations
 * and other additional information on your screen.
 *
 * Created by yana on 08.01.17.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private List<DictionaryItem> list;

    public DictionaryAdapter(List<DictionaryItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DictionaryItem item = list.get(position);

        String index = getItemCount() > 1 ? (position + 1) + ") " : "";
        String origin = item.getOriginText();
        String partOfSpeech = item.isKnownPartOfSpeech() ? " (" + item.getPartOfSpeech() + ")" : "";
        String transcription = item.hasTranscription() ? "    [" + item.getTranscription() + "]" : "";

        SpannableString text = new SpannableString(index + origin + partOfSpeech + transcription);

        //index (like 1, 2 etc.)
        int from = 0;
        int to = index.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorSecondaryText)), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //origin word
        from = to;
        to += origin.length();
        text.setSpan(new UnderlineSpan(), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //part of speech (for origin word)
        from = to;
        to = from + partOfSpeech.length();
        text.setSpan(new RelativeSizeSpan(0.4f), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.ITALIC), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorPrimary)), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //transcription
        from = to;
        to = from + transcription.length();
        text.setSpan(new RelativeSizeSpan(0.85f), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context, R.color.colorSecondaryText)),
                from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.itemKey.setText(text);

        //list of possible translations related with origin word
        TranslationsAdapter translationsAdapter = new TranslationsAdapter(item.getTranslations());
        holder.recyclerView.setAdapter(translationsAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private TextView itemKey;
        private RecyclerView recyclerView;

        ViewHolder(Context context, View view) {
            super(view);

            this.context = context;

            itemKey = (TextView) view.findViewById(R.id.item_key);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }
}
