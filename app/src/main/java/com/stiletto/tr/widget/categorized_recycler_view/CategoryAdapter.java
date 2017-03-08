package com.stiletto.tr.widget.categorized_recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.stiletto.tr.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by yana on 08.03.17.
 */

public class CategoryAdapter extends BaseAdapter implements OnScrollListener, ListHeader, Filterable {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SECTION + 1;

    private int currentCategoryPosition = 0;
    private int nextCategoryPosition = 0;

    private ArrayList<Integer> positions;
    private ArrayList<String> items;
    private Filter filter;

    public CategoryAdapter(Filter filter,  ArrayList<String> listItems, ArrayList<Integer> listSectionPos) {
        this.items = listItems;
        this.positions = listSectionPos;
        this.filter = filter;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !positions.contains(position);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return positions.contains(position) ? TYPE_SECTION : TYPE_ITEM;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_ITEM:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, null);
                    break;
                case TYPE_SECTION:
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_row_view, null);
                    break;
            }
            holder.textView = (TextView) convertView.findViewById(R.id.row_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(items.get(position).toString());
        return convertView;
    }

    @Override
    public int getPinnedHeaderState(int position) {
        // hide pinned header when items count is zero OR position is less than
        // zero OR
        // there is already a header in list view
        if (getCount() == 0 || position < 0 || positions.indexOf(position) != -1) {
            return PINNED_HEADER_GONE;
        }

        // the header should get pushed up if the top item shown
        // is the last item in a section for a particular letter.
        currentCategoryPosition = getCurrentSectionPosition(position);
        nextCategoryPosition = getNextSectionPosition(currentCategoryPosition);
        if (nextCategoryPosition != -1 && position == nextCategoryPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }

        return PINNED_HEADER_VISIBLE;
    }

    public int getCurrentSectionPosition(int position) {
        String listChar = items.get(position).toString().substring(0, 1).toUpperCase(Locale.getDefault());
        return items.indexOf(listChar);
    }

    public int getNextSectionPosition(int currentSectionPosition) {
        int index = positions.indexOf(currentSectionPosition);
        if ((index + 1) < positions.size()) {
            return positions.get(index + 1);
        }
        return positions.get(index);
    }

    @Override
    public void configurePinnedHeader(View v, int position) {
        // set text in pinned header
        TextView header = (TextView) v;
        currentCategoryPosition = getCurrentSectionPosition(position);
        header.setText(items.get(currentCategoryPosition));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof CategorizedListView) {
            ((CategorizedListView) view).configureHeaderView(firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
