package com.stiletto.tr.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;

import com.stiletto.tr.text.style.InteractiveSpan;
import com.stiletto.tr.widget.ClickableTextView;


public class TextUtils {


    public static SpannableStringBuilder makeTextClickable(String str, final ClickableTextView.OnWordClickListener onWordClickListener) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);


        String[] arr = str.split(" ");

        int currentIndex = 0;
        for (final String text : arr) {

            int indexStart = str.indexOf(text, currentIndex);
            int indexEnd = indexStart + text.length();
            currentIndex = indexEnd + 1;

            builder.setSpan(new InteractiveSpan() {

                @Override
                public void onClick(View widget) {
                    onWordClickListener.onClick(text);
                }

            }, indexStart, indexEnd, 0);
        }

        return builder;
    }


}
