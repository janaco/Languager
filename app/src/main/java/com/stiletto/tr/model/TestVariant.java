package com.stiletto.tr.model;

/**
 * Created by yana on 09.05.17.
 */

public class TestVariant {

    private String proposedAnswer;
    private String correctAnsver;

    private boolean correct;

    public TestVariant(String correctAnsver, String variant, boolean correct) {
        this.proposedAnswer = variant;
        this.correctAnsver = correctAnsver;
        this.correct = correct;
    }

    public String getProposedAnswer() {
        return proposedAnswer;
    }

    public void setProposedAnswer(String proposedAnswer) {
        this.proposedAnswer = proposedAnswer;
    }

    public String getCorrectAnsver() {
        return correctAnsver;
    }

    public void setCorrectAnsver(String correctAnsver) {
        this.correctAnsver = correctAnsver;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
