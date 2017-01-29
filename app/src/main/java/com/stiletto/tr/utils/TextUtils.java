package com.stiletto.tr.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import com.stiletto.tr.text.style.InteractiveSpan;
import com.stiletto.tr.widget.ClickableTextView;

import java.io.FileOutputStream;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


public class TextUtils {


    public static Bitmap textAsBitmap(String text) {

        Bitmap bmp = Bitmap.createBitmap(600 , 800, Bitmap.Config.ARGB_8888);
        bmp.eraseColor(Color.WHITE);

        android.graphics.Bitmap.Config bitmapConfig = bmp.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bmp = bmp.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bmp);

        TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize(18);
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - 25;

        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(
                text, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // get height of multiline text
        int textHeight = textLayout.getHeight();

        // get position of text's top left corner
        float x = (bmp.getWidth() - textWidth)/2;
        float y = (bmp.getHeight() - textHeight)/2;

        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bmp;
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
