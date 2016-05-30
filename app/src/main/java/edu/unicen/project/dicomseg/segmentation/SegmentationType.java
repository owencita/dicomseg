package edu.unicen.project.dicomseg.segmentation;

import edu.unicen.project.dicomseg.segmentation.validators.IVUSValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS("ivus", new IVUSValidator());

    private String value;
    private SegmentationValidator validator;

    SegmentationType(String value, SegmentationValidator validator) {
        this.value = value;
        this.validator = validator;
    }

    public String getValue() {
        return value;
    }

    public SegmentationValidator getValidator() {
        return validator;
    }
}
