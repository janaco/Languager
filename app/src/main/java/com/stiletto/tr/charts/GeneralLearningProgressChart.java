package com.stiletto.tr.charts;

import android.support.v4.content.ContextCompat;

import com.stiletto.tr.R;
import com.stiletto.tr.model.test.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

public class GeneralLearningProgressChart {

    private static final String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private LineChartView lineChartView;

    private GeneralLearningProgressChart(LineChartView lineChartView) {
        this.lineChartView = lineChartView;
    }

    public static GeneralLearningProgressChart init(LineChartView lineChartView) {
        return new GeneralLearningProgressChart(lineChartView);
    }

    public void showChart(List<Result> testResults, Period period) {

        List<PointValue> points = new ArrayList<>();
        List<AxisValue> xLegendValues = new ArrayList<>();

        switch (period) {

            case DAY:
                getDailyStatisticsData(testResults, points, xLegendValues);
                break;

            case MONTH:
                getMonthlyStatisticsData(testResults, points, xLegendValues);
                break;

            case YEAR:
                getYearlyStatisticsData(testResults, points, xLegendValues);
                break;
        }
        drawChart(points, xLegendValues);
    }


    private void drawChart(List<PointValue> points, List<AxisValue> xLegendValues) {

        Line line = new Line(points);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setCubic(true);
        line.setHasLabels(true);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData lineData = new LineChartData(lines);

        Axis axisX = new Axis(xLegendValues);
        axisX.setHasLines(false);
        lineData.setAxisXBottom(axisX);


        lineData.setValueLabelBackgroundEnabled(false);
        lineData.setValueLabelsTextColor(ContextCompat.getColor(lineChartView.getContext(), R.color.green_400));

        lineChartView.setLineChartData(lineData);
        lineChartView.setViewportCalculationEnabled(false);

        int items = lineData.getAxisXBottom().getValues().size();
        Viewport maxViewport = new Viewport(0, 105, items, 0);
        lineChartView.setCurrentViewport(items > 5 ? new Viewport(items - 5, 110, items, 0) : maxViewport);
        lineChartView.setMaximumViewport(maxViewport);

        lineChartView.setScrollEnabled(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.cancelDataAnimation();
        lineChartView.startDataAnimation(300);
    }


    private void getDailyStatisticsData(List<Result> results, List<PointValue> values, List<AxisValue> axisValues) {

        for (int i = 0; i < results.size(); ++i) {
            Result result = results.get(i);
            values.add(new PointValue(i, result.getPercentage()));
            axisValues.add(new AxisValue(i).setLabel(result.getDate()));
        }
    }


    private static void getYearlyStatisticsData(List<Result> results, List<PointValue> points, List<AxisValue> axisValues) {

        Map<Integer, List<Integer>> data = new TreeMap<>();
        organizeYearlyStatisticsData(results, data);
        convertToYearlyChartData(data, points, axisValues);
    }

    private static void organizeYearlyStatisticsData(List<Result> testResults, Map<Integer, List<Integer>> map) {
        Calendar calendar = Calendar.getInstance();

        for (Result result : testResults) {
            calendar.setTime(new Date(result.getTimestamp()));
            int year = calendar.get(Calendar.YEAR);

            List<Integer> values = map.containsKey(year) ? map.get(year) : new ArrayList<Integer>();
            values.add(result.getPercentage());
            map.put(year, values);

        }
    }

    private static void organizeMonthlyStatistics(List<Result> testResults,
                                                  Map<Integer, TreeMap<Integer, List<Integer>>> data) {

        Calendar calendar = Calendar.getInstance();

        for (Result result : testResults) {
            calendar.setTime(new Date(result.getTimestamp()));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            TreeMap<Integer, List<Integer>> yearMap;
            List<Integer> values;

            if (data.containsKey(year)) {
                yearMap = data.get(year);
                values = yearMap.containsKey(month) ? yearMap.get(month) : new ArrayList<Integer>();

            } else {
                yearMap = new TreeMap<>();
                values = new ArrayList<>();
            }

            values.add(result.getPercentage());
            yearMap.put(month, values);
            data.put(year, yearMap);
        }
    }

    private static void getMonthlyStatisticsData(List<Result> results, List<PointValue> values, List<AxisValue> axisValues) {
        Map<Integer, TreeMap<Integer, List<Integer>>> data = new TreeMap<>();
        organizeMonthlyStatistics(results, data);
        convertToMonthlyChartData(data, values, axisValues);
    }

    private static void convertToYearlyChartData(Map<Integer, List<Integer>> data,
                                                 List<PointValue> points, List<AxisValue> xLegendValues) {
        int xIndex = 0;

        for (Map.Entry<Integer, List<Integer>> entry : data.entrySet()) {

            int year = entry.getKey();

            List<Integer> res = entry.getValue();
            int averageValue = 0;
            for (int value : res) {
                averageValue += value;
            }
            averageValue /= res.size();

            points.add(new PointValue(xIndex, averageValue));
            xLegendValues.add(new AxisValue(xIndex++).setLabel(String.valueOf(year)));
        }
    }

    private static void convertToMonthlyChartData(Map<Integer, TreeMap<Integer, List<Integer>>> data,
                                           List<PointValue> points,
                                           List<AxisValue> xLegendValues){

        int xIndex = 0;
        for (Map.Entry<Integer, TreeMap<Integer, List<Integer>>> entryYear : data.entrySet()) {

            xLegendValues.add(new AxisValue(xIndex++).setLabel(String.valueOf(entryYear.getKey())));

            for (Map.Entry<Integer, List<Integer>> entryMonth : entryYear.getValue().entrySet()) {

                List<Integer> res = entryMonth.getValue();
                int averageValue = 0;
                if (res.size() > 0) {
                    for (int value : res) {
                        averageValue += value;
                    }
                    averageValue /= res.size();
                }
                int month = entryMonth.getKey();

                points.add(new PointValue(xIndex, averageValue));
                xLegendValues.add(new AxisValue(xIndex++).setLabel(MONTHS[month]));
            }
        }
    }


}
