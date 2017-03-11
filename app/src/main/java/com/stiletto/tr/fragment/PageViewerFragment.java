package com.stiletto.tr.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BookDictionaryAdapter;
import com.stiletto.tr.adapter.PagerAdapter;
import com.stiletto.tr.core.OnLanguageSelectedListener;
import com.stiletto.tr.core.TranslationCallback;
import com.stiletto.tr.db.tables.BooksTable;
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.dialog.ChooseLanguageDialog;
import com.stiletto.tr.model.Book;
import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.EPUBReader;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.readers.TxtReader;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.translator.yandex.model.YandexTranslateResponse;
import com.stiletto.tr.utils.ReaderPrefs;
import com.stiletto.tr.view.Fragment;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.yuweiguocn.lib.squareloading.SquareLoading;

/**
 * Created by yana on 04.01.17.
 */

public class PageViewerFragment extends Fragment
        implements ViewPager.OnPageChangeListener, DiscreteSeekBar.OnProgressChangeListener,
        TranslationCallback {

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.item_progress)
    SquareLoading progressBar;
    @Bind(R.id.item_alert)
    TextView itemAlert;
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


    private BookDictionaryAdapter myDictionaryAdapter;


    private boolean isFullScreen = false;

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
        pageNumber.setText(textProgress);


        String bookName = book.getName();
        String header = bookName.length() > 25 ? bookName.substring(0, 22).concat("...") : bookName;
        itemHeader.setText(header);

        dictionaryList.setLayoutManager(new LinearLayoutManager(getContext()));
        myDictionaryAdapter = new BookDictionaryAdapter();
        dictionaryList.setAdapter(myDictionaryAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setDecorViewState();
        setUpPages();


    }


    private void setUpPages() {

        String path = book.getPath();
        if (path.endsWith(".pdf")) {
            parsePdf(new File(path));
            return;
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                pagination = new Pagination(getBookContent(),
                        ReaderPrefs.getPreferences(getContext()));
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, book));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                int bookmark = book.getBookmark();
                progressBar.setVisibility(View.GONE);
                itemAlert.setVisibility(View.GONE);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(bookmark);
                seekBar.setMax(pagination.getPagesCount());
                seekBar.setProgress(bookmark);

                String textProgress = bookmark + "/" + pagination.getPagesCount();
                itemPages.setText(textProgress);
                pageNumber.setText(textProgress);

            }
        }.execute();

    }

    private void parsePdf(final File file) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case 1:
                        int count = msg.getData().getInt("pages");
                        seekBar.setMax(count);
                        seekBar.setProgress(book.getBookmark());
                        String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
                        itemPages.setText(textProgress);
                        pageNumber.setText(textProgress);

                        int bookmark = viewPager.getCurrentItem();
                        book.setBookmark(bookmark);
                        BooksTable.setBookmark(getContext(), bookmark, book.getPath());

                        break;

                    case 2:

                        progressBar.setVisibility(View.GONE);
                        itemAlert.setVisibility(View.GONE);

                        int currentItem = viewPager.getCurrentItem();
                        viewPager.setAdapter(pagerAdapter);
                        viewPager.setCurrentItem(currentItem);
                        break;
                }

            }
        };

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                String filePath = book.getPath();

                int step = 0;
                int maxStep = 10;

                StringBuilder stringBuilder = new StringBuilder();

                try {
                    PdfReader reader = new PdfReader(filePath);

                    int pages = reader.getNumberOfPages();
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putInt("pages", pages);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    int bookmark = 1;
                    if (book.getBookmark() > 0 && book.getBookmark() > maxStep / 2) {
                        bookmark = book.getBookmark() - maxStep / 2;
                    }

                    for (int page = bookmark; page <= pages; page++, step++) {
                        stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, page));

                        if (step > maxStep) {
                            step = 0;
                            pagination = new Pagination(
                                    stringBuilder.toString(), ReaderPrefs.getPreferences(getContext()));
                            Log.d("PAGER_", "setAdapter: " + pagination.getPagesCount());
                            setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, book));
                            handler.sendEmptyMessage(2);

                        }

                    }

                    pagination = new Pagination(
                            stringBuilder.toString(), ReaderPrefs.getPreferences(getContext()));
                    Log.d("PAGER_", "setAdapter: " + pagination.getPagesCount());
                    setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, book));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                viewPager.setAdapter(pagerAdapter);
            }
        }.execute();

    }


    private CharSequence getBookContent() {

        File file = new File(book.getPath());

        String extension = file.getName().substring(file.getName().indexOf(".")).toLowerCase();

        switch (extension) {

            case ".pdf":
                return PDFReader.parseAsText(file.getPath());

            case ".epub":
                return EPUBReader.parseAsText(file);

            case ".txt":
                return TxtReader.parseAsText(file);
        }


        return "";
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void setDecorViewState() {
        View decorView = getActivity().getWindow().getDecorView();
        int newUiOptions = decorView.getSystemUiVisibility();

        if (!isFullScreen) {
            isFullScreen = true;
            Log.d("DECOR_VIEW", "hide");
            if (Build.VERSION.SDK_INT > 18) {

                newUiOptions ^=
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            } else if (Build.VERSION.SDK_INT >= 16) {

                newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            } else if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            decorView.setSystemUiVisibility(newUiOptions);
        } else {
            Log.d("DECOR_VIEW", "show");
            isFullScreen = false;

            getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            if (Build.VERSION.SDK_INT >= 16) {
                getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_FULLSCREEN);
            }

            if (Build.VERSION.SDK_INT >= 19) {

                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            ActivitiesCurrentContentView.requestLayout();

//            getView().requestLayout();


        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        seekBar.setProgress(position + 1);
        String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
        itemPages.setText(textProgress);
        pageNumber.setText(textProgress);

        int bookmark = viewPager.getCurrentItem();
        book.setBookmark(bookmark);
        BooksTable.setBookmark(getContext(), bookmark, book.getPath());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void setAdapter(PagerAdapter adapter) {
        this.pagerAdapter = adapter;
        this.pagerAdapter.setTranslationCallback(this);

    }

    @Override
    public void onDestroy() {
        BooksTable.setBookmark(getContext(), viewPager.getCurrentItem(), book.getPath());
        super.onDestroy();

    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean byUser) {

        if (byUser) {
            viewPager.setCurrentItem(value);

            String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
            itemPages.setText(textProgress);
            pageNumber.setText(textProgress);

            book.setBookmark(viewPager.getCurrentItem());
            BooksTable.setBookmark(getContext(), viewPager.getCurrentItem(), book.getPath());

        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @OnClick(R.id.item_language_from)
    void chooseBookPrimaryLanguage() {
        ChooseLanguageDialog.show(getActivity(), new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                itemLanguageFrom.setText(displayLanguage);
                book.setOriginLanguage(language);
                BooksTable.setOriginLanguage(getContext(), language, book.getPath());
            }
        });
    }

    @OnClick(R.id.item_language_to)
    void chooseBookTranslationLanguage() {
        ChooseLanguageDialog.show(getActivity(), new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                itemLanguageTo.setText(displayLanguage);
                book.setTranslationLanguage(language);
                BooksTable.setTranslationLanguage(getContext(), language, book.getPath());
            }
        });
    }

    @Override
    public void newTranslation(CharSequence originText, DictionaryItem item) {

        myDictionaryAdapter.addTranslation(item);
        itemDictionaryAlert.setVisibility(View.GONE);

        DictionaryTable.insert(getContext(), item);

    }
}
