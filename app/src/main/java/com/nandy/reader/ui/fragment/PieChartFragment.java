package com.nandy.reader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.nandy.reader.R;
import com.nandy.reader.emums.Period;
import com.nandy.reader.mvp.contract.PieChartContract;
import com.nandy.reader.mvp.model.PieChartModel;
import com.nandy.reader.mvp.presenter.PieChartPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by yana on 06.08.17.
 */

public class PieChartFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, PieChartContract.View {

    @Bind(R.id.chart_pie)
    PieChartView pieChartView;

    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    private PieChartContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_pie_progress, container, false);
        ButterKnife.bind(this, view);
        radioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setPresenter(PieChartContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void drawChart(PieChartData data) {
        pieChartView.setPieChartData(data);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {

            case R.id.button_days:
                presenter.setPeriod(Period.DAY);
                break;

            case R.id.button_months:
                presenter.setPeriod(Period.MONTH);
                break;

            case R.id.button_year:
                presenter.setPeriod(Period.YEAR);
                break;
        }
    }

    public static PieChartFragment newInstance(Context context){
        PieChartFragment fragment = new PieChartFragment();
        PieChartPresenter presenter = new PieChartPresenter(fragment);
        presenter.setPieChartModel(new PieChartModel(context));
        fragment.setPresenter(presenter);

        return fragment;
    }

}
