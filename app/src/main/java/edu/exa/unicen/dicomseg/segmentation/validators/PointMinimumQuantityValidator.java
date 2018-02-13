package edu.exa.unicen.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.exa.unicen.dicomseg.segmentation.Segmentation;
import edu.exa.unicen.dicomseg.segmentation.SegmentationMessages;

public class PointMinimumQuantityValidator implements SegmentationValidator {

    private static Map<String, String> errors = new HashMap<String, String>();
    private Integer minimum;

    public PointMinimumQuantityValidator(Integer minimum) {
        this.minimum = minimum;
    }

    @Override
    public Boolean validate(List<Point> points, Segmentation toCompare, int imageWidth, int imageHeight) {
        if (points.size() < minimum) {
            errors.put(SegmentationMessages.POINTS_MINIMUM_QUANTITY_ERROR, "");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
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
