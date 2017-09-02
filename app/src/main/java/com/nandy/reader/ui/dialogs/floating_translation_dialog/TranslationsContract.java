package com.nandy.reader.ui.dialogs.floating_translation_dialog;

import com.nandy.reader.BasePresenter;
import com.nandy.reader.BaseView;
import com.nandy.reader.model.word.DictionaryItem;

import java.util.List;

/**
 * Created by yana on 02.09.17.
 */

public class TranslationsContract {

    public interface View extends BaseView<Presenter> {

        void setTranslation(String translation);

        void setDictioary(List<DictionaryItem> items);

        void onTranslationFailed();
    }

    public interface Presenter extends BasePresenter {

        void translate(CharSequence text);

    }
}
