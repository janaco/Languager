package com.nandy.reader.mvp.presenter;

import com.nandy.reader.emums.Period;
import com.nandy.reader.mvp.contract.LineChartContract;
import com.nandy.reader.mvp.model.LineChartModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listListPair -> view.drawChart(listListPair.first, listListPair.second));
    }


}
