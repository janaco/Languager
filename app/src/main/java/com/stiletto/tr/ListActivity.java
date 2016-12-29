package com.stiletto.tr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.stiletto.tr.adapter.PagesListAdapter;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.utils.PageSplitter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 25.12.16.
 */

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(16f);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int densityDpi = (int) metrics.density;

        int width = 320;
        int height = 500;
        Log.d("PAGE_", "w: " + width + ", h: " + height + ", d: " + densityDpi);
        PageSplitter pageSplitter = new PageSplitter(width, height, 1, 0);
        pageSplitter.append(getBookContent(), textPaint);

        PagesListAdapter adapter = new PagesListAdapter(pageSplitter.getPages());
        recyclerView.setAdapter(adapter);
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
