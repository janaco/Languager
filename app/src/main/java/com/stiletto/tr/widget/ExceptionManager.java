package com.stiletto.tr.widget;

import java.util.ArrayList;

/**
 * Created by yana on 20.01.17.
 */

public class ExceptionManager {

    static void judge(PiecePlaceEnum piecePlaceEnum,
                      ButtonPlaceEnum buttonPlaceEnum,
                      ButtonEnum buttonEnum,
                      BoomEnum boomEnum,
                      ArrayList<BoomButtonBuilder> boomButtonBuilders) {
        judge(piecePlaceEnum);
        judge(buttonPlaceEnum);
        judge(buttonEnum);
        judge(boomEnum);
        judge(boomButtonBuilders);

        int pieces = piecePlaceEnum.pieceNumber();
        int buttons = buttonPlaceEnum.buttonNumber();
        int builders = boomButtonBuilders.size();

        if (pieces == -1) {
            if (PiecePlaceEnum.Share == piecePlaceEnum) {
                int minPieces = piecePlaceEnum.minPieceNumber();
                int maxPieces = piecePlaceEnum.maxPieceNumber();
                if (buttonPlaceEnum.buttonNumber() < minPieces
                        || buttonPlaceEnum.buttonNumber() > maxPieces) {
                    throw new RuntimeException("ButtonPlaceEnum(" + buttonPlaceEnum + ") is not match for PiecePlaceEnum(" + piecePlaceEnum + ")!");
                } else if (builders < minPieces || builders > maxPieces) {
                    throw new RuntimeException("Number of builders(" + builders + ") is not match for PiecePlaceEnum(" + piecePlaceEnum + ")!");
                } else if (buttonEnum == ButtonEnum.HAM) {
                    throw new RuntimeException("Share style BMB is not support ham-boom-buttons");
                }
            }
        }

        if (pieces != buttons
                && buttonPlaceEnum != ButtonPlaceEnum.Horizontal
                && buttonPlaceEnum != ButtonPlaceEnum.Vertical
                && piecePlaceEnum != PiecePlaceEnum.Share) {
            throw new RuntimeException("Number of pieces is not equal to buttons'!");
        }
        if (pieces != builders && piecePlaceEnum != PiecePlaceEnum.Share)
            throw new RuntimeException("Number of builders is not equal to buttons'!");

    }

    private static void judge(PiecePlaceEnum piecePlaceEnum) {
        if (piecePlaceEnum == null || piecePlaceEnum == PiecePlaceEnum.Unknown)
            throw new RuntimeException("UNKNOWN piece-place-enum!");
    }

    private static void judge(ButtonPlaceEnum buttonPlaceEnum) {
        if (buttonPlaceEnum == null || buttonPlaceEnum == ButtonPlaceEnum.Unknown)
            throw new RuntimeException("UNKNOWN button-place-enum!");
    }

    private static void judge(ArrayList<BoomButtonBuilder> boomButtonBuilders) {
        if (boomButtonBuilders == null || boomButtonBuilders.size() == 0)
            throw new RuntimeException("Empty builders!");
    }

    private static void judge(ButtonEnum buttonEnum) {
        if (buttonEnum == null || buttonEnum == ButtonEnum.UNKNOWN)
            throw new RuntimeException("UNKNOWN button-enum!");
    }

    private static void judge(BoomEnum boomEnum) {
        if (boomEnum == null || boomEnum == BoomEnum.Unknown)
            throw new RuntimeException("UNKNOWN boom-enum!");
    }

    private static ExceptionManager ourInstance = new ExceptionManager();

    public static ExceptionManager getInstance() {
        return ourInstance;
    }

    private ExceptionManager() {
    }
}
