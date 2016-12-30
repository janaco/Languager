package com.stiletto.tr.tests;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.stiletto.tr.R;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.widget.ClickableTextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by yana on 29.12.16.
 */

public class PageActivity extends AppCompatActivity {

    Pagination mPagination;
    int mCurrentIndex = 0;
    ClickableTextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page);

        tvContent = (ClickableTextView) findViewById(R.id.tvContent);
        tvContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                // Removing layout listener to avoid multiple calls
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    tvContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    tvContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mPagination = new Pagination(
                        getBookContent(),
                        tvContent.getWidth(),
                        tvContent.getHeight(),
                        tvContent.getPaint(),
                        tvContent.getLineSpacingMultiplier(),
                        tvContent.getLineSpacingExtra(),
                        tvContent.getIncludeFontPadding());
                Log.d("PAGINATION", " " + mPagination);
                update();
            }
        });

        LinearLayout previous = (LinearLayout) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex--;
                    update();
                }
            }
        });

        LinearLayout next = (LinearLayout) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex < mPagination.size()) {
                    mCurrentIndex++;
                    update();
                }
            }
        });

    }

    private void update() {
        final CharSequence text = mPagination.get(mCurrentIndex);
        if (text != null) tvContent.setText(text);
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