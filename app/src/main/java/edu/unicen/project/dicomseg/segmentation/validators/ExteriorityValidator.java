package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.polar.CartesianToPolarCalculator;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;

public class ExteriorityValidator implements SegmentationValidator {

    private static final int TOLERANCE = 9;
    private static List<String> errors = new ArrayList<String>();

    /**
     * Validates outter segmentation
     *
     * @param points
     * @param toCompare
     * @param imageWidth
     * @param imageHeight
     * @return true if segmentation is outside the other one, false otherwise
     */
    @Override
    public Boolean validate(List<Point> points, List<Segmentation> toCompare, int imageWidth, int imageHeight) {
        errors = new ArrayList<String>();
        if (!points.isEmpty()&&(!toCompare.isEmpty())) {
            for (Segmentation segmentation: toCompare) {
                // TODO: if segType is {a, b , c}
                List<PointF> polarPoints = CartesianToPolarCalculator.getPolarPoints(points, imageWidth, imageHeight);
                List<PointF> segPolarPoints = CartesianToPolarCalculator.getPolarPoints(segmentation.getPoints(), imageWidth, imageHeight);

                for (PointF polarPoint : polarPoints) {
                    List<PointF> sameDegreePoints = CartesianToPolarCalculator.getClosestDegreePoints(segPolarPoints, polarPoint.y);

                    if (sameDegreePoints.size() == 2) {
                        PointF segInfPoint = sameDegreePoints.get(0);
                        PointF segSupPoint = sameDegreePoints.get(1);
                        if ((polarPoint.x < segInfPoint.x) && (polarPoint.x > segSupPoint.x)) {
                            errors.add(SegmentationMessages.EXTERIORITY_ERROR + " " + segmentation.getType().getName());
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
