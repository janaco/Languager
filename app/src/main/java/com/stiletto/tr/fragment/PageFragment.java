package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ClickableTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment {

    @Bind(R.id.item_content)
    ClickableTextView itemBookPage;

    public static final String ARG_PAGE = "page";
    public static final String ARG_CONTENT = "content";
    private int pageNumber;
    private CharSequence content;

    public static PageFragment create(int pageNumber, CharSequence content) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putCharSequence(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE);
        content = getArguments().getCharSequence(ARG_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        itemBookPage.setText(content);
    }


    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return pageNumber;
    }


}

