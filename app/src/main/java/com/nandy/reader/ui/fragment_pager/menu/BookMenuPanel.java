package com.nandy.reader.ui.fragment_pager.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nandy.reader.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 20.08.17.
 */

public class BookMenuPanel implements MenuContract.View,
        SeekBar.OnSeekBarChangeListener, DiscreteSeekBar.OnProgressChangeListener {

    private static final int PANEL_HEIGHT = 160;
    private static final int ANIM_DURATION = 500;

    @Bind(R.id.panel_top)
    View viewTop;
    @Bind(R.id.panel_bottom)
    View viewBottom;
    @Bind(R.id.background)
    View viewBackground;
    @Bind(R.id.footer)
    View viewFooter;
    @Bind(R.id.pager)
    View mainView;

    @Bind(R.id.languages)
    TextView viewLanguages;
    @Bind(R.id.title)
    TextView viewTitle;
    @Bind(R.id.author)
    TextView viewAuthor;
    @Bind(R.id.page)
    TextView viewCurrentPage;
    @Bind(R.id.pages_count)
    TextView viewPagesCount;
    @Bind(R.id.items_in_dictionary)
    TextView viewItemsInDictionary;
    @Bind(R.id.items_to_learn)
    TextView viewItemsToLearn;
    @Bind(R.id.footer_pages)
    TextView viewPagesFooter;
    @Bind(R.id.footer_title)
    TextView viewTitleFooter;

    @Bind(R.id.brightness)
    DiscreteSeekBar seekBarBrightness;
    @Bind(R.id.seekbar_pages)
    DiscreteSeekBar seekBarPages;

    private Window window;

    private boolean opened;
    private float scaledDensity;

    private MenuContract.Presenter presenter;

    public BookMenuPanel(View view, Window window) {
        ButterKnife.bind(this, view);
        this.window = window;

        WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        seekBarBrightness.setOnProgressChangeListener(this);

        scaledDensity = metrics.scaledDensity;

//        seekBarPages.setOnSeekBarChangeListener(this);
    }

    @Override
    public void setPresenter(MenuContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.start(window.getContext());
    }

    @Override
    public void setBrightnessOnStart(int value) {
        seekBarBrightness.setProgress(value);
    }

    @OnClick(R.id.menu)
    void onMenuClick() {
        presenter.onMenuClick();
    }

    @OnClick(R.id.background)
    void onBackgroundClick() {
        presenter.onBackgroundClick();
    }

    @OnClick(R.id.settings)
    void onSettingsClick() {
        presenter.onSettingsClick();
    }

    @OnClick(R.id.dictionary)
    void onDictionaryClick() {
        presenter.onDictionaryClick();
    }

    @OnClick(R.id.info)
    void onInfoClick() {
        presenter.onInfoClick();
    }

    @OnClick(R.id.close)
    void onCloseClick() {
        presenter.onCloseClick();
    }

    @Override
    public void setLanguages(String languages) {
        viewLanguages.setText(languages);
    }

    @Override
    public void setTitle(String title) {
        viewTitle.setText(title);
        viewTitleFooter.setText(title);
    }

    @Override
    public void setAuthor(String author) {
        viewAuthor.setText(author);
    }

    @Override
    public void setCurrentPage(String page) {
        viewCurrentPage.setText(page);
    }

    @Override
    public void setPagesCount(String count) {
        viewPagesCount.setText(count);
    }

    @Override
    public void setItemsInDictionary(String text) {
        viewItemsInDictionary.setText(text);
    }

    @Override
    public void setItemsToLearn(String text) {
        viewItemsToLearn.setText(text);
        viewItemsToLearn.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCurrentPagesProgress(int progress) {
        seekBarPages.setProgress(progress);
    }

    @Override
    public void setMaxPagesProgress(int progress) {
        seekBarPages.setMax(progress);
    }

    @Override
    public void onPageChanged(int page) {
        presenter.onPageChanged(page);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        presenter.onPagesProgressChanged(progress, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void setNoItemsInTheDictionary() {
        viewItemsInDictionary.setText("No items in the dictionary.");
        viewItemsToLearn.setVisibility(View.GONE);
    }

    @Override
    public void setFooterPagesText(String text) {
        viewPagesFooter.setText(text);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBookParsingFinished(int pagesCount) {
        presenter.onBookParsingFinished(pagesCount);
        viewFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {

        if (opened) {
            return;
        }
        ObjectAnimator topAnimator = ObjectAnimator.ofFloat(viewTop, "translationY", -PANEL_HEIGHT * scaledDensity, 0);
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(viewBottom, "translationY", PANEL_HEIGHT * scaledDensity, 0);
        ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(viewFooter, "translationY", 0, scaledDensity * PANEL_HEIGHT);
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofFloat(viewBackground, "alpha", 0, 1);

        ObjectAnimator mainViewScaleXAnim = ObjectAnimator.ofFloat(mainView, "scaleX", 1.0f, 0.8f);
        ObjectAnimator mainViewScaleYAnim = ObjectAnimator.ofFloat(mainView, "scaleY", 1.0f, 0.9f);
        ObjectAnimator mainViewAlphaAnim = ObjectAnimator.ofFloat(mainView, "alpha", 1.0f, 0.5f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_DURATION);
        set.playTogether(topAnimator, bottomAnimator, backgroundAnimator, footerAnimator,
                mainViewScaleXAnim, mainViewScaleYAnim, mainViewAlphaAnim);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewBackground.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
        opened = true;
    }


    @Override
    public void hide() {
        if (!opened) {
            return;
        }

        ObjectAnimator topAnimator = ObjectAnimator.ofFloat(viewTop, "translationY", 0, -PANEL_HEIGHT * scaledDensity);
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(viewBottom, "translationY", 0, PANEL_HEIGHT * scaledDensity);
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofFloat(viewBackground, "alpha", 1, 0);
        ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(viewFooter, "translationY", scaledDensity * PANEL_HEIGHT, 0);

        ObjectAnimator mainViewScaleXAnim = ObjectAnimator.ofFloat(mainView, "scaleX", 0.8f, 1.0f);
        ObjectAnimator mainViewScaleYAnim = ObjectAnimator.ofFloat(mainView, "scaleY", 0.9f, 1.0f);
        ObjectAnimator mainViewAlphaAnim = ObjectAnimator.ofFloat(mainView, "alpha", 0.5f, 1.0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_DURATION);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewBackground.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                viewBackground.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.playTogether(topAnimator, bottomAnimator, backgroundAnimator, footerAnimator,
                mainViewScaleXAnim, mainViewScaleYAnim, mainViewAlphaAnim);
        set.start();
        opened = false;
    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

        if (!fromUser) {
            return;
        }

        switch (seekBar.getId()) {

            case R.id.brightness:
                presenter.onBrightnessChanged(window, value);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }
}
