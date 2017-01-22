package com.stiletto.tr.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stiletto.tr.R;
import com.stiletto.tr.manager.BuilderManager;
import com.stiletto.tr.view.Fragment;
import com.stiletto.tr.widget.ListMenuButton;

/**
 * Created by yana on 20.01.17.
 */

public class TranslationSetupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traslation_setup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final ListMenuButton bmb1 = (ListMenuButton) view.findViewById(R.id.bmb1);

        for (int i = 0; i < bmb1.getPieceNumber(); i++) {
            bmb1.addBuilder(BuilderManager.getHamButtonBuilder());
        }

        bmb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmb1.boom();
            }
        });
    }
}
