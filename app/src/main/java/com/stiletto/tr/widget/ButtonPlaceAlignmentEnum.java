package com.stiletto.tr.widget;

/**
 * Created by yana on 20.01.17.
 */

public  enum ButtonPlaceAlignmentEnum {

    Center(0),
    Top(1),
    Bottom(2),
    Left(3),
    Right(4),
    TL(5),
    TR(6),
    BL(7),
    BR(8);


    private final int value;

    ButtonPlaceAlignmentEnum(int value) {
        this.value = value;
    }

}
