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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Translation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by yana on 02.09.17.
 */

public class FloatingDictionaryAdapter extends RecyclerView.Adapter<FloatingDictionaryAdapter.ViewHolder> {

    private List<DictionaryItem> list;

    public FloatingDictionaryAdapter(List<DictionaryItem> list) {
        this.list = list;
    }

    @Override
    public FloatingDictionaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FloatingDictionaryAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floating_dictionary, parent, false));
    }

    @Override
    public void onBindViewHolder(FloatingDictionaryAdapter.ViewHolder holder, int position) {

        DictionaryItem item = list.get(position);

        holder.setPartOfSpeech(item.isKnownPartOfSpeech() ? item.getPartOfSpeech() : "");
        holder.setText(item.getOriginText());
        holder.setTranscription(item.getTranscription());
        holder.showTranscription(item.hasTranscription());
        holder.setTranslations(holder.convertTranslationsToSpannableString(item.getTranslations()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text1)
        TextView itemText;
        @Bind(R.id.text2)
        TextView itemTranslations;
        @Bind(R.id.part_of_speech)
        TextView viewPartOfSpeech;
        @Bind(R.id.transcription)
        TextView viewTranscription;
        private int paleBrownColor;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            paleBrownColor = ContextCompat.getColor(view.getContext(), R.color.pale_brown);
        }

        void setText(String text) {
            itemText.setText(text);
        }

        void setTranslations(Spannable text) {
            itemTranslations.setText(text);
        }

        void setPartOfSpeech(String text) {
            viewPartOfSpeech.setText(text);
        }

        void setTranscription(String text) {
            viewTranscription.setText(text);
        }

        void showTranscription(boolean show) {
            viewTranscription.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        Spannable convertTranslationsToSpannableString(List<Translation> translations) {
            StringBuilder builder = new StringBuilder();
            List<Pair<Integer, Integer>> indexes = new ArrayList<>();
            int indexFrom = 0;
            int indexTo = 0;
            for (Translation translation : translations) {

                String translated = translation.getText();
                String meanings = translation.hasMeanings() ? " (" + translation.getMeaningsAsString() + ")" : "";
                String synonyms = translation.hasSynonyms() ? ", " + translation.getSynonymsAsString() : "";

                builder.append(translated).append(meanings).append(synonyms).append(", ");
                indexFrom += translated.length();
                indexTo = indexFrom + meanings.length();
                indexes.add(new Pair<>(indexFrom, indexTo));
                indexFrom = builder.length();
            }

            SpannableString spannableString = new SpannableString(builder.toString().substring(0, builder.length() - 2));

            for (Pair<Integer, Integer> pair : indexes) {
                spannableString.setSpan(new RelativeSizeSpan(0.85f), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.ITALIC), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(paleBrownColor), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            return spannableString;
        }
    }
}
