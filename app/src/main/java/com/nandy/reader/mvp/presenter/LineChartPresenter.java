package com.nandy.reader.mvp.presenter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import com.nandy.reader.R;
import com.nandy.reader.charts.Period;
import com.nandy.reader.mvp.contract.LineChartContract;
import com.nandy.reader.mvp.model.LineChartModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;

/**
 * Created by yana on 06.10.17.
 */

public class LineChartPresenter implements LineChartContract.Presenter {

    private LineChartContract.View view;
    private LineChartModel lineChartModel;

    private Disposable chartSubscription;

    public LineChartPresenter(LineChartContract.View view) {
        this.view = view;
    }

    public void setLineChartModel(LineChartModel lineChartModel) {
        this.lineChartModel = lineChartModel;
    }

    @Override
    public void start() {
        setPeriod(Period.MONTH);
    }

    @Override
    public void destroy() {
        if (chartSubscription != null && !chartSubscription.isDisposed()) {
            chartSubscription.dispose();
        }
    }

    @Override
    public void setPeriod(Period period) {
       chartSubscription = lineChartModel.getChart(period)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listListPair -> view.drawChart(listListPair.first, listListPair.second));
    }


}
