package com.nandy.reader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nandy.reader.R;
import com.nandy.reader.adapter.BookDictionaryAdapter;
import com.nandy.reader.adapter.PagerAdapter;
import com.nandy.reader.core.OnLanguageSelectedListener;
import com.nandy.reader.core.TranslationCallback;
import com.nandy.reader.dialog.ChooseLanguageDialog;
import com.nandy.reader.emums.FileType;
import com.nandy.reader.model.Book;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.pagination.Pagination;
import com.nandy.reader.readers.PagesParserCallback;
import com.nandy.reader.readers.task.BaseParser;
import com.nandy.reader.readers.task.PDFParser;
import com.nandy.reader.translator.yandex.Language;
import com.nandy.reader.ui.fragment_pager.menu.BookMenuPanel;
import com.nandy.reader.ui.fragment_pager.menu.MenuModel;
import com.nandy.reader.ui.fragment_pager.menu.MenuPresenter;
import com.nandy.reader.view.Fragment;
import com.victor.loading.book.BookLoading;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Book content viewer.
 * <p>
 * Created by yana on 04.01.17.
 */

public class PageViewerFragment extends Fragment
        implements ViewPager.OnPageChangeListener, TranslationCallback,
        PagesParserCallback {

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
    private Pagination pagination;

    private Book book;

    public static PageViewerFragment create(Book book) {

        PageViewerFragment fragment = new PageViewerFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable("book", book);
        arguments.putString("lang_from", book.getOriginLanguage().toString());
        arguments.putString("lang_to", book.getTranslationLanguage().toString());
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        book = getArguments().getParcelable("book");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmet_viewer, container, false);
        ButterKnife.bind(this, view);

        menuPanel = new BookMenuPanel(view);
        menuPanel.setPresenter(new MenuPresenter(
                new MenuModel(book), menuPanel));

        bookLoading.start();
        itemAlert.setText(book.getName());

        viewPager.addOnPageChangeListener(this);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.pagerAdapter = new PagerAdapter(getChildFragmentManager(), book);
        this.pagerAdapter.setTranslationCallback(this);
        viewPager.setAdapter(pagerAdapter);

        setUpPages();
    }

    @Override
    public void onPageSelected(int position) {
        menuPanel.onPageChanged(position);
        int bookmark = viewPager.getCurrentItem();
        book.setBookmark(bookmark, pagination.getPagesCount());
    }

    @Override
    public void onDestroy() {
        if (pagination != null) {
            book.setBookmark(viewPager.getCurrentItem(), pagination.getPagesCount());
        }
        super.onDestroy();

    }

    @Override
    public void newTranslation(Word word) {
        word.setBookId(book.getPath());
        word.insert(getContext());
    }

    private void setUpPages() {

        if (book.getFileType() == FileType.PDF) {
            new PDFParser(getContext()).parserCallback(this).execute(book.getPath());
            return;
        }

        new BaseParser(getContext()).pagesParserCallback(this).execute(book.getPath());

    }

    @Override
    public void onPagesParsed(Pagination pagination) {
        this.pagination = pagination;
        pagerAdapter.setPages(pagination.getPages());

        bookLoading.stop();
        layoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void afterPagesParsingFinished(Pagination pagination) {
        this.pagination = pagination;

        int bookmark = book.getBookmark();
        viewPager.setCurrentItem(bookmark);
        menuPanel.onBookParsingFinished(pagination.getPagesCount());
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Do nothing.
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Do nothing.
    }

}
