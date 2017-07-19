package com.stiletto.tr.model.test;

/**
 * Created by yana on 18.07.17.
 */

public class WritingTest extends Test<String> {

    public WritingTest(String task, String answer){
        super(task, answer);
    }

    public boolean isAnswerCorrect(String answer) {
        return answer.equalsIgnoreCase(getAnswer());
    }
}
