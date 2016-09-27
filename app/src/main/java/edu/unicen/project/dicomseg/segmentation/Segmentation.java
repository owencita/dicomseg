package edu.unicen.project.dicomseg.segmentation;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public class Segmentation {

    private SegmentationType type;
    private Point referencePoint;
    private List<Point> points = new ArrayList<Point>();
    private Segmentation relatedSegmentation;

    public SegmentationType getType() {
        return type;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Segmentation getRelatedSegmentation() {
        return relatedSegmentation;
    }

    public void setType(SegmentationType type) {
        this.type = type;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void clearPoints() {
        this.points = new ArrayList<Point>();
    }

    public Point getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(Point referencePoint) {
        this.referencePoint = referencePoint;
    }

    public void setRelatedSegmentation(Segmentation relatedSegmentation) {
        this.relatedSegmentation = relatedSegmentation;
    }

    public Boolean isValid() {
        for (SegmentationValidator validator: type.getValidators()) {
            if (!validator.validate(points, relatedSegmentation, referencePoint.x, referencePoint.y)) {
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

    public Boolean isContained(List<Segmentation> segmentations) {
        for (Segmentation segmentation: segmentations) {
            if (segmentation.getType().equals(this.type)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
