package com.stiletto.tr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.PagerAdapter;
import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ClickableTextView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 04.01.17.
 */

public class PageViewerFragment extends Fragment {

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.item_content)
    ClickableTextView itemBookPage;

    private PagerAdapter pagerAdapter;
    private Pagination pagination;
    private String path;

    public static PageViewerFragment create(Book book){

        PageViewerFragment fragment = new PageViewerFragment();

        Bundle arguments = new Bundle();
        arguments.putString("path", book.getPath());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        itemBookPage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    itemBookPage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    itemBookPage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                setUpPages();
            }
        });

    }


    private void setUpPages() {
        pagination = new Pagination(getBookContent(), itemBookPage);

        pagerAdapter = new PagerAdapter(getFragmentManager(), pagination);
        viewPager.setAdapter(pagerAdapter);

    }


    private String getBookContent() {

        File file = new File(path);

        String extension = file.getName().substring(file.getName().indexOf(".")).toLowerCase();

        switch (extension){

            case ".pdf":
                    return PDFReader.parseAsText(file.getPath(), 1, 20);

            case ".epub":
                break;

            case ".txt":
                break;
        }


        return "";
    }
}
