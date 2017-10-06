package com.nandy.reader.mvp.presenter;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.contract.ColumnsChartContract;
import com.nandy.reader.mvp.model.ColumnsChartModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 06.10.17.
 */

public class ColumnsChartPresenter implements BasePresenter {

    private ColumnsChartContract.View view;
    private ColumnsChartModel columnsChartModel;

    private Disposable chartSubscription;

    public ColumnsChartPresenter(ColumnsChartContract.View view){
        this.view = view;
    }

    public void setColumnsChartModel(ColumnsChartModel columnsChartModel) {
        this.columnsChartModel = columnsChartModel;
    }

    @Override
    public void start() {
        chartSubscription = columnsChartModel.getChartValues().observeOn(AndroidSchedulers.mainThread())
                .subscribe(values -> view.drawChart(values));
    }

    @Override
    public void destroy() {
        if (chartSubscription != null && !chartSubscription.isDisposed()){
            chartSubscription.dispose();
        }
    }
}
