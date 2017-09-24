package com.nandy.reader.mvp.contract;

import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 24.09.17.
 */

public class WordContract {

    public interface View extends BaseView<Presenter>{

        void onWordLoaded(Word word);

        void onWordRemoved();
    }

    public interface Presenter extends BasePresenter{

        void removeWord();
    }
}
