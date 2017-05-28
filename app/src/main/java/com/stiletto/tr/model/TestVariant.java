package com.stiletto.tr.model;

/**
 * Created by yana on 27.05.17.
 */

public class TestVariant {

    private String proposedVariant;
    private String correctMeaning;
    private boolean isCorrect;

    public TestVariant(String proposedVariant, String correctMeaning, boolean isCorrect) {
        this.proposedVariant = proposedVariant;
        this.correctMeaning = correctMeaning;
        this.isCorrect = isCorrect;
    }

    public String getProposedVariant() {
        return proposedVariant;
    }

    public void setProposedVariant(String proposedVariant) {
        this.proposedVariant = proposedVariant;
    }

    public String getCorrectMeaning() {
        return correctMeaning;
    }

    public void setCorrectMeaning(String correctMeaning) {
        this.correctMeaning = correctMeaning;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
