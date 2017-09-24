package com.nandy.reader.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.nandy.reader.Navigable;
import com.nandy.reader.R;
import com.nandy.reader.ui.fragment.BookshelfFragment;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.utils.ReaderPrefs;

import io.realm.Realm;

/**
 * Created by yana on 30.12.16.
 */

public class MainActivity extends AppCompatActivity implements Navigable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ReaderPrefs.getPreferences(this);
        requestPermissions();

        Realm.init(this);
        replace(BookshelfFragment.newInstance(this));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                NavigationManager.replaceFragment(this, new BookshelfFragment());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void requestPermissions() {
        //TODO: move it to SplashActivity
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void replace(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
                .replace(R.id.container, fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public void remove(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction().remove(fragment)
                .commit();
    }
}
