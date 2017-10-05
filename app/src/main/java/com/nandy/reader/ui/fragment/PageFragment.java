package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.reader.model.Book;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.ui.dialog.TranslationDialog;
import com.nandy.reader.mvp.model.TranslationsModel;
import com.nandy.reader.mvp.presenter.TranslationsPresenter;
import com.softes.clickabletextview.ClickableTextView;
import com.nandy.reader.R;
import com.nandy.reader.core.ActionModeCallback;
import com.nandy.reader.utils.ReaderPrefs;
import com.nandy.reader.view.StyleCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Single book page content is displayed there.
 * <p>
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment
        implements ClickableTextView.OnWordClickListener, ActionModeCallback {

    private static final String KEY_CONTENT = "text";
    private static final String KEY_BOOK_ID = "book_id";
    private static final String KEY_LANGUAGE_PRIMARY = "lang_primary";
    private static final String KEY_LANGUAGE_TRANSLATION = "lang_translation";
    @Bind(R.id.item_content)
    ClickableTextView textView;

    private String text;
    private String bookId;
    private Pair<Language, Language> languages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = getArguments().getString(KEY_CONTENT);
        bookId = getArguments().getString(KEY_BOOK_ID);
        Language primary = Language.valueOf(getArguments().getString(KEY_LANGUAGE_PRIMARY));
        Language translation = Language.valueOf(getArguments().getString(KEY_LANGUAGE_TRANSLATION));
        languages = new Pair<>(primary, translation);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page, container, false);
        ButterKnife.bind(this, view);


        ReaderPrefs prefs = ReaderPrefs.getPreferences(getContext());
        textView.setPadding(prefs.getPaddingHorizontal(), prefs.getPaddingVertical(), prefs.getPaddingHorizontal(), 0);

        TextPaint paint = prefs.getTextPaint();
        textView.setTextSize(prefs.getTextSize());
        textView.setTextColor(paint.getColor());
        textView.setTypeface(paint.getTypeface());
        textView.setLineSpacing(prefs.getLineSpacingExtra(), prefs.getLineSpacingMultiplier());
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setCustomSelectionActionModeCallback(new StyleCallback(textView, this));
        textView.setTextIsSelectable(true);
        textView.setOnWordClickListener(this);
        textView.setText(text);

        return view;
    }

    @Override
    public void onTranslateOptionSelected(CharSequence text) {
        //TODO: determine word coordinates on the screen to show floating dialog
    }

    @Override
    public void onClick(final String word, int x, int y, int start, int end) {
        showFloatingTranslationDialog(word, x, y);
    }

    private void showFloatingTranslationDialog(String text, int x, int y) {
        TranslationDialog translationDialog = new TranslationDialog(getContext(), new int[]{x, y});

        TranslationsPresenter presenter = new TranslationsPresenter(translationDialog);
        presenter.setTranslationsModel(new TranslationsModel(bookId, languages));

        translationDialog.setPresenter(presenter);
        translationDialog.show(text);
    }

    public static PageFragment getInstance(Book book, CharSequence content) {

        PageFragment fragment = new PageFragment();

        Bundle args = new Bundle();
        args.putString(KEY_CONTENT, content.toString());
        args.putString(KEY_BOOK_ID, book.getId());
        args.putString(KEY_LANGUAGE_PRIMARY, book.getOriginLanguage().name());
        args.putString(KEY_LANGUAGE_TRANSLATION, book.getTranslationLanguage().name());

        fragment.setArguments(args);

        return fragment;

    }
}

