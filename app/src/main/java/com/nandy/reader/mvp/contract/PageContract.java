package com.nandy.reader.mvp.contract;

import android.util.Pair;

import com.nandy.reader.model.Book;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.translator.yandex.Language;

/**
 * Created by yana on 27.08.17.
 */

public class PageContract {

    public interface View extends BaseView<Presenter>{

        void setContentText(String content);

        void showTranslationDialog(CharSequence word, Pair<Language, Language> languages, String bookId,  int x, int y);
    }

    public interface Presenter extends BasePresenter{

        void onWordClick(String word, int x, int y);
    }
}
