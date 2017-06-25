package com.stiletto.tr.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.LanguagesListAdapter;
import com.stiletto.tr.core.DialogListener;
import com.stiletto.tr.core.OnLanguageSelectedListener;
import com.stiletto.tr.core.OnListItemClickListener;
import com.stiletto.tr.translator.yandex.Language;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Dialog with list of all available languages.
 * <p>
 * Created by yana on 28.01.17.
 */

public class ChooseLanguageDialog extends DialogFragment implements OnListItemClickListener<Language> {

    public static void show(FragmentActivity activity, DialogListener dialogListener, OnLanguageSelectedListener onLanguageSelectedListener) {
        ChooseLanguageDialog dialog = new ChooseLanguageDialog();
        dialog.setOnLanguageSelectedListener(onLanguageSelectedListener);
        dialog.setDialogListener(dialogListener);

        dialog.show(activity.getSupportFragmentManager(), ChooseLanguageDialog.class.getName());
    }

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private OnLanguageSelectedListener onLanguageSelectedListener;
    private DialogListener dialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_languages, container);

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        animShow();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (dialogListener != null) {
            dialogListener.onDialogCreated();
        }
        List<Language> languages = Arrays.asList(Language.values());

        LanguagesListAdapter adapter = new LanguagesListAdapter(languages);
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(Language item, int position) {
        onLanguageSelectedListener.onLanguageSelected(item);
        onDestroy();
    }

    @Override
    public void onDestroy() {
        animHide();
        if(dialogListener != null){dialogListener.afterDialogClosed();}

        if (getDialog() != null && getRetainInstance()) {
            getDialog().setOnDismissListener(null);
        }

        onDestroyView();
        super.onDestroy();
    }

    public void setOnLanguageSelectedListener(OnLanguageSelectedListener onLanguageSelectedListener) {
        this.onLanguageSelectedListener = onLanguageSelectedListener;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    private void animShow(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(recyclerView, "alpha", 0, 1);
        animator.setDuration(1000);
        animator.start();
    }

    private void animHide(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(recyclerView, "alpha", 1, 0);
        animator.setDuration(1000);
        animator.start();
    }
}
