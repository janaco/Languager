package com.stiletto.tr.adapter;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.UsageSample;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yana on 08.01.17.
 */

public class UsagesAdapter extends RecyclerView.Adapter<UsagesAdapter.ViewHolder> {

    private List<UsageSample> list;

    public UsagesAdapter(List<UsageSample>  examples) {
        this.list = examples;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UsageSample example = list.get(position);

        String origin = example.getText();
        String translations = example.hasTranslations() ? ": " + example.getTranslationsAsString()  :" ";

        SpannableString text = new SpannableString(origin + " " + translations);

        text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(holder.textView.getContext(),
                R.color.colorPrimary)), 0, origin.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setSpan(new RelativeSizeSpan(0.9f), origin.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.ITALIC), origin.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        try {
            return list.size();
        }catch (NullPointerException e){
            e.printStackTrace();
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public ViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.item_text);
        }
    }
}
