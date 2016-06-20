package edu.unicen.project.dicomseg.segmentation;

import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LU("ivus-lu", Arrays.asList(new ClosureValidator(), new ExteriorityValidator())),
    IVUS_MA("ivus-ma", Arrays.asList(new ClosureValidator(), new InteriorityValidator()));

    private String value;
    private List<SegmentationValidator> validators;

    SegmentationType(String value, List<SegmentationValidator> validators) {
        this.value = value;
        this.validators = validators;
    }

    public String getValue() {
        return value;
    }

    public List<SegmentationValidator> getValidators() {
        return validators;
    }
}
