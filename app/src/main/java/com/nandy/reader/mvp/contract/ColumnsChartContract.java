package com.nandy.reader.mvp.contract;

import com.nandy.reader.mvp.BasePresenter;
import com.nandy.reader.mvp.BaseView;
import com.nandy.reader.mvp.model.ColumnsChartModel;

import java.util.List;

/**
 * Created by yana on 06.10.17.
 */

public class ColumnsChartContract {

    public interface View extends BaseView<BasePresenter> {

        void drawChart(List<Value> values);

    }

    public static class Value {

        public final String book;
        public final int words;
        public final int unknownWords;

        public Value(String book, int words, int unknownWords) {
            this.book = book;
            this.words = words;
            this.unknownWords = unknownWords;
        }
    }
}
