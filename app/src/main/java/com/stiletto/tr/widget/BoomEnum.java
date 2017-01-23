package com.stiletto.tr.widget;

/**
 * Created by yana on 20.01.17.
 */

public enum  BoomEnum {

    HORIZONTAL_THROW_1(5),
    HORIZONTAL_THROW_2(6),
    RANDOM            (7);

    private final int value;

    BoomEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

