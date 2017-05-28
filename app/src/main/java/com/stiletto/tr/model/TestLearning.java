package com.stiletto.tr.model;

import java.util.List;

/**
 * Created by yana on 27.05.17.
 */

public class TestLearning {

    private String itemMain;
    private List<TestVariant> variants;
    private boolean passed;

    public TestLearning(String itemMain, List<TestVariant> variants) {
        this.itemMain = itemMain;
        this.variants = variants;
    }

    public String getItemMain() {
        return itemMain;
    }

    public void setItemMain(String itemMain) {
        this.itemMain = itemMain;
    }

    public List<TestVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<TestVariant> variants) {
        this.variants = variants;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
