package com.nandy.reader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.softes.cardviewer.ExpandableCard;
import com.nandy.reader.R;
import com.nandy.reader.core.BookItemListener;
import com.nandy.reader.core.RenameModeCallback;
import com.nandy.reader.model.Book;

import java.io.File;

/**
 * Item of books list
 *
 * Created by yana on 19.03.17.
 */

public class BookExpandingFragment extends ExpandableCard implements RenameModeCallback{

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
    public FragmentTop createFragmentTop() {
        return FragmentTop.newInstance(book, this);
    }

    @Override
    public FragmentBottom createFragmentBottom() {
        return FragmentBottom.newInstance(book, bookItemListener, this, position);
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

    public void setBookItemListener(BookItemListener bookItemListener) {
        this.bookItemListener = bookItemListener;
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

        File newFile = new File(newPath);
        if (file.renameTo(newFile)){
            book.rename(newName, newPath);
            bookItemListener.rename(book, position);
        }else {
            Toast.makeText(getContext(), getString(R.string.failed_to_remove_book), Toast.LENGTH_SHORT).show();
        }
        onRenameModeCanceled();

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

}