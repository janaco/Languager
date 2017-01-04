package com.stiletto.tr.fragment.viewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.fragment.slide.ScreenSlideActivity;
import com.stiletto.tr.fragment.slide.ScreenSlidePageFragment;
import com.stiletto.tr.view.Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by yana on 03.01.17.
 */

public class PDFViewPagerFragment extends Fragment {

    @Bind(R.id.pager)
    ViewPager mPager;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_screen_slide, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);


    }

    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


//    @Bind(R.id.viewPager) ReversibleViewPager viewPager;
//    @Bind(R.id.item_content) ClickableTextView itemBookPage;
//
//    private static final String kPdfFile = "pdf_file";
//    private static final String kPageIndex = "page_index";
//
//    private View view;
//    Pagination pagination;
//
//
//    public static PDFViewPagerFragment newInstance(File pdfFile) {
//        PDFViewPagerFragment fragment = new PDFViewPagerFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(kPdfFile, pdfFile);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.fragment_viewer, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        this.view = view;
//        ButterKnife.bind(this, view);
//
//        itemBookPage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    itemBookPage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    itemBookPage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//                setUpPages();
//            }
//        });
//
//    }
//
//    private void setUpPages() {
//        pagination = new Pagination(getBookContent(), itemBookPage);
//
//        int width = itemBookPage.getWidth();
//        int height = itemBookPage.getHeight();
//
//        PdfPagerAdapter adapter = new PdfPagerAdapter(getContext(), pagination.getPages(), width, height);
//        adapter.setOnViewTapListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                handleTap(motionEvent.getX(), motionEvent.getY());
//                return false;
//            }
//        });
//        viewPager.setAdapter(adapter);
//    }
//
//
//    private String getBookContent() {
//
//        File file = new File("/storage/emulated/0/Download/451_za_Farenheitom.pdf");
//        try {
//            return PDFReader.parseAsText(file.getPath(), 1, 10);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            int position = pageIndexToPosition(savedInstanceState.getInt(kPageIndex));
//            viewPager.setCurrentItem(position);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        hideSystemUI();
//    }
//
//    @Override
//    public void onPause() {
//        showSystemUI();
//        super.onPause();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
////        outState.putSerializable(kPdfFile, pdfFile);
//        outState.putInt(kPageIndex, positionToPageIndex(viewPager.getCurrentItem()));
//    }
//
//    boolean isPortrait() {
//        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
//    }
//
//    int positionToPageIndex(int position) {
//        if (!isPortrait()) {
//            return position;
//        } else {
//            if (position == 0) {
//                return 0;
//            } else {
//                return position * 2 - 1;
//            }
//        }
//    }
//
//    int pageIndexToPosition(int pageIndex) {
//        if (isPortrait()) {
//            return pageIndex;
//        } else {
//            if (pageIndex == 0) {
//                return 0;
//            } else {
//                return (pageIndex + 1) / 2;
//            }
//        }
//    }
//
//    boolean handleTap(float x, float y) {
//        float thirdX = viewPager.getWidth() / 3.0f;
//
//        if (x < thirdX) {
//            return viewPager.arrowScroll(View.FOCUS_LEFT);
//        } else if (x > 2 * thirdX) {
//            return viewPager.arrowScroll(View.FOCUS_RIGHT);
//        }
//        toggleSystemUi();
//        return true;
//    }
//
//    /**
//     * To hide system UI and dive into IMMERSIVE mode.
//     * <p>
//     * <blockquote>
//     * If you're building a book reader, news reader, or a magazine, use the IMMERSIVE flag in conjunction with
//     * SYSTEM_UI_FLAG_FULLSCREEN and SYSTEM_UI_FLAG_HIDE_NAVIGATION. Because users may want to access the action bar and other
//     * UI controls somewhat frequently, but not be bothered with any UI elements while flipping through content, IMMERSIVE is
//     * a
//     * good option for this use case.
//     * </blockquote>
//     *
//     * @see <a href="http://developer.android.com/intl/ja/training/system-ui/immersive.html">Using Immersive Full-Screen
//     * Mode</a>
//     */
//    private void hideSystemUI() {
//        view.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
//    }
//
//    private void showSystemUI() {
//        view.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//    }
//
//    private void toggleSystemUi() {
//        if (checkFrag(view.getSystemUiVisibility(), View.SYSTEM_UI_FLAG_IMMERSIVE)) {
//            showSystemUI();
//        } else {
//            hideSystemUI();
//        }
//    }
//
//    private boolean checkFrag(int vec, int flag) {
//        return (vec & flag) == flag;
//    }
}
