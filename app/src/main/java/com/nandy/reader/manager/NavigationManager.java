package com.nandy.reader.manager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nandy.reader.R;

/**
 * Navigation between fragments, dialogs, activities.
 *
 * Created by yana on 30.12.16.
 */

public class NavigationManager {



    public static void removeFragment(FragmentActivity activity, Fragment fragment) {

        if (fragment == null) {
            return;
        }
        activity.getSupportFragmentManager().popBackStack();

        activity.getSupportFragmentManager()
                .beginTransaction().remove(fragment)
                .commit();
    }

    public static void hideKeyboard(Activity activity) {

            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
    }

    public static void showKeyboard(Context context){
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
