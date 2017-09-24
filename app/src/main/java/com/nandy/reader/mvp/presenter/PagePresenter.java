package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.PageContract;
import com.nandy.reader.mvp.model.PageModel;
import com.nandy.reader.translator.yandex.Translator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yana on 27.08.17.
 */

public class PagePresenter implements PageContract.Presenter {

    private PageModel pageModel;
    private PageContract.View view;

    public PagePresenter(PageContract.View view){
        this.view = view;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    @Override
    public void start() {
        view.setContentText(pageModel.getContent());
    }

    @Override
    public void destroy() {

    }

    @Override
    public void translate( CharSequence text) {

        view.showPopupWindow();
        view.setPopupHeader(text.toString());
         pageModel.translate(text.toString(), new Translator.Callback<Word>() {
             @Override
             public void translationSuccess(Word word) {
                 word.setTranslations(word.getTranslations());
                 view.showPopupWindow();
                 view.setTranslation(text.toString(), word.getTranslationsAsString());
                 pageModel.saveWord( word);

                 if (word.hasTranslations()) {
                     requestDictionaryTranslation( word);
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

    private void requestDictionaryTranslation(final Word word) {
        pageModel.requestDictionary(word.getText(), new Translator.Callback<Dictionary>() {
            @Override
            public void translationSuccess(Dictionary dictionary) {
                view.showPopupWindow();
                view.setDictionaryContent(dictionary.getItems());

                word.setDictionary(dictionary);
                pageModel.saveWord( word);

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
