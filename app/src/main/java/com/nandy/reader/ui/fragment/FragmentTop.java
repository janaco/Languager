package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softes.cardviewer.OnExpandableItemClickListener;
import com.nandy.reader.R;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.model.Book;
import com.nandy.reader.view.Fragment;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * It is a front part (cover) of BookExpandingFragment.
 *
 * Created by yana on 19.03.17.
 */

public class FragmentTop extends Fragment {

    @Bind(R.id.item_name)
    EditText itemName;
    @Bind(R.id.item_format)
    TextView itemExtention;

    private Book book;
    private OnExpandableItemClickListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        ButterKnife.bind(this, view);
        itemName.setText(book.getName());
        itemExtention.setText(book.getFileType().name());
        return view;
    }

    @OnClick(R.id.item_cover)
    void onItemClick() {
        listener.onExpandableItemClick();
    }

    public void setListener(OnExpandableItemClickListener listener) {
        this.listener = listener;
    }

    public static FragmentTop newInstance(Book book, OnExpandableItemClickListener listener) {
        Bundle args = new Bundle();
        args.putParcelable("book", book);

        FragmentTop fragment = new FragmentTop();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

}
