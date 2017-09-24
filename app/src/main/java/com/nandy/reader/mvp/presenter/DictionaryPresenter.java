package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.DictionaryContract;
import com.nandy.reader.mvp.model.DictionaryModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 24.09.17.
 */

public class DictionaryPresenter implements DictionaryContract.Presenter {

    private DictionaryContract.View view;
    private DictionaryModel dictionaryModel;

    private Disposable loadingSubscription;

    public DictionaryPresenter(DictionaryContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {
        loadingSubscription = dictionaryModel.loadItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(word -> view.addItem(word));
    }

    @Override
    public void destroy() {
        if (loadingSubscription != null && !loadingSubscription.isDisposed()) {
            loadingSubscription.dispose();
        }
    }

    public void setDictionaryModel(DictionaryModel dictionaryModel) {
        this.dictionaryModel = dictionaryModel;
    }
}
