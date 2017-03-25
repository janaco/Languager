package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.view.Fragment;

/**
 * Created by yana on 19.03.17.
 */

public class FragmentTop  extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    public static FragmentTop newInstance() {
        Bundle args = new Bundle();
        FragmentTop fragment = new FragmentTop();
        return fragment;
    }
}
