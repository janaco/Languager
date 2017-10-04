package com.nandy.reader.mvp.contract;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 04.10.17.
 */

public class BookCoverContract {

    public interface View extends BaseView<BasePresenter>{

        void setTitle(String title);

        void setExtention(String extention);
    }
}
