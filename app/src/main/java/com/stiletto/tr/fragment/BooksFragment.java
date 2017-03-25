package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import com.stiletto.tr.view.Fragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 25.03.17.
 */

public class BooksFragment extends Fragment implements FileSeekerCallback{

    @Bind(R.id.pager)
    ViewPager viewPager;

    private BookShelfAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        ButterKnife.bind(this, view);
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

    private void openBook(Book book) {

        if (book.hasOriginLanguage() && book.hasTranslationLanguage()) {
            NavigationManager.addFragment(getActivity(), PageViewerFragment.create(book));
            return;
        }

        NavigationManager.addFragment(getActivity(), BookSetupFragment.create(book));

    }
}
