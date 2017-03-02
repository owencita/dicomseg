package edu.unicen.project.dicomseg.segmentation;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.ClosureValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("IVUS LI (Lumen-Intima)",
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
            SegmentationColors.BLUE),
    IVUS_MA("IVUS MA (Media-Adventitia)",
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
            SegmentationColors.RED),
    BRAIN_TUMOR_NECROSIS("Necrosis",
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
            SegmentationColors.BLUE),
    BRAIN_TUMOR_BORDER("Tumor Border",
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
            SegmentationColors.RED),
    OPTIC_DISC("Optic Disc",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosureValidator())),
            SegmentationColors.BLUE),
    VESSELS("Vessels",
            Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(),
            SegmentationColors.RED),
    CAROTID_LI_ANTERIOR("Carotid Anterior LI",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(),
            SegmentationColors.BLUE),
    CAROTID_LI_POSTERIOR("Carotid Posterior LI",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(),
            SegmentationColors.RED),
    CAROTID_MA_POSTERIOR("Carotid Posterior MA",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(),
            SegmentationColors.GREEN),
    SNAKE("Snake segmentation",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            null,
            SegmentationColors.YELLOW);

    private String name;
    private Boolean selectableReferencePoint;
    private Boolean drawableReferencePoint;
    private Boolean allowsRepeats;
    private Boolean isAdjustable;
    private List<SegmentationValidator> ownValidators;
    private Integer color;

    private ImmutableMap<SegmentationType, List<SegmentationValidator>> related;
    private String referencePointHint;

    static {
        /* Relations */
        IVUS_LI.related = SegmentationRelationMap.IVUS_LI_RELATED;
        IVUS_MA.related = SegmentationRelationMap.IVUS_MA_RELATED;
        BRAIN_TUMOR_NECROSIS.related = SegmentationRelationMap.BRAIN_TUMOR_NECROSIS_RELATED;
        BRAIN_TUMOR_BORDER.related = SegmentationRelationMap.BRAIN_TUMOR_BORDER_RELATED;
        OPTIC_DISC.related = null;
        VESSELS.related = null;
        CAROTID_LI_ANTERIOR.related = SegmentationRelationMap.CAROTID_LI_ANTERIOR_RELATED;
        CAROTID_LI_POSTERIOR.related = SegmentationRelationMap.CAROTID_LI_POSTERIOR_RELATED;
        CAROTID_MA_POSTERIOR.related = SegmentationRelationMap.CAROTID_MA_POSTERIOR_RELATED;

        // TODO: review a better way to add this
        /* Hint for reference point */
        IVUS_LI.referencePointHint = "";
        IVUS_MA.referencePointHint = "";
        BRAIN_TUMOR_NECROSIS.referencePointHint = "(segmentations must be drawn around this point)";
        BRAIN_TUMOR_BORDER.referencePointHint = "(segmentations must be drawn around this point)";
        OPTIC_DISC.referencePointHint = "";
        VESSELS.referencePointHint = "";
        CAROTID_LI_ANTERIOR.referencePointHint = "";
        CAROTID_LI_POSTERIOR.referencePointHint = "";
        CAROTID_MA_POSTERIOR.referencePointHint = "";
    }

    SegmentationType(String name, Boolean selectableReferencePoint, Boolean drawableReferencePoint,
                     Boolean allowsRepeats, Boolean isAdjustable, List<SegmentationValidator> ownValidators,
                     Integer color) {
        this.name = name;
        this.selectableReferencePoint = selectableReferencePoint;
        this.drawableReferencePoint = drawableReferencePoint;
        this.allowsRepeats = allowsRepeats;
        this.isAdjustable = isAdjustable;
        this.ownValidators = ownValidators;
        this.color = color;
    }

    public String getName() {
        return name;
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

    public List<SegmentationValidator> getOwnValidators() {
        return ownValidators;
    }

    public ImmutableMap<SegmentationType, List<SegmentationValidator>> getRelated() {
        return related;
    }

    public Integer getColor() {
        return color;
    }

    public Boolean isAdjustable() {
        return isAdjustable;
    }
}
