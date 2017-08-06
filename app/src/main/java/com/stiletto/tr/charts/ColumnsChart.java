package com.stiletto.tr.charts;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.stiletto.tr.R;
import com.stiletto.tr.emums.Status;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.model.word.Word;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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
    private int colorGeneral;
    private int colorUnknown;


    private ColumnsChart(ColumnChartView columnChartView) {
        this.columnChartView = columnChartView;
        colorGeneral = ContextCompat.getColor(columnChartView.getContext(), R.color.pale_sandy_brown);
        colorUnknown = ContextCompat.getColor(columnChartView.getContext(), R.color.tea_rose);
    }

    public static ColumnsChart init(ColumnChartView columnChartView) {
        return new ColumnsChart(columnChartView);
    }

    public void showChart() {
        drawChart(getValues());
    }


    private void drawChart(List<Value> values) {
        int index = 0;


        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        for (Value value : values) {
            List<SubcolumnValue> subcolumns = new ArrayList<>();
            subcolumns.add(new SubcolumnValue(value.words, colorGeneral));
            subcolumns.add(new SubcolumnValue(value.unknownWords, colorUnknown));

            Column column = new Column(subcolumns);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);

            axisValues.add(new AxisValue(index++).setLabel(value.book));
        }

        ColumnChartData columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(false));

        Axis axisY = new Axis();
        axisY.setHasLines(true);
        axisY.setTextColor(Color.TRANSPARENT);
        columnData.setAxisYLeft(axisY);


        columnChartView.setColumnChartData(columnData);
        columnChartView.setValueSelectionEnabled(true);
        columnChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);

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

    private List<Value> getValues() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Book> books = realm
                .where(Book.class).findAllSorted("name");
        books.load();

        List<Value> items = new ArrayList<>();

        for (Book book : books) {
            int wordsCount = (int) realm.where(Word.class)
                    .equalTo("info.bookId", book.getPath())
                    .count();

            Log.d("COLUMN_VALUE", "book: " + book.getName() + ", words: " + wordsCount);

            if (wordsCount > 0) {
                int unknownWordsCount = (int) realm.where(Word.class)
                        .equalTo("info.bookId", book.getPath())
                        .equalTo("info.status", Status.UNKNOWN.name())
                        .count();

                items.add(new Value(book.getName(), wordsCount, unknownWordsCount));
            }
        }

        return items;
    }

    private static class Value {

        final String book;
        final int words;
        final int unknownWords;

        public Value(String book, int words, int unknownWords) {
            this.book = book;
            this.words = words;
            this.unknownWords = unknownWords;
        }
    }
}
