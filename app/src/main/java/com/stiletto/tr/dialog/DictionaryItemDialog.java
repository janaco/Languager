package com.stiletto.tr.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.stiletto.tr.R;
import com.stiletto.tr.adapter.DictionaryAdapter;
import com.stiletto.tr.model.DictionaryItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yana on 11.03.17.
 */

public class DictionaryItemDialog extends DialogFragment {

    public static void show(FragmentActivity activity, ArrayList<DictionaryItem> items) {
        DictionaryItemDialog dialog = new DictionaryItemDialog();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", items);
        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(), "DictionaryItemDialog");
    }

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ArrayList<DictionaryItem> items;
    private DictionaryAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = getArguments().getParcelableArrayList("items");
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_word_info, container, false);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        adapter = new DictionaryAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);

    }


}