package edu.exa.unicen.dicomseg.segmentation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SegmentationDrawingUtils {

    private static final int STROKE_WIDTH = 512;
    private static final int POINT_STROKE_FACTOR = 7;

    private static float mX, mY;
    private static final Paint paint = new Paint();
    private static final Paint notePointPaint = new Paint();

    public static Paint getPaint(int width, int color, int strokeFactor) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(((float)strokeFactor / STROKE_WIDTH) * width);
        return paint;
    }

    public static Paint getPaintForPoint() {
        notePointPaint.setColor(SegmentationColors.BLUE);
        notePointPaint.setStyle(Paint.Style.FILL);
        return notePointPaint;
    }

    public static float getRadiusForPoint(int width) {
        return ((float)POINT_STROKE_FACTOR / STROKE_WIDTH) * width;
    }

    public static Point setPathFromTouchEvent(Path path, Canvas canvas, View view, MotionEvent event, int x, int y, float touchTolerance) {
        Point stop = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(path, x, y);
                view.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(path, x, y, touchTolerance);
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

    public static Path setPathFromPointList(List<Point> points, Canvas canvas, float touchTolerance) {
        Path path = new Path();
        Point first = points.get(0);
        touch_start(path, first.x, first.y);
        for (int i=1; i < points.size(); i++) {
            Point point = points.get(i);
            touch_move(path, point.x, point.y, touchTolerance);
        }
        touch_up(path, canvas);
        return path;
    }

    public static boolean isEnd(Point start, Point end, int x, int y, float touchTolerance) {
        if (((x <= start.x + touchTolerance) && (x >= start.x - touchTolerance) &&
                (y <= start.y + touchTolerance) && (y >= start.y - touchTolerance))
            ||((x <= end.x + touchTolerance) && (x >= end.x - touchTolerance) &&
                (y <= end.y + touchTolerance) && (y >= end.y - touchTolerance))) {
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

    private static void touch_move(Path path, int x, int y, float touchTolerance) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= touchTolerance || dy >= touchTolerance) {
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
