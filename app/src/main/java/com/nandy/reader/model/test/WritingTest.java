package com.nandy.reader.model.test;

import com.nandy.reader.emums.TaskType;

/**
 * Created by yana on 18.07.17.
 */

public class WritingTest extends Test<String> {

    public WritingTest(MetaData metaData, String task, String answer){
        super(metaData, task, answer, TaskType.WRITING);
    }

    public boolean isAnswerCorrect(String answer) {
        return answer.equalsIgnoreCase(getAnswer());
    }
}
