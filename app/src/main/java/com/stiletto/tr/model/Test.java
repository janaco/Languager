package com.stiletto.tr.model;

import java.util.List;

/**
 * Created by yana on 09.05.17.
 */

public class Test {

    private String task;
    private List<TestVariant> answers;

    public Test(String task, List<TestVariant> answers) {
        this.task = task;
        this.answers = answers;
    }


    public int getPossibleAnsversCount(){
return answers.size();
    }
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public List<TestVariant> getAnswers() {
        return answers;
    }

    public void setAnswers(List<TestVariant> answers) {
        this.answers = answers;
    }
}
