package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stiletto.tr.R;
import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.core.RenameModeCallback;
import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.Fragment;

import java.io.File;

/**
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandingFragment implements RenameModeCallback{


    private int position;
    private Book book;
    private BookItemListener bookItemListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book = getArguments().getParcelable("book");
        position = getArguments().getInt("position");

    }

    @Override
    public FragmentTop onCreateFragmentTop() {
        return FragmentTop.newInstance(book, this);
    }

    @Override
    public FragmentBottom onCreateFragmentBottom() {
        return FragmentBottom.newInstance(book, bookItemListener, this, position);
    }


    public void setBookItemListener(BookItemListener bookItemListener) {
        this.bookItemListener = bookItemListener;
    }

    public static BookExpandingFragment newInstance(Book book, int position, BookItemListener listener) {

        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putInt("position", position);

        BookExpandingFragment fragment = new BookExpandingFragment();
        fragment.setArguments(args);
        fragment.setBookItemListener(listener);

        return fragment;
    }

    @Override
    public void onRenameModeEnabled() {
        ((FragmentTop)getFragmentFront()).setRenameModeEnabled(true);
        ((FragmentBottom) getFragmentBottom()).setRenameModeEnabled(true);
    }

    @Override
    public void onRenameModeCanceled() {
        ((FragmentTop)getFragmentFront()).setRenameModeEnabled(false);
        ((FragmentBottom) getFragmentBottom()).setRenameModeEnabled(false);
    }

    @Override
    public void onBookRenamed(Book book, int position) {

        String newName = ((FragmentTop) getFragmentFront()).getNameFromInput();
        if (newName == null || newName.isEmpty()){
            ((FragmentTop) getFragmentFront()).setNameError();
            return;
        }

        String path = book.getPath();
        File file = new File(path);

        String oldName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));

        String newPath = path.replace(oldName, newName);

        Log.d("RENAME_", "from: " + path + "\tto: " + newPath);
        File newFile = new File(newPath);
        boolean remamed = file.renameTo(newFile);

        if (remamed){

            book.setPath(newPath);
            book.setName(newName);

            BooksTable.rename(getContext(), book, path);
            bookItemListener.rename(book, position);
        }else {
            Toast.makeText(getContext(), "Failed to remove book.", Toast.LENGTH_SHORT).show();
        }
        onRenameModeCanceled();

    }
}