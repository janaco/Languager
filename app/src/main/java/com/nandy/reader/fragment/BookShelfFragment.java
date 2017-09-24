package com.nandy.reader.fragment;

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

import com.nandy.reader.SimpleOnPageChangeListener;
import com.nandy.reader.activity.MainActivity;
import com.nandy.reader.mvp.contract.BookshelfContract;
import com.nandy.reader.mvp.model.BookshelfModel;
import com.nandy.reader.mvp.presenter.BookshelfPresenter;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.ui.fragment.ViewerFragment;
import com.softes.cardviewer.ExpandableCard;
import com.softes.cardviewer.ExpandablePagerFactory;
import com.nandy.reader.R;
import com.nandy.reader.adapter.AutocompleteAdapter;
import com.nandy.reader.adapter.BooksAdapter;
import com.nandy.reader.core.BookItemListener;
import com.nandy.reader.manager.NavigationManager;
import com.nandy.reader.model.Book;
import com.nandy.reader.view.Fragment;

import java.util.List;
import java.util.Locale;

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
        implements BookshelfContract.View,  BookItemListener, AdapterView.OnItemClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {

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

    private BookshelfContract.Presenter presenter;

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
        viewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position) {
                closeItemBeforeMoveToAnother();
            }
        });

        presenter.start();
    }


    @Override
    public void setPresenter(BookshelfContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onBookLoaded(Book book) {
       adapter.add(book);
        autocompeteAdapter.add(book.getName());
    }


    @Override
    public void onBookNotFound(String message) {

        Snackbar snackbar = Snackbar.make(layoutCoordinator, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightPrimary));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.red_500));
        snackbar.show();
    }

    @Override
    public void moveToPosition(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override //book was renamed
    public void rename(Book book, int position) {
        adapter.set(book, position);
    }


    @Override
    public void read(Book book) {

        if (!book.hasOriginLanguage()){
            Language  originLanguage = book.getMetaData() == null ? Language.ENGLISH
                    : Language.getLanguage(book.getMetaData().getLanguage());
            book.setOriginLanguage(originLanguage);
        }

        if(!book.hasTranslationLanguage()){
           Language translationLanguage = Language.getLanguage(Locale.getDefault().getLanguage());
           book.setTranslationLanguage(translationLanguage);

        }

        ((MainActivity) getActivity()).replace( ViewerFragment.getInstance(book));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (v.getId()) {
            case R.id.item_text:
                //action was performed on search view, so we can search for book with entered name
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.searchForBook(adapter, autoCompleteTextView.getText().toString());
                    return true;
                }

            default:
                return false;
        }
    }

    @Override //On autocomplete  dropdown list item click
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.searchForBook(adapter, autoCompleteTextView.getText().toString());
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
        NavigationManager.addFragment(getActivity(), new DictionariesFragment());
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
        ((MainActivity) getActivity()).replace( new SettingsFragment());
    }

    @OnClick(R.id.item_statistics)
    void onStatisticsClick() {
        ((MainActivity) getActivity()).replace(new FragmentStatistics());
    }

    @OnClick(R.id.item_exams)
    void onExamsClick() {
        ((MainActivity) getActivity()).replace(new FragmentTests());
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

    public static BookShelfFragment newInstance(Context context){
        BookShelfFragment fragment = new BookShelfFragment();

        BookshelfPresenter presenter = new BookshelfPresenter(fragment);
        presenter.setBookshelfModel(new BookshelfModel(context));

        fragment.setPresenter(presenter);

        return fragment;
    }
}
