package edu.unicen.project.dicomseg.polar;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartesianToPolarCalculator {

    private static final Integer DEGREE_TOLERANCE = 2;
    private static final String TAG = "PolarCalculator";

    public static PointF getPolarPoint(Point point, int width, int height) {
        int poleX = width / 2;
        int poleY = height / 2;
        Log.i(TAG, "Punto Cartesiano: (" + point.x + "," + point.y + ")");
        PointF polarPoint = new PointF();
        int xSh = point.x - poleX;
        int ySh = -point.y + poleY;
        Log.i(TAG, "Punto Trasladado: (" + xSh + "," + ySh + ")");
        polarPoint.x = (float) getDistance(xSh, ySh);
        polarPoint.y = (float) toDegrees(getAngle(xSh, ySh));
        Log.i(TAG, "Punto Polar: (" + polarPoint.x + "," + toDegrees(polarPoint.y) + "°)");
        return polarPoint;
    }

    public static List<PointF> getPolarPoints(List<Point> points, int width, int height) {
        List<PointF> polarPoints = new ArrayList<PointF>();
        for (Point p: points) {
            PointF polarPoint = getPolarPoint(p, width, height);
            polarPoints.add(polarPoint);
        }
        return polarPoints;
    }

    /**
     * Returns the closest to 'degress' points from a list of polar points.
     *
     * @param points The list of polar points to get the closest to 'degree' points from
     * @param degrees The degrees to compare
     * @return
     */
    public static List<PointF> getClosestDegreePoints(List<PointF> points, Float degrees) {

        List<PointF> closestDegrees = new ArrayList<PointF>();

        List<PointF> supDegrees = new ArrayList<PointF>();
        for (PointF point: points) {
            if (point.y <= (degrees + DEGREE_TOLERANCE)&&(point.y >= degrees)) {
                supDegrees.add(point);
            }
        }

        // From all superior (close to 'degree') points, pick the one with shortest 'distance'
        int minIndex = 0;
        if (!supDegrees.isEmpty()) {
            if (supDegrees.size() > 1) {
                minIndex = supDegrees.indexOf(Collections.min(supDegrees, new PolarPointDistanceComparator()));
            }
            closestDegrees.add(supDegrees.get(minIndex));
        }

        List<PointF> infDegrees = new ArrayList<PointF>();
        for (PointF point: points) {
            if (point.y > (degrees - DEGREE_TOLERANCE)&&(point.y < degrees)) {
                infDegrees.add(point);
            }
        }

        // From all inferior (close to 'degree') points, pick the one with shortest 'distance'
        minIndex = 0;
        if (!infDegrees.isEmpty()) {
            if (infDegrees.size() > 1) {
                minIndex = infDegrees.indexOf(Collections.min(infDegrees, new PolarPointDistanceComparator()));
            }
            closestDegrees.add(infDegrees.get(minIndex));
        }

        return closestDegrees;
    }

    private static double getDistance(int x, int y) {
        double x2 = Math.pow(x, 2);
        double y2 = Math.pow(y, 2);
        return Math.sqrt(x2 + y2);
    }

    private static double getAngle(int x, int y) {
        if (x != 0) {
            double div = (double) y / x;
            return Math.atan(div);
        } else {
            if (y < 0) {
                return Math.PI/2;
            } else {
                return -Math.PI/2;
            }
        }
    }

    private static double toDegrees(double radians) {
        return radians * (180/Math.PI);
    }
}
