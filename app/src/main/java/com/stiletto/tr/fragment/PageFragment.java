package com.stiletto.tr.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionaryAdapter;
import com.stiletto.tr.core.ActionModeCallback;
import com.stiletto.tr.core.TranslationCallback;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.model.Translation;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.translator.yandex.Translator;
import com.stiletto.tr.translator.yandex.TranslatorCallback;
import com.stiletto.tr.translator.yandex.model.YandexDictionaryResponse;
import com.stiletto.tr.utils.ReaderPrefs;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.view.PopupFragment;
import com.stiletto.tr.view.StyleCallback;
import com.stiletto.tr.widget.JCTextView;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yana on 01.01.17.
 */

public class PageFragment extends Fragment implements JCTextView.OnWordClickListener, ActionModeCallback {

    @Bind(R.id.item_content)
    JCTextView textView;

    private TranslationCallback translationCallback;


    public static final String ARG_PAGE = "page";
    public static final String ARG_CONTENT = "content";
    private int pageNumber;
    private CharSequence content;

    private View popView;
    private PopupFragment popupFragment;

    private Language primaryLanguage;
    private Language translationLangusage;


    public static PageFragment create(int pageNumber, CharSequence content, Language primaryLang,
                                      Language translationLang, TranslationCallback translationCallback) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putCharSequence(ARG_CONTENT, content);
        args.putString("primary_lang", primaryLang.toString());
        args.putString("trans_lang", translationLang.toString());
        fragment.setArguments(args);

        fragment.setTranslationCallback(translationCallback);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageNumber = getArguments().getInt(ARG_PAGE);
        content = getArguments().getCharSequence(ARG_CONTENT);
        primaryLanguage = Language.getLanguage(getArguments().getString("primary_lang"));
        translationLangusage = Language.getLanguage(getArguments().getString("trans_lang"));
    }

    @Override
    protected void onCreateActionBar(ActionBar actionBar) {
        super.onCreateActionBar(actionBar);
        hideActionBar(actionBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page, container, false);
        ButterKnife.bind(this, view);

        popupFragment = new PopupFragment(getActivity(), view, R.layout.pop_view);

        ReaderPrefs prefs = ReaderPrefs.getPreferences(getContext());
        textView.setPadding(prefs.getPaddingHorizontal(), prefs.getPaddingVertical(),
                prefs.getPaddingHorizontal(), 0);
//        textView.setWidth(prefs.getPageWidth());
//        textView.setHeight(prefs.getPageHeight());

        TextPaint paint = prefs.getTextPaint();

        textView.setTextSize(prefs.getTextSize());

        textView.setTextColor(paint.getColor());
        textView.setTypeface(paint.getTypeface());

        textView.setLineSpacing(prefs.getLineSpacingExtra(), prefs.getLineSpacingMultiplier());

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setCustomSelectionActionModeCallback(new StyleCallback(textView, this));

        textView.setOnWordClickListener(this);
        textView.setTextIsSelectable(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView.setText(content.toString());
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return pageNumber;
    }


    @Override
    public void onClick(final String word) {

        onTranslate(word);

    }

    private void onTranslate(CharSequence text) {
        showPopup();
        TextView textOrigin = (TextView) popView.findViewById(R.id.item_origin);
        textOrigin.setTextColor(Color.WHITE);
        textOrigin.setText(text);
        translate(text);
        if (text.toString().split(" ").length < 3) {
            lookup(text);
        }
    }

    public void showPopup() {
        if (!popupFragment.isShowing()) {
            popView = popupFragment.showPopup();
        }
        popView.findViewById(R.id.item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFragment.hidePopup();
            }
        });

    }


    private void setUpDictionary(List<DictionaryItem> dictionary) {
        RecyclerView recyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DictionaryAdapter adapter = new DictionaryAdapter(getContext(), dictionary);
        recyclerView.setAdapter(adapter);
    }


    private void translate(final CharSequence word) {
        Translator.translate(word, primaryLanguage, translationLangusage, new TranslatorCallback() {
            @Override
            public void translationSuccess(List<DictionaryItem> items) {

                if (popView == null) {
                    showPopup();
                }

                DictionaryItem item = items.get(0);
                String translations = item.getTranslationsAsString();


                popView.findViewById(R.id.layout_translation).setVisibility(View.VISIBLE);

                TextView textView = (TextView) popView.findViewById(R.id.item_translation);

                String primary = word + "\n";
                primary = primary.toUpperCase(Locale.getDefault());

                SpannableString text = new SpannableString(primary + translations);
                text.setSpan(new UnderlineSpan(), 0, primary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.BOLD), 0, primary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                text.setSpan(new RelativeSizeSpan(0.85f), primary.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.ITALIC), primary.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorSecondaryText)),
                        primary.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.setText(text);

                translationCallback.newTranslation(word, item);

            }

            @Override
            public void translationFailure(Call call, Response response) {

            }

            @Override
            public void translationError(Call call, Throwable error) {

            }
        });


    }


    private void lookup(final CharSequence word) {
        Translator.getDictionary(word, primaryLanguage, translationLangusage, new TranslatorCallback() {
                    @Override
                    public void translationSuccess(List<DictionaryItem> items) {

                        if (popView == null) {
                            showPopup();
                        }

                        setUpDictionary(items);
                        DictionaryItem dictionaryItem = new DictionaryItem(word.toString());
                        dictionaryItem.setOriginLanguage(primaryLanguage);
                        dictionaryItem.setTranslationLanguage(translationLangusage);
                        DictionaryTable.insert(getContext(), items, dictionaryItem);

//                    List<DictionaryItem> list = new ArrayList<>();
//                    if (dictionary != null && dictionary.getDictionary() != null) {
//
//                        for (DictionaryWord w : dictionary.getDictionary()) {
//                            String origin = w.getOriginText();
//                            if (w.getTranslations() != null) {
//
//                                for (DictionaryTranslation translation : w.getTranslations()) {
//
//                                    DictionaryItem item = new DictionaryItem(origin);
//                                    item.addTranslation(translation.getTranslatedText());
//                                    item.setTranscription(w.getTranscryption());
//                                    item.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(
//                                            translation.getTranslatedWordType()));
//                                    item.setOriginLanguage(primaryLanguage);
//                                    item.setTranslationLanguage(translationLangusage);
//                                    list.add(item);
//
//                                    if (translation.hasMeanings()) {
//                                        item.setTranscription(null);
//                                        for (Text text : translation.getMeanings()) {
//                                            item.setOrigin(text.getText());
//                                            list.add(item);
//                                        }
//                                    }
//
//                                }
//                            }
//                        }
//                        DictionaryTable.insert(getContext(), list);
//                    }

                    }

                    @Override
                    public void translationFailure(Call call, Response response) {

                    }

                    @Override
                    public void translationError(Call call, Throwable error) {

                    }
                });

    }


    @Override
    public void onTranslateOptionSelected(CharSequence text) {

        onTranslate(text);
    }

    public void setTranslationCallback(TranslationCallback translationCallback) {
        this.translationCallback = translationCallback;
    }
}

