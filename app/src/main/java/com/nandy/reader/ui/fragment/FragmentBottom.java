package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.nandy.reader.R;
import com.nandy.reader.core.BookItemListener;
import com.nandy.reader.core.RenameModeCallback;
import com.nandy.reader.model.Book;
import com.nandy.reader.view.Fragment;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * It is a bottom part of BookExpandingFragment.
 *
 * Created by yana on 19.03.17.
 */

public class FragmentBottom extends Fragment {

    @Bind(R.id.item_progress)
    NumberProgressBar progressBar;
    @Bind(R.id.item_languages)
    TextView itemLanguages;

    private Book book;
    private BookItemListener bookItemListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int bookmark = book.getBookmark();
        if (bookmark > 0) {
            progressBar.setMax(book.getPages());
            progressBar.setProgress(bookmark);
            progressBar.setVisibility(View.VISIBLE);
        }

        if (book.hasOriginLanguage() && book.hasTranslationLanguage()) {
            String origin = new Locale(book.getOriginLanguage().toString()).getDisplayLanguage();
            String translation = new Locale(book.getTranslationLanguage().toString()).getDisplayLanguage();

            itemLanguages.setText(origin.concat(" - ").concat(translation));
        } else {
            itemLanguages.setText(getString(R.string.you_have_not_read_book));
        }

    }

    @OnClick(R.id.item_read)
    void onReadItemClick() {
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
