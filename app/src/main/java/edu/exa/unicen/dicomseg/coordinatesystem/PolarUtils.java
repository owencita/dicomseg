package edu.exa.unicen.dicomseg.coordinatesystem;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PolarUtils {

    private static final String TAG = "PolarCalculator";

    public static PointF getPolarPoint(Point point, int poleX, int poleY) {
        Log.i(TAG, "Punto Cartesiano: (" + point.x + "," + point.y + ")");
        PointF polarPoint = new PointF();
        int xSh = point.x - poleX;
        int ySh = -point.y + poleY;
        Log.i(TAG, "Punto Trasladado: (" + xSh + "," + ySh + ")");
        polarPoint.x = (float) getDistance(xSh, ySh);
        double radio = Math.sqrt(xSh * xSh + ySh * ySh);
        double cos = xSh / radio;
        double ang = Math.acos(cos);
        polarPoint.y = (float) toDegrees(getAngle(ang, ySh));
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
     * Returns the closest to 'degrees' points from a list of polar points.
     *
     * @param points The list of polar points to get the closest to 'degree' points from
     * @param degrees The degrees to compare
     * @return
     */
    public static List<PointF> getClosestDegreePoints(List<PointF> points, Float degrees) {

        List<PointF> closest = new ArrayList<PointF>();

        PointF closestInf = null;
        for (PointF p : points) {
            if (p.y < degrees) {
                if (closestInf == null || p.y > closestInf.y) {
                    closestInf = p;
                }
            }
        }
        if (closestInf != null) {
            closest.add(closestInf);
        }

        PointF closestSup = null;
        for (PointF p : points) {
            if (p.y > degrees) {
                if (closestSup == null || p.y < closestSup.y) {
                    closestSup = p;
                }
            }
        }
        if (closestSup != null) {
            closest.add(closestSup);
        }

        return closest;
    }

    public static float getInterpolatedDistance(PointF inf, PointF sup, float degrees) {
        float slope = (sup.x - inf.x)/(sup.y - inf.y);
        return slope * (degrees - inf.y) + sup.x;
    }

    private static double getDistance(int x, int y) {
        double x2 = Math.pow(x, 2);
        double y2 = Math.pow(y, 2);
        return Math.sqrt(x2 + y2);
    }

    private static double getAngle(double ang, int ySh) {
        if (ang > 0 && ySh < 0) {
            ang = 2 * Math.PI-ang;
        }
        if (ang < 0 && ySh > 0) {
            ang = Math.PI+ang;
        }
        if (ang < 0 && ySh < 0) {
            ang = Math.PI-ang;
        }
        return ang;
    }

    private static double toDegrees(double radians) {
        return radians * (180/Math.PI);
    }
}
