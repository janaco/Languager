package com.nandy.reader.ui.fragments.pager.page;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.model.Book;
import com.nandy.reader.ui.dialogs.floating_translation_dialog.TranslationDialog;
import com.softes.clickabletextview.ClickableTextView;
import com.nandy.reader.R;
import com.nandy.reader.adapter.DictionaryAdapter;
import com.nandy.reader.core.ActionModeCallback;
import com.nandy.reader.core.TranslationCallback;
import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.translator.yandex.Translator;
import com.nandy.reader.utils.ReaderPrefs;
import com.nandy.reader.view.Fragment;
import com.nandy.reader.view.PopupFragment;
import com.nandy.reader.view.StyleCallback;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Single book page content is displayed there.
 * <p>
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment
        implements PageContract.View, ClickableTextView.OnWordClickListener, ActionModeCallback {

    @Bind(R.id.item_content)
    ClickableTextView textView;

    private View popView;
    private PopupFragment popupFragment;

    private PageContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page, container, false);
        ButterKnife.bind(this, view);

        popupFragment = new PopupFragment(getActivity(), view, R.layout.pop_view);

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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.start(getContext());
    }

    @Override
    public void setPresenter(PageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setContentText(String text) {
        textView.setText(text);
    }



    @Override
    public void onTranslateOptionSelected(CharSequence text) {
        presenter.translate(getContext(), text);
    }

    @Override
    public void onClick(final String word, int x, int y) {
        new TranslationDialog().show(getContext(), x, y);
//        presenter.translate(getContext(), word);
    }


    @Override
    public void showPopupWindow() {
     //TODO: change
        if (!popupFragment.isShowing()) {
            popView = popupFragment.showPopup();
        }
        popView.findViewById(R.id.item_close)
                .setOnClickListener(v -> popupFragment.hidePopup());

    }

    @Override
    public void setPopupHeader(String text) {
        TextView textOrigin = (TextView) popView.findViewById(R.id.item_origin);
        textOrigin.setTextColor(Color.WHITE);
        textOrigin.setText(text);
    }


    @Override
    public void setTranslation(String text, String translation) {

        popView.findViewById(R.id.layout_translation).setVisibility(View.VISIBLE);

        TextView textView = (TextView) popView.findViewById(R.id.item_translation);

        String primary = text + "\n";
        primary = primary.toUpperCase(Locale.getDefault());

        SpannableString spannable = new SpannableString(primary + translation);
        spannable.setSpan(new UnderlineSpan(), 0, primary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, primary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new RelativeSizeSpan(0.85f), primary.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.ITALIC), primary.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorSecondaryText)),
                primary.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannable);

    }

    @Override
    public void setDictionaryContent(List<DictionaryItem> items) {
        RecyclerView recyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DictionaryAdapter adapter = new DictionaryAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    public static PageFragment getInstance(Book book, CharSequence content){
        PageFragment fragment = new PageFragment();
        PageModel pageModel = new PageModel(book.getId(), content, new Pair<>(book.getOriginLanguage(), book.getTranslationLanguage()));
        fragment.setPresenter(new PagePresenter(pageModel, fragment));

        return fragment;
    }
}

