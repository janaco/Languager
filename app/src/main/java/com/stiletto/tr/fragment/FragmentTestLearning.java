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
import com.stiletto.tr.adapter.TestLearningAdapter;
import com.stiletto.tr.model.TestLearning;
import com.stiletto.tr.model.TestVariant;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 27.05.17.
 */

public class FragmentTestLearning extends Fragment {

    @Bind(R.id.header)
    TextView header;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_learning, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String item = "Read";
        List<TestVariant> variants = new ArrayList<>();
        variants.add(new TestVariant("Вивчати", "Learn", false));
        variants.add(new TestVariant("Читати", "Read", true));
        variants.add(new TestVariant("Дізнаватись", "Learn", false));
        variants.add(new TestVariant("Досліджувати", "Explore", false));

        TestLearning testLearning = new TestLearning(item, variants);

        header.setText(item);
        TestLearningAdapter adapter = new TestLearningAdapter(getContext(), testLearning.getVariants());
        recyclerView.setAdapter(adapter);
    }
}
