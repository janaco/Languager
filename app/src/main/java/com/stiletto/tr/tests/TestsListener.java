package com.stiletto.tr.tests;

import com.stiletto.tr.emums.TaskType;
import com.stiletto.tr.emums.TestType;
import com.stiletto.tr.model.test.Test;
import com.stiletto.tr.model.word.Word;

/**
 * Created by yana on 19.07.17.
 */

public interface TestsListener {

    void onNextTest(Test test, TaskType taskType);

    void onTextIsDone(TaskType taskType, boolean passed);
}
