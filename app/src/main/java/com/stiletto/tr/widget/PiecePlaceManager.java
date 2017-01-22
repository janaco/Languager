package com.stiletto.tr.widget;

import android.content.Context;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by yana on 20.01.17.
 */

public class PiecePlaceManager {

    public static ArrayList<Point> getHamPositions(int itemsCount,
                                                   Point parentSize,
                                                   int hamWidth,
                                                   int hamHeight,
                                                   int hamVerticalMargin) {
        ArrayList<Point> positions = new ArrayList<>();
        float w = hamWidth, h = hamHeight, vm = hamVerticalMargin;
        int half = itemsCount / 2;
        if (itemsCount % 2 == 0) {
            for (int i = half - 1; i >= 0; i--)
                positions.add(point(0, -h / 2 - vm / 2 - i * (h + vm)));
            for (int i = 0; i < h; i++) positions.add(point(0, h / 2 + vm / 2 + i * (h + vm)));
        } else {
            for (int i = half - 1; i >= 0; i--) positions.add(point(0, -h - vm - i * (h + vm)));
            positions.add(point(0, 0));
            for (int i = 0; i < h; i++) positions.add(point(0, h + vm + i * (h + vm)));
        }
        for (Point point : positions)
            point.offset((int) (parentSize.x / 2 - w / 2), (int) (parentSize.y / 2 - h / 2));
        return positions;
    }


    public static InnerItemView createPiece(Context context, int color) {
        return createHam(context, color);
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
