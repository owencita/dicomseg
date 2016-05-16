package edu.unicen.project.dicomseg.segmentation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class SegmentationUtils {

    private static final float TOUCH_TOLERANCE = 4;
    private static float mX, mY;
    private static Paint paint = new Paint();

    public static Paint getPaint(int width, int height) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.rgb(51, 98, 178));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        // TODO: scale stroke according to the image size
        paint.setStrokeWidth(8);

        return paint;
    }

    public static void setPath(Path path, Canvas canvas, View view, MotionEvent event, int x, int y) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(path, x, y);
                view.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(path, x, y);
                view.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(path, canvas);
                view.invalidate();
                break;
        }
    }

    private static void touch_start(Path path, int x, int y) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private static void touch_move(Path path, int x, int y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private static void touch_up(Path path, Canvas mCanvas) {
        path.lineTo(mX, mY);
        mCanvas.drawPath(path, paint);
        path.reset();
    }
}
