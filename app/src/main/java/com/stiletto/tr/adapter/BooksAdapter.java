package com.stiletto.tr.adapter;

import android.content.Context;
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
    private LayoutInflater lInflater;
    private OnListItemClickListener<Book> onItemClickListener;

    public BooksAdapter(Context context, List<Book> bookList) {
        this.bookList = bookList;
        Collections.sort(this.bookList);
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(OnListItemClickListener<Book> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        if (convertView == null) {
            view = lInflater.inflate(R.layout.item_book, parent, false);
            holder = new ViewHolder();
            holder.itemName = (TextView) view.findViewById(R.id.item_name);
            holder.itemCover = (ImageView) view.findViewById(R.id.item_cover);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        final Book book = getItem(index);
        holder.itemName.setText(book.getName());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onListItemClick(book, index);
            }
        });

        Log.d("BOOK_", "name: " + book.getName() + ", size: " + book.getSize());

        return view;

    }


    @Override
    public int getCount() {
        try {
            return bookList.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public class ViewHolder {
        public TextView itemName;
        public ImageView itemCover;
    }
}