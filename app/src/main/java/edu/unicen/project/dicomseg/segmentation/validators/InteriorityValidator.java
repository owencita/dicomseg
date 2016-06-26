package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.polar.CartesianToPolarCalculator;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
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
    public Boolean validate(List<Point> points, List<Segmentation> toCompare, int imageWidth, int imageHeight) {
        errors = new ArrayList<String>();
        if (!points.isEmpty()&&(!toCompare.isEmpty())) {
            for (Segmentation segmentation: toCompare) {
                // TODO: if segType is {a, b , c}
                for (Point segPoint : points) {
                    PointF segPointF = CartesianToPolarCalculator.getPolarPoint(segPoint, imageWidth, imageHeight);
                    for (Point externalPoint : segmentation.getPoints()) {
                        PointF externalPointF = CartesianToPolarCalculator.getPolarPoint(externalPoint, imageWidth, imageHeight);
                        if ((segPointF.x - TOLERANCE) > externalPointF.x) {
                            errors.add(segmentation.getType().getName() + ": " + SegmentationMessages.INTERIORITY_ERROR);
                            break;
                        }
                    }
                    if (!errors.isEmpty()) {
                        break;
                    }
                }
            }
        }
        return errors.isEmpty();
    }

    @Override
    public List<String> errors() {
        return errors;
    }
}
