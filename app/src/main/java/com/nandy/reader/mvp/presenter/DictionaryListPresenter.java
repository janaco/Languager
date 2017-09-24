package com.nandy.reader.mvp.presenter;

import com.nandy.reader.adapter.DictionariesListAdapter;
import com.nandy.reader.mvp.contract.DictionaryListContract;
import com.nandy.reader.mvp.model.DictionaryListModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 24.09.17.
 */

public class DictionaryListPresenter implements DictionaryListContract.Presenter {

    private DictionaryListModel dictionaryListModel;
    private DictionaryListContract.View view;

    private Disposable dictionariesLoadingSubscription;

    public DictionaryListPresenter(DictionaryListContract.View view) {
        this.view = view;
    }

    public void setDictionaryListModel(DictionaryListModel dictionaryListModel) {
        this.dictionaryListModel = dictionaryListModel;
    }

    @Override
    public void start() {
        dictionaryListModel.loadDictionaries().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DictionariesListAdapter.Item>() {
                    boolean isDictionaries = false;

                    @Override
                    public void onSubscribe(Disposable d) {
                        dictionariesLoadingSubscription = d;
                    }

                    @Override
                    public void onNext(DictionariesListAdapter.Item item) {
                        view.addDictionary(item);
                        isDictionaries = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!isDictionaries) {
                            view.onDictionaryListEmpty();
                        }
                    }
                });
    }

    @Override
    public void destroy() {

    }
}
