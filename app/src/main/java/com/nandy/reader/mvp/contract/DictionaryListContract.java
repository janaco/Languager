package com.nandy.reader.mvp.contract;

import com.nandy.reader.adapter.DictionariesListAdapter;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 24.09.17.
 */

public class DictionaryListContract {

    public interface View extends BaseView<Presenter>{

        void onDictionaryListEmpty();

        void addDictionary(DictionariesListAdapter.Item item);
    }

    public interface Presenter extends BasePresenter{}
}
