package com.nandy.reader.ui.dialogs.floating_translation_dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.chuross.library.ExpandableLayout;
import com.michael.easydialog.EasyDialog;
import com.nandy.reader.R;
import com.nandy.reader.adapter.DictionaryAdapter;
import com.nandy.reader.model.word.DictionaryItem;
import com.nandy.reader.ui.dialogs.floating_translation_dialog.adapter.FloatingDictionaryAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 27.08.17.
 */

public class TranslationDialog implements TranslationsContract.View {


    @Bind(R.id.translation)
    TextView viewTranslation;
    @Bind(R.id.layout_expandable)
    ExpandableLayout expandableLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private EasyDialog dialog;
    private View view;

    private TranslationsContract.Presenter presenter;

    public TranslationDialog(Context context, int coordinates[]) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_translation, null, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        dialog = new EasyDialog(context)
                .setLayout(view)
                .setBackgroundColor(context.getResources().getColor(R.color.white))
                .setLocation(coordinates)
                .setGravity(EasyDialog.GRAVITY_BOTTOM)
                .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 500, -800, 0)
                .setAnimationAlphaShow(500, 0f, 1.0f)
                .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, 0, 800)
                .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                .setTouchOutsideDismiss(true)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24)
                .setOutsideColor(Color.parseColor("#20000000"));

    }

    @OnClick(R.id.translation)
    void onTranslationClick() {
        if (expandableLayout.isCollapsed()) {
            expandableLayout.expand();
        } else {
            expandableLayout.collapse();
        }
    }

    @Override
    public void setPresenter(TranslationsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTranslation(String translation) {
        viewTranslation.setText(translation);
    }

    @Override
    public void setDictioary(List<DictionaryItem> items) {
        FloatingDictionaryAdapter adapter = new FloatingDictionaryAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTranslationFailed() {
        viewTranslation.setText("Failed to translate");
    }

    public void show(CharSequence text) {
        presenter.translate(text);
        dialog.show();
    }
}
