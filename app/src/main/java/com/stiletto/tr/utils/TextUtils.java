package com.stiletto.tr.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;


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
        paint.setTextSize(22);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        int textWidth = canvas.getWidth() - 75;

        StaticLayout textLayout = new StaticLayout(
                text, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int textHeight = textLayout.getHeight();

        float x = (bmp.getWidth() - textWidth)/2;
        float y = (bmp.getHeight() - textHeight)/2;

        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bmp;
    }

}
