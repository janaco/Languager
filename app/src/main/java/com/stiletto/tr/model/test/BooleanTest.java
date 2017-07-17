package com.stiletto.tr.model.test;

/**
 * Created by yana on 17.07.17.
 */

public class BooleanTest {

    private String text;
    private String answer;
    private String correctAnswer;
    private boolean correct;

    public BooleanTest(String text, String answer, String correctAnswer, boolean answerTrue) {
        this.text = text;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.correct = answerTrue;
    }

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}
