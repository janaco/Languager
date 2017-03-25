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

public class FragmentBottom extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom, container, false);
    }

    public static FragmentBottom newInstance() {
        Bundle args = new Bundle();
        FragmentBottom fragment = new FragmentBottom();
        return fragment;
    }
}
