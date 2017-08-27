package com.nandy.reader.ui.fragments.pager.page;

import android.content.Context;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Translator;

import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;

/**
 * Created by yana on 27.08.17.
 */

public class PagePresenter implements PageContract.Presenter {

    private PageContract.Model model;
    private PageContract.View view;

    public PagePresenter(PageContract.Model model, PageContract.View view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void start(Context context) {
        view.setContentText(model.getContent());
    }

    @Override
    public void destroy() {

    }

    @Override
    public void translate(Context context, CharSequence text) {

        view.showPopupWindow();
        view.setPopupHeader(text.toString());
         model.translate(text.toString(), new Translator.Callback<Word>() {
             @Override
             public void translationSuccess(Word word) {
                 word.setTranslations(word.getTranslations());
                 view.showPopupWindow();
                 view.setTranslation(text.toString(), word.getTranslationsAsString());
                 model.saveWord(context, word);

                 if (word.hasTranslations()) {
                     requestDictionaryTranslation(context, word);
                 }

             }

             @Override
             public void translationFailure(Call call, Response response) {

             }

             @Override
             public void translationError(Call call, Throwable error) {

             }
         });

    }

    private void requestDictionaryTranslation(Context context, final Word word) {
        model.requestDictionary(word.getText(), new Translator.Callback<Dictionary>() {
            @Override
            public void translationSuccess(Dictionary dictionary) {
                view.showPopupWindow();
                view.setDictionaryContent(dictionary.getItems());

                word.setDictionary(dictionary);
                model.saveWord(context, word);

            }

            @Override
            public void translationFailure(Call call, Response response) {

            }

            @Override
            public void translationError(Call call, Throwable error) {

            }
        });

    }
}
