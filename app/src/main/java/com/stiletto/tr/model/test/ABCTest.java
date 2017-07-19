package com.stiletto.tr.model.test;

import java.util.List;

/**
 * Created by yana on 17.07.17.
 */

public class ABCTest extends Test<List<ABCTest.Variant>>{


    public ABCTest(String text, List<Variant> variants) {
        super(text, variants);
    }

    public boolean isAnswerCorrect(Variant variant){
        return variant.isCorrect();
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
