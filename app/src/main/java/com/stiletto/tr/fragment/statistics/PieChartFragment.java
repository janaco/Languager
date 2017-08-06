package com.stiletto.tr.fragment.statistics;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.stiletto.tr.R;
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
import lecho.lib.hellocharts.view.LineChartView;
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
