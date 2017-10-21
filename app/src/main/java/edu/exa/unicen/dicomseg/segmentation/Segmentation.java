package edu.exa.unicen.dicomseg.segmentation;

import android.content.SharedPreferences;
import android.graphics.Point;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.exa.unicen.dicomseg.segmentation.validators.SegmentationValidator;

public class Segmentation {

    private SegmentationType type;
    private Point referencePoint;
    private List<Point> points = new ArrayList<Point>();
    private List<Segmentation> existingRelatedSegmentations;

    public SegmentationType getType() {
        return type;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Segmentation> getExistingRelatedSegmentations() {
        return existingRelatedSegmentations;
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

    public void setReferencePoint(List<Segmentation> relatedSegs) {
        for (Segmentation relatedSeg: relatedSegs) {
            if (relatedSeg.getReferencePoint() != null) {
                this.referencePoint = relatedSeg.getReferencePoint();
                break;
            }
        }
    }

    public void setExistingRelatedSegmentations(List<Segmentation> existingRelatedSegmentations) {
        this.existingRelatedSegmentations = existingRelatedSegmentations;
    }

    public Boolean isValid() {
        resetErrors();
        return validTowardsItself() && validTowardsRelatedSegmentations();
    }

    private Boolean validTowardsItself() {
        for (SegmentationValidator validator: type.getOwnValidators()) {
            if (!validator.validate(points, null, referencePoint.x, referencePoint.y)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private Boolean validTowardsRelatedSegmentations() {
        Boolean returnValue = Boolean.TRUE;
        ImmutableMap<SegmentationType, List<SegmentationValidator>> related = type.getRelated();
        if (related != null) {
            for (SegmentationType relatedType : related.keySet()) {
                Segmentation relatedSeg = getExistingSegmentation(relatedType);
                if (relatedSeg != null) {
                    List<SegmentationValidator> validators = related.get(relatedType);
                    for (SegmentationValidator validator : validators) {
                        if (!validator.validate(points, relatedSeg, referencePoint.x, referencePoint.y)) {
                            returnValue = Boolean.FALSE;
                        }
                    }
                }
            }
        }
        return returnValue;
    }

    public Set<String> errors() {
        Set<String> errors = new HashSet<String>();
        for (SegmentationValidator validator: type.getOwnValidators()) {
            errors.addAll(validator.errors());
        }
        ImmutableMap<SegmentationType, List<SegmentationValidator>> related = type.getRelated();
        if (related != null) {
            for (SegmentationType relatedType : related.keySet()) {
                if (getExistingSegmentation(relatedType) != null) {
                    List<SegmentationValidator> validators = related.get(relatedType);
                    for (SegmentationValidator validator : validators) {
                        errors.addAll(validator.errors());
                    }
                }
            }
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

    public Segmentation getExistingSegmentation(SegmentationType segType) {
        for (Segmentation existingRelatedSegmentation: existingRelatedSegmentations) {
            if (segType.equals(existingRelatedSegmentation.getType())) {
                return existingRelatedSegmentation;
            }
        }
        return null;
    }

    private void resetErrors() {
        for (SegmentationValidator validator: type.getOwnValidators()) {
           validator.resetErrors();
        }
        ImmutableMap<SegmentationType, List<SegmentationValidator>> related = type.getRelated();
        if (related != null) {
            for (SegmentationType relatedType : related.keySet()) {
                if (getExistingSegmentation(relatedType) != null) {
                    List<SegmentationValidator> validators = related.get(relatedType);
                    for (SegmentationValidator validator : validators) {
                        validator.resetErrors();
                    }
                }
            }
        }
    }
}
