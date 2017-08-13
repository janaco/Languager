package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.StatisticsPagerAdapter;
import com.stiletto.tr.fragment.statistics.ColumnChartFragment;
import com.stiletto.tr.fragment.statistics.LineChartFragment;
import com.stiletto.tr.fragment.statistics.PieChartFragment;
import com.stiletto.tr.model.test.Result;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by yana on 22.07.17.
 */

public class FragmentStatistics extends Fragment {

    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.view_empty)
    View viewEmpty;
    @Bind(R.id.alert)
    TextView viewAlert;

    private StatisticsPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Result> testResults = getTestResults();

        if (testResults.size() == 0){
            viewEmpty.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            viewAlert.setText(R.string.no_learning_progress);
            return;
        }

        Bundle args = new Bundle();
        args.putParcelableArrayList("results", testResults);

        LineChartFragment lineChartFragment = new LineChartFragment();
        lineChartFragment.setArguments(args);

        PieChartFragment pieChartFragment = new PieChartFragment();
        pieChartFragment.setArguments(args);


        adapter = new StatisticsPagerAdapter(getFragmentManager(),
                new Fragment[]{lineChartFragment, pieChartFragment, new ColumnChartFragment()});
        viewPager.setAdapter(adapter);

    }

    @OnClick(R.id.close)
    void onCloseWindowClick(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.share)
    void onShareClick(){

    }

    private ArrayList<Result> getTestResults(){
        RealmResults<Result> results =
                Realm.getDefaultInstance()
                        .where(Result.class)
                        .greaterThan("percentage", 0)
                        .findAllSortedAsync("timestamp");
        results.load();

        return new ArrayList<>(results);
    }
}
