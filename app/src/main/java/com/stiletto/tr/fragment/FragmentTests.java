package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.view.Fragment;

import butterknife.ButterKnife;

/**
 * Created by yana on 27.05.17.
 */

public class FragmentTests extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
