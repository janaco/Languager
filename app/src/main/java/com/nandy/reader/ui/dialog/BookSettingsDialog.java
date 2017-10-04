package com.nandy.reader.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michael.easydialog.EasyDialog;
import com.nandy.reader.R;
import com.nandy.reader.mvp.contract.SettingsContract;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.ui.SimpleOnItemSelectedListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 24.08.17.
 */

public class BookSettingsDialog implements SettingsContract.View{

    @Bind(R.id.origin_spinner)
    Spinner spinnerLangOrigin;
    @Bind(R.id.translations_spinner)
    Spinner spinnerLangTranslation;

    private Context context;
    private EasyDialog dialog;
    private View view;
    private View attachedView;

    private SettingsContract.Presenter presenter;
    private Callback callback;

    public BookSettingsDialog(View attachedView) {
        this.context = attachedView.getContext();
        this.attachedView = attachedView;
        view = LayoutInflater.from(context).inflate(R.layout.layout_settings_book, null);
        ButterKnife.bind(this, view);
        dialog = new EasyDialog(context);
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void setupSpinners(List<String> values, int originSelection, int translationSelection) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.item_spinner_language, values);
        adapter.setDropDownViewResource(R.layout.item_language_dropdown_item);

        spinnerLangOrigin.setAdapter(adapter);
        spinnerLangOrigin.setSelection(originSelection);
        spinnerLangOrigin.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                presenter.onOriginLanguageSelected(position);
            }
        });

        spinnerLangTranslation.setAdapter(adapter);
        spinnerLangTranslation.setSelection(translationSelection);
        spinnerLangTranslation.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                presenter.onTranslationLanguageSelected(position);
            }
        });
    }


    @Override
    public void onLanguageChanged(Pair<Language, Language> languagePair) {
        callback.onBookLanguageChanged(languagePair);
    }

    public void show() {
        presenter.start();
        dialog
                .setLayout(view)
                .setBackgroundColor(context.getResources().getColor(R.color.white))
                .setLocationByAttachedView(attachedView)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 500, -800, 0)
                .setAnimationAlphaShow(500, 0f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, 0, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(Color.parseColor("#20000000"))
                .show();
    }

    public interface Callback{

        void onBookLanguageChanged(Pair<Language, Language> languagePair);
    }
}
