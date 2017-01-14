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
import com.stiletto.tr.translator.yandex.model.DictionaryWord;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yana on 08.01.17.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {

    private List<DictionaryWord> list;
    private Context context;

    public DictionaryAdapter(Context context, DictionaryWord[] words) {
        this.list = Arrays.asList(words);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DictionaryWord word = list.get(position);

        if (position == 0){
            holder.itemLine.setVisibility(View.GONE);
        }else {
            holder.itemLine.setVisibility(View.VISIBLE);
        }

        String originText = word.getOriginText();
        String type = word.hasWordType() ? " (" + word.getWordType() + ")" : "";
        String transcryption = word.hasTranscryption() ? " [" + word.getTranscryption() + "]" : "";

        SpannableString text = new SpannableString(originText + transcryption + type);
        text.setSpan(new UnderlineSpan(), 0, originText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, originText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setSpan(new RelativeSizeSpan(0.85f), originText.length(), originText.length() + transcryption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), originText.length(), originText.length() + transcryption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorSecondaryText)),
                originText.length(), originText.length() + transcryption.length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setSpan(new RelativeSizeSpan(0.5f), text.length() - type.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.ITALIC), text.length() - type.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.itemKey.setText(text);


        TranslationsAdapter translationsAdapter = new TranslationsAdapter(word.getTranslations());
        holder.recyclerView.setAdapter(translationsAdapter);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemKey;
        private TextView itemLine;
        private RecyclerView recyclerView;

         ViewHolder(Context context, View view) {
            super(view);

            itemKey = (TextView) view.findViewById(R.id.item_key);
            itemLine = (TextView) view.findViewById(R.id.item_line);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
