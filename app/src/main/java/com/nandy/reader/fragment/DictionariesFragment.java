package com.nandy.reader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.adapter.DictionariesListAdapter;
import com.nandy.reader.emums.Status;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.model.word.WordInfo;
import com.nandy.reader.view.Fragment;

import java.util.ArrayList;
import java.util.List;

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

public class DictionariesFragment extends Fragment implements DictionariesListAdapter.OnItemClickListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.view_empty)
    View viewEmty;
    @Bind(R.id.alert)
    TextView viewAlert;



    private DictionariesListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionaries_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<WordInfo> query = realm.where(WordInfo.class);
        RealmResults<WordInfo> results =
                query.distinct("originLanguage", "translationLanguage")
                        .sort("originLanguage", Sort.ASCENDING, "translationLanguage", Sort.ASCENDING);

        List<DictionariesListAdapter.Item> items = new ArrayList<>();
        for (WordInfo info : results) {

            int wordsCount = (int) Realm.getDefaultInstance()
                    .where(Word.class)
                    .equalTo("info.originLanguage", info.getOriginLanguage())
                    .equalTo("info.translationLanguage", info.getTranslationLanguage())
                    .count();

            int unknownWordsCount = (int) Realm.getDefaultInstance()
                    .where(Word.class)
                    .equalTo("info.originLanguage", info.getOriginLanguage())
                    .equalTo("info.translationLanguage", info.getTranslationLanguage())
                    .equalTo("info.status", Status.UNKNOWN.name())
                    .count();

            items.add(new DictionariesListAdapter.Item(info.getOriginLanguage(), info.getTranslationLanguage(), wordsCount, unknownWordsCount));
        }

        if (items.size() == 0) {
            viewEmty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            viewAlert.setText(R.string.no_items_yet);
            return;
        }

        adapter = new DictionariesListAdapter(items);
        adapter.setOnListItemClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onListItemClick(DictionariesListAdapter.Item item, int position) {
        DictionaryFragment fragment = new DictionaryFragment();

        Bundle args = new Bundle();
        args.putString("primary", item.langOrigin);
        args.putString("translation", item.langTranslation);

        fragment.setArguments(args);

        NavigationManager.addFragment(getActivity(), fragment);

    }

    @Override
    public void onItemMenuClick(DictionariesListAdapter.Item item, int position) {

    }


    @OnClick(R.id.close)
    void onBackPressed() {
        getActivity().onBackPressed();
    }

    void onCleanAllClick() {
//        DictionaryTable.clean(getContext());
//        Map<String, ArrayList<Word>> dictionaries = DictionaryTable.getDictionaries(getContext());
//        adapter = new DictionariesPagerAdapter(getActivity().getSupportFragmentManager(), dictionaries);
//        viewPager.setAdapter(adapter);
    }
}
