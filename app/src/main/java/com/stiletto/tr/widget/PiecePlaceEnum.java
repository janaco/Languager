package com.stiletto.tr.widget;

/**
 * Created by yana on 20.01.17.
 */

public enum  PiecePlaceEnum {

    HAM_1  (35),
    HAM_2  (36),
    HAM_3  (37),
    HAM_4  (38),
    HAM_5  (39),
    HAM_6  (40);

//    Unknown(-1);

    private final int value;

    PiecePlaceEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

//    public static PiecePlaceEnum getEnum(int value) {
//        if (value < 0 || value >= values().length) return HAM_1;
//        return values()[value];
//    }

    /**
     * Get number of pieces from a piece-place-enum.
     *
     * @return the number of pieces
     */
    public int pieceNumber() {
        switch (this) {
            case HAM_1:
                return 1;
            case HAM_2:
                return 2;
            case HAM_3:
                return 3;
            case HAM_4:
                return 4;
            case HAM_5:
                return 5;
            case HAM_6:
                return 6;
            default:
                return -1;
        }
    }


}
