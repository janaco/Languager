package com.stiletto.tr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.Test;
import com.stiletto.tr.model.TestVariant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 09.05.17.
 */

public class SimpleTestAdapter extends RecyclerView.Adapter<SimpleTestAdapter.ViewHolder>{

    private Test test;

    public SimpleTestAdapter(Test test) {
        this.test = test;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_test, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TestVariant answer = test.getAnswers().get(position);
        holder.itemFront.setText(answer.getProposedAnswer());
        holder.itemBack.setText(answer.getCorrectAnsver());

        holder.layoutBack.setBackgroundResource(
                answer.isCorrect() ? R.drawable.test_card_correct : R.drawable.test_card_wrong
        );
    }

    @Override
    public int getItemCount() {
        return test.getPossibleAnsversCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layoutBack;
        private TextView itemFront;
        private TextView itemBack;

        public ViewHolder(View itemView) {
            super(itemView);

            layoutBack = (LinearLayout) itemView.findViewById(R.id.layout_back);
            itemFront = (TextView) itemView.findViewById(R.id.item_front);
            itemBack = (TextView) itemView.findViewById(R.id.item_back);
        }
    }
}
