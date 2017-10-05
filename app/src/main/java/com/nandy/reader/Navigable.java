package com.nandy.reader;


import android.support.v4.app.Fragment;

/**
 * Created by yana on 24.09.17.
 */

public interface Navigable {

    void replace(Fragment fragment);

    void add(Fragment fragment);

    void remove(Fragment fragment);
}
