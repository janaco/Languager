package com.stiletto.tr;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by yana on 11.12.16.
 */

public abstract class InteractiveSpan extends ClickableSpan {


    @Override
    public void updateDrawState(TextPaint textPaint) {// override updateDrawState
//        textPaint.setUnderlineText(false); // set to false to remove underline
    }

}
