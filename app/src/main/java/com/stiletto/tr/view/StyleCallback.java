package com.stiletto.tr.view;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.ActionModeCallback;

/**
 * Created by yana on 11.12.16.
 */

public class StyleCallback implements ActionMode.Callback {

    private TextView view;
    private ActionModeCallback callback;

    public StyleCallback(TextView view, ActionModeCallback callback) {
        this.view = view;
        this.callback = callback;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.style, menu);
        menu.removeItem(android.R.id.selectAll);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        int indexStart = view.getSelectionStart();
        int indexEnd = view.getSelectionEnd();

        CharSequence selectedText =  view.getText().subSequence(indexStart, indexEnd);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(view.getText());
        ForegroundColorSpan foregroundColorSpan;

        switch (item.getItemId()) {

            case R.id.item_translate:
                CharacterStyle characterStyle = new StyleSpan(Typeface.BOLD);
                foregroundColorSpan = new ForegroundColorSpan(
                        ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));

                stringBuilder.setSpan(characterStyle, indexStart, indexEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.setSpan(foregroundColorSpan, indexStart, indexEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                view.setText(stringBuilder);

                callback.onTranslateOptionSelected(selectedText);
                return true;

            case R.id.item_highlight:
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(
                        ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
                foregroundColorSpan = new ForegroundColorSpan(
                        ContextCompat.getColor(view.getContext(), R.color.colorSecondaryText));

                stringBuilder.setSpan(backgroundColorSpan, indexStart, indexEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilder.setSpan(foregroundColorSpan, indexStart, indexEnd,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                view.setText(stringBuilder);
                return true;

        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {}
}