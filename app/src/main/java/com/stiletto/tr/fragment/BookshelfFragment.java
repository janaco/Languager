package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BooksAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.utils.FileSeeker;
import com.stiletto.tr.view.Fragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 02.01.17.
 */

public class BookshelfFragment extends Fragment implements OnListItemClickListener<Book> {

    @Bind(R.id.gridView)
    GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookshelf, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        List<Book> bookList = FileSeeker.getBooks();
        BooksAdapter adapter = new BooksAdapter(getContext(), bookList);
        adapter.setOnItemClickListener(this);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(Book item, int position) {
        openBook(item);
    }

    private void openBook(Book book) {

        NavigationManager.addFragment(getActivity(), PageViewerFragment.create(book));

    }
}
