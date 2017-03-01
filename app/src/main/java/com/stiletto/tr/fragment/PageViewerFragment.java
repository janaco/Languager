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

import com.stiletto.tr.Preferences;
import com.stiletto.tr.R;
import com.stiletto.tr.adapter.MyDictionaryAdapter;
import com.stiletto.tr.adapter.PagerAdapter;
import com.stiletto.tr.core.OnLanguageSelectedListener;
import com.stiletto.tr.core.TranslationCallback;
import com.stiletto.tr.dialog.ChooseLanguageDialog;
import com.stiletto.tr.pagination.Pagination;
import com.stiletto.tr.readers.EPUBReader;
import com.stiletto.tr.readers.PDFReader;
import com.stiletto.tr.readers.TxtReader;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.translator.yandex.Translation;
import com.stiletto.tr.utils.ReaderPrefs;
import com.stiletto.tr.view.Fragment;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    @Bind(R.id.pages)TextView pageNumber;


    private MyDictionaryAdapter myDictionaryAdapter;


    private boolean isFullScreen = false;

    private PagerAdapter pagerAdapter;
    private Pagination pagination;
    private String path;
    private String bookName;

    private int bookmark = 0;

    private Language languagePrimary;
    private Language languageTranslation;

    public static PageViewerFragment create(Bundle arguments, Language from, Language to) {

        PageViewerFragment fragment = new PageViewerFragment();

        arguments.putString("lang_from", from.toString());
        arguments.putString("lang_to", to.toString());
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
        bookName = getArguments().getString("name");
        languagePrimary = Language.getLanguage(getArguments().getString("lang_from"));
        languageTranslation = Language.getLanguage(getArguments().getString("lang_to"));

        bookmark = Preferences.getBookmark(getContext(), bookName);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmet_viewer, container, false);
        ButterKnife.bind(this, view);

        itemLanguageFrom.setText(new Locale(languagePrimary.toString()).getDisplayLanguage());
        itemLanguageTo.setText(new Locale(languageTranslation.toString()).getDisplayLanguage());

        viewPager.addOnPageChangeListener(this);

        seekBar.setMin(1);
        seekBar.setProgress(bookmark);
        seekBar.setMax(bookmark + 50);
        seekBar.setOnProgressChangeListener(this);

        String textProgress = bookmark + "/--";
        itemPages.setText(textProgress);
        pageNumber.setText(textProgress);


        String header = bookName.length() > 25 ? bookName.substring(0, 22).concat("...") : bookName;
        itemHeader.setText(header);

        dictionaryList.setLayoutManager(new LinearLayoutManager(getContext()));
        myDictionaryAdapter = new MyDictionaryAdapter(new ArrayList<Translation>());
        dictionaryList.setAdapter(myDictionaryAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setDecorViewState();
        setUpPages();


    }


    private void setUpPages() {

        if (path.endsWith(".pdf")) {
            parsePdf(new File(path));
            return;
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                pagination = new Pagination(getBookContent(),
                        ReaderPrefs.getPreferences(getContext()));
                Log.d("PAGINATION_", "pagination:" + pagination);
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, languagePrimary, languageTranslation));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
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

//                layoutPageControl.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                itemAlert.setVisibility(View.GONE);
                viewPager.setAdapter(pagerAdapter);
            }
        };

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                pagination = new Pagination(PDFReader.parseAsText(file.getPath(), 1, 10),
                        ReaderPrefs.getPreferences(getContext()));
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, languagePrimary, languageTranslation));

                handler.sendEmptyMessage(1);

                pagination = new Pagination(PDFReader.parseAsText(file.getPath()),
                        ReaderPrefs.getPreferences(getContext()));
                setAdapter(new PagerAdapter(getChildFragmentManager(), pagination, languagePrimary, languageTranslation));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressBar.setVisibility(View.GONE);
//                layoutPageControl.setVisibility(View.VISIBLE);
                itemAlert.setVisibility(View.GONE);
                int currentPage = viewPager.getCurrentItem();
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(currentPage, false);

            }
        }.execute();

    }


    private CharSequence getBookContent() {

        File file = new File(path);

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
        Preferences.setBookmark(getContext(), bookName, viewPager.getCurrentItem());
        super.onDestroy();

    }

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean byUser) {

        if (byUser) {
            viewPager.setCurrentItem(value);

            String textProgress = seekBar.getProgress() + "/" + seekBar.getMax();
            itemPages.setText(textProgress);
            pageNumber.setText(textProgress);

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
                languagePrimary = language;
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                itemLanguageFrom.setText(displayLanguage);
            }
        });
    }

    @OnClick(R.id.item_language_to)
    void chooseBookTranslationLanguage() {
        ChooseLanguageDialog.show(getActivity(), new OnLanguageSelectedListener() {
            @Override
            public void onLanguageSelected(Language language) {
                languageTranslation = language;
                String displayLanguage = new Locale(language.toString()).getDisplayLanguage();
                itemLanguageTo.setText(displayLanguage);
            }
        });
    }

    @Override
    public void newTranslation(CharSequence originText, Translation translation) {

        translation.setOrigin(originText);
        myDictionaryAdapter.addTranslation(translation);
        itemDictionaryAlert.setVisibility(View.GONE);

    }
}
