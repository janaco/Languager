package com.nandy.reader;

import com.nandy.reader.emums.TaskType;
import com.nandy.reader.model.test.Test;

/**
 * Created by yana on 19.07.17.
 */

public interface TestsListener {

    void onNextTest(Test test, TaskType taskType);

    void onTextIsDone(Test test);

    void skipTest();
}

