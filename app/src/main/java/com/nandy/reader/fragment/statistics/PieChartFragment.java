package com.nandy.reader.fragment.statistics;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.nandy.reader.R;
import com.nandy.reader.charts.Period;
import com.nandy.reader.charts.PieChart;
import com.nandy.reader.model.test.Result;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by yana on 06.08.17.
 */

public class PieChartFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.chart_pie)
    PieChartView pieChartView;

    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    private PieChart pieChart;
    private List<Result> testResults;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testResults = getArguments().getParcelableArrayList("results");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_pie_progress, container, false);
        ButterKnife.bind(this, view);
        radioGroup.setOnCheckedChangeListener(this);
        pieChart = PieChart.init(pieChartView);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onCheckedChanged(radioGroup, radioGroup.getCheckedRadioButtonId());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {

            case R.id.button_days:
                pieChart.showChart(Period.DAY);
                break;

            case R.id.button_months:
                pieChart.showChart(Period.MONTH);
                break;

            case R.id.button_year:
                pieChart.showChart(Period.YEAR);
                break;
        }
    }

}
