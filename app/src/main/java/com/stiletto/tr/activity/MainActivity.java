package com.stiletto.tr.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stiletto.tr.R;
import com.stiletto.tr.fragment.BookshelfFragment;
import com.stiletto.tr.fragment.PageViewerFragment;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.utils.ReaderPrefs;

/**
 * Created by yana on 30.12.16.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ReaderPrefs.getPreferences(this);

        NavigationManager.replaceFragment(this, new BookshelfFragment());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
