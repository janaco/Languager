package com.nandy.reader.mvp;

import android.content.Context;

/**
 * Created by yana on 20.08.17.
 */

public interface BasePresenter {

    void start(Context context);

    void destroy();
}
