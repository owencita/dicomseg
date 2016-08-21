package edu.unicen.project.dicomseg.segmentation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SegmentationDrawingUtils {

    private static final int STROKE_WIDTH = 512;
    private static final int LINE_STROKE_FACTOR = 5;
    private static final int POINT_STROKE_FACTOR = 8;
    private static final float TOUCH_TOLERANCE = 4;
    private static float mX, mY;
    private static final Paint paint = new Paint();
    private static final Paint notePointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int[] colors = { SegmentationColors.BLUE, SegmentationColors.RED, SegmentationColors.YELLOW, SegmentationColors.GREEN };
    private static int COLOR_INDEX = 0;

    public static Paint getPaint(int width, int color) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(((float)LINE_STROKE_FACTOR / STROKE_WIDTH) * width);
        return paint;
    }

    public static Paint getPaintForPointNote() {
        notePointPaint.setColor(SegmentationColors.BLUE);
        notePointPaint.setStyle(Paint.Style.FILL);
        return notePointPaint;
    }

    public static float getRadiusForPointNote(int width) {
        return ((float)POINT_STROKE_FACTOR / STROKE_WIDTH) * width;
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

    public static int getColor() {
        if (COLOR_INDEX < colors.length) {
            int color = COLOR_INDEX;
            COLOR_INDEX++;
            return colors[color];
        } else {
            resetColor();
            return colors[COLOR_INDEX];
        }
    }

    public static void resetColor() {
        COLOR_INDEX = 0;
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
