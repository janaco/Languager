package com.stiletto.tr.fragment.statistics;

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
 * Created by yana on 06.08.17.
 */

public class ColumnChartFragment extends Fragment {

    @Bind(R.id.columns_chart)
    ColumnChartView columnChartView;

    private ColumnsChart columnsChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_statistics, container, false);
        ButterKnife.bind(this, view);
        columnsChart = ColumnsChart.init(columnChartView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        columnsChart.showChart();
    }
}
