package com.nandy.reader.mvp.contract;

import android.content.Context;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Translator;

import java.util.List;

/**
 * Created by yana on 27.08.17.
 */

public class PageContract {

    public interface View extends BaseView<Presenter>{

        void setContentText(String content);

        void showPopupWindow();

        void setPopupHeader(String text);

        void setTranslation(String text, String translation);

        void setDictionaryContent(List<DictionaryItem> items);

    }

    public interface Presenter extends BasePresenter{


        void translate(CharSequence text);
    }
}
