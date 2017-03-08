package com.stiletto.tr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.OnLanguageSelectedListener;
import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.dialog.ChooseLanguageDialog;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.view.Fragment;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 20.01.17.
 */

public class BookSetupFragment extends Fragment {

    @Bind(R.id.btn_translate_from)
    TextView viewTranslateFrom;
    @Bind(R.id.btn_translate_to)
    TextView viewTranslateTo;

    private Language languagePrimary;
    private Language languageTranslation;

    private Book book;

    public static BookSetupFragment create(Book book) {

        BookSetupFragment fragment = new BookSetupFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable("book", book);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traslation_setup, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        languagePrimary = Language.ENGLISH;
        try {
            String countryCode = ((TelephonyManager) getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso();

            if (countryCode != null) {
                languageTranslation = Language.getLanguageForCountry(countryCode);
            }

            if (languageTranslation == null) {
                languageTranslation = Language.getLanguage(Locale.getDefault().getLanguage());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            languageTranslation = Language.ENGLISH;
        }

        viewTranslateFrom.setText(new Locale(languagePrimary.toString()).getDisplayLanguage());
        viewTranslateTo.setText(new Locale(languageTranslation.toString()).getDisplayLanguage());


    }

    @OnClick(R.id.btn_translate_from)
    void chooseBookPrimaryLanguage() {
        ChooseLanguageDialog.show(getActivity(), new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                languagePrimary = language;
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                viewTranslateFrom.setText(displayLanguage);
            }
        });
    }

    @OnClick(R.id.btn_translate_to)
    void chooseBookTranslationLanguage() {
        ChooseLanguageDialog.show(getActivity(), new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                languageTranslation = language;
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                viewTranslateTo.setText(displayLanguage);
            }
        });
    }

    @OnClick(R.id.btn_read)
    void read() {
        book.setOriginLanguage(languagePrimary);
        book.setTranslationLanguage(languageTranslation);

        BooksTable.setLanguages(getContext(), new Language[]{languagePrimary, languageTranslation}, book.getPath());

        FragmentActivity activity = getActivity();

        NavigationManager.removeFragment(activity, this);
        NavigationManager.addFragment(activity, PageViewerFragment.create(book));
    }


}
