package com.stiletto.tr.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.model.Book;

import java.util.Collections;
import java.util.List;

/**
 * Created by yana on 02.01.17.
 */

public class BooksAdapter extends BaseAdapter {

    private List<Book> bookList;
    private OnListItemClickListener<Book> onItemClickListener;
    private boolean showNextLoadingProgress = true;

    public BooksAdapter( List<Book> bookList) {
        this.bookList = bookList;
        this.showNextLoadingProgress = bookList.size() == 0;
    }

    public void setOnItemClickListener(OnListItemClickListener<Book> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setShowNextLoadingProgress(boolean showNextLoadingProgress) {
        this.showNextLoadingProgress = showNextLoadingProgress;
    }

    @Override
    public Book getItem(int i) {
        return bookList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        View view;

        ViewHolder holder;

        if (showNextLoadingProgress && index == getCount() - 1) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_loading, parent, false);
        }

        if (convertView == null || convertView.getTag() == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
            holder = ViewHolder.bind(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = ViewHolder.bind(view);
                view.setTag(holder);
            }
        }

        bindView(view, holder, index);
        return view;
    }

    private void bindView(View view, ViewHolder holder, final int position) {

        final Book book = getItem(position);

        Log.d("BOOK_ADAPTER", position + ", " + book);

        holder.itemName.setText(book.getName().length() > 30 ? book.getName().substring(0, 30).concat("...") : book.getName());
        holder.itemExtension.setText(book.getFileType().name().toUpperCase());

        if (book.hasCover()) {
            holder.itemCover.setImageBitmap(book.getCover());
        } else {
            holder.itemCover.setImageDrawable(ContextCompat.getDrawable(
                    holder.itemCover.getContext(), R.drawable.book));
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onListItemClick(book, position);
            }
        });

    }

    public void addBook(Book book) {

        if (!bookList.contains(book)) {
            bookList.add(book);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        try {
            return showNextLoadingProgress ? bookList.size() + 1 : bookList.size();
        } catch (NullPointerException e) {
            return showNextLoadingProgress ? 1 : 0;
        }
    }


    static class ViewHolder {

        TextView itemName;
        TextView itemExtension;

        ImageView itemCover;

        private static ViewHolder bind(View view) {

            ViewHolder holder = new ViewHolder();
            holder.itemName = (TextView) view.findViewById(R.id.item_name);
            holder.itemCover = (ImageView) view.findViewById(R.id.item_cover);
            holder.itemExtension = (TextView) view.findViewById(R.id.item_extension);

            return holder;
        }
    }
}