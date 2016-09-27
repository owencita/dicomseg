package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.polar.CartesianToPolarCalculator;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;

public class BelowValidator implements SegmentationValidator {

    private static List<String> errors = new ArrayList<String>();

    /**
     * Validates segmentation is below (y axis) its related segmentation
     *
     * @param points
     * @param toCompare
     * @param refX
     * @param refY
     * @return
     */
    // TODO: this is not the right approach, does not work for all scenarios
    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int refX, int refY) {
        errors = new ArrayList<String>();
        if (!points.isEmpty() && (toCompare != null)) {

            List<PointF> segPolarPoints = CartesianToPolarCalculator.getPolarPoints(points, refX, refY);
            List<PointF> segToComparePolarPoints = CartesianToPolarCalculator.getPolarPoints(toCompare.getPoints(), refX, refY);

            for (PointF polarPoint: segPolarPoints) {
                for (PointF polarPointToCompare: segToComparePolarPoints) {
                    if (polarPoint.x < polarPointToCompare.x) {
                        errors.add(SegmentationMessages.NOT_BELOW_ERROR + " " + toCompare.getType().getName());
                        return Boolean.FALSE;
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
}
