package com.stiletto.tr.fragment;

import android.os.AsyncTask;
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
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.PagerAdapter;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.EPUBReader;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.readers.TxtReader;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ClickableTextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.yuweiguocn.lib.squareloading.SquareLoading;

/**
 * Created by yana on 04.01.17.
 */

public class PageViewerFragment extends Fragment {

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.item_content)
    ClickableTextView itemBookPage;
    @Bind(R.id.item_progress)
    SquareLoading progressBar;
    @Bind(R.id.item_alert)
    TextView itemAlert;

    private boolean isFullScreen = false;

    private PagerAdapter pagerAdapter;
    private Pagination pagination;
    private String path;

    public static PageViewerFragment create(Book book) {

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
        setDecorViewState();

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

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                pagination = new Pagination(getBookContent(), itemBookPage);
                pagerAdapter = new PagerAdapter(getFragmentManager(), pagination);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                itemAlert.setVisibility(View.GONE);
                viewPager.setAdapter(pagerAdapter);
            }
        }.execute();

    }


    private CharSequence getBookContent() {

        File file = new File(path);

        String extension = file.getName().substring(file.getName().indexOf(".")).toLowerCase();

        switch (extension) {

            case ".pdf":
                return PDFReader.parseAsText(file.getPath());

            case ".epub":
                return EPUBReader.parseAsText(file);

            case ".txt":
                return TxtReader.parseAsText(file);
        }


        return "";
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void setDecorViewState() {
        View decorView = getActivity().getWindow().getDecorView();
        int newUiOptions = decorView.getSystemUiVisibility();

        if (!isFullScreen) {
            isFullScreen = true;
            Log.d("DECOR_VIEW", "hide");
            if (Build.VERSION.SDK_INT > 18) {

                newUiOptions ^=
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else if (Build.VERSION.SDK_INT >= 16) {

                newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            } else if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            decorView.setSystemUiVisibility(newUiOptions);
        } else {
            Log.d("DECOR_VIEW", "show");
            isFullScreen = false;

            getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            if (Build.VERSION.SDK_INT >= 16) {
                getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }

            if (Build.VERSION.SDK_INT >= 19) {

                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            ActivitiesCurrentContentView.requestLayout();

//            getView().requestLayout();


        }

    }
}
