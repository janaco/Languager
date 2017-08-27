package com.nandy.reader.ui.fragments.pager.page;

import android.content.Context;

import com.nandy.reader.BasePresenter;
import com.nandy.reader.BaseView;
import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.Translator;

import java.util.List;

import rx.Observable;

/**
 * Created by yana on 27.08.17.
 */

public class PageContract {

    public interface Model{

        String getContent();

        void translate(String text, Translator.Callback<Word> callback);

        void requestDictionary(String text, Translator.Callback<Dictionary> callback);

        void saveWord(Context context, Word word);
    }


    public interface View extends BaseView<Presenter>{

        void setContentText(String content);

        void showPopupWindow();

        void setPopupHeader(String text);

        void setTranslation(String text, String translation);

        void setDictionaryContent(List<DictionaryItem> items);

    }

    public interface Presenter extends BasePresenter{


        void translate(Context context, CharSequence text);
    }
}
