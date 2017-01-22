package com.stiletto.tr.widget;

import java.util.ArrayList;

/**
 * Created by yana on 20.01.17.
 */

public class ExceptionManager {


    private static void judge(ArrayList<BoomButtonBuilder> boomButtonBuilders) {
        if (boomButtonBuilders == null || boomButtonBuilders.size() == 0)
            throw new RuntimeException("Empty builders!");
    }


    private static void judge(BoomEnum boomEnum) {
        if (boomEnum == null
//                || boomEnum == BoomEnum.Unknown
                )
            throw new RuntimeException("UNKNOWN boom-enum!");
    }

    private static ExceptionManager ourInstance = new ExceptionManager();

    public static ExceptionManager getInstance() {
        return ourInstance;
    }

    private ExceptionManager() {
    }
}
