package com.nandy.reader.ui.fragments.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.model.Book;
import com.nandy.reader.ui.SimpleOnPageChangeListener;
import com.nandy.reader.ui.fragments.pager.adapter.PagerAdapter;
import com.nandy.reader.ui.fragments.pager.menu.BookMenuPanel;
import com.nandy.reader.ui.fragments.pager.menu.MenuModel;
import com.nandy.reader.ui.fragments.pager.menu.MenuPresenter;
import com.nandy.reader.view.Fragment;
import com.victor.loading.book.BookLoading;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Book content viewer.
 * <p>
 * Created by yana on 04.01.17.
 */

public class ViewerFragment extends Fragment implements ViewerContract.View {

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.loading_progress)
    BookLoading bookLoading;
    @Bind(R.id.layout_loading)
    RelativeLayout layoutLoading;
    @Bind(R.id.item_alert)
    TextView itemAlert;

    private BookMenuPanel menuPanel;

    private PagerAdapter pagerAdapter;
    private ViewerContract.Presenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmet_viewer, container, false);
        ButterKnife.bind(this, view);

        menuPanel = new BookMenuPanel(view, getActivity().getWindow());
        menuPanel.setPresenter(new MenuPresenter(new MenuModel(presenter.getBook()), menuPanel));

        pagerAdapter = new PagerAdapter(getChildFragmentManager(), presenter.getBook());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            protected void onNextPageSelected(int position) {
                presenter.onPageSelected(position);
            }
        });

        return view;
    }

    @Override
    public void setPresenter(ViewerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startLoadingProgress(String text) {
        bookLoading.start();
        itemAlert.setText(text);
    }


    @Override
    public void cancelLoadingProgress() {
        bookLoading.stop();
        layoutLoading.setVisibility(View.GONE);
    }


    @Override
    public void afterParsingFinished(List<CharSequence> pages, int pagesCount, int bookmark) {
        pagerAdapter.setPages(pages);
        viewPager.setCurrentItem(bookmark);
        menuPanel.onBookParsingFinished(pagesCount);
    }

    @Override
    public void notifyNextPageOpened(int position) {
        menuPanel.onNextPageOpened(position);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        presenter.start(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.saveBookmarkOnDestroy(viewPager.getCurrentItem());
        presenter.destroy();
    }



    public static ViewerFragment getInstance(Book book) {

        ViewerFragment fragment = new ViewerFragment();
        fragment.setPresenter(new ViewerPresenter(new ViewerModel(book), fragment));
        return fragment;
    }


}
