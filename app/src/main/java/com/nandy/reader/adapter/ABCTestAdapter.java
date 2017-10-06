package com.nandy.reader.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.nandy.reader.SimpleAnimationEndListener;
import com.softes.flippy.FlipView;
import com.nandy.reader.R;
import com.nandy.reader.OnListItemClickListener;
import com.nandy.reader.model.test.ABCTest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 27.05.17.
 */

public class ABCTestAdapter extends RecyclerView.Adapter<ABCTestAdapter.ViewHolder> {

    private List<ABCTest.Variant> tests = new ArrayList<>();
    private int colorWrong;
    private int colorCorrect;
    private int colorBase;
    private OnListItemClickListener<ABCTest.Variant> onListItemClickListener;

    public ABCTestAdapter(Context context) {
        colorWrong = ContextCompat.getColor(context, R.color.tea_rose);
        colorCorrect = ContextCompat.getColor(context, R.color.verdigris);
        colorBase = ContextCompat.getColor(context, R.color.pale_brown);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_abc, parent, false));
    }

    public void setOnListItemClickListener(OnListItemClickListener<ABCTest.Variant> onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ABCTest.Variant variant = tests.get(position);

        holder.reset();
        holder.setFrontText(variant.getText());
        holder.setBackText(variant.getAnswer());

        holder.setBackColor(variant.isCorrect() ? colorCorrect : colorWrong);
        holder.setFrontColor(colorBase);
        holder.flipView.setAnimationListener(new SimpleAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                onListItemClickListener.onListItemClick(variant, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return tests.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void setTests(List<ABCTest.Variant> tests) {
        this.tests.clear();
        this.tests.addAll(tests);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_front)
        TextView textFront;
        @Bind(R.id.text_back)
        TextView textBack;
        @Bind(R.id.flip_view)
        FlipView flipView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void reset() {
            flipView.reset();
            if (flipView.isFlipped()) {
                flipView.toggleView();
            }
        }

        void setFrontText(String text) {
            textFront.setText(text);
        }

        void setBackText(String text) {
            textBack.setText(text);
        }

        void setBackColor(int color) {
            textBack.setBackgroundColor(color);
        }

        void setFrontColor(int color) {
            textFront.setBackgroundColor(color);
        }
    }
}
