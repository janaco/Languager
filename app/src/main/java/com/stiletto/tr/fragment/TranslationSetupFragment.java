package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.core.OnLanguageSelectedListener;
import com.stiletto.tr.dialog.ChooseLanguageDialog;
import com.stiletto.tr.manager.BuilderManager;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ListMenuButton;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 20.01.17.
 */

public class TranslationSetupFragment extends Fragment {

    @Bind(R.id.btn_translate_from) TextView viewTranslateFrom;
    @Bind(R.id.btn_translate_to) TextView viewTranslateTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_traslation_setup, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String defaultLanguage = new Locale(Language.ENGLISH.toString()).getDisplayLanguage();
        String deviceLanguage = Locale.getDefault().getDisplayLanguage();

        viewTranslateFrom.setText(defaultLanguage);
        viewTranslateTo.setText(deviceLanguage);

    }

    @OnClick(R.id.btn_translate_from)
    void chooseBookPrimaryLanguage(){
      showDialog(new OnLanguageSelectedListener() {
          @Override
          public void onLanguageSelected(Language language) {
              String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
              viewTranslateFrom.setText(displayLanguage);
          }
      });
    }

    @OnClick(R.id.btn_translate_to)
    void chooseBookTranslationLanguage(){
        showDialog(new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                viewTranslateTo.setText(displayLanguage);
            }
        });
    }

    @OnClick(R.id.btn_read)
    void read(){
        
    }

    private void showDialog(OnLanguageSelectedListener onLanguageSelectedListener){
        ChooseLanguageDialog dialog = new ChooseLanguageDialog();
        dialog.setOnLanguageSelectedListener(onLanguageSelectedListener);
        dialog.show(getActivity().getSupportFragmentManager(), "ChooseLanguageDialog");
    }
}
