package edu.unicen.project.dicomseg.segmentation;

import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("ivus-li", "IVUS LI (Lumen-Intima)", Arrays.asList(new ClosureValidator(), new ExteriorityValidator())),
    IVUS_MA("ivus-ma", "IVUS MA (Media-Adventitia)", Arrays.asList(new ClosureValidator(), new InteriorityValidator()));

    private String value;
    private String name;
    private List<SegmentationValidator> validators;

    SegmentationType(String value, String name, List<SegmentationValidator> validators) {
        this.value = value;
        this.name = name;
        this.validators = validators;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public List<SegmentationValidator> getValidators() {
        return validators;
    }
}
