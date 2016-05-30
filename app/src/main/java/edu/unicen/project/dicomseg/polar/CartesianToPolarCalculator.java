package edu.unicen.project.dicomseg.polar;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class CartesianToPolarCalculator {

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
