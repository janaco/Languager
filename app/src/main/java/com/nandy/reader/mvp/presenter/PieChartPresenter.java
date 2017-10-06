package com.nandy.reader.mvp.presenter;

import com.nandy.reader.charts.Period;
import com.nandy.reader.mvp.contract.PieChartContract;
import com.nandy.reader.mvp.model.PieChartModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 06.10.17.
 */

public class PieChartPresenter implements PieChartContract.Presenter {

    private PieChartContract.View view;
    private PieChartModel pieChartModel;
    private Disposable chartSubscription;

    public PieChartPresenter(PieChartContract.View view){
        this.view = view;
    }

    public void setPieChartModel(PieChartModel pieChartModel) {
        this.pieChartModel = pieChartModel;
    }

    @Override
    public void start() {
        setPeriod(Period.MONTH);
    }

    @Override
    public void destroy() {
        if (chartSubscription != null && !chartSubscription.isDisposed()){
            chartSubscription.dispose();
        }
    }

    @Override
    public void setPeriod(Period period) {
        chartSubscription = pieChartModel.getChart(period)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> view.drawChart(data));
    }
}
