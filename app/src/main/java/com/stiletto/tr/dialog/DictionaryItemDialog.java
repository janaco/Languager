package com.stiletto.tr.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingfisherphuoc.quickactiondialog.QuickActionDialogFragment;
import com.stiletto.tr.R;

/**
 * Created by yana on 11.03.17.
 */

public class DictionaryItemDialog extends QuickActionDialogFragment {

    @Override
    protected int getArrowImageViewId() {
        return R.id.item_arrow; //return 0; that mean you do not have an up arrow icon
    }
    @Override
    protected int getLayout() {
        return R.layout.dialog_word_info;
    }
    @Override
    protected boolean isStatusBarVisible() {
        return true; //optional: if status bar is visible in your app
    }
    @Override
    protected boolean isCanceledOnTouchOutside() {
        return true; //optional
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // Set listener, view, data for your dialog fragment
//        view.findViewById(R.id.btnSample).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "Button inside Dialog!!", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }


}