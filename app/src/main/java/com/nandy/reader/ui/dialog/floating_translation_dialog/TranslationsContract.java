package com.nandy.reader.ui.dialog.floating_translation_dialog;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.model.word.DictionaryItem;

import java.util.List;

/**
 * Created by yana on 02.09.17.
 */

public class TranslationsContract {

    public interface View extends BaseView<Presenter> {

        void setTranslation(String translation);

        void setDictionary(List<DictionaryItem> items);

        void setDictionaryPreview(String text);

        void setHasDictionary(boolean hasDictionary);

        void onTranslationFailed();
    }

    public interface Presenter extends BasePresenter {

        void translate(CharSequence text);

    }
}
