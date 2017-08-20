package com.nandy.reader.view;

/**
 * Customized support.v4.app.Fragment
 *
 * Created by yana on 30.12.16.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    public String getName() {
        return getClass().getName();
    }
}
