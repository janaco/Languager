package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.GeneralDictionaryAdapter;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.categorized_recycler_view.CategorizedListView;
import com.stiletto.tr.widget.categorized_recycler_view.CategoryAdapter;
import com.stiletto.tr.widget.categorized_recycler_view.CategoryBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 08.03.17.
 */

public class DictionaryFragment extends Fragment {

    @Bind(R.id.recycler_view)
    CategorizedListView listView;


    // unsorted list items
    ArrayList<String> mItems;

    // array list to store section positions
    ArrayList<Integer> mListSectionPos;

    // array list to store listView data
    ArrayList<String> mListItems;


    // custom adapter
    CategoryAdapter mAdaptor;

    List<DictionaryItem> dictionary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dictionary = DictionaryTable.getDictionary(getContext());
        setData(dictionary);
        setListAdaptor();
    }


    private void setData(List<DictionaryItem> list) {
        String prev_section = "";
        for (DictionaryItem item : list) {
            String current_item = item.getOrigin();
            String current_section = current_item.substring(0, 1).toUpperCase(Locale.getDefault());

            if (!prev_section.equals(current_section)) {
                mListItems.add(current_section);
                mListItems.add(current_item);
                // array list of section positions
                mListSectionPos.add(mListItems.indexOf(current_section));
                prev_section = current_section;
            } else {
                mListItems.add(current_item);
            }
        }
    }

    private void setListAdaptor() {
        // create instance of PinnedHeaderAdapter and set adapter to list view
        mAdaptor = new CategoryAdapter(new ListFilter(), mListItems, mListSectionPos);
        listView.setAdapter(mAdaptor);


        // set header view
        View pinnedHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.section_row_view, listView, false);
        listView.setPinnedHeaderView(pinnedHeaderView);

        // set index bar view
        CategoryBarView indexBarView = (CategoryBarView) LayoutInflater.from(getContext())
                .inflate(R.layout.index_bar_view, listView, false);
        indexBarView.setData(listView, mListItems, mListSectionPos);
        listView.setIndexBarView(indexBarView);

        // set preview text view
        View previewTextView = LayoutInflater.from(getContext()).inflate(R.layout.preview_view, listView, false);
        listView.setPreviewView(previewTextView);

        // for configure pinned header view on scroll change
        listView.setOnScrollListener(mAdaptor);
    }

    public class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // NOTE: this function is *always* called from a background thread,
            // and
            // not the UI thread.
            String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
            FilterResults result = new FilterResults();

            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<String> filterItems = new ArrayList<String>();

                synchronized (this) {
                    for (String item : mItems) {
                        if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
                            filterItems.add(item);
                        }
                    }
                    result.count = filterItems.size();
                    result.values = filterItems;
                }
            } else {
                synchronized (this) {
                    result.count = mItems.size();
                    result.values = mItems;
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            ArrayList<String> filtered = (ArrayList<String>) results.values;
            setIndexBarViewVisibility(constraint.toString());
            // sort array and extract sections in background Thread
            setData(dictionary);
            setListAdaptor();
        }

    }

    private void setIndexBarViewVisibility(String constraint) {
        // hide index bar for search results
        if (constraint != null && constraint.length() > 0) {
            listView.setIndexBarVisibility(false);
        } else {
            listView.setIndexBarVisibility(true);
        }
    }
}
