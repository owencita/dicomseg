package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;

public class IVUSValidator implements SegmentationValidator {

    private static final int TOLERANCE = 5;
    private static List<String> errors = new ArrayList<String>();

    /**
     * Validate IVUS segmentation:
     *                              - closure
     * @param points
     * @param imageWidth
     * @param imageHeight
     * @return true if the segmentation is valid, false otherwise
     */
    @Override
    public Boolean validate(List<Point> points, int imageWidth, int imageHeight) {
        if (!points.isEmpty()) {
            Point start = points.get(0);
            Point end = points.get(points.size()-1);
            if ((Math.abs(start.x - end.x) > TOLERANCE)&&(Math.abs(start.y - end.y) > TOLERANCE)) {
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
