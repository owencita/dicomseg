package edu.unicen.project.dicomseg.segmentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.AboveLineValidator;
import edu.unicen.project.dicomseg.segmentation.validators.BelowLineValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("IVUS LI (Lumen-Intima)",
            Arrays.asList(new ClosureValidator(), new InteriorityValidator()),
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE),
    IVUS_MA("IVUS MA (Media-Adventitia)",
            Arrays.asList(new ClosureValidator(), new ExteriorityValidator()),
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE),
    INNER_SELECTABLE_POLE("Tumor Border",
            Arrays.asList(new ClosureValidator(), new InteriorityValidator()),
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE),
    OUTER_SELECTABLE_POLE("Necrosis",
            Arrays.asList(new ClosureValidator(), new ExteriorityValidator()),
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE),
    OPTIC_DISC("Optic Disc",
               new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
               Boolean.FALSE, Boolean.FALSE, Boolean.FALSE),
    VESSELS("Vessels",
            new ArrayList<SegmentationValidator>(),
            Boolean.FALSE, Boolean.FALSE, Boolean.TRUE),
    CAROTID_LI_ANTERIOR("Carotid Anterior LI",
            new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())),
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE),
    CAROTID_LI_POSTERIOR("Carotid Posterior LI",
            new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())),
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

    private String name;
    private Boolean selectableReferencePoint;
    private String referencePointHint;
    private Boolean drawableReferencePoint;
    private Boolean allowsRepeats;
    private SegmentationType related;
    private List<SegmentationValidator> validators;

    static {
        /* Relations */
        IVUS_LI.related = IVUS_MA;
        IVUS_MA.related = IVUS_LI;
        INNER_SELECTABLE_POLE.related = OUTER_SELECTABLE_POLE;
        OUTER_SELECTABLE_POLE.related = INNER_SELECTABLE_POLE;
        OPTIC_DISC.related = null;
        VESSELS.related = null;
        CAROTID_LI_ANTERIOR.related = CAROTID_LI_POSTERIOR;
        CAROTID_LI_POSTERIOR.related = CAROTID_LI_ANTERIOR;

        // TODO: review a better way to add this
        /* Hint for reference point */
        IVUS_LI.referencePointHint = "";
        IVUS_MA.referencePointHint = "";
        INNER_SELECTABLE_POLE.referencePointHint = "(segmentations must be drawn around this point)";
        OUTER_SELECTABLE_POLE.referencePointHint = "(segmentations must be drawn around this point)";
        OPTIC_DISC.referencePointHint = "";
        VESSELS.referencePointHint = "";
        CAROTID_LI_ANTERIOR.referencePointHint = "(segmentations must be drawn below this point)";
        CAROTID_LI_POSTERIOR.referencePointHint = "(segmentations must be drawn below this point)";
    }

    SegmentationType(String name, List<SegmentationValidator> validators, Boolean selectableReferencePoint,
                     Boolean drawableReferencePoint, Boolean allowsRepeats) {
        this.name = name;
        this.validators = validators;
        this.selectableReferencePoint = selectableReferencePoint;
        this.drawableReferencePoint = drawableReferencePoint;
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

    public Boolean isReferencePointSelectable() {
        return selectableReferencePoint;
    }

    public Boolean isReferencePointDrawable() {
        return drawableReferencePoint;
    }

    public Boolean allowsRepeats() {
        return allowsRepeats;
    }

    public String getReferencePointHint() {
        return referencePointHint;
    }
}
