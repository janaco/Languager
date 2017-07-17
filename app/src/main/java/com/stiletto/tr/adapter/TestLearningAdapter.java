package com.stiletto.tr.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.softes.flippy.FlipView;
import com.stiletto.tr.R;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.model.test.ABCTest;

import java.util.List;

/**
 * Created by yana on 27.05.17.
 */

public class TestLearningAdapter extends RecyclerView.Adapter<TestLearningAdapter.ViewHolder> {

    private List<ABCTest.Variant> tests;
    private int colorWrong;
    private int colorCorrect;
    private int colorBase;
    private OnListItemClickListener<ABCTest.Variant> onListItemClickListener;

    public TestLearningAdapter(Context context) {
        colorWrong = ContextCompat.getColor(context, R.color.red_400);
        colorCorrect = ContextCompat.getColor(context, R.color.green_400);
        colorBase = ContextCompat.getColor(context, R.color.colorPrimaryDark);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_learning, null, false));
    }

    public void setOnListItemClickListener(OnListItemClickListener<ABCTest.Variant> onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ABCTest.Variant variant = tests.get(position);

        holder.textFront.setText(variant.getText());
        holder.textBack.setText(variant.getAnswer());

        if (holder.flipView.isFlipped()){
            holder.flipView.toggleView();
        }
        Drawable backDrawable = ContextCompat.getDrawable(holder.textBack.getContext(), R.drawable.rectangle_rounded);
        Drawable frontDrawable = ContextCompat.getDrawable(holder.textBack.getContext(), R.drawable.rectangle_rounded);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frontDrawable.setTint(colorBase);
            backDrawable.setTint(variant.isCorrect() ? colorCorrect : colorWrong);
        }

        holder.textBack.setBackground(backDrawable);
        holder.textFront.setBackground(frontDrawable);

        holder.flipView.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onListItemClickListener.onListItemClick(variant, position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return tests.size();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return 0;
    }

    public void setTests(List<ABCTest.Variant> tests) {
        this.tests = tests;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textFront;
        private TextView textBack;
        private FlipView flipView;

        private ViewHolder(View itemView) {
            super(itemView);

            textFront = (TextView) itemView.findViewById(R.id.text_front);
            textBack = (TextView) itemView.findViewById(R.id.text_back);
            flipView = (FlipView) itemView.findViewById(R.id.flip_view);
        }
    }
}
