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
 * Created by yana on 25.03.17.
 */

public class BooksFragment extends Fragment
        implements FileSeekerCallback, BookItemListener, ViewPager.OnPageChangeListener {

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

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    autoCompleteTextView.setSelection(0, 0);
                }
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
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
        });
        autoCompleteTextView.setAdapter(autocompeteAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchForBook(autoCompleteTextView.getText().toString());
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchForBook(autoCompleteTextView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BooksAdapter(getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(BooksFragment.this);

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
            FileSeeker.getBooks(this);
        } else {
            BooksTable.setBooksList(getContext(), books);
        }
    }


    @Override
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        closeItemBeforeMoveToAnother();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
    void onDictionaryButtonClick(){
        NavigationManager.addFragment(getActivity(), new DictionariesFragment());
    }

    @OnClick(R.id.item_open_search)
    void onOpenSearchItemClick(){
        if (layoutSearch.getVisibility() == View.GONE) {
            autoCompleteTextView.setText("");
            layoutSearch.setVisibility(View.VISIBLE);
            layoutSearch.setAnimation(AnimationUtils.makeInAnimation(getContext(), true));
        }else {
            closeSearchView();
        }
    }

    @OnClick(R.id.item_settings)
    void onSettingsClick(){
        Toast.makeText(getContext(), "Not implemented yet.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.item_statistics)
    void onStatisticsClick(){
        Toast.makeText(getContext(), "Not implemented yet.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.item_exams)
    void onExamsClick(){
        Toast.makeText(getContext(), "Not implemented yet.", Toast.LENGTH_SHORT).show();
    }

    private void searchForBook(final String name) {

        NavigationManager.hideKeyboard(getActivity());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int index = adapter.getPositionOf(name);
                if (index >= 0) {
                    viewPager.setCurrentItem(index);
                } else {

                    Snackbar snackbar = Snackbar.make(layoutCoordinator, "Oops... Nothing found =(", Snackbar.LENGTH_SHORT);
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
}
