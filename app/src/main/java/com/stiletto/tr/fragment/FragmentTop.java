package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.view.OnExpandableItemClickListener;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 19.03.17.
 */

public class FragmentTop extends Fragment {

    @Bind(R.id.item_cover)
    LinearLayout layoutCover;
    @Bind(R.id.item_name)
    EditText itemName;

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
        layoutCover.setBackgroundResource(getCover());
        itemName.setText(book.getName());
        return view;
    }

    @OnClick(R.id.item_cover)
    void onItemClick() {
        listener.onExpandableItemClick();
    }

    private int getCover() {
        int[] covers = {R.drawable.cover_1, R.drawable.cover_3, R.drawable.cover_4, R.drawable.cover_6, R.drawable.cover_7, R.drawable.cover_8, R.drawable.cover_9, R.drawable.cover_10};
        int index = new Random().nextInt(covers.length);
        Log.d("COVER_", "index: " + index);
        return index == covers.length ? covers[0] : covers[index];
    }

    public void setListener(OnExpandableItemClickListener listener) {
        this.listener = listener;
    }

    public void setRenameModeEnabled(boolean enable) {
        itemName.setEnabled(enable);
        if (enable) {
            itemName.setSelection(itemName.getText().length());
            itemName.requestFocus();
            NavigationManager.showKeyboard(getContext());
        }
    }

    public String getNameFromInput() {
        return itemName.getText().toString();
    }

    public void setNameError() {
        itemName.setError("Please, enter corect name.");
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
