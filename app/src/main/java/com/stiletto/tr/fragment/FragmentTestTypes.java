package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.TestTypesAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.emums.TestType;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentTestTypes extends Fragment implements OnListItemClickListener<TestType>{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private TestTypesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);
        ButterKnife.bind(this, view);

        adapter = new TestTypesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onListItemClick(TestType item, int position) {

        switch (item){
            case LEARNING:
                NavigationManager.addFragment(getActivity(), FragmentTestLearning.getInstance(getArguments()));
                break;

            case QUICK:
                break;

            case GENERAL:
                NavigationManager.addFragment(getActivity(), FragmentGeneralTest.getInstance(getArguments()));
                break;

            case ON_TIME:
                break;

            case EXAM:
                break;
        }
    }
}
