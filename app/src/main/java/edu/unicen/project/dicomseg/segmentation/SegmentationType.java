package edu.unicen.project.dicomseg.segmentation;

import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("ivus-li", "IVUS LI (Lumen-Intima)", Arrays.asList(new ClosureValidator(), new InteriorityValidator())),
    IVUS_MA("ivus-ma", "IVUS MA (Media-Adventitia)", Arrays.asList(new ClosureValidator(), new ExteriorityValidator())),
    // TODO: replace InteriorityValidator -> SelectablePoleInteriorityValidator
    // TODO: replace ExteriorityValidator -> SelectablePoleExteriorityValidator
    INNER_SELECTABLE_POLE("selectable-pole", "Inner Selectable Pole Segmentation", Arrays.asList(new ClosureValidator(), new InteriorityValidator())),
    OUTER_SELECTABLE_POLE("selectable-pole", "Outer Selectable Pole Segmentation", Arrays.asList(new ClosureValidator(), new ExteriorityValidator()));

    private String value;
    private String name;
    private SegmentationType related;
    private List<SegmentationValidator> validators;

    static {
        IVUS_LI.related = IVUS_MA;
        IVUS_MA.related = IVUS_LI;
        INNER_SELECTABLE_POLE.related = OUTER_SELECTABLE_POLE;
        OUTER_SELECTABLE_POLE.related = INNER_SELECTABLE_POLE;
    }

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

    public SegmentationType getRelated(){
        return this.related;
    }

    public List<SegmentationValidator> getValidators() {
        return validators;
    }
}
