package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionariesAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.word.WordInfo;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by yana on 21.05.17.
 */

public class DictionariesFragment extends Fragment implements OnListItemClickListener<WordInfo>{

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.title)
    TextView itemTitle;

    private DictionariesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionaries_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemTitle.setText("My dictionary");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        Map<String, ArrayList<Word>> dictionaries = DictionaryTable.getDictionaries(getContext());
//        adapter = new DictionariesPagerAdapter(getChildFragmentManager(), dictionaries);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<WordInfo> query = realm.where(WordInfo.class);
        RealmResults<WordInfo> results = query.distinct("originLanguage", "translationLanguage");
        adapter = new DictionariesAdapter(
                results.sort("originLanguage", Sort.ASCENDING, "translationLanguage", Sort.ASCENDING));
        adapter.setOnListItemClickListener(this);
        recyclerView.setAdapter(adapter);

        Log.d("REALM_DB", "results: " + results.size());
    }

    @Override
    public void onListItemClick(WordInfo item, int position) {
        DictionaryFragment fragment = new DictionaryFragment();

        Bundle args = new Bundle();
        args.putString("primary", item.getOriginLanguage());
        args.putString("translation", item.getTranslationLanguage());

        fragment.setArguments(args);

        NavigationManager.addFragment(getActivity(), fragment);
    }

    @OnClick(R.id.item_back)
    void onBackPressed(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.item_clean)
    void onCleanAllClick(){
//        DictionaryTable.clean(getContext());
//        Map<String, ArrayList<Word>> dictionaries = DictionaryTable.getDictionaries(getContext());
//        adapter = new DictionariesPagerAdapter(getActivity().getSupportFragmentManager(), dictionaries);
//        viewPager.setAdapter(adapter);
    }
}
