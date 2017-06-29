package com.stiletto.tr.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.word.Translation;

import java.util.List;

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
        return new ViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_translation, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Translation translation = list.get(position);

        String translated = translation.getText();
        String meanings = translation.hasMeanings() ? " (" + translation.getMeaningsAsString() + ")" : "";
        String synonyms = translation.hasSynonyms() ? ", " + translation.getSynonymsAsString() : "";

        SpannableString text = new SpannableString(translated + meanings + synonyms);

        //highlight possible meanings
        int indexFrom = translated.length();
        int indexTo = indexFrom + meanings.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.itemTranslation.getContext(),
                R.color.colorSecondaryText)), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.itemTranslation.setText(text);

        if (translation.hasUsageExamples()) {
            holder.layoutUsages.setVisibility(View.VISIBLE);

            //show usage examples
            UsagesAdapter adapter = new UsagesAdapter(translation.getUsageSamples());
            holder.recyclerView.setAdapter(adapter);
        }else {
            holder.layoutUsages.setVisibility(View.GONE);
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

        private TextView itemTranslation;
        private RecyclerView recyclerView;
        private LinearLayout layoutUsages;

        public ViewHolder(Context context, View view) {
            super(view);

            itemTranslation = (TextView) view.findViewById(R.id.item_translation);

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(layoutManager);

            layoutUsages = (LinearLayout) view.findViewById(R.id.layout_examples);

        }
    }
}
