package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionariesPagerAdapter;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.model.Word;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * All available dictionaries are displayed there.
 *
 * Created by yana on 12.03.17.
 */

public class DictionariesFragment extends Fragment {

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.title)
    TextView itemTitle;

    private DictionariesPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_pager, container, false);
        ButterKnife.bind(this, view);
        itemTitle.setText(getString(R.string.my_dictionary));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Map<String, ArrayList<Word>> dictionaries = DictionaryTable.getDictionary(getContext());
        adapter = new DictionariesPagerAdapter(getActivity().getSupportFragmentManager(), dictionaries);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.item_back)
    void onBackPressed(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.item_clean)
    void onCleanAllClick(){
        DictionaryTable.clean(getContext());
        Map<String, ArrayList<Word>> dictionaries = DictionaryTable.getDictionary(getContext());
        adapter = new DictionariesPagerAdapter(getActivity().getSupportFragmentManager(), dictionaries);
        viewPager.setAdapter(adapter);    }
}
