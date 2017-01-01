package com.stiletto.tr.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.stiletto.tr.R;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ClickableTextView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.item_content)
    ClickableTextView itemBookPage;
    @Bind(R.id.previous)
    LinearLayout itemPrevious;
    @Bind(R.id.next)
    LinearLayout itemNext;
    Pagination pagination;
    int currentIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        itemPrevious.setOnClickListener(this);
        itemNext.setOnClickListener(this);

        itemBookPage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    itemBookPage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    itemBookPage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                pagination = new Pagination(getBookContent(), itemBookPage);
                displayPageContent();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.previous:
                toPreviousPage();
                break;

            case R.id.next:
                toNextPage();
                break;
        }
    }

    private void toPreviousPage(){
        if (currentIndex > 0) {
            currentIndex--;
            displayPageContent();
        }
    }

    private void toNextPage(){
        if (currentIndex < pagination.getPagesCount()) {
            currentIndex++;
            displayPageContent();
        }
    }

    private void displayPageContent() {
        final CharSequence text = pagination.get(currentIndex);
        if (text != null){ itemBookPage.setText(text);}
    }

    private String getBookContent() {

        File file = new File("/storage/emulated/0/Download/451_za_Farenheitom.pdf");
        try {
            return PDFReader.parseAsText(file.getPath(), 1, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}

