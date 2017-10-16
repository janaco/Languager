package com.nandy.reader.adapter;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.model.word.Translation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Used to adapt list of possible translations with its explanations to word.
 *
 * Created by yana on 08.01.17.
 */

public class TranslationsAdapter extends RecyclerView.Adapter<TranslationsAdapter.ViewHolder> {

    private List<Translation> list;

    TranslationsAdapter(List<Translation> translations) {
        this.list = translations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_translation, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Translation translation = list.get(position);

        holder.setTranslation(holder.representAsSpannable(translation));
        holder.showUsages(translation.hasUsageExamples());
        if (translation.hasUsageExamples()) {
            holder.setUsages(new UsagesAdapter(translation.getUsageSamples()));
        }
    }

    @Override
    public int getItemCount() {
        try {
            return list.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_translation)
        TextView itemTranslation;
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;
        @Bind(R.id.layout_examples)
        LinearLayout layoutUsages;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }

        void setTranslation(Spannable text){
            itemTranslation.setText(text);
        }

        void setUsages(UsagesAdapter adapter){
            recyclerView.setAdapter(adapter);
        }

        void showUsages(boolean show){
            layoutUsages.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        Spannable representAsSpannable(Translation translation){
            String translated = translation.getText();
            String meanings = translation.hasMeanings() ? " (" + translation.getMeaningsAsString() + ")" : "";
            String synonyms = translation.hasSynonyms() ? ", " + translation.getSynonymsAsString() : "";

            SpannableString text = new SpannableString(translated + meanings + synonyms);

            //highlight possible meanings
            int indexFrom = translated.length();
            int indexTo = indexFrom + meanings.length();
            text.setSpan(new StyleSpan(Typeface.ITALIC), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(itemTranslation.getContext(), R.color.colorSecondaryText)), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return text;
        }
    }
}
