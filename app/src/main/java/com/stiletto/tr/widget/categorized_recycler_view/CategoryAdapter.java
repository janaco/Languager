package com.stiletto.tr.widget.categorized_recycler_view;

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

/**
 * Created by yana on 08.03.17.
 */

public abstract class CategoryAdapter<T extends CategoryAdapter.ViewHolder>
        extends BaseAdapter implements OnScrollListener,  Filterable {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SECTION + 1;

    private int currentCategoryPosition = 0;
    private int nextCategoryPosition = 0;

    private ArrayList<Integer> positions;

    public CategoryAdapter(ArrayList<Integer> listSectionPos) {
        this.positions = listSectionPos;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_ITEM:
                    convertView = getBaseItemView(position, parent);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                    break;
                case TYPE_SECTION:
                    convertView = getCategoryView(position, parent);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bindView(position, holder, convertView);
        return convertView;
    }

    public abstract View getBaseItemView(int position, ViewGroup parent);

    public abstract View getCategoryView(int position, ViewGroup parent);

    public abstract void bindView(int position, ViewHolder viewHolder, View convertView);
//
//    @Override
//    public int getPinnedHeaderState(int position) {
//        // hide pinned header when items count is zero OR position is less than
//        // zero OR
//        // there is already a header in list view
//        if (getCount() == 0 || position < 0 || positions.indexOf(position) != -1) {
//            return PINNED_HEADER_GONE;
//        }
//
//        // the header should get pushed up if the top item shown
//        // is the last item in a section for a particular letter.
//        currentCategoryPosition = getCurrentSectionPosition(position);
//        nextCategoryPosition = getNextSectionPosition(currentCategoryPosition);
//        if (nextCategoryPosition != -1 && position == nextCategoryPosition - 1) {
//            return PINNED_HEADER_PUSHED_UP;
//        }
//
//        return PINNED_HEADER_VISIBLE;
//    }

    public abstract int getCurrentSectionPosition(int position);

    public int getNextSectionPosition(int currentSectionPosition) {
        int index = positions.indexOf(currentSectionPosition);
        if ((index + 1) < positions.size()) {
            return positions.get(index + 1);
        }
        return positions.get(index);
    }

//    @Override
//    public void configurePinnedHeader(View v, int position) {
//        // set text in pinned header
//        TextView header = (TextView) v;
//        currentCategoryPosition = getCurrentSectionPosition(position);
//        header.setText(getHeader(currentCategoryPosition));
//    }

    public abstract String getHeader(int position);

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof CategorizedListView) {
//            ((CategorizedListView) view).configureHeaderView(firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class ViewHolder {
        public TextView itemOrigin;
        public TextView itemTranslation;

        public ViewHolder(View itemView) {

            itemOrigin = (TextView) itemView.findViewById(R.id.item_text);
            itemTranslation = (TextView) itemView.findViewById(R.id.item_translation);
        }
    }
}
