package com.stiletto.tr.widget.categorized_recycler_view;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by yana on 08.03.17.
 */

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
//                for (String item : mItems) {
//                    if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
//                        filterItems.add(item);
//                    }
//                }
                result.count = filterItems.size();
                result.values = filterItems;
            }
        } else {
            synchronized (this) {
//                result.count = mItems.size();
//                result.values = mItems;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        ArrayList<String> filtered = (ArrayList<String>) results.values;
//        setIndexBarViewVisibility(constraint.toString());
//         sort array and extract sections in background Thread
//        new Poplulate().execute(filtered);
    }

}