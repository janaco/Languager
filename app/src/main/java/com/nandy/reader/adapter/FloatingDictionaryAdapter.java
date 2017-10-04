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
        return new FloatingDictionaryAdapter.ViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floating_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(FloatingDictionaryAdapter.ViewHolder holder, int position) {

        DictionaryItem item = list.get(position);

        holder.viewPartOfSpeech.setText(item.isKnownPartOfSpeech() ? item.getPartOfSpeech() : "");
        holder.itemText.setText(item.getOriginText());
        holder.viewTranscription.setText( item.getTranscription() );
        holder.viewTranscription.setVisibility(item.hasTranscription() ? View.VISIBLE : View.GONE);

        //list of possible translations related with origin word
        List<Translation> translations = item.getTranslations();
        StringBuilder builder = new StringBuilder();
        List<Pair<Integer, Integer>> indexes = new ArrayList<>();
        int indexFrom  = 0;
        int indexTo = 0;
        for (Translation translation: translations){

        String translated = translation.getText();
        String meanings = translation.hasMeanings() ? " (" + translation.getMeaningsAsString() + ")" : "";
        String synonyms = translation.hasSynonyms() ? ", " + translation.getSynonymsAsString() : "";

        builder.append(translated).append(meanings).append(synonyms).append(", ");
        indexFrom += translated.length();
        indexTo =  indexFrom + meanings.length();
        indexes.add(new Pair<>(indexFrom, indexTo ));
        indexFrom = builder.length();
        }

        SpannableString spannableString = new SpannableString(builder.toString().substring(0, builder.length() -2));

        for (Pair<Integer, Integer> pair: indexes){
            spannableString.setSpan(new RelativeSizeSpan(0.85f), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.context,
                    R.color.pale_brown)), pair.first, pair.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        holder.itemTranslations.setText(spannableString);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private TextView itemText;
        private TextView itemTranslations;
        private TextView viewPartOfSpeech;
        private TextView viewTranscription;

        ViewHolder(Context context, View view) {
            super(view);

            this.context = context;

            itemText = (TextView) view.findViewById(R.id.text1);
            itemTranslations = (TextView) view.findViewById(R.id.text2);
            viewPartOfSpeech = (TextView) view.findViewById(R.id.part_of_speech);
            viewTranscription = (TextView) view.findViewById(R.id.transcription);
        }
    }
}
