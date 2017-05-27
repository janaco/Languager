package com.softes.flippy;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.softes.flippy.listeners.ToggleListener;

/**
 * Created by yana on 27.05.17.
 */

public class FlipAnimator extends Animation {

    private static final float EXPERIMENTAL_VALUE = 50.f;
    private Camera camera;
    private float centerX;
    private float centerY;
    private boolean visibilitySwapped;
    private Direction direction;
    private ToggleListener toggleListener;

    public FlipAnimator() {
        setFillAfter(true);
    }

    public void setVisibilitySwapped() {
        visibilitySwapped = false;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
        this.centerX = width / 2;
        this.centerY = height / 2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // Angle around the y-axis of the rotation at the given time. It is
        // calculated both in radians and in the equivalent degrees.
        final double radians = Math.PI * interpolatedTime;

        float degrees = (float) (180.0 * radians / Math.PI);

        if (direction == Direction.UP || direction == Direction.LEFT) {
            degrees = -degrees;
        }

        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in
        // flipped around. This is the main problem with SDK sample, it does
        // not
        // do this.
        if (interpolatedTime >= 0.5f) {
            switch (direction) {
                case LEFT:
                case UP:
                    degrees += 180.f;
                    break;
                case RIGHT:
                case DOWN:
                    degrees -= 180.f;
                    break;
            }

            if (!visibilitySwapped) {
                toggleListener.toggleView();
                visibilitySwapped = true;
            }
        }

        final Matrix matrix = t.getMatrix();

        camera.save();
        //you can delete this line, it move camera a little far from view and get back
        camera.translate(0.0f, 0.0f, (float) (EXPERIMENTAL_VALUE * Math.sin(radians)));
        switch (direction) {
            case DOWN:
            case UP:
                camera.rotateX(degrees);
                camera.rotateY(0);
                break;
            case LEFT:
            case RIGHT:
                camera.rotateY(degrees);
                camera.rotateX(0);
                break;
        }
        camera.rotateZ(0);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setToggleListener(ToggleListener toggleListener) {
        this.toggleListener = toggleListener;
    }
}