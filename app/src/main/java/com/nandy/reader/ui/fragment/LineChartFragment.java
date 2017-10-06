package com.nandy.reader.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.nandy.reader.R;
import com.nandy.reader.charts.Period;
import com.nandy.reader.model.test.Result;
import com.nandy.reader.mvp.contract.LineChartContract;
import com.nandy.reader.mvp.model.LineChartModel;
import com.nandy.reader.mvp.presenter.LineChartPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by yana on 06.08.17.
 */

public class LineChartFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, LineChartContract.View {

    @Bind(R.id.chart_general_progress)
    LineChartView lineChartView;

    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    private LineChartContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_line_progress, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        radioGroup.setOnCheckedChangeListener(this);
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setPresenter(LineChartContract.Presenter presenter) {
        this.presenter = presenter;
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

    @Override
    public void drawChart(List<PointValue> points, List<AxisValue> xLegendValues) {

        Line line = new Line(points);
        line.setColor(ContextCompat.getColor(getContext(), R.color.pale_brown));
        line.setCubic(true);
        line.setHasLabels(true);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData lineData = new LineChartData(lines);

        Axis axisX = new Axis(xLegendValues);
        axisX.setHasLines(false);
        axisX.setTextColor(ContextCompat.getColor(getContext(), R.color.pale_sandy_brown));
        lineData.setAxisXBottom(axisX);

        Axis axisY = new Axis();
        axisY.setHasLines(true);
        axisY.setHasSeparationLine(false);
        axisY.setHasTiltedLabels(false);
        axisY.setTextColor(Color.TRANSPARENT);
        lineData.setAxisYLeft(axisY);

        lineData.setValueLabelBackgroundEnabled(false);
        lineData.setValueLabelsTextColor(ContextCompat.getColor(getContext(), R.color.pale_sandy_brown));

        lineChartView.setLineChartData(lineData);
        lineChartView.setViewportCalculationEnabled(false);

        int items = lineData.getAxisXBottom().getValues().size();
        Viewport maxViewport = new Viewport(0, 105, items, 0);
        lineChartView.setCurrentViewport(items > 5 ? new Viewport(items - 5, 110, items, 0) : maxViewport);
        lineChartView.setMaximumViewport(maxViewport);

        lineChartView.setScrollEnabled(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChartView.cancelDataAnimation();
        lineChartView.startDataAnimation(300);
    }


    public static LineChartFragment newInstance(List<Result> testResults) {
        LineChartFragment fragment = new LineChartFragment();
        LineChartPresenter presenter = new LineChartPresenter(fragment);
        LineChartModel model = new LineChartModel(testResults);
        presenter.setLineChartModel(model);
        fragment.setPresenter(presenter);

        return fragment;
    }
}
