package com.stiletto.tr.model.test;

/**
 * Created by yana on 18.07.17.
 */

public abstract class Test<Answer> {

    private String task;
    private Answer answer;
    private boolean passed;

    public Test(String task, Answer answer) {
        this.task = task;
        this.answer = answer;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
