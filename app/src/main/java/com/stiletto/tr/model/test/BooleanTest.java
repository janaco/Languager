package com.stiletto.tr.model.test;

import com.stiletto.tr.emums.TaskType;

/**
 * Created by yana on 17.07.17.
 */

public class BooleanTest extends Test<String>{

    private String correctAnswer;
    private boolean approvable;

    public BooleanTest(MetaData metaData, String text, String answer, String correctAnswer, boolean aprovable) {
        super(metaData, text, answer, TaskType.BOOLEAN);
        this.correctAnswer = correctAnswer;
        this.approvable = aprovable;
    }

    @Override
    public String toString() {
        return "BooleanTest{" +
                "correctAnswer='" + correctAnswer + '\'' +
                ", approvable=" + approvable +
                ", " + super.toString() +
                '}';
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isApprovable() {
        return approvable;
    }

    public boolean isAnswerCorrect(boolean approved) {
        return (approvable && approved) || (!approvable && !approved);
    }
}
