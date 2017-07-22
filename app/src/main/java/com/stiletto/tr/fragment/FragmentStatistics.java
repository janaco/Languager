package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.charts.ColumnsChart;
import com.stiletto.tr.charts.GeneralLearningProgressChart;
import com.stiletto.tr.charts.Period;
import com.stiletto.tr.charts.PieChart;
import com.stiletto.tr.model.test.Result;
import com.stiletto.tr.view.Fragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by yana on 22.07.17.
 */

public class FragmentStatistics extends Fragment implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.chart_general_progress)
    LineChartView lineChartView;
    @Bind(R.id.columns_chart)
    ColumnChartView columnChartView;
    @Bind(R.id.chart_pie)
    PieChartView pieChartView;

    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    private GeneralLearningProgressChart generalLearningProgress;
    private PieChart pieChart;
    private ColumnsChart columnsChart;
    private List<Result> testResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);
        radioGroup.setOnCheckedChangeListener(this);

        generalLearningProgress = GeneralLearningProgressChart.init(lineChartView);
        pieChart = PieChart.init(pieChartView);
        columnsChart = ColumnsChart.init(columnChartView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        testResults = getTestResults();

        onCheckedChanged(radioGroup, radioGroup.getCheckedRadioButtonId());
        columnsChart.showChart();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {

            case R.id.button_days:
                generalLearningProgress.showChart(testResults, Period.DAY);
                pieChart.showChart(Period.DAY);
                break;

            case R.id.button_months:
                generalLearningProgress.showChart(testResults, Period.MONTH);
                pieChart.showChart(Period.MONTH);
                break;

            case R.id.button_year:
                generalLearningProgress.showChart(testResults, Period.YEAR);
                pieChart.showChart(Period.YEAR);
                break;
        }
    }

    private List<Result> getTestResults(){
        RealmResults<Result> results =
                Realm.getDefaultInstance()
                        .where(Result.class)
                        .findAllSortedAsync("timestamp");
        results.load();
        return results;
    }
}
