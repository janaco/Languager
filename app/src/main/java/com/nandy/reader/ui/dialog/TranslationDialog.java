package com.nandy.reader.ui.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.chuross.library.ExpandableLayout;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.michael.easydialog.EasyDialog;
import com.nandy.reader.R;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.mvp.contract.TranslationsContract;
import com.nandy.reader.adapter.FloatingDictionaryAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 27.08.17.
 */

public class TranslationDialog implements TranslationsContract.View {


    @Bind(R.id.translation)
    TextView viewTranslation;
    @Bind(R.id.translations)
    TextView viewTranslations;
    @Bind(R.id.layout_expandable)
    ExpandableLayout expandableLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progress)
    TextView viewProgress;
    @Bind(R.id.layout_translations)
    ExpandableLayout layoutTranslations;

    private EasyDialog dialog;
    private RotatingPlane rotatingPlane;
    private TranslationsContract.Presenter presenter;

    private float scaledDensity;
    private boolean isExpandable;

    public TranslationDialog(Context context, int coordinates[]) {
       View view = LayoutInflater.from(context).inflate(R.layout.dialog_translation, null, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        scaledDensity = metrics.scaledDensity;

        int bounds = (int) scaledDensity * 10;
        rotatingPlane = new RotatingPlane();
        rotatingPlane.setBounds(0, 0, bounds, bounds);
        rotatingPlane.setColor(ContextCompat.getColor(context, R.color.pine_green));

        viewProgress.setCompoundDrawables(rotatingPlane, null, null, null);
        rotatingPlane.start();

        dialog = new EasyDialog(context)
                .setLayout(view)
                .setBackgroundColor(context.getResources().getColor(R.color.white))
                .setLocation(coordinates)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 500, -800, 0)
                .setAnimationAlphaShow(500, 0f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, 0, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight((int) scaledDensity * 24, (int) scaledDensity * 24)
                .setOutsideColor(Color.parseColor("#20000000"));

    }

    @OnClick(R.id.translation_preview)
    void onTranslationClick() {
        if (expandableLayout.isCollapsed() && isExpandable) {
            ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(viewTranslations, "alpha", 1f, 0f);
            ObjectAnimator animatorExpandable = ObjectAnimator.ofFloat(expandableLayout, "translationY", 0, -35 * scaledDensity);

            AnimatorSet set = new AnimatorSet();
            set.setDuration(500);
            set.playTogether(animatorTranslation, animatorExpandable);
            set.start();
            expandableLayout.expand();

        } else {
            ObjectAnimator animatorTranslation = ObjectAnimator.ofFloat(viewTranslations, "alpha", 0f, 1f);
            ObjectAnimator animatorExpandable = ObjectAnimator.ofFloat(expandableLayout, "translationY", -35 * scaledDensity, 0);

            AnimatorSet set = new AnimatorSet();
            set.setDuration(500);
            set.playTogether(animatorTranslation, animatorExpandable);
            set.start();
            expandableLayout.collapse();
        }
    }

    @Override
    public void setPresenter(TranslationsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTranslation(String translation) {
        viewTranslation.setText(translation);
        viewTranslation.setVisibility(View.VISIBLE);
        rotatingPlane.stop();
        viewProgress.setVisibility(View.GONE);
    }

    @Override
    public void setDictionary(List<DictionaryItem> items) {
        FloatingDictionaryAdapter adapter = new FloatingDictionaryAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setDictionaryPreview(String text) {
        viewTranslations.setText(text);
    }

    @Override
    public void setHasDictionary(boolean hasDictionary) {
        isExpandable = hasDictionary;

        if (hasDictionary) {
            layoutTranslations.expand();
        }
    }

    @Override
    public void onTranslationFailed() {
        viewTranslation.setText("Failed to translate");
    }

    public void show(CharSequence text) {
        presenter.translate(text);
        dialog.show();
    }
}
