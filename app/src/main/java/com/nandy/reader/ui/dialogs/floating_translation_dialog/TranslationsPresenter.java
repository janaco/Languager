package com.nandy.reader.ui.dialogs.floating_translation_dialog;

import android.content.Context;
import android.view.View;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Translation;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Translator;

import java.util.List;

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

    private void requestDictionary(CharSequence text) {
        translationsModel.requestDictionary(text.toString(), new Translator.Callback<Dictionary>() {
            @Override
            public void translationSuccess(Dictionary item) {
                view.setHasDictionary(item.getItems().size() > 0);
                if (item.getItems().size() > 0){
                view.setDictionaryPreview(getTranslationsPreviewString(item.getItems()));
                view.setDictionary(item.getItems());
            }}

            @Override
            public void translationFailure(Call call, Response response) {
                view.setHasDictionary(false);
            }

            @Override
            public void translationError(Call call, Throwable error) {
                view.setHasDictionary(false);

            }
        });
    }

    public void setTranslationsModel(TranslationsModel translationsModel) {
        this.translationsModel = translationsModel;
    }

    private String getTranslationsPreviewString(List<DictionaryItem> items) {

        StringBuilder builder = new StringBuilder();

        for (DictionaryItem item : items) {

            List<Translation> translations = item.getTranslations();

            if (items.size() > 3) {
                builder.append(translations.get(0).getText()).append(", ");
            } else if (items.size() > 2) {
                for (int i = 0; i < 2 && i < translations.size(); i++) {
                    builder.append(translations.get(i).getText()).append(", ");
                }
            } else if (items.size() > 1) {
                for (int i = 0; i < 3 && i < translations.size(); i++) {
                    builder.append(translations.get(i).getText()).append(", ");
                }
            } else {

                for (int i = 0; i < 5 && i < translations.size(); i++) {
                    builder.append(translations.get(i).getText()).append(", ");
                }
            }
        }

        return builder.substring(0, builder.length() - 2).concat("…");
    }
}
