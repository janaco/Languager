package com.stiletto.tr;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.stiletto.tr.adapter.PagesListAdapter;
import com.stiletto.tr.readers.PDFReader;

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
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

       List<String> pages = splitOnPages(getBookContent());

        PagesListAdapter adapter = new PagesListAdapter(pages);
        recyclerView.setAdapter(adapter);
    }

    private List<String> splitOnPages(String text){
        List<String> list = new ArrayList<>();

        int range = 32*25;
        int indexStart = 0;
        int indexEnd = range;


            while (text.length() > indexEnd){
                list.add(text.substring(indexStart, indexEnd));
                indexStart = indexEnd;
                indexEnd += range;
            }
        list.add(text.substring(indexStart));

        return list;
    }

    private String getBookContent(){

        File file = new File("/storage/emulated/0/Download/451_za_Farenheitom.pdf");
        try {
            return PDFReader.parseAsText(file.getPath(), 1, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
