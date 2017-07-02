package com.stiletto.tr.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.stiletto.tr.db.tables.DictionaryTable;
import com.stiletto.tr.model.test.TestLearning;
import com.stiletto.tr.model.test.TestVariant;
import com.stiletto.tr.model.word.Dictionary;
import com.stiletto.tr.model.word.Translation;
import com.stiletto.tr.model.word.Word;
import com.stiletto.tr.translator.yandex.Language;
import com.stiletto.tr.view.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 27.05.17.
 */

public class FragmentTestLearning extends Fragment implements OnListItemClickListener<TestVariant> {

    @Bind(R.id.header)
    TextView header;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<TestLearning> tests;
    private List<TestLearning> passed = new ArrayList<>();
    private List<TestLearning> failed = new ArrayList<>();
    private TestLearningAdapter learningAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        learningAdapter = new TestLearningAdapter(getContext());
        learningAdapter.setOnListItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_learning, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(learningAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        List<Word> words = DictionaryTable.getDictionary(getContext(), new Language[]{Language.ENGLISH, Language.UKRAINIAN});
        Map<String, ArrayList<String>> map = new HashMap<>();
        for (Word word : words) {
            ArrayList<String> translations = new ArrayList<>();
            for (Dictionary.Item item : word.getDictionaryItems()) {

                for (Translation translation : item.getTranslations()) {

                    if (translation.getMeanings() == null) {
                        continue;
                    }

                    translations.add(translation.getText());
                }
            }
            if (translations.size() > 0) {
                map.put(word.getText(), translations);
                Log.d("TESTS_", "map: " + word.getText() + ": " + translations);
            }
        }

        Set<Map.Entry<String, ArrayList<String>>> entrySet = map.entrySet();
        tests = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : entrySet) {
            String itemMain = entry.getKey();
            if (entry.getValue().size() == 0) continue;

            String translation = entry.getValue().get(new Random().nextInt(entry.getValue().size()));
            TestVariant correctVariant = new TestVariant(translation, itemMain, true);


            List<TestVariant> variants = new ArrayList<>();
            variants.addAll(getWrongVariants(itemMain, entrySet));
            variants.add(new Random().nextInt(variants.size()), correctVariant);

            TestLearning test = new TestLearning(itemMain, variants);
            Log.d("TESTS_", "test: " + itemMain);

            tests.add(test);
        }

        TestLearning firstTest = tests.get(0);
        header.setText(firstTest.getItemMain());
        learningAdapter.setTests(firstTest.getVariants());
//        String item = "Read";
//        List<TestVariant> variants = new ArrayList<>();
//        variants.add(new TestVariant("Вивчати", "Learn", false));
//        variants.add(new TestVariant("Читати", "Read", true));
//        variants.add(new TestVariant("Дізнаватись", "Learn", false));
//        variants.add(new TestVariant("Досліджувати", "Explore", false));
//
//        TestLearning testLearning = new TestLearning(item, variants);

//        header.setText(item);
//        TestLearningAdapter adapter = new TestLearningAdapter(getContext(), testLearning.getVariants());
//        recyclerView.setAdapter(adapter);

    }

    private List<TestVariant> getWrongVariants(String word, Set<Map.Entry<String, ArrayList<String>>> entrySet) {
        List<TestVariant> variants = new ArrayList<>();

        int range = entrySet.size();
        int[] indexes = new int[]{-1, -1, -1};
        indexes[0] = getIndex(range, indexes[1], indexes[2]);
        indexes[1] = getIndex(range, indexes[0], indexes[2]);
        indexes[2] = getIndex(range, indexes[0], indexes[1]);

        int index = 0;
        for (Map.Entry<String, ArrayList<String>> entry : entrySet) {
            if (!entry.getKey().equals(word) &&
                    (index == indexes[0] || index == indexes[1] || index == indexes[2])) {
                List<String> translations = entry.getValue();
                if (translations.size() > 0) {
                    String correct = translations.get(new Random().nextInt(translations.size()));
                    variants.add(new TestVariant(correct, entry.getKey(), false));
                }
            }
            index++;
        }


        return variants;
    }

    private int getIndex(int range, int... values) {

        int index = -1;
        Random random = new Random();

        while (index < 0 || index == values[0] || index == values[1]) {
            index = random.nextInt(range);
        }

        return index;
    }

    @Override
    public void onListItemClick(TestVariant variant, int position) {

        if (position < tests.size() - 1) {
            final TestLearning nextTest = tests.get(position + 1);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ObjectAnimator animator = ObjectAnimator.ofFloat(recyclerView, "alpha", 1, 0.5f);
                    animator.setDuration(400);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            header.setText(nextTest.getItemMain());
                            learningAdapter.setTests(nextTest.getVariants());
                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(recyclerView, "alpha", 0.5f, 1);
                            animator1.setDuration(400);
                            animator1.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            header.setText(nextTest.getItemMain());
                            learningAdapter.setTests(nextTest.getVariants());
                            ObjectAnimator animator1 = ObjectAnimator.ofFloat(recyclerView, "alpha", 0.5f, 1);
                            animator1.setDuration(400);
                            animator1.start();
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.start();

                }
            }, 500);

        }
    }
}
