package com.stiletto.tr.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softes.cardviewer.ExpandableCard;
import com.softes.cardviewer.ExpandablePagerFactory;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.AutocompleteAdapter;
import com.stiletto.tr.adapter.BooksAdapter;
import com.stiletto.tr.core.BookItemListener;
import com.stiletto.tr.core.FileSeekerCallback;
import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.manager.NavigationManager;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.utils.FileSeeker;
import com.stiletto.tr.view.Fragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * There list of all books is displayed.
 * That is main screen of app.
 * <p>
 * Created by yana on 25.03.17.
 */

public class BookShelfFragment extends Fragment
        implements FileSeekerCallback, BookItemListener, AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {

    @Bind(R.id.pager)
    ViewPager viewPager;

    @Bind(R.id.layout_search)
    RelativeLayout layoutSearch;
    @Bind(R.id.item_text)
    AutoCompleteTextView autoCompleteTextView;
    @Bind(R.id.layout_coordinator)
    CoordinatorLayout layoutCoordinator;

    private BooksAdapter adapter;

    private AutocompleteAdapter autocompeteAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autocompeteAdapter = new AutocompleteAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        ButterKnife.bind(this, view);
        ExpandablePagerFactory.setupViewPager(viewPager);

        Point pointSize = new Point();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(pointSize);
        autoCompleteTextView.setDropDownWidth(pointSize.x);
        autoCompleteTextView.setDropDownWidth(pointSize.x);

        autoCompleteTextView.setOnEditorActionListener(this);
        autoCompleteTextView.setOnFocusChangeListener(this);
        autoCompleteTextView.setOnItemClickListener(this);
        autoCompleteTextView.addTextChangedListener(getSearchInputTextWatcher());
        autoCompleteTextView.setAdapter(autocompeteAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BooksAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(BookShelfFragment.this);

        //select and display books that are already available in db.
        //it is too long to search for them in file system
        //(search in file system will be performed but in background mode)
        BooksTable.getBooksAsynchronously(getContext(), this);
    }

    @Override
    public void onBookFound(Book book) {
        adapter.add(book);
        autocompeteAdapter.add(book.getName());
    }

    @Override
    public void afterBookSearchResults(List<Book> books, boolean fromDB) {

        if (fromDB) {
            //Means that all books from db are shown to user, but maybe there
            //are new books available in file system.
            //Lets search for them.
            FileSeeker.getBooks(this);
        } else {
            //Search in file system is finished. So, we need to update our records in database.
            BooksTable.setBooksList(getContext(), books);
        }
    }

    @Override //book was renamed
    public void rename(Book book, int position) {
        adapter.set(book, position);
    }


    @Override
    public void read(Book book) {
        if (book.hasOriginLanguage() && book.hasTranslationLanguage()) {
            NavigationManager.addFragment(getActivity(), PageViewerFragment.create(book));
            return;
        }

        NavigationManager.addFragment(getActivity(), BookSetupFragment.create(book));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId()) {
            case R.id.item_text:
                //action was performed on search view, so we can search for book with entered name
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchForBook(autoCompleteTextView.getText().toString());
                    return true;
                }

            default:
                return false;
        }
    }

    @Override //On autocomplete  dropdown list item click
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        searchForBook(autoCompleteTextView.getText().toString());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {

            case R.id.item_text:
                if (!hasFocus) {
                    autoCompleteTextView.setSelection(0, 0);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        closeItemBeforeMoveToAnother();
    }

    @Override
    public void onPageSelected(int position) {
        //Do nothing
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Do nothing
    }

    @OnClick(R.id.item_back)
    void closeSearchView() {
        layoutSearch.setVisibility(View.GONE);
    }

    @OnClick(R.id.item_clean)
    void cleanSearchView() {
        autoCompleteTextView.setText("");
    }

    @OnClick(R.id.item_dictionary)
    void onDictionaryButtonClick() {
        NavigationManager.addFragment(getActivity(), new DictionariesPagerFragment());
    }

    @OnClick(R.id.item_open_search)
    void onOpenSearchItemClick() {
        if (layoutSearch.getVisibility() == View.GONE) {
            autoCompleteTextView.setText("");
            layoutSearch.setVisibility(View.VISIBLE);
            layoutSearch.setAnimation(AnimationUtils.makeInAnimation(getContext(), true));
        } else {
            closeSearchView();
        }
    }

    @OnClick(R.id.item_settings)
    void onSettingsClick() {
        Toast.makeText(getContext(), getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.item_statistics)
    void onStatisticsClick() {
        Toast.makeText(getContext(), getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.item_exams)
    void onExamsClick() {
//        Toast.makeText(getContext(), getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
        NavigationManager.addFragment(getActivity(), new FragmentTests());
    }

    /**
     * This method will be called after search action will be performed by user.
     * It will search for the book with the entered name in the list
     * (in future in the file system) and scroll to it.
     *
     * @param name - name of book to search
     */
    private void searchForBook(final String name) {

        NavigationManager.hideKeyboard(getActivity());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int index = adapter.getPositionOf(name);
                if (index >= 0) {
                    viewPager.setCurrentItem(index);
                } else {

                    Snackbar snackbar = Snackbar.make(layoutCoordinator, getString(R.string.nothing_found), Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightPrimary));
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.red_500));
                    snackbar.show();
                }
            }
        }, 1000);

    }

    private void closeItemBeforeMoveToAnother() {
        ExpandableCard expandingFragment = ExpandablePagerFactory.getCurrentFragment(viewPager);
        if (expandingFragment != null && expandingFragment.isOpened()) {
            expandingFragment.close();
        }
    }

    private TextWatcher getSearchInputTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (autocompeteAdapter != null && autoCompleteTextView != null && autoCompleteTextView.getLayoutParams() != null) {
                        autocompeteAdapter.notifyDataSetChanged();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }
}
