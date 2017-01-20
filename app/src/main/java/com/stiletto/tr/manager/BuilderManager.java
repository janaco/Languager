package com.stiletto.tr.manager;

import com.stiletto.tr.R;
import com.stiletto.tr.widget.HamButton;

/**
 * Created by yana on 20.01.17.
 */
public class BuilderManager {


    public static HamButton.Builder getHamButtonBuilder() {
        return new HamButton.Builder()
                .normalImageRes(R.drawable.book)
                .normalText("Normal Text")
                .subNormalText("subNormalTextRes");
    }


    private static BuilderManager ourInstance = new BuilderManager();

    public static BuilderManager getInstance() {
        return ourInstance;
    }

    private BuilderManager() {
    }
}