package com.nandy.reader.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
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

import com.nandy.reader.R;
import com.nandy.reader.model.word.DictionaryItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Use it to organize all words with its translations
 * and other additional information on your screen.
 * <p>
 * Created by yana on 08.01.17.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private List<DictionaryItem> list;
    private int colorSecondary;
    private int colorPrimary;

    public DictionaryAdapter(Context context, List<DictionaryItem> list) {
        this.list = list;
        colorSecondary = ContextCompat.getColor(context, R.color.colorSecondaryText);
        colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DictionaryItem item = list.get(position);

        holder.setText(getSpannableDictionaryItemText(item, holder.getAdapterPosition()));
        holder.setTranslationsAdapter(new TranslationsAdapter(item.getTranslations()));
    }

    private Spannable getSpannableDictionaryItemText(DictionaryItem item, int position){
        String index = getItemCount() > 1 ? (position + 1) + ") " : "";
        String origin = item.getOriginText();
        String partOfSpeech = item.isKnownPartOfSpeech() ? " (" + item.getPartOfSpeech() + ")" : "";
        String transcription = item.hasTranscription() ? "    [" + item.getTranscription() + "]" : "";

        SpannableString text = new SpannableString(index + origin + partOfSpeech + transcription);

        //index (like 1, 2 etc.)
        int from = 0;
        int to = index.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(colorSecondary), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
        text.setSpan(new ForegroundColorSpan(colorPrimary), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //transcription
        from = to;
        to = from + transcription.length();
        text.setSpan(new RelativeSizeSpan(0.85f), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(colorSecondary), from, to, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return text;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_key)
        TextView itemKey;
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setText(Spannable text){
            itemKey.setText(text);
        }

        void setTranslationsAdapter(TranslationsAdapter adapter){
            recyclerView.setAdapter(adapter);
        }
    }
}
