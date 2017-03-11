package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingfisherphuoc.quickactiondialog.AlignmentFlag;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.CategoryAdapter;
import com.stiletto.tr.adapter.GeneralDictionaryAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.dialog.DictionaryItemDialog;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.list.CategoriredList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryFragment extends Fragment implements GeneralDictionaryAdapter.OnItemClickListener {

    @Bind(R.id.recycler_view)
    CategoriredList recyclerView;
//    @Bind(R.id.category_view)
//    CategoriredList categoriesList;

    private GeneralDictionaryAdapter adapter;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        categoriesList.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<DictionaryItem> dictionary = DictionaryTable.getDictionary(getContext());
        adapter = new GeneralDictionaryAdapter(dictionary);
        adapter.setOnListItemClickListener(this);
        categoryAdapter = new CategoryAdapter(dictionary);
        recyclerView.setAdapter(adapter);

        HashSet<String> set = new HashSet<>();
        for (DictionaryItem item : dictionary) {
            String word = item.getOriginText().substring(0, 1).toUpperCase();
            set.add(word);
        }

        ArrayList<String> items = new ArrayList<>(set);
        Collections.sort(items);
        recyclerView.setIndexBarItems(items);

        if (adapter.getItemCount() <= 10){
            recyclerView.setIndexBarVisibility(View.GONE);
        }

//        categoriesList.setAdapter(categoryAdapter);
    }

    @Override
    public void onItemClick(View view, DictionaryItem item, int position) {
        DictionaryItemDialog dialog = new DictionaryItemDialog();
        dialog.setAnchorView(view);
        dialog.setAligmentFlags(AlignmentFlag.ALIGN_ANCHOR_VIEW_LEFT | AlignmentFlag.ALIGN_ANCHOR_VIEW_BOTTOM);
        dialog.show(getActivity().getSupportFragmentManager(), "DictionaryItemDialog");
    }


//    private void setListAdaptor(List<DictionaryItem> list) {
//        // create instance of PinnedHeaderAdapter and set adapter to list view
//        mAdaptor = new DAdapter(list, mListSectionPos);
//        listView.setAdapter(mAdaptor);
//
//
//        // set header view
//        View pinnedHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.section_row_view, listView, false);
//        listView.setPinnedHeaderView(pinnedHeaderView);
//
//        // set index bar view
//        CategoryBarView indexBarView = (CategoryBarView) LayoutInflater.from(getContext())
//                .inflate(R.layout.index_bar_view, listView, false);
//        indexBarView.setData(listView, mListItems, mListSectionPos);
//        listView.setIndexBarView(indexBarView);
//
//        // set preview text view
//        View previewTextView = LayoutInflater.from(getContext()).inflate(R.layout.preview_view, listView, false);
//        listView.setPreviewView(previewTextView);
//
//        // for configure pinned header view on scroll change
//        listView.setOnScrollListener(mAdaptor);
//    }

}
