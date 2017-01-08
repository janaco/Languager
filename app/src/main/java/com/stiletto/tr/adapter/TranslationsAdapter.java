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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.translator.yandex.model.Translation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yana on 08.01.17.
 */

public class TranslationsAdapter extends RecyclerView.Adapter<TranslationsAdapter.ViewHolder> {

    private List<Translation> list;

    public TranslationsAdapter(Translation[] translations) {
        this.list = Arrays.asList(translations);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_translation, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.layoutUsages.setVisibility(View.GONE);

        Translation translation = list.get(position);

        String translated = translation.getTranslatedText();
        String type = " (" + translation.getTranslatedWordType() + ")";
        String origin = translation.hasMeanings() ? " [" + translation.getOriginalMeanings() + "]" : "";
        String synonyms = translation.hasSynonyms() ? ", " + translation.getTranslatedSynonyms() : "";

        String textStr = translated + origin + type + synonyms;

        SpannableString text = new SpannableString(textStr);

        int indexFrom = translated.length();
        int indexTo = translated.length() + origin.length();
        text.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.itemTranslation.getContext(),
                R.color.blue_grey_400)), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        indexFrom = translated.length() + origin.length();
        indexTo = indexFrom + type.length();

        text.setSpan(new RelativeSizeSpan(0.5f), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.ITALIC), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        indexFrom = indexTo;
        indexTo = text.length();

        text.setSpan(new RelativeSizeSpan(0.8f), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.ITALIC), indexFrom, indexTo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.itemTranslation.setText(text);


        if (translation.hasUsages()) {

            holder.layoutUsages.setVisibility(View.VISIBLE);

            UsagesAdapter adapter = new UsagesAdapter(translation.getUsageExamples());
            holder.recyclerView.setAdapter(adapter);
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
