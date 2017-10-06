package com.nandy.reader.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.OnListItemClickListener;
import com.nandy.reader.translator.yandex.Language;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays all languages available on this or another case.
 * <p>
 * Created by yana on 28.01.17.
 */

public class LanguagesListAdapter extends RecyclerView.Adapter<LanguagesListAdapter.ViewHolder> {

    private List<Language> list;
    private OnListItemClickListener<Language> onItemClickListener;

    private int[] colors = {R.drawable.btn_background_1, R.drawable.btn_background_2, R.drawable.btn_background_3};

    public LanguagesListAdapter(List<Language> languages) {
        this.list = languages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {

        final Language language = list.get(position);

        int drawableRes = new Random().nextInt(colors.length);
        drawableRes = drawableRes >= colors.length ? colors.length - 1 : drawableRes;

        holder.setText(new Locale(language.toString()).getDisplayLanguage());
        holder.setBackground(ContextCompat.getDrawable(holder.textView.getContext(), colors[drawableRes]));

        holder.textView.setOnClickListener(view -> onItemClickListener.onListItemClick(language, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void setOnItemClickListener(OnListItemClickListener<Language> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_name)
        TextView textView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setBackground(Drawable drawable){
            textView.setBackground(drawable);
        }

        void setText(String text){
            textView.setText(text);
        }
    }
}
