package com.stiletto.tr.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.TestLearningAdapter;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.model.test.ABCTest;
import com.stiletto.tr.model.test.BooleanTest;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by yana on 17.07.17.
 */

public class FragmentGeneralTest extends Fragment implements OnListItemClickListener<ABCTest.Variant> {

//    @Bind(R.id.header)
//    TextView itemHeader;
//    @Bind(R.id.recycler_view)
//    RecyclerView recyclerView;


    @Bind(R.id.item_text)
    TextView itemText;
    @Bind(R.id.item_answer)
    TextView itemAnswer;
    @Bind(R.id.item_approve)
    TextView itemApprove;
    @Bind(R.id.item_reject)
    TextView itemReject;

    private String langPrimary;
    private String langTranslation;

    private List<ABCTest> abcTests;
    private List<BooleanTest> booleanTests;

//    private TestLearningAdapter adapter;

    private int index = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        langPrimary = getArguments().getString("primary");
        langTranslation = getArguments().getString("translation");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_test_abc, container, false);
        View view = inflater.inflate(R.layout.fragment_test_boolean, container, false);
        ButterKnife.bind(this, view);

//        adapter = new TestLearningAdapter(getContext());
//        adapter.setOnListItemClickListener(this);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Word> query = realm.where(Word.class)
                .equalTo("info.originLanguage", langPrimary)
                .equalTo("info.translationLanguage", langTranslation)
                .equalTo("info.status", "Unknown");

        RealmResults<Word> results = query.findAllAsync();
        results.load();

        abcTests = new ArrayList<>();
        booleanTests = new ArrayList<>();

        for (Word word : results) {
            abcTests.add(createABCTest(word, results, results.size()));
            booleanTests.add(createBooleanTest(word, results, results.size()));
        }

//        ABCTest test = abcTests.get(index++);
//        itemHeader.setText(test.getText());
//        adapter.setTests(test.getVariants());

        BooleanTest test = booleanTests.get(index);
        itemText.setText(test.getText());
        itemAnswer.setText(test.getAnswer());

    }

    @Override
    public void onListItemClick(ABCTest.Variant item, int position) {
//        if (index < abcTests.size()) {
//            final ABCTest nextTest = abcTests.get(index++);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(recyclerView, "alpha", 1, 0.5f);
//                    animator.setDuration(400);
//                    animator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            itemHeader.setText(nextTest.getText());
//                            adapter.setTests(nextTest.getVariants());
//                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(recyclerView, "alpha", 0.5f, 1);
//                            animator1.setDuration(400);
//                            animator1.start();
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//                            itemHeader.setText(nextTest.getText());
//                            adapter.setTests(nextTest.getVariants());
//                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(recyclerView, "alpha", 0.5f, 1);
//                            animator1.setDuration(400);
//                            animator1.start();
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    animator.start();
//
//                }
//            }, 500);
//
//        }
    }

    @OnClick(R.id.item_approve)
    void onApproveClick() {

        BooleanTest test = booleanTests.get(index++);

        final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.rectangle_rounded);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(test.isCorrect() ?
                    ContextCompat.getColor(getContext(), R.color.green_500) :
                    ContextCompat.getColor(getContext(), R.color.red_500));
        }

        itemApprove.setBackground(drawable);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BooleanTest nextTest = booleanTests.get(index);
                itemText.setText(nextTest.getText());
                itemAnswer.setText(nextTest.getAnswer());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTint(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
                itemApprove.setBackground(drawable);
            }
        }, 500);
    }

    @OnClick(R.id.item_reject)
    void onRejectClick() {
        BooleanTest test = booleanTests.get(index++);

        final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.rectangle_rounded);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(test.isCorrect() ?
                    ContextCompat.getColor(getContext(), R.color.red_500) :
                    ContextCompat.getColor(getContext(), R.color.green_500));
        }

        itemReject.setBackground(drawable);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BooleanTest nextTest = booleanTests.get(index);
                itemText.setText(nextTest.getText());
                itemAnswer.setText(nextTest.getAnswer());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTint(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
                itemReject.setBackground(drawable);
            }
        }, 500);
    }

    private ABCTest createABCTest(Word word, List<Word> words, int limit) {

        List<ABCTest.Variant> variants = new ArrayList<>();

        int count = 0;

        while (count < 3) {

            int i = new Random().nextInt(limit);
            Log.d("TEST_", "VARIANT.get: " + i + "(limit: " + limit + ")");
            Word w = words.get(i);
            ABCTest.Variant variant = new ABCTest.Variant(w.getTranslationsAsString(), w.getText(), false);
            if (!variants.contains(variant)) {
                variants.add(variant);
                count++;
            }
        }

        variants.add(new Random().nextInt(4), new ABCTest.Variant(word.getTranslationsAsString(), word.getText(), true));

        return new ABCTest(word.getText(), variants);
    }

    private BooleanTest createBooleanTest(Word word, List<Word> words, int limit) {

        int number = new Random().nextInt(140);

        boolean answerTrue;
        String answer;
        if (number % 2 == 0) {
            answerTrue = true;
            answer = word.getTranslationsAsString();
            //true

        } else {
            answerTrue = false;

            Word w = words.get(new Random().nextInt(limit));
            if (w.getDictionaryItems().size() > 0) {
                int itemsSize = w.getDictionaryItems().size();
                answer = w.getDictionaryItems().get(new Random().nextInt(itemsSize)).getTranslationsAsString();
            } else {
                answer = w.getTranslationsAsString();
            }

            //false
        }
        return new BooleanTest(word.getText(), answer, word.getTranslationsAsString(), answerTrue);

    }


    public static FragmentGeneralTest getInstance(Bundle args) {

        FragmentGeneralTest fragment = new FragmentGeneralTest();
        fragment.setArguments(args);

        return fragment;
    }


}
