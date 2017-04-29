package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.stiletto.tr.R;
import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.core.RenameModeCallback;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.Fragment;

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
    @Bind(R.id.layout_base)
    LinearLayout layoutBase;
    @Bind(R.id.layout_rename)
    LinearLayout layoutRemane;

    private int position;
    private Book book;
    private BookItemListener bookItemListener;
    private RenameModeCallback renameModeCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
        position = getArguments().getInt("position");
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

    @OnClick(R.id.item_rename)
    void onRenameItemClick() {
        renameModeCallback.onRenameModeEnabled();
    }

    @OnClick(R.id.item_read)
    void onReadItemClick() {
        bookItemListener.read(book);
    }

    @OnClick(R.id.item_cancel)
    void onCancelRenameModeClick() {
        renameModeCallback.onRenameModeCanceled();
    }

    @OnClick(R.id.item_save)
    void onSaveChangesClick() {

        renameModeCallback.onBookRenamed(book, position);
    }

    public void setBookItemListener(BookItemListener bookItemListener) {
        this.bookItemListener = bookItemListener;
    }

    public void setRenameModeEnabled(boolean enable) {

        if (enable) {
            layoutBase.setVisibility(View.GONE);
            layoutRemane.setVisibility(View.VISIBLE);
        } else {
            layoutBase.setVisibility(View.VISIBLE);
            layoutRemane.setVisibility(View.GONE);
        }

    }

    public void setRenameModeCallback(RenameModeCallback renameModeCallback) {
        this.renameModeCallback = renameModeCallback;
    }

    public static FragmentBottom newInstance(Book book, BookItemListener listener, RenameModeCallback renameModeCallback, int position) {
        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putInt("position", position);

        FragmentBottom fragment = new FragmentBottom();
        fragment.setArguments(args);
        fragment.setBookItemListener(listener);
        fragment.setRenameModeCallback(renameModeCallback);

        return fragment;
    }
}
