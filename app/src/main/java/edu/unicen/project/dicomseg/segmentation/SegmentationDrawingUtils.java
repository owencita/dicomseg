package edu.unicen.project.dicomseg.segmentation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SegmentationDrawingUtils {

    private static final float TOUCH_TOLERANCE = 4;
    private static final float T_STEP = 0.00001f;
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

    public static Point setPathFromTouchEvent(Path path, Canvas canvas, View view, MotionEvent event, int x, int y) {
        Point stop = null;
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
                stop = new Point();
                stop.x = x;
                stop.y = y;
                break;
        }
        return stop;
    }

    public static Path setPathFromPointList(List<Point> points, Canvas canvas) {
        Path path = new Path();
        Point first = points.get(0);
        touch_start(path, first.x, first.y);
        for (int i=1; i < points.size(); i++) {
            Point point = points.get(i);
            touch_move(path, point.x, point.y);
        }
        touch_up(path, canvas);
        return path;
    }

    public static List<Point> getPointsBetweenUpdates(Point start, Point end) {
        List<Point> points = new ArrayList<Point>();
        Point control = new Point();
        control.x = (start.x + end.x)/2;
        control.y = (start.y + end.y)/2;
        for (int n=0; n < 100000; n++) {
            float t = n + T_STEP;
            Point point = new Point();
            point.x = (int) ((1-t)*(1-t) * start.x + 2 * (1-t) * t * control.x + t * t * end.x);
            point.y = (int) ((1-t)*(1-t) * start.y + 2 * (1-t) * t * control.y + t * t * end.y);
            points.add(point);
        }
        return points;
    }

    public static boolean isEnd(Point start, Point end, int x, int y) {
        if (((x <= start.x + TOUCH_TOLERANCE)&&(x >= start.x - TOUCH_TOLERANCE)&&
                (y <= start.y + TOUCH_TOLERANCE)&&(y >= start.y - TOUCH_TOLERANCE))
            ||((x <= end.x + TOUCH_TOLERANCE)&&(x >= end.x - TOUCH_TOLERANCE)&&
                (y <= end.y + TOUCH_TOLERANCE)&&(y >= end.y - TOUCH_TOLERANCE))) {
            return true;
        } else {
            return false;
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
