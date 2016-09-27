package edu.unicen.project.dicomseg.segmentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("ivus-li", "IVUS LI (Lumen-Intima)",
            Arrays.asList(new ClosureValidator(), new InteriorityValidator()),
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE),
    IVUS_MA("ivus-ma", "IVUS MA (Media-Adventitia)",
            Arrays.asList(new ClosureValidator(), new ExteriorityValidator()),
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE),
    INNER_SELECTABLE_POLE("selectable-pole", "Tumor Border",
            Arrays.asList(new ClosureValidator(), new InteriorityValidator()),
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE),
    OUTER_SELECTABLE_POLE("selectable-pole", "Necrosis",
            Arrays.asList(new ClosureValidator(), new ExteriorityValidator()),
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE),
    OPTIC_DISC("optic-disc", "Optic Disc",
               new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
               Boolean.FALSE, Boolean.FALSE, Boolean.FALSE),
    VESSELS("vessels", "Vessels",
            new ArrayList<SegmentationValidator>(),
            Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);

    private String name;
    private Boolean selectablePole;
    private Boolean drawablePole;
    private Boolean allowsRepeats;
    private SegmentationType related;
    private List<SegmentationValidator> validators;

    static {
        IVUS_LI.related = IVUS_MA;
        IVUS_MA.related = IVUS_LI;
        INNER_SELECTABLE_POLE.related = OUTER_SELECTABLE_POLE;
        OUTER_SELECTABLE_POLE.related = INNER_SELECTABLE_POLE;
        OPTIC_DISC.related = null;
        VESSELS.related = null;
    }

    SegmentationType(String value, String name, List<SegmentationValidator> validators, Boolean selectablePole,
                     Boolean drawablePole, Boolean allowsRepeats) {
        this.name = name;
        this.validators = validators;
        this.selectablePole = selectablePole;
        this.drawablePole = drawablePole;
        this.allowsRepeats = allowsRepeats;
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

    public Boolean isPoleDrawable() {
        return drawablePole;
    }

    public Boolean allowsRepeats() {
        return allowsRepeats;
    }
}
