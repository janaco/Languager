package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.stiletto.tr.R;
import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.Fragment;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by yana on 19.03.17.
 */

public class FragmentBottom extends Fragment {

    @Bind(R.id.item_progress)
    NumberProgressBar progressBar;
    @Bind(R.id.item_languages)
    TextView itemLanguages;

    private int position;
    private Book book;
    private BookItemListener bookItemListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
        position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bottom, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int bookmark = book.getBookmark();
        if (bookmark > 0){
            progressBar.setProgress(bookmark);
            progressBar.setVisibility(View.VISIBLE);
        }

        if (book.hasOriginLanguage() && book.hasTranslationLanguage()){

            String origin = new Locale("", book.getOriginLanguage().toString()).getDisplayLanguage();
            String translation = new Locale("", book.getTranslationLanguage().toString()).getDisplayLanguage();

            itemLanguages.setText(origin.concat(" - ").concat(translation));
            itemLanguages.setVisibility(View.VISIBLE);

        }
    }

    @OnClick(R.id.item_rename)
    void onRemoveItemClick(){
        Log.d("TR_", "remove click");
        bookItemListener.rename(book,position );
    }

    @OnClick(R.id.item_read)
    void onReadItemClick(){
        Log.d("TR_", "read click");
        bookItemListener.read(book);
    }

    public void setBookItemListener(BookItemListener bookItemListener) {
        this.bookItemListener = bookItemListener;
    }

    public static FragmentBottom newInstance(Book book, BookItemListener listener, int position) {
        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putInt("position", position);

        FragmentBottom fragment = new FragmentBottom();
        fragment.setArguments(args);
        fragment.setBookItemListener(listener);

        return fragment;
    }
}
