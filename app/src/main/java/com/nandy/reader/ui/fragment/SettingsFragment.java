package com.nandy.reader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.reader.R;
import com.nandy.reader.model.test.Result;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 13.08.17.
 */

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.statistics)
    void onAddStatisticsClick(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 3);

        new Result(System.currentTimeMillis(), 42, false).insert();

        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        new Result(System.currentTimeMillis(), 56, false).insert();


        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        new Result(System.currentTimeMillis(), 71, true).insert();


        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 1);

        new Result(System.currentTimeMillis(), 62, false).insert();

        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 18);
        new Result(System.currentTimeMillis(), 46, false).insert();


        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 24);
        new Result(System.currentTimeMillis(), 80, true).insert();

        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        new Result(System.currentTimeMillis(), 72, true).insert();


        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 2);

        new Result(System.currentTimeMillis(), 62, false).insert();

        calendar.set(Calendar.MONTH, 3);
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        new Result(System.currentTimeMillis(), 69, false).insert();

        calendar.set(Calendar.DAY_OF_MONTH, 15);
        new Result(System.currentTimeMillis(), 72, true).insert();


        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        new Result(System.currentTimeMillis(), 80, true).insert();

        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        new Result(System.currentTimeMillis(), 72, true).insert();

        calendar.set(Calendar.MONTH, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 22);
        new Result(System.currentTimeMillis(), 62, false).insert();


        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 10);
        new Result(System.currentTimeMillis(), 85, true).insert();


        calendar.set(Calendar.DAY_OF_MONTH, 12);
        new Result(System.currentTimeMillis(), 70, true).insert();



        calendar.set(Calendar.DAY_OF_MONTH, 20);
        new Result(System.currentTimeMillis(), 56, false).insert();



        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        new Result(System.currentTimeMillis(), 91, true).insert();




    }
}
