package com.nandy.reader.mvp.contract;

import com.nandy.reader.emums.Period;
import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;

import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by yana on 06.10.17.
 */

public class LineChartContract {

    public interface View extends BaseView<Presenter>{

        void drawChart(List<PointValue> points, List<AxisValue> axisValues);

    }

    public interface Presenter extends BasePresenter{

        void setPeriod(Period period);
    }
}
