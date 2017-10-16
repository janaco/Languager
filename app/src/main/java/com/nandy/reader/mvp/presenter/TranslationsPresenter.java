package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Translation;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.TranslationsContract;
import com.nandy.reader.mvp.model.TranslationsModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 02.09.17.
 */

public class TranslationsPresenter implements TranslationsContract.Presenter {

    private TranslationsContract.View view;

    private TranslationsModel translationsModel;

    private Disposable translationSubscription;
    private Disposable dictionarySubscription;

    public TranslationsPresenter(TranslationsContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

        if (translationSubscription != null && !translationSubscription.isDisposed()) {
            translationSubscription.dispose();
        }

        if (dictionarySubscription != null && !dictionarySubscription.isDisposed()) {
            dictionarySubscription.dispose();
        }
    }

    @Override
    public void translate(CharSequence text) {

        translationsModel.translate(text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Word>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        translationSubscription = d;
                    }

                    @Override
                    public void onSuccess(Word word) {
                        word.setText(text.toString());
                        view.setTranslation(word.getTranslationsAsString());
                        requestDictionary(word);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onTranslationFailed();
                    }
                });
    }

    private void requestDictionary(Word word) {
       dictionarySubscription =  translationsModel.requestDictionary(word.getText())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dictionary -> {
                    word.setDictionary(dictionary);
                    translationsModel.saveWord(word);
                    view.setHasDictionary(dictionary.getItems().size() > 0);
                    if (dictionary.getItems().size() > 0) {
                        view.setDictionaryPreview(getTranslationsPreviewString(dictionary.getItems()));
                        view.setDictionary(dictionary.getItems());
                    }
                }, throwable -> view.setHasDictionary(false));
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
