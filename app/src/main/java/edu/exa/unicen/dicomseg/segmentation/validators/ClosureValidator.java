package edu.exa.unicen.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.exa.unicen.dicomseg.app.DicomSegApp;
import edu.exa.unicen.dicomseg.segmentation.Segmentation;
import edu.exa.unicen.dicomseg.segmentation.SegmentationMessages;

public class ClosureValidator implements SegmentationValidator {

    private static List<String> errors = new ArrayList<String>();

    /**
     * Validates closure on a segmentation
     *
     * @param points
     * @param refX
     * @param refY
     * @return true if the segmentation has closure, false otherwise
     */
    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int refX, int refY) {
        if (!points.isEmpty()) {
            Point start = points.get(0);
            Point end = points.get(points.size()-1);
            if ((Math.abs(start.x - end.x) > DicomSegApp.getClousureTolerance())||(Math.abs(start.y - end.y) > DicomSegApp.getClousureTolerance())) {
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

    @Override
    public void resetErrors() {
        errors = new ArrayList<String>();
    }
}
