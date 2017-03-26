package com.stiletto.tr.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BookShelfAdapter;
import com.stiletto.tr.core.FileSeekerCallback;
import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.utils.FileSeeker;
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.ExpandingPagerFactory;
import com.stiletto.tr.view.Fragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 25.03.17.
 */

public class BooksFragment extends Fragment implements FileSeekerCallback, ExpandingFragment.OnExpandingClickListener{

    @Bind(R.id.pager)
    ViewPager viewPager;

    private BookShelfAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        ButterKnife.bind(this, view);
        ExpandingPagerFactory.setupViewPager(viewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BookShelfAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        FileSeeker.getBooks(this);
    }


    @Override
    public void onBookFound(Book book) {

        adapter.addBook(book);
    }

    @Override
    public void afterBookSearchResults(List<Book> books) {

        BooksTable.setBooksList(getContext(), books);
    }


//    @Override
//    public void onBackPressed() {
//        if(!ExpandingPagerFactory.onBackPressed(viewPager)){
//            super.onBackPressed();
//        }
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode slideTransition = new Explode();
        getActivity().getWindow().setReenterTransition(slideTransition);
        getActivity().getWindow().setExitTransition(slideTransition);
    }

    @Override
    public void onExpandingClick(View v) {
        //v is expandingfragment layout
//        View view = v.findViewById(R.id.image);
//        Travel travel = generatePagesList().get(viewPager.getCurrentItem());
//        startInfoActivity(view,travel);
    }

    private void openBook(Book book) {

        if (book.hasOriginLanguage() && book.hasTranslationLanguage()) {
            NavigationManager.addFragment(getActivity(), PageViewerFragment.create(book));
            return;
        }

        NavigationManager.addFragment(getActivity(), BookSetupFragment.create(book));

    }
}
