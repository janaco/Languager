package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.BookCoverContract;
import com.nandy.reader.mvp.presenter.BookCoverPresenter;
import com.softes.cardviewer.OnExpandableItemClickListener;
import com.nandy.reader.R;
import com.nandy.reader.model.Book;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * It is a front part (cover) of BookExpandingFragment.
 *
 * Created by yana on 19.03.17.
 */

public class FragmentTop extends Fragment implements BookCoverContract.View{

    @Bind(R.id.item_name)
    EditText itemName;
    @Bind(R.id.item_format)
    TextView itemExtention;

    private OnExpandableItemClickListener listener;
    private BasePresenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.start();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTitle(String title) {
        itemName.setText(title);
    }

    @Override
    public void setExtention(String extention) {
        itemExtention.setText(extention);
    }

    @OnClick(R.id.item_cover)
    void onItemClick() {
        listener.onExpandableItemClick();
    }

    public void setListener(OnExpandableItemClickListener listener) {
        this.listener = listener;
    }

    public static FragmentTop newInstance(Book book, OnExpandableItemClickListener listener) {

        FragmentTop fragment = new FragmentTop();

        BookCoverPresenter presenter = new BookCoverPresenter(fragment);
        presenter.setBook(book);

        fragment.setPresenter(presenter);
        fragment.setListener(listener);
        return fragment;
    }

}
