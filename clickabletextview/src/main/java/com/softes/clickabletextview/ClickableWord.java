package com.softes.clickabletextview;

/**
 * Represents the data model of clickable word.
 *
 * Created by yana on 25.12.16.
 */

public class ClickableWord {

    private int start;
    private int end;

    ClickableWord(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

}
