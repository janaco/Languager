package com.stiletto.tr;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.BookPagesAdapter;
import com.stiletto.tr.view.ExpandingFragment;
import com.stiletto.tr.view.ExpandingPagerFactory;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        setupWindowAnimations();

        BookPagesAdapter adapter = new BookPagesAdapter(getSupportFragmentManager());
        adapter.addAll(generatePagesList());
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

    private List<String> generatePagesList(){
        String text = " The Google Cloud Translation API can dynamically translate text between thousands of language pairs. The Cloud Translation API lets websites and programs integrate with the translation service programmatically. The Google Translation API is part of the larger Cloud Machine Learning API family. ";
        List<String> pages = new ArrayList<>();
        for(int i=0;i<5;++i){
            pages.add(text);
            pages.add(text);
            pages.add(text);
            pages.add(text);
        }
        return pages;
    }
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