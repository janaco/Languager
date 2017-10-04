package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.nandy.reader.R;
import com.nandy.reader.activity.MainActivity;
import com.nandy.reader.model.Book;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.BookListItemContract;
import com.nandy.reader.mvp.presenter.BookListItemPresenter;
import com.nandy.reader.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * It is a bottom part of BookExpandingFragment.
 * <p>
 * Created by yana on 19.03.17.
 */

public class FragmentBottom extends Fragment implements BookListItemContract.View {

    @Bind(R.id.item_progress)
    NumberProgressBar progressBar;
    @Bind(R.id.item_languages)
    TextView itemLanguages;

    private BookListItemContract.Presenter presenter;

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

        presenter.start();
    }

    @Override
    public void setPresenter(BookListItemContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLanguages(String languages) {
        itemLanguages.setText(languages);
    }

    @Override
    public void setLanguageAlert(int resId) {
        itemLanguages.setText(resId);
    }

    @Override
    public void setProgress(int current, int max) {
        progressBar.setMax(max);
        progressBar.setProgress(current);
        progressBar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.item_read)
    void onReadItemClick() {
        presenter.onReadBookClick();

    }

    @Override
    public void openBook(Book book) {
        ((MainActivity) getActivity()).replace(ViewerFragment.getInstance(getContext(), book));
    }

    public static FragmentBottom newInstance(Book book) {


        FragmentBottom fragment = new FragmentBottom();

        BookListItemPresenter presenter = new BookListItemPresenter(fragment);
        presenter.setBook(book);
        fragment.setPresenter(presenter);

        return fragment;
    }
}
