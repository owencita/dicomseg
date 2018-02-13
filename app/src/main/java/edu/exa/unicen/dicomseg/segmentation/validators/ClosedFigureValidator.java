package edu.exa.unicen.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.exa.unicen.dicomseg.app.DicomSegApp;
import edu.exa.unicen.dicomseg.segmentation.Segmentation;
import edu.exa.unicen.dicomseg.segmentation.SegmentationMessages;

public class ClosedFigureValidator implements SegmentationValidator {

    private static Map<String, String> errors = new HashMap<String, String>();

    /**
     * Validates if the segmentation points form a closed figure
     *
     * @param points - Segmentation points to validate
     * @param refX - Reference point x
     * @param refY - Reference point y
     * @return true if the segmentation is a closed figure, false otherwise
     */
    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int refX, int refY) {
        if (!points.isEmpty()) {
            Point start = points.get(0);
            Point end = points.get(points.size()-1);
            if ((Math.abs(start.x - end.x) > DicomSegApp.getClosedFigureTolerance())||
                    (Math.abs(start.y - end.y) > DicomSegApp.getClosedFigureTolerance())) {
                errors.put(SegmentationMessages.CLOSED_FIGURE_ERROR, "");
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Map<String, String> errors() {
        return errors;
    }

    @Override
    public void resetErrors() {
        errors = new HashMap<String, String>();
    }
}
