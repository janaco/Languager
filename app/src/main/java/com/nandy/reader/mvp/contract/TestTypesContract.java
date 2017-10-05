package com.nandy.reader.mvp.contract;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.nandy.reader.emums.TestType;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

/**
 * Created by yana on 05.10.17.
 */

public class TestTypesContract {

    public interface View extends BaseView<BasePresenter>{

        <T extends RecyclerView.Adapter>void setAdapter(T adapter);

        void showNoTestsAlert();

        void replaceWithLearningFragment(String originLanguage, String translationLanguage);

        void replaceWithTestFragment(TestType testType, String originLanguage, String translationLanguage);

    }

}
