package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.WordContract;
import com.nandy.reader.mvp.model.WordModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 24.09.17.
 */

public class WordPresenter implements WordContract.Presenter {

    private WordContract.View view;
    private WordModel wordModel;

    private Disposable wordSubscription;
    private Disposable removeSubscription;

    public WordPresenter(WordContract.View view) {
        this.view = view;
    }

    public void setWordModel(WordModel wordModel) {
        this.wordModel = wordModel;
    }

    @Override
    public void start() {
        wordSubscription = wordModel.loadWord().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(word -> view.onWordLoaded(word));
    }

    @Override
    public void destroy() {
        if (wordSubscription != null && !wordSubscription.isDisposed()) {
            wordSubscription.dispose();
        }
    }

    @Override
    public void removeWord() {
        removeSubscription = wordModel.remove().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> view.onWordRemoved());
    }
}
