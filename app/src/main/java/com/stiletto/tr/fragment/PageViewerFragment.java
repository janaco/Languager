package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BookDictionaryAdapter;
import com.stiletto.tr.adapter.PagerAdapter;
import com.stiletto.tr.core.OnLanguageSelectedListener;
import com.stiletto.tr.core.TranslationCallback;
import com.stiletto.tr.dialog.ChooseLanguageDialog;
import com.stiletto.tr.emums.FileType;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.PagesParserCallback;
import com.stiletto.tr.readers.task.BaseParser;
import com.stiletto.tr.readers.task.PDFParser;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.view.Fragment;
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
        implements ViewPager.OnPageChangeListener,
        DiscreteSeekBar.OnProgressChangeListener, TranslationCallback,
        PagesParserCallback {

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.loading_progress)
    BookLoading bookLoading;
    @Bind(R.id.layout_loading)
    RelativeLayout layoutLoading;
    @Bind(R.id.seek_bar)
    DiscreteSeekBar seekBar;
    @Bind(R.id.item_header)
    TextView itemHeader;
    @Bind(R.id.item_pages)
    TextView itemPages;
    @Bind(R.id.item_language_from)
    Button itemLanguageFrom;
    @Bind(R.id.item_language_to)
    Button itemLanguageTo;
    @Bind(R.id.alert_dictionary)
    TextView itemDictionaryAlert;
    @Bind(R.id.dictionary_list)
    RecyclerView dictionaryList;
    @Bind(R.id.pages)
    TextView pageNumber;
    @Bind(R.id.item_alert)
    TextView itemAlert;

    private BookDictionaryAdapter bookDictionaryAdapter;

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

        bookLoading.start();
        itemAlert.setText(book.getName());

        itemLanguageFrom.setText(new Locale(book.getOriginLanguage().toString()).getDisplayLanguage());
        itemLanguageTo.setText(new Locale(book.getTranslationLanguage().toString()).getDisplayLanguage());

        viewPager.addOnPageChangeListener(this);

        int bookmark = book.getBookmark();
        seekBar.setMin(1);
        seekBar.setProgress(bookmark);
        seekBar.setMax(bookmark + 50);
        seekBar.setOnProgressChangeListener(this);

        String textProgress = bookmark + "/--";
        itemPages.setText(textProgress);


        String bookName = book.getName();
        String header = bookName.length() > 25 ? bookName.substring(0, 22).concat("...") : bookName;
        itemHeader.setText(header);

        dictionaryList.setLayoutManager(new LinearLayoutManager(getContext()));
        bookDictionaryAdapter = new BookDictionaryAdapter();
        dictionaryList.setAdapter(bookDictionaryAdapter);

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
        seekBar.setProgress(position + 1);
        String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
        itemPages.setText(textProgress);
        pageNumber.setText(textProgress);

        int bookmark = viewPager.getCurrentItem();
        book.setBookmark(bookmark, pagination.getPagesCount());
    }

    @Override
    public void onDestroy() {
        if (pagination !=null) {
            book.setBookmark(viewPager.getCurrentItem(), pagination.getPagesCount());
        }
        super.onDestroy();

    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean byUser) {

        if (byUser) {
            viewPager.setCurrentItem(value);

            String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
            itemPages.setText(textProgress);
            pageNumber.setText(textProgress);

            book.setBookmark(viewPager.getCurrentItem(), pagination.getPagesCount());
        }
    }

    @OnClick(R.id.item_language_from)
    void chooseBookPrimaryLanguage() {
        ChooseLanguageDialog.show(getActivity(), null, new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                itemLanguageFrom.setText(displayLanguage);
                book.setOriginLanguage(language);
            }
        });
    }

    @OnClick(R.id.item_language_to)
    void chooseBookTranslationLanguage() {
        ChooseLanguageDialog.show(getActivity(), null, new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                itemLanguageTo.setText(displayLanguage);
                book.setTranslationLanguage(language);
            }
        });
    }

    @Override
    public void newTranslation(Word word) {

        word.insert(getContext());
        bookDictionaryAdapter.addTranslation(word);
        itemDictionaryAlert.setVisibility(View.GONE);

    }

    private void setUpPages() {

        if (book.getFileType() == FileType.PDF) {
            new PDFParser(getContext()).parserCallback(this).execute(book);
            return;
        }

        new BaseParser(getContext()).pagesParserCallback(this).execute(book);

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
        seekBar.setMax(pagination.getPagesCount());
        seekBar.setProgress(bookmark);

        String textProgress = bookmark + "/" + pagination.getPagesCount();
        itemPages.setText(textProgress);
        pageNumber.setText(textProgress);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Do nothing.
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Do nothing.
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
        //Do nothing.
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
        //Do nothing.
    }
}
