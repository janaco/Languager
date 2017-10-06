package com.nandy.reader.mvp.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.nandy.reader.R;
import com.nandy.reader.charts.Period;
import com.nandy.reader.model.test.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

/**
 * Created by yana on 06.10.17.
 */

public class PieChartModel {

    private Context context;

    public PieChartModel(Context context) {
        this.context = context;
    }


    public Single<PieChartData> getChart(Period period) {

        return Single.create(e -> {

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
            e.onSuccess(getChartData(successPercent));
        });
    }

    private List<Result> getTestResults(long timeFrom) {
        RealmResults<Result> results =
                Realm.getDefaultInstance()
                        .where(Result.class)
                        .greaterThan("timestamp", timeFrom).findAllAsync();
        results.load();
        return results;
    }

    private PieChartData getChartData(float successPercent) {

        float failedPercent = 100 - successPercent;

        List<SliceValue> values = new ArrayList<>();

        values.add(new SliceValue(successPercent, ContextCompat.getColor(context, R.color.verdigris)));
        values.add(new SliceValue(failedPercent, ContextCompat.getColor(context, R.color.tea_rose)));


        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOutside(false);
        data.setHasCenterCircle(false);
        data.setSlicesSpacing(24);
        data.setValueLabelBackgroundEnabled(false);

        return data;
    }

    private float getSuccessPercent(List<Result> results) {

        float numberOfPassedTests = 0;
        int numberOfTests = results.size();

        for (Result result : results) {

            if (result.isPassed()) {
                numberOfPassedTests++;
            }
        }

        return numberOfPassedTests / numberOfTests * 100;

    }


}
