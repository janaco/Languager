package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.SimpleTestAdapter;
import com.stiletto.tr.model.Test;
import com.stiletto.tr.model.TestVariant;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 09.05.17.
 */

public class SimpleTestFrament extends Fragment {

    @Bind(R.id.title)
    TextView itemTitle;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private SimpleTestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_test, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemTitle.setText("Read");

        List<TestVariant> list = new ArrayList<>();
        list.add(new TestVariant("Learn", "Вивчати", false));
        list.add(new TestVariant("Learn", "Дізначатись", false));
        list.add(new TestVariant("Read", "Читати", true));
        list.add(new TestVariant("Explore", "Досліджувати", false));
        Test test = new Test("Read", list);

        adapter = new SimpleTestAdapter(test);

        recyclerView.setAdapter(adapter);
    }
}
