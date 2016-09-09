package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;

public class ClosureValidator implements SegmentationValidator {

    private static final int TOLERANCE = 8;
    private static List<String> errors = new ArrayList<String>();

    /**
     * Validates closure on a segmentation
     *
     * @param points
     * @param imageWidth
     * @param imageHeight
     * @return true if the segmentation has closure, false otherwise
     */
    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int imageWidth, int imageHeight) {
        errors = new ArrayList<String>();
        if (!points.isEmpty()) {
            Point start = points.get(0);
            Point end = points.get(points.size()-1);
            if ((Math.abs(start.x - end.x) > TOLERANCE)||(Math.abs(start.y - end.y) > TOLERANCE)) {
                errors.add(SegmentationMessages.CLOSURE_ERROR);
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public List<String> errors() {
        return errors;
    }
}
