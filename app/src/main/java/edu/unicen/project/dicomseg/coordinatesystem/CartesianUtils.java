package edu.unicen.project.dicomseg.coordinatesystem;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class CartesianUtils {

    public static List<Point> getClosestPoints(List<Point> points, int x) {
        List<Point> closest = new ArrayList<Point>();

        Point closestInf = points.get(0);
        if (closestInf.x < x) {
            for (Point p : points) {
                if (p.x < x) {
                    if (p.x > closestInf.x) {
                        closestInf = p;
                    }
                }
            }
            closest.add(closestInf);
        }

        Point closestSup = points.get(points.size()-1);
        if (closestSup.x > x) {
            for (Point p : points) {
                if (p.x > x) {
                    if (p.x < closestSup.x) {
                        closestSup = p;
                    }
                }
            }
            closest.add(closestSup);
        }

        return closest;
    }

    public static float getInterpolatedY(Point inf, Point sup, int x) {
        float slope = (sup.y - inf.y)/(sup.x - inf.x);
        return slope * (x - inf.x) + sup.y;
    }

}
