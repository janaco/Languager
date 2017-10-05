package com.nandy.reader.adapter;

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

/**
 * Created by yana on 23.07.17.
 */

public class TestGroupsAdapter extends RecyclerView.Adapter<TestGroupsAdapter.ViewHolder> {

    private List<Item> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_languages, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Item item = list.get(position);
        String originLanguage = new Locale(item.langOrigin).getDisplayLanguage();
        String translationLanguage = new Locale(item.langTranslation).getDisplayLanguage();

        holder.viewLanguages.setText(originLanguage + " - " + translationLanguage);
        holder.viewWordsCount.setText(item.wordsCount + " words");

        if (item.wordsCount == item.unknownWordsCount) {
            holder.viewInfo.setText("No learned words");
        } else {
            holder.viewInfo.setText(item.unknownWordsCount + " words to learn");
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandableLayout.isCollapsed()) {
                    holder.expandableLayout.expand();
                } else {
                    holder.expandableLayout.collapse();
                }
            }
        });

        holder.viewLearn.setOnClickListener(getMenuClickListener(item, TestType.LEARNING));
        holder.viewGeneralTest.setOnClickListener(getMenuClickListener(item, TestType.GENERAL));
        holder.viewQuickTest.setOnClickListener(getMenuClickListener(item, TestType.QUICK));
        holder.viewExam.setOnClickListener(getMenuClickListener(item, TestType.EXAM));
        holder.viewCheckSkills.setOnClickListener(getMenuClickListener(item, TestType.CHECK_SKILLS));

    }

    private View.OnClickListener getMenuClickListener(final Item item, final TestType testType){
        return v -> onItemClickListener.onItemClick(item.langOrigin, item.langTranslation, testType);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Item item){
        list.add(item);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView viewLanguages;
        private TextView viewWordsCount;
        private TextView viewInfo;
        private ExpandableLayout expandableLayout;
        private TextView viewLearn;
        private TextView viewGeneralTest;
        private TextView viewQuickTest;
        private TextView viewExam;
        private TextView viewCheckSkills;
        private LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            viewLanguages = (TextView) itemView.findViewById(R.id.languages);
            viewWordsCount = (TextView) itemView.findViewById(R.id.words_count);
            viewInfo = (TextView) itemView.findViewById(R.id.words_info);
            viewLearn = (TextView) itemView.findViewById(R.id.item_learn);
            viewGeneralTest = (TextView) itemView.findViewById(R.id.item_general_test);
            viewQuickTest = (TextView) itemView.findViewById(R.id.item_quick_test);
            viewExam = (TextView) itemView.findViewById(R.id.item_exam);
            viewCheckSkills = (TextView) itemView.findViewById(R.id.item_check_skills);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.layout_expandable);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.layout_main);
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

    public interface OnItemClickListener{
        void onItemClick(String originLanguage, String translationLanguage, TestType testType);
    }
}
