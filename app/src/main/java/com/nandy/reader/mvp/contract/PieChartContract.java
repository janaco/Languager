package com.nandy.reader.mvp.contract;

import com.nandy.reader.charts.Period;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

import lecho.lib.hellocharts.model.PieChartData;

/**
 * Created by yana on 06.10.17.
 */

public class PieChartContract {

    public interface View extends BaseView<Presenter>{
        void drawChart(PieChartData data);
    }

    public interface Presenter extends BasePresenter{

        void setPeriod(Period period);

    }
}
