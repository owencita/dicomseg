package edu.unicen.project.dicomseg.segmentation;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public class Segmentation {

    private SegmentationType type;
    private int imageWidth;
    private int imageHeight;
    private List<Point> points = new ArrayList<Point>();
    private Segmentation relatedSeg;

    public SegmentationType getType() {
        return type;
    }

    public void setType(SegmentationType type) {
        this.type = type;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Boolean isValid() {
        for (SegmentationValidator validator: type.getValidators()) {
            if (!validator.validate(points, relatedSeg.getPoints(), imageWidth, imageHeight)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public List<String> errors() {
        List<String> errors = new ArrayList<String>();
        for (SegmentationValidator validator: type.getValidators()) {
            errors.addAll(validator.errors());
        }
        return errors;
    }
}
