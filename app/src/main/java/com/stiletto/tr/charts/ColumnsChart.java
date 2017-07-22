package com.stiletto.tr.charts;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by yana on 22.07.17.
 */

public class ColumnsChart {

    private ColumnChartView columnChartView;


    private void generateData() {
        generateSubcolumnsData();
    }


    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = 8;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        data.setAxisXBottom(null);
        data.setAxisYLeft(null);

        columnChartView.setColumnChartData(data);

    }

    /**
     * Generates columns with subcolumns, columns have larger separation than subcolumns.
     */
    private void generateSubcolumnsData() {
        int numSubcolumns = 3;
        int numColumns = 3;



        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);


        data.setAxisXBottom(null);
        data.setAxisYLeft(null);

        columnChartView.setColumnChartData(data);

    }


    /**
     */
    private void prepareDataAnimation(ColumnChartData data) {
        for (Column column : data.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget((float) Math.random() * 100);
            }
        }
    }
}
