package com.stiletto.tr.tests;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.PagesListAdapter;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PDFReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class ListActivity extends AppCompatActivity {

    private Pagination mPagination;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

//        TextPaint textPaint = new TextPaint();
//        textPaint.setTextSize(16f);
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        int densityDpi = (int) metrics.density;
//
//        int width = 320;
//        int height = 500;
//        Log.d("PAGE_", "w: " + width + ", h: " + height + ", d: " + densityDpi);
//        PageSplitter pageSplitter = new PageSplitter(width, height, 1, 0);
//        pageSplitter.append(getBookContent(), textPaint);

        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        Log.d("PAGINATION", "r.w=" + recyclerView.getWidth());
                        Log.d("PAGINATION", "r.h=" + recyclerView.getHeight()
                        );

//                        final CharSequence mText = getBookContent();
//                        mPagination = new Pagination(mText,
//                                recyclerView.getWidth(),
//                                recyclerView.getHeight(),
//                                mTextView.getPaint(),
//                                mTextView.getLineSpacingMultiplier(),
//                                mTextView.getLineSpacingExtra(),
//                                mTextView.getIncludeFontPadding());

                    }
                });

                            PagesListAdapter adapter = new PagesListAdapter(getBookContent());
                            recyclerView.setAdapter(adapter);
//                        }

                        // Removing layout listener to avoid multiple calls
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                            mTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                        } else {
//                            mTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        }
//                    }
//                });

    }

    private List<String> splitOnPages(String text) {
        List<String> list = new ArrayList<>();

        int range = 32 * 25;
        int indexStart = 0;
        int indexEnd = range;


        while (text.length() > indexEnd) {
            list.add(text.substring(indexStart, indexEnd));
            indexStart = indexEnd;
            indexEnd += range;
        }
        list.add(text.substring(indexStart));

        return list;
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
