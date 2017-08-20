package com.nandy.reader.model.test;

import com.nandy.reader.emums.TaskType;

/**
 * Created by yana on 18.07.17.
 */

public abstract class Test<Answer> {

    private String task;
    private Answer answer;
    private boolean passed;
    private MetaData metaData;
    private TaskType taskType;

    public Test(MetaData metaData, String task, Answer answer, TaskType taskType) {
        this.task = task;
        this.answer = answer;
        this.metaData = metaData;
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "Test{" +
                "task='" + task + '\'' +
                ", answer=" + answer +
                ", passed=" + passed +
                '}';
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public MetaData getMetaData() {
        return metaData;
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

    public static class MetaData{

        private String wordKey;
        private String langPrimary;
        private String langTranslation;

        public MetaData(String wordKey, String langPrimary, String langTranslation) {
            this.wordKey = wordKey;
            this.langPrimary = langPrimary;
            this.langTranslation = langTranslation;
        }

        public String getWordKey() {
            return wordKey;
        }

        public String getLangPrimary() {
            return langPrimary;
        }

        public String getLangTranslation() {
            return langTranslation;
        }
    }
}
