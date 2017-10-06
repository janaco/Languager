package com.nandy.reader.mvp.model;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;

import com.nandy.reader.R;
import com.nandy.reader.charts.Period;
import com.nandy.reader.model.test.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by yana on 06.10.17.
 */

public class LineChartModel {

    private static final String[] MONTHS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private List<Result> testResults;

    public LineChartModel(List<Result> testResults) {
        this.testResults = testResults;
    }


    public Single<Pair<List<PointValue>, List<AxisValue>>> getChart(Period period) {

        return Single.create(e -> {
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

            e.onSuccess(new Pair<>(points, xLegendValues));
        });


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
                                                  List<AxisValue> xLegendValues) {

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
