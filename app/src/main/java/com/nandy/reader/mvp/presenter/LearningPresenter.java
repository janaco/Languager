package com.nandy.reader.mvp.presenter;

import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.contract.LearningContract;
import com.nandy.reader.mvp.model.LearningModel;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 06.10.17.
 */

public class LearningPresenter implements LearningContract.Presenter {

    private LearningContract.View view;
    private LearningModel learningModel;

    private Disposable wordsSubscription;

    public LearningPresenter(LearningContract.View view) {
        this.view = view;
    }

    public void setLearningModel(LearningModel learningModel) {
        this.learningModel = learningModel;
    }

    @Override
    public void start() {
        view.setLanguages(learningModel.getLanguages());

        wordsSubscription = learningModel.loadWords().observeOn(AndroidSchedulers.mainThread())
                .subscribe(loaded -> {
                    if (loaded) {
                        nextWord();
                    }
                });
    }

    @Override
    public void destroy() {
        if (wordsSubscription != null && !wordsSubscription.isDisposed()) {
            wordsSubscription.dispose();
        }
    }

    @Override
    public void previousWord() {
        if (learningModel.hasWords()) {
            showWord(learningModel.getPrevious());
        }
    }

    @Override
    public void nextWord() {

        if (learningModel.hasWords()) {
            showWord(learningModel.getNext());
        }

    }

    private void showWord(Word word) {
        if (word.getDictionaryItems().size() > 0) {
            DictionaryItem item = word.getDictionaryItems().get(
                    new Random().nextInt(word.getDictionaryItems().size()));
            view.setText(item.getOriginText());
            view.setTranscription("[" + item.getTranscription() + "]");
            view.setTranslation(item.getTranslationsAsString());
        } else {
            view.setText(word.getText());
            view.setTranscription("No transcription");
            view.setTranslation(word.getTranslationsAsString());
        }

        String counter = learningModel.getCounterText();
        view.setSmallCount(counter);
        view.setLargeCount(counter);

        view.setPreviousButtonEnabled(learningModel.hasPreviousWord());
        view.setNextButtonEnabled(learningModel.hasNextWord());
    }
}
