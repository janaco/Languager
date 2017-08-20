package com.nandy.reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for list of dictionary items used in
 * bottomsheet-panel on PageViewerFragment
 * (while user read book and learn new words)
 * and displays words that was considered as unknown by user
 * during reading.
 *
 * Created by yana on 26.02.17.
 */

public class BookDictionaryAdapter extends RecyclerView.Adapter<BookDictionaryAdapter.ViewHolder> {

    private List<DictionaryItem> list;

    public BookDictionaryAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_dictionary, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DictionaryItem item = list.get(position);

        holder.itemOrigin.setText(item.getOriginText());
        holder.itemTranslated.setText(item.getTranslationsAsString());

    }

    public void addTranslation(Word word){
        list.addAll(word.getDictionaryItems());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemOrigin;
        private TextView itemTranslated;

        ViewHolder( View view) {
            super(view);

            itemOrigin = (TextView) view.findViewById(R.id.item_origin);
            itemTranslated = (TextView) view.findViewById(R.id.item_translation);
        }
    }
}
