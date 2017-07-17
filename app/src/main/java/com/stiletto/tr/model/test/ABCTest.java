package com.stiletto.tr.model.test;

import java.util.List;

/**
 * Created by yana on 17.07.17.
 */

public class ABCTest {

    private String text;
    private boolean passed;
    private List<Variant> variants;

    public ABCTest(String text, List<Variant> variants) {
        this.text = text;
        this.variants = variants;
    }

    public String getText() {
        return text;
    }

    public boolean isPassed() {
        return passed;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public static class Variant{

        private String text;
        private String answer;
        private boolean correct;

        public Variant(String text, String answer, boolean correct) {
            this.text = text;
            this.answer = answer;
            this.correct = correct;
        }

        public String getText() {
            return text;
        }

        public String getAnswer() {
            return answer;
        }

        public boolean isCorrect() {
            return correct;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Variant variant = (Variant) o;

            return answer != null ? answer.equals(variant.answer) : variant.answer == null;

        }

        @Override
        public int hashCode() {
            return answer != null ? answer.hashCode() : 0;
        }
    }
}
