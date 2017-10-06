package com.nandy.reader.adapter;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chuross.library.ExpandableLayout;
import com.nandy.reader.R;
import com.nandy.reader.emums.TestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 23.07.17.
 */

public class TestGroupsAdapter extends RecyclerView.Adapter<TestGroupsAdapter.ViewHolder> {

    private List<Item> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_languages, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Item item = list.get(position);

        holder.setLanguages(new Locale(item.langOrigin).getDisplayLanguage() + " - " + new Locale(item.langTranslation).getDisplayLanguage());
        holder.setWordsCount(item.wordsCount);

        if (item.wordsCount == item.unknownWordsCount) {
            holder.setInfo(R.string.no_learned_words);
        } else {
            holder.setInfo(String.valueOf(item.unknownWordsCount));
        }

        holder.mainLayout.setOnClickListener(v -> {
            if (holder.expandableLayout.isCollapsed()) {
                holder.expandableLayout.expand();
            } else {
                holder.expandableLayout.collapse();
            }
        });

        holder.viewLearn.setOnClickListener(getMenuClickListener(item, TestType.LEARNING));
        holder.viewGeneralTest.setOnClickListener(getMenuClickListener(item, TestType.GENERAL));
        holder.viewQuickTest.setOnClickListener(getMenuClickListener(item, TestType.QUICK));
        holder.viewExam.setOnClickListener(getMenuClickListener(item, TestType.EXAM));
        holder.viewCheckSkills.setOnClickListener(getMenuClickListener(item, TestType.CHECK_SKILLS));

    }

    private View.OnClickListener getMenuClickListener(final Item item, final TestType testType) {
        return v -> onItemClickListener.onItemClick(item.langOrigin, item.langTranslation, testType);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Item item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.languages)
        TextView viewLanguages;
        @Bind(R.id.words_count)
        TextView viewWordsCount;
        @Bind(R.id.words_info)
        TextView viewInfo;
        @Bind(R.id.layout_expandable)
        ExpandableLayout expandableLayout;
        @Bind(R.id.item_learn)
        TextView viewLearn;
        @Bind(R.id.item_general_test)
        TextView viewGeneralTest;
        @Bind(R.id.item_quick_test)
        TextView viewQuickTest;
        @Bind(R.id.item_exam)
        TextView viewExam;
        @Bind(R.id.item_check_skills)
        TextView viewCheckSkills;
        @Bind(R.id.layout_main)
        LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setLanguages(String text) {
            viewLanguages.setText(text);
        }

        void setWordsCount(int count) {
            viewWordsCount.setText(String.valueOf(count).concat(" ").concat(viewWordsCount.getContext().getString(R.string.has_words)));
        }

        void setInfo(String countOfWords){
            viewInfo.setText(countOfWords.concat(" ").concat(viewInfo.getContext().getString(R.string.words_to_learn)));
        }

        void setInfo(@StringRes int resId){
            viewInfo.setText(resId);
        }
    }

    public static class Item {

        private String langOrigin;
        private String langTranslation;
        private int wordsCount;
        private int unknownWordsCount;

        public Item(String langOrigin, String langTranslation, int wordsCount, int unknownWordsCount) {
            this.langOrigin = langOrigin;
            this.langTranslation = langTranslation;
            this.wordsCount = wordsCount;
            this.unknownWordsCount = unknownWordsCount;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String originLanguage, String translationLanguage, TestType testType);
    }
}
