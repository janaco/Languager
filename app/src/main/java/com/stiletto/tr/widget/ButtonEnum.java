package com.stiletto.tr.widget;

/**
 * Created by yana on 20.01.17.
 */

public enum ButtonEnum {

    HAM(3);

//    UNKNOWN(-1);

    private final int value;

    ButtonEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

//    public static ButtonEnum getEnum(int value) {
//        if (value < 0 || value > values().length) return HAM;
//        else return values()[value];
//    }
}
