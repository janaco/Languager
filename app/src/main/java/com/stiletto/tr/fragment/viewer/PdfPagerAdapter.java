package com.stiletto.tr.fragment.viewer;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.stiletto.tr.widget.ClickableTextView;

import java.util.List;

/**
 * Created by yana on 03.01.17.
 */

public class PdfPagerAdapter extends PagerAdapter {

    private final Context context;
    private List<CharSequence> list;
private int width, height;


    private View.OnTouchListener onViewTapListener;

    public PdfPagerAdapter(Context context, List<CharSequence> list, int width, int height) {
        this.context = context;
        this.list = list;
        this.width = width;
        this.height = height;

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ClickableTextView textView = new ClickableTextView(context);
        textView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        textView.setOnTouchListener(onViewTapListener);

        textView.setText(list.get(position));

        container.addView(textView);
        return textView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnViewTapListener(View.OnTouchListener onViewTapListener) {
        this.onViewTapListener = onViewTapListener;
    }

}
