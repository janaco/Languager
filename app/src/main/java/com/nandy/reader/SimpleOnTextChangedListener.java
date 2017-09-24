package com.nandy.reader;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by yana on 24.09.17.
 */

public abstract class SimpleOnTextChangedListener implements TextWatcher {

    public abstract void onTextChanged(CharSequence s);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        onTextChanged(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
