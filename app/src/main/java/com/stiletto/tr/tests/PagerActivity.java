package com.stiletto.tr.tests;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BookPagesAdapter;
import com.stiletto.tr.readers.PDFReader;
<<<<<<< HEAD:app/src/main/java/com/stiletto/tr/PagerActivity.java
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.ExpandingPagerFactory;
=======
import com.stiletto.tr.view.fragment.expanding.ExpandingFragment;
import com.stiletto.tr.view.fragment.expanding.ExpandingPagerFactory;
>>>>>>> text_pager:app/src/main/java/com/stiletto/tr/tests/PagerActivity.java

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 25.12.16.
 */

public class PagerActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener {
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.back)ViewGroup back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        setupWindowAnimations();

        BookPagesAdapter adapter = new BookPagesAdapter(getSupportFragmentManager());
        adapter.addAll(splitOnPages(getBookContent()));
        viewPager.setAdapter(adapter);


        ExpandingPagerFactory.setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ExpandingFragment expandingFragment = ExpandingPagerFactory.getCurrentFragment(viewPager);
                if(expandingFragment != null && expandingFragment.isOpenend()){
                    expandingFragment.close();
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<String> splitOnPages(String text){
        List<String> list = new ArrayList<>();

        int range = 15*24;
        int indexStart = 0;
        int indexEnd = range;


        while (text.length() > indexEnd){
            list.add(text.substring(indexStart, indexEnd));
            indexStart = indexEnd;
            indexEnd += range;
        }
        list.add(text.substring(indexStart));

        return list;
    }

    private String getBookContent(){

        File file = new File("/storage/emulated/0/Download/451_za_Farenheitom.pdf");
        try {
            return PDFReader.parseAsText(file.getPath(), 1, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onBackPressed() {
        if(!ExpandingPagerFactory.onBackPressed(viewPager)){
            super.onBackPressed();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode slideTransition = new Explode();
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }
    private List<String> splitOnPages(String text){
        List<String> list = new ArrayList<>();

<<<<<<< HEAD:app/src/main/java/com/stiletto/tr/PagerActivity.java
=======
        int range = 15*24;
        int indexStart = 0;
        int indexEnd = range;


        while (text.length() > indexEnd){
            list.add(text.substring(indexStart, indexEnd));
            indexStart = indexEnd;
            indexEnd += range;
        }
        list.add(text.substring(indexStart));

        return list;
    }

    private String getBookContent(){

        File file = new File("/storage/emulated/0/Download/451_za_Farenheitom.pdf");
        try {
            return PDFReader.parseAsText(file.getPath(), 1, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
>>>>>>> text_pager:app/src/main/java/com/stiletto/tr/tests/PagerActivity.java
//    @SuppressWarnings("unchecked")
//    private void startInfoActivity(View view, Travel travel) {
//        Activity activity = this;
//        ActivityCompat.startActivity(activity,
//                InfoActivity.newInstance(activity, travel),
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity,
//                        new Pair<>(view, getString(R.string.transition_image)))
//                        .toBundle());
//    }

    @Override
    public void onExpandingClick(View v) {
        //v is expandingfragment layout
//        View view = v.findViewById(R.id.image);
//        Travel travel = generatePagesList().get(viewPager.getCurrentItem());
//        startInfoActivity(view,travel);
    }
}