package com.stiletto.tr.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * User with search view on BookShelfFragment
 *
 * Created by yana on 26.03.17.
 */

public class AutocompleteAdapter extends ArrayAdapter<String> {

    private List<String> items = new ArrayList<>();

    public AutocompleteAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line);
    }

    @Override
    public void add(String object) {
        if (!items.contains(object)) {
            items.add(object);
            super.add(object);
        }
    }

}
