package com.stiletto.tr.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.model.test.Result;
import com.stiletto.tr.view.Fragment;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by yana on 22.07.17.
 */

public class FragmentStatistics extends Fragment implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.chart_2)
    LineChartView lineChartView;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;

    private LineChartData lineData;

    enum ProgressGrade {
        DAY, MONTH, YEAR
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);
        radioGroup.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateInitialLineData(getResults(), ProgressGrade.DAY);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {

            case R.id.button_days:
                generateInitialLineData(getResults(), ProgressGrade.DAY);
                break;

            case R.id.button_months:
                generateInitialLineData(getResults(), ProgressGrade.MONTH);
                break;

            case R.id.button_year:
                generateInitialLineData(getResults(), ProgressGrade.YEAR);
                break;
        }
    }


    private void generateInitialLineData(List<Result> results, ProgressGrade grade) {

        List<AxisValue> axisValues = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();

        fillValues(results, values, axisValues, grade);

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setCubic(true);
        line.setHasLabels(true);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        lineData = new LineChartData(lines);

        Axis axisX = new Axis(axisValues);
        axisX.setHasLines(false);
        lineData.setAxisXBottom(axisX);


        lineData.setValueLabelBackgroundEnabled(false);
        lineData.setValueLabelsTextColor(ContextCompat.getColor(getContext(), R.color.green_400));

        lineChartView.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        lineChartView.setViewportCalculationEnabled(false);

//         And set initial max viewport and current viewport- remember to set viewports after data.

        int items = lineData.getAxisXBottom().getValues().size();

        Viewport viewport = new Viewport(0, 105, items, 0);
        if (items > 5){
            lineChartView.setCurrentViewport(new Viewport(items - 5, 110, items, 0));
        }else {
            lineChartView.setCurrentViewport(viewport);
        }
        lineChartView.setMaximumViewport(viewport);

        lineChartView.setScrollEnabled(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.cancelDataAnimation();
        lineChartView.startDataAnimation(300);
    }

    private void fillValues(List<Result> results, List<PointValue> values, List<AxisValue> axisValues, ProgressGrade grade) {

        switch (grade) {
            case DAY:
                fillValuesForDays(results, values, axisValues);
                break;

            case MONTH:
                fillValuesForMonths(results, values, axisValues);
                break;

            case YEAR:
                fillValuesForYears(results, values, axisValues);
        }
    }


    private void fillValuesForDays(List<Result> results, List<PointValue> values, List<AxisValue> axisValues) {

        for (int i = 0; i < results.size(); ++i) {
            Result result = results.get(i);
            values.add(new PointValue(i, result.getPercentage()));
            axisValues.add(new AxisValue(i).setLabel(result.getDate()));
        }
    }


    private void fillValuesForYears(List<Result> results, List<PointValue> values, List<AxisValue> axisValues) {


        Map<Integer, List<Integer>> map = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();

        for (Result result : results) {
            calendar.setTime(new Date(result.getTimestamp()));
            int year = calendar.get(Calendar.YEAR);

            List<Integer> testResults = map.containsKey(year) ? map.get(year) : new ArrayList<Integer>();
            testResults.add(result.getPercentage());
            map.put(year, testResults);

        }

        int xIndex = 0;
        int lastYear = 0;
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {

            int year = entry.getKey();
            if (lastYear == 0) {
                lastYear = year;
            }


            List<Integer> res = entry.getValue();
            int avarageValue = 0;
            for (int value : res) {
                avarageValue += value;
            }
            avarageValue /= res.size();

            while (lastYear < year - 1) {
                Log.d("GENERAL_PROGRESS", "YEAR: " + lastYear);
                values.add(new PointValue(xIndex, 0));
                axisValues.add(new AxisValue(xIndex++).setLabel(String.valueOf(lastYear++)));
            }

            values.add(new PointValue(xIndex, avarageValue));
            axisValues.add(new AxisValue(xIndex++).setLabel(String.valueOf(year)));
            lastYear = year;
        }

    }


    private void fillValuesForMonths(List<Result> results, List<PointValue> values, List<AxisValue> axisValues) {

        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        Map<Integer, TreeMap<Integer, List<Integer>>> map = new TreeMap<>();

//        Map<Integer, List<Integer>> monthsMaps = new TreeMap<>();
//        int i = 0;
//        while (i < 12) {
//            monthsMaps.put(i++, new ArrayList<Integer>());
//        }


        Calendar calendar = Calendar.getInstance();

        for (Result result : results) {
            calendar.setTime(new Date(result.getTimestamp()));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            TreeMap<Integer, List<Integer>> yearMap;
            List<Integer> testResults;

            if (map.containsKey(year)) {
                yearMap = map.get(year);
                testResults = yearMap.containsKey(month) ? yearMap.get(month) : new ArrayList<Integer>();

            } else {
                yearMap = new TreeMap<>();
//                yearMap.putAll(monthsMaps);
                testResults = new ArrayList<>();
            }

            testResults.add(result.getPercentage());
            yearMap.put(month, testResults);
            map.put(year, yearMap);

        }

        int xIndex = 0;
        for (Map.Entry<Integer, TreeMap<Integer, List<Integer>>> entryYear : map.entrySet()) {

            axisValues.add(new AxisValue(xIndex++).setLabel(String.valueOf(entryYear.getKey())));

            for (Map.Entry<Integer, List<Integer>> entryMonth : entryYear.getValue().entrySet()) {

                List<Integer> res = entryMonth.getValue();
                int avarageValue = 0;
                if (res.size() > 0) {
                    for (int value : res) {
                        avarageValue += value;
                    }
                    avarageValue /= res.size();
                }
                int month = entryMonth.getKey();

                values.add(new PointValue(xIndex, avarageValue));
                axisValues.add(new AxisValue(xIndex++).setLabel(months[month]));
            }
        }
    }

    private List<Result> getResults() {
        Calendar calendar = Calendar.getInstance();

        List<Result> results = new ArrayList<>();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, 10);
        results.add(new Result(calendar.getTimeInMillis(), 83, true));


        calendar.set(Calendar.DAY_OF_MONTH, 14);
        results.add(new Result(calendar.getTimeInMillis(), 43, false));

        calendar.set(Calendar.DAY_OF_MONTH, 8);
        results.add(new Result(calendar.getTimeInMillis(), 43, false));

        calendar.set(Calendar.DAY_OF_MONTH, 4);
        results.add(new Result(calendar.getTimeInMillis(), 53, false));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.DAY_OF_MONTH, 4);
        results.add(new Result(calendar.getTimeInMillis(), 54, false));

        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 14);
        results.add(new Result(calendar.getTimeInMillis(), 64, false));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 4);
        results.add(new Result(calendar.getTimeInMillis(), 54, false));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 24);
        results.add(new Result(calendar.getTimeInMillis(), 24, false));

        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        results.add(new Result(calendar.getTimeInMillis(), 24, false));

        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 18);
        results.add(new Result(calendar.getTimeInMillis(), 44, false));

        calendar.set(Calendar.DAY_OF_MONTH, 22);
        results.add(new Result(calendar.getTimeInMillis(), 74, true));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        results.add(new Result(calendar.getTimeInMillis(), 71, true));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        results.add(new Result(calendar.getTimeInMillis(), 61, false));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        results.add(new Result(calendar.getTimeInMillis(), 87, true));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 8);
        results.add(new Result(calendar.getTimeInMillis(), 74, true));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        results.add(new Result(calendar.getTimeInMillis(), 92, true));


        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 4);
        results.add(new Result(calendar.getTimeInMillis(), 54, false));

        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        results.add(new Result(calendar.getTimeInMillis(), 88, true));

        calendar.setTime(new Date(System.currentTimeMillis()));
        results.add(new Result(calendar.getTimeInMillis(), 80, true));

        return results;
    }


}
