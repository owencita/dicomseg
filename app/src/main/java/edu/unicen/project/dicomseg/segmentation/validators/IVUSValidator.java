package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.List;

import edu.unicen.project.dicomseg.polar.CartesianToPolarCalculator;
import edu.unicen.project.dicomseg.segmentation.SegmentationDrawingUtils;

public class IVUSValidator implements SegmentationValidator {

    private static final int TOLERANCE = 1;

    @Override
    public Boolean validate(List<Point> points, int imageWidth, int imageHeight) {
        if (!points.isEmpty()) {
            for (int i=0; i < points.size(); i++) {
                Point start = points.get(i);
                Point end = null;
                if (i != (points.size()-1)) {
                    end = points.get(i+1);
                }
                List<Point> allPoints = SegmentationDrawingUtils.getPointsBetweenUpdates(start, end);
                allPoints.add(0, start);
                allPoints.add(allPoints.size(), end);

                Point previous = allPoints.get(0);
                for (int j=1; j< allPoints.size(); j++) {
                    PointF previousPolarPoint = CartesianToPolarCalculator.getPolarPoint(previous, imageWidth, imageWidth);
                    PointF polarPoint = CartesianToPolarCalculator.getPolarPoint(allPoints.get(j), imageWidth, imageWidth);
                    if ((Math.abs(polarPoint.y - previousPolarPoint.y) > TOLERANCE)) {
                        return Boolean.FALSE;
                    }
                    previous = allPoints.get(j);
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String onTouchUp() {
        return "Ending or starting point must be matched to continue with segmentation";
    }
}
