package com.nandy.reader.ui;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by yana on 27.08.17.
 */

public abstract class SimpleOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public abstract void onItemSelected(int position);

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        onItemSelected(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
