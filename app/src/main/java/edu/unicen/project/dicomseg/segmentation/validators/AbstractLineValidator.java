package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.coordinatesystem.CartesianUtils;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public abstract class AbstractLineValidator implements SegmentationValidator {

    protected static List<String> errors = new ArrayList<String>();

    public abstract Boolean compare(float interpolatedY, int y, SegmentationType segType);

    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int refX, int refY) {
        if (!points.isEmpty() && (toCompare != null)) {
            for (Point p: points) {
                List<Point> closestPoints = CartesianUtils.getClosestPoints(toCompare.getPoints(), p.x);
                if (!closestPoints.isEmpty()) {
                    if (closestPoints.size() == 2) {
                        Point inf = closestPoints.get(0);
                        Point sup = closestPoints.get(1);
                        float interpolatedY = CartesianUtils.getInterpolatedY(inf, sup, p.x);
                        if (!compare(interpolatedY, p.y, toCompare.getType())) {
                            return Boolean.FALSE;
                        }
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public List<String> errors() {
        return errors;
    }

    @Override
    public void resetErrors() {
        errors = new ArrayList<String>();
    }
}

