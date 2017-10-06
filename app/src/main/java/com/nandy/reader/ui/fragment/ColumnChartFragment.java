package com.nandy.reader.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.reader.R;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.mvp.contract.ColumnsChartContract;
import com.nandy.reader.mvp.model.ColumnsChartModel;
import com.nandy.reader.mvp.presenter.ColumnsChartPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by yana on 06.08.17.
 */

public class ColumnChartFragment extends Fragment implements ColumnsChartContract.View {

    @Bind(R.id.columns_chart)
    ColumnChartView columnChartView;


    private BasePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void drawChart(List<ColumnsChartContract.Value> values) {
        int index = 0;


        List<AxisValue> axisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        for (ColumnsChartContract.Value value : values) {
            List<SubcolumnValue> subcolumns = new ArrayList<>();
            subcolumns.add(new SubcolumnValue(value.words, ContextCompat.getColor(getContext(), R.color.pale_sandy_brown)));
            subcolumns.add(new SubcolumnValue(value.unknownWords, ContextCompat.getColor(getContext(), R.color.tea_rose)));

            Column column = new Column(subcolumns);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);

            axisValues.add(new AxisValue(index++).setLabel(value.book));
        }

        ColumnChartData columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(false));

        Axis axisY = new Axis();
        axisY.setHasLines(true);
        axisY.setTextColor(Color.TRANSPARENT);
        columnData.setAxisYLeft(axisY);


        columnChartView.setColumnChartData(columnData);
        columnChartView.setValueSelectionEnabled(true);
        columnChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);

    }

    public static ColumnChartFragment newInstance(){
        ColumnChartFragment fragment = new ColumnChartFragment();
        ColumnsChartPresenter presenter = new ColumnsChartPresenter(fragment);
        presenter.setColumnsChartModel(new ColumnsChartModel());
        fragment.setPresenter(presenter);

        return fragment;
    }
}
