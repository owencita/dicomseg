package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.polar.CartesianToPolarCalculator;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;

public class InteriorityValidator implements SegmentationValidator {

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
    public Boolean validate(List<Point> points, Segmentation toCompare, int imageWidth, int imageHeight) {
        errors = new ArrayList<String>();
        if (!points.isEmpty() && (toCompare != null)) {

            List<PointF> polarPoints = CartesianToPolarCalculator.getPolarPoints(points, imageWidth, imageHeight);
            List<PointF> segToComparePolarPoints = CartesianToPolarCalculator.getPolarPoints(toCompare.getPoints(), imageWidth, imageHeight);

            for (PointF polarPoint : polarPoints) {
                List<PointF> sameDegreePoints = CartesianToPolarCalculator.getClosestDegreePoints(segToComparePolarPoints, polarPoint.y);

                if (sameDegreePoints.size() == 2) {
                    PointF segInfPoint = sameDegreePoints.get(0);
                    PointF segSupPoint = sameDegreePoints.get(1);
                    if ((polarPoint.x > segInfPoint.x) && (polarPoint.x > segSupPoint.x)) {
                        errors.add(SegmentationMessages.INTERIORITY_ERROR + " " + toCompare.getType().getName());
                        break;
                    }
                }

                if (!errors.isEmpty()) {
                    break;
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
