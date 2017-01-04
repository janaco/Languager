package com.stiletto.tr.view;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.stiletto.tr.R;

/**
 * Created by yana on 11.12.16.
 */

public class StyleCallback implements ActionMode.Callback {

    private TextView view;

    public StyleCallback(TextView view) {
        this.view = view;
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.style, menu);
        menu.removeItem(android.R.id.selectAll);
        return true;
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.d("CLICK_STR", String.format("onActionItemClicked item=%s/%d", item.toString(), item.getItemId()));
        CharacterStyle cs;
        int start = view.getSelectionStart();
        int end = view.getSelectionEnd();
        SpannableStringBuilder ssb = new SpannableStringBuilder(view.getText());

        switch (item.getItemId()) {

            case R.id.bold:
                cs = new StyleSpan(Typeface.BOLD);
                ssb.setSpan(cs, start, end, 1);
                view.setText(ssb);
                return true;

            case R.id.italic:
                cs = new StyleSpan(Typeface.ITALIC);
                ssb.setSpan(cs, start, end, 1);
                view.setText(ssb);
                return true;

        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}