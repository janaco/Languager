package com.stiletto.tr.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.model.TestVariant;

import java.util.List;

/**
 * Created by yana on 27.05.17.
 */

public class TestLearningAdapter extends RecyclerView.Adapter<TestLearningAdapter.ViewHolder> {

    private List<TestVariant> tests;
    private int colorWrong;
    private int colorCorrect;
    private int colorBase;

    public TestLearningAdapter(Context context, List<TestVariant> tests) {
        this.tests = tests;

        colorWrong = ContextCompat.getColor(context, R.color.red_400);
        colorCorrect = ContextCompat.getColor(context, R.color.green_400);
        colorBase = ContextCompat.getColor(context, R.color.colorPrimaryDark);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_learning, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TestVariant variant = tests.get(position);

        holder.textFront.setText(variant.getProposedVariant());
        holder.textBack.setText(variant.getCorrectMeaning());

        Drawable backDrawable = ContextCompat.getDrawable(holder.textBack.getContext(), R.drawable.rectangle_rounded);
        Drawable frontDrawable = ContextCompat.getDrawable(holder.textBack.getContext(), R.drawable.rectangle_rounded);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frontDrawable.setTint(colorBase);
            backDrawable.setTint(variant.isCorrect() ? colorCorrect : colorWrong);
        }

        holder.textBack.setBackground(backDrawable);
        holder.textFront.setBackground(frontDrawable);
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textFront;
        private TextView textBack;

        private ViewHolder(View itemView) {
            super(itemView);

            textFront = (TextView) itemView.findViewById(R.id.text_front);
            textBack = (TextView) itemView.findViewById(R.id.text_back);
        }
    }
}
