package com.nandy.reader.ui.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michael.easydialog.EasyDialog;
import com.nandy.reader.R;
import com.nandy.reader.translator.yandex.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 24.08.17.
 */

public class BookSettingsDialog {

    @Bind(R.id.origin_spinner)
    Spinner spinnerLangOrigin;
    @Bind(R.id.translations_spinner)
    Spinner spinnerLangTranslation;

    private EasyDialog dialog;
    private View view;
    private Context context;
    private View attachedView;

    public BookSettingsDialog( View attachedView){
        this.context = attachedView.getContext();
        this.attachedView = attachedView;
        view = LayoutInflater.from(context).inflate(R.layout.layout_settings_book, null);
        ButterKnife.bind(this, view);
        dialog = new EasyDialog(context);

        bindView();
    }

    private void bindView(){

        List <String> languages = new ArrayList<>();
        for (Language language: Language.values()){
            languages.add(new Locale(language.toString()).getDisplayLanguage());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLangOrigin.setAdapter(adapter);
        spinnerLangTranslation.setAdapter(adapter);
    }


    public void show(){
        dialog
//                 .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                .setLayout(view)
                .setBackgroundColor(context.getResources().getColor(R.color.white))
                // .setLocation(new location[])//point in screen
                .setLocationByAttachedView(attachedView)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 500, -800, 0)
                .setAnimationAlphaShow(500, 0f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, 0, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(Color.parseColor("#30000000"))
                .show();
    }
}
