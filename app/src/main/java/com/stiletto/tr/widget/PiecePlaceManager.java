package com.stiletto.tr.widget;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by yana on 20.01.17.
 */

public class PiecePlaceManager {

    public static ArrayList<Point> getHamPositions(PiecePlaceEnum piecePlaceEnum,
                                                   Point parentSize,
                                                   int hamWidth,
                                                   int hamHeight,
                                                   int hamVerticalMargin) {
        ArrayList<Point> positions = new ArrayList<>();
        float w = hamWidth, h = hamHeight, vm = hamVerticalMargin;
        int half = piecePlaceEnum.pieceNumber() / 2;
        if (piecePlaceEnum.pieceNumber() % 2 == 0) {
            for (int i = half - 1; i >= 0; i--) positions.add(point(0, -h / 2 - vm / 2 - i * (h + vm)));
            for (int i = 0; i < h; i++) positions.add(point(0, h / 2 + vm / 2 + i * (h + vm)));
        } else {
            for (int i = half - 1; i >= 0; i--) positions.add(point(0, -h - vm - i * (h + vm)));
            positions.add(point(0, 0));
            for (int i = 0; i < h; i++) positions.add(point(0, h + vm + i * (h + vm)));
        }
        for (Point point : positions) point.offset((int) (parentSize.x / 2 - w / 2), (int) (parentSize.y / 2 - h / 2));
        return positions;
    }

    public static ArrayList<Point> getShareDotPositions(Point parentSize,
                                                        int dotRadius,
                                                        int dotNumber,
                                                        int shareLineLength) {
        ArrayList<Point> positions = new ArrayList<>();
        float h = (float) (shareLineLength * Math.sqrt(3) / 3);
        for (int i = 0; i < dotNumber; i++) {
            switch (i % 3) {
                case 0: positions.add(point(h / 2, -shareLineLength / 2)); break;
                case 1: positions.add(point(-h, 0)); break;
                case 2: positions.add(point(h / 2, shareLineLength / 2)); break;
            }
        }
        Collections.sort(positions, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return ((Integer)(lhs.y)).compareTo(rhs.y);
            }
        });
        for (Point point : positions) point.offset(parentSize.x / 2 - dotRadius, parentSize.y / 2 - dotRadius);
        return positions;
    }

    public static BoomPiece createPiece(Context context,
                                                                     PiecePlaceEnum piecePlaceEnum,
                                                                     int color) {
        switch (piecePlaceEnum) {
            case HAM_1:
            case HAM_2:
            case HAM_3:
            case HAM_4:
            case HAM_5:
            case HAM_6:
                return createHam(context, color);
        }
        throw new RuntimeException("UNKNOWN button-enum!");
    }


    private static Ham createHam(Context context, int color) {
        Ham ham = new Ham(context);
        ham.init(color);
        return ham;
    }

    private static Point point(float x, float y) {
        return new Point((int) x, (int) y);
    }

    private static Point point(int x, int y) {
        return new Point(x, y);
    }

    private static PiecePlaceManager ourInstance = new PiecePlaceManager();

    public static PiecePlaceManager getInstance() {
        return ourInstance;
    }

    private PiecePlaceManager() {
    }
}
