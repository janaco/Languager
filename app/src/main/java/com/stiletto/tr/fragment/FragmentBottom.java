package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.stiletto.tr.R;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by yana on 19.03.17.
 */

public class FragmentBottom extends Fragment {

    @Bind(R.id.item_progress)
    NumberProgressBar progressBar;
    @Bind(R.id.item_languages)
    TextView itemLanguages;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom, container, false);
    }

    @OnClick(R.id.item_remove)
    void onRemoveItemClick(){}

    @OnClick(R.id.item_read)
    void onReadItemClick(){}



    public static FragmentBottom newInstance(Book book) {
        Bundle args = new Bundle();
        args.putParcelable("book", book);

        FragmentBottom fragment = new FragmentBottom();
        fragment.setArguments(args);

        return fragment;
    }
}
