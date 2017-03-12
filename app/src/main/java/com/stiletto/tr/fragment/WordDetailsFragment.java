package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionaryAdapter;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 12.03.17.
 */

public class WordDetailsFragment extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ArrayList<DictionaryItem> items;
    private DictionaryAdapter adapter;
    private String key;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = getArguments().getParcelableArrayList("items");
        key = getArguments().getString("key");
    }

    @Override
    protected void onCreateActionBar(ActionBar actionBar) {
        super.onCreateActionBar(actionBar);
        setHasOptionsMenu(true);
        showActionBar(actionBar);
        showUpNavigation(actionBar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_title, null);
        TextView itemTitle = (TextView) view.findViewById(R.id.title);
        itemTitle.setText(key);

        actionBar.setCustomView(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_details, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_word_details, menu);
        MenuItem menuItem = menu.findItem(R.id.item_lang);
        menuItem.setTitle("en-ua");
        menuItem.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                break;

            case R.id.action_remove:

                DictionaryTable.remove(getContext(), new DictionaryItem(key));
                NavigationManager.removeFragment(getActivity(), this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new DictionaryAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
    }

    public static WordDetailsFragment getInstance(String key, ArrayList<DictionaryItem> items) {
        Log.d("TOOLBAR_", "getInstance: " + key);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", items);
        bundle.putString("key", key);

        WordDetailsFragment fragment = new WordDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
}
