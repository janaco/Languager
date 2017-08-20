package com.nandy.reader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.core.OnListItemClickListener;
import com.nandy.reader.emums.TestType;

/**
 * Created by yana on 17.07.17.
 */

public class TestTypesAdapter extends RecyclerView.Adapter<TestTypesAdapter.ViewHolder> {

    private TestType[] tests;
    private OnListItemClickListener<TestType> onListItemClickListener;

    public TestTypesAdapter(OnListItemClickListener<TestType> onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
        this.tests = TestType.values();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate( R.layout.item_language, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final TestType type = tests[position];

        holder.viewName.setText(type.name());
        holder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onListItemClick(type, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tests.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView viewName;

        public ViewHolder(View itemView) {
            super(itemView);

            viewName = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
