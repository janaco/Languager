package com.nandy.reader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.activity.MainActivity;
import com.nandy.reader.adapter.DictionariesListAdapter;
import com.nandy.reader.mvp.contract.DictionaryListContract;
import com.nandy.reader.mvp.model.DictionaryListModel;
import com.nandy.reader.mvp.presenter.DictionaryListPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 21.05.17.
 */

public class DictionariesListFragment extends Fragment implements DictionariesListAdapter.OnItemClickListener, DictionaryListContract.View {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.view_empty)
    View viewEmty;
    @Bind(R.id.alert)
    TextView viewAlert;

    private DictionariesListAdapter adapter;
    private DictionaryListContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionaries_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DictionariesListAdapter();
        adapter.setOnListItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void addDictionary(DictionariesListAdapter.Item item) {
        adapter.add(item);
    }

    @Override
    public void onDictionaryListEmpty() {
        viewEmty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        viewAlert.setText(R.string.no_items_yet);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void setPresenter(DictionaryListContract.Presenter presenter) {
this.presenter = presenter;
    }

    @Override
    public void onListItemClick(DictionariesListAdapter.Item item, int position) {

        ((MainActivity) getActivity()).add( DictionaryFragment.newInstance(new Pair<>(item.langOrigin, item.langTranslation)));

    }

    @Override
    public void onItemMenuClick(DictionariesListAdapter.Item item, int position) {

    }


    @OnClick(R.id.close)
    void onBackPressed() {
        getActivity().onBackPressed();
    }

    public static DictionariesListFragment newInstance(Context context){
        DictionariesListFragment fragment = new DictionariesListFragment();

        DictionaryListPresenter presenter =new DictionaryListPresenter(fragment);
        presenter.setDictionaryListModel(new DictionaryListModel(context));

        fragment.setPresenter(presenter);

        return fragment;
    }
}
