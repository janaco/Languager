package com.stiletto.tr.charts;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.stiletto.tr.model.test.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by yana on 22.07.17.
 */

public class PieChart {

    private PieChartView pieChartView;

    private PieChart(PieChartView pieChartView){
        this.pieChartView = pieChartView;
    }

    public static PieChart init(PieChartView pieChartView){
        return new PieChart(pieChartView);
    }

    public void showChart(Period period) {

        Calendar calendar = Calendar.getInstance();

        switch (period) {

            case DAY:
                calendar.set(Calendar.HOUR_OF_DAY, -1);
                break;

            case MONTH:
                calendar.set(Calendar.MONTH, -1);
                break;

            case YEAR:
                calendar.set(Calendar.YEAR, -1);
                break;
        }

        float successPercent = getSuccessPercent(getTestResults(calendar.getTimeInMillis()));
        Log.d("PIE_CHART", "successPercent: " + successPercent );
        drawChart(successPercent);
    }

    private List<Result> getTestResults(long timeFrom) {
        RealmResults<Result> results =
                Realm.getDefaultInstance()
                        .where(Result.class)
                        .greaterThan("timestamp", timeFrom).findAllAsync();
        results.load();
        return results;
    }

    private void drawChart(float successPercent) {

        float failedPercent = 100 - successPercent;

        List<SliceValue> values = new ArrayList<>();

        values.add(new SliceValue(successPercent, Color.GREEN));
        values.add(new SliceValue(failedPercent, Color.RED));


        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOutside(false);
        data.setHasCenterCircle(false);
        data.setSlicesSpacing(24);
        data.setValueLabelBackgroundEnabled(false);

        pieChartView.setPieChartData(data);
    }

    private float getSuccessPercent(List<Result> results) {

        float numberOfPassedTests = 0;
        int numberOfTests = results.size();

        for (Result result : results) {

            if (result.isPassed()) {
                numberOfPassedTests++;
            }
        }

        return numberOfPassedTests / numberOfTests * 100 ;

    }

}
