package com.nandy.reader.fragment.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.reader.R;
import com.nandy.reader.charts.ColumnsChart;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.ColumnChartView;

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
