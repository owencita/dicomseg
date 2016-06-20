package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.polar.CartesianToPolarCalculator;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;

public class InteriorityValidator implements SegmentationValidator {

    private static final int TOLERANCE = 8;
    private static List<String> errors = new ArrayList<String>();

    /**
     * Validates inner segmentation
     *
     * @param points
     * @param toCompare
     * @param imageWidth
     * @param imageHeight
     * @return true if segmentation is inside the other one, false otherwise
     */
    @Override
    public Boolean validate(List<Point> points, List<Point> toCompare, int imageWidth, int imageHeight) {
        if (!points.isEmpty()&&(!toCompare.isEmpty())) {
            for (Point segPoint: points) {
                PointF segPointF = CartesianToPolarCalculator.getPolarPoint(segPoint, imageWidth, imageHeight);
                for (Point externalPoint: toCompare) {
                    PointF externalPointF = CartesianToPolarCalculator.getPolarPoint(externalPoint, imageWidth, imageHeight);
                    if ((segPointF.x - TOLERANCE) > externalPointF.x) {
                        errors.add(SegmentationMessages.INTERIORITY_ERROR);
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public List<String> errors() {
        return errors;
    }
}
