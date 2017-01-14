package com.stiletto.tr.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.stiletto.tr.text.style.InteractiveSpan;
import com.stiletto.tr.widget.ClickableTextView;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class TextUtils {


    public static Bitmap textAsBitmap(String text) {
        // adapted from http://stackoverflow.com/a/8799344/1476989
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
//        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = 400; // round
        int height = 800;

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
//
//    public static SpannableStringBuilder makeTextClickable(String str, final ClickableTextView.OnWordClickListener onWordClickListener) {
//        SpannableStringBuilder builder = new SpannableStringBuilder(str);
//
//
//        String[] arr = str.split(" ");
//
//        int currentIndex = 0;
//        for (final String text : arr) {
//
//            int indexStart = str.indexOf(text, currentIndex);
//            int indexEnd = indexStart + text.length();
//            currentIndex = indexEnd + 1;
//
//            builder.setSpan(new InteractiveSpan() {
//
//                @Override
//                public void onClick(View widget) {
//                    onWordClickListener.onClick(text);
//                }
//
//            }, indexStart, indexEnd, 0);
//        }
//
//        return builder;
//    }
//

}
