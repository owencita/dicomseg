package edu.unicen.project.dicomseg.segmentation;

import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("ivus-li", "IVUS LI (Lumen-Intima)",
            Arrays.asList(new ClosureValidator(), new InteriorityValidator()),
            Boolean.FALSE),
    IVUS_MA("ivus-ma", "IVUS MA (Media-Adventitia)",
            Arrays.asList(new ClosureValidator(), new ExteriorityValidator()),
            Boolean.FALSE),
    INNER_SELECTABLE_POLE("selectable-pole", "Tumor Border",
            Arrays.asList(new ClosureValidator(), new InteriorityValidator()),
            Boolean.TRUE),
    OUTER_SELECTABLE_POLE("selectable-pole", "Necrosis",
            Arrays.asList(new ClosureValidator(), new ExteriorityValidator()),
            Boolean.TRUE);

    private String value;
    private String name;
    private Boolean selectablePole;
    private SegmentationType related;
    private List<SegmentationValidator> validators;

    static {
        IVUS_LI.related = IVUS_MA;
        IVUS_MA.related = IVUS_LI;
        INNER_SELECTABLE_POLE.related = OUTER_SELECTABLE_POLE;
        OUTER_SELECTABLE_POLE.related = INNER_SELECTABLE_POLE;
    }

    SegmentationType(String value, String name, List<SegmentationValidator> validators, Boolean selectablePole) {
        this.value = value;
        this.name = name;
        this.validators = validators;
        this.selectablePole = selectablePole;
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

    public Boolean isPoleSelectable() {
        return selectablePole;
    }
}
