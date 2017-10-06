package com.nandy.reader.mvp.contract;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 06.10.17.
 */

public class LearningContract {

    public interface View extends BaseView<Presenter>{

        void setLanguages(String languages);

        void setText(String text);

        void setTranslation(String translation);

        void setTranscription(String transcription);

        void setSmallCount(String text);

        void setLargeCount(String text);

        void setPreviousButtonEnabled(boolean enabled);

        void setNextButtonEnabled(boolean enabled);
    }

    public interface Presenter extends BasePresenter{

        void previousWord();

        void nextWord();
    }
}
