package com.nandy.reader.ui.dialogs.floating_translation_dialog;

import android.content.Context;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Translator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yana on 02.09.17.
 */

public class TranslationsPresenter implements TranslationsContract.Presenter {

    private TranslationsContract.View view;

    private TranslationsModel translationsModel;

    public TranslationsPresenter(TranslationsContract.View view) {
        this.view = view;
    }

    @Override
    public void start(Context context) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void translate(CharSequence text) {

        translationsModel.translate(text.toString(), new Translator.Callback<Word>() {
            @Override
            public void translationSuccess(Word item) {
                view.setTranslation(item.getTranslationsAsString());
                translationsModel.saveWord(item);
                requestDictionary(text);
            }

            @Override
            public void translationFailure(Call call, Response response) {
                view.onTranslationFailed();
            }

            @Override
            public void translationError(Call call, Throwable error) {
                view.onTranslationFailed();
            }
        });
    }

    private void requestDictionary(CharSequence text){
        translationsModel.requestDictionary(text.toString(), new Translator.Callback<Dictionary>() {
            @Override
            public void translationSuccess(Dictionary item) {
                view.setDictioary(item.getItems());
            }

            @Override
            public void translationFailure(Call call, Response response) {

            }

            @Override
            public void translationError(Call call, Throwable error) {

            }
        });
    }
    public void setTranslationsModel(TranslationsModel translationsModel) {
        this.translationsModel = translationsModel;
    }
}
