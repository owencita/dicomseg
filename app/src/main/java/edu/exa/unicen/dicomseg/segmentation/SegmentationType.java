package edu.exa.unicen.dicomseg.segmentation;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.exa.unicen.dicomseg.segmentation.validators.AboveLineValidator;
import edu.exa.unicen.dicomseg.segmentation.validators.BelowLineValidator;
import edu.exa.unicen.dicomseg.segmentation.validators.ClosedFigureValidator;
import edu.exa.unicen.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.exa.unicen.dicomseg.segmentation.validators.InteriorityValidator;
import edu.exa.unicen.dicomseg.segmentation.validators.PointMinimumQuantityValidator;
import edu.exa.unicen.dicomseg.segmentation.validators.SegmentationValidator;

public enum SegmentationType {

    IVUS_LI("IVUS LI (Lumen-Intima)",
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosedFigureValidator())),
            SegmentationColors.BLUE),
    IVUS_MA("IVUS MA (Media-Adventitia)",
            Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosedFigureValidator())),
            SegmentationColors.RED),
    BRAIN_TUMOR_NECROSIS("Necrosis",
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosedFigureValidator())),
            SegmentationColors.BLUE),
    BRAIN_TUMOR_BORDER("Tumor Border",
            Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosedFigureValidator())),
            SegmentationColors.RED),
    OPTIC_DISC("Optic Disc",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new ClosedFigureValidator())),
            SegmentationColors.BLUE),
    VESSELS("Vessels",
            Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new PointMinimumQuantityValidator(5))),
            SegmentationColors.YELLOW),
    CAROTID_LI_ANTERIOR("Carotid Anterior LI",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new PointMinimumQuantityValidator(5))),
            SegmentationColors.BLUE),
    CAROTID_LI_POSTERIOR("Carotid Posterior LI",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new PointMinimumQuantityValidator(5))),
            SegmentationColors.RED),
    CAROTID_MA_POSTERIOR("Carotid Posterior MA",
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
            new ArrayList<SegmentationValidator>(Arrays.asList(new PointMinimumQuantityValidator(5))),
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
        IVUS_LI.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.IVUS_MA,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new InteriorityValidator())))
                .build();
        IVUS_MA.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.IVUS_LI,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new ExteriorityValidator())))
                .build();
        BRAIN_TUMOR_NECROSIS.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.BRAIN_TUMOR_BORDER,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new InteriorityValidator())))
                .build();
        BRAIN_TUMOR_BORDER.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.BRAIN_TUMOR_NECROSIS,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new ExteriorityValidator())))
                .build();
        OPTIC_DISC.related = null;
        VESSELS.related = null;
        CAROTID_LI_ANTERIOR.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.CAROTID_LI_POSTERIOR,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())))
                .put(SegmentationType.CAROTID_MA_POSTERIOR,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())))
                .build();
        CAROTID_LI_POSTERIOR.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.CAROTID_LI_ANTERIOR,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())))
                .put(SegmentationType.CAROTID_MA_POSTERIOR,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())))
                .build();
        CAROTID_MA_POSTERIOR.related = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
                .put(SegmentationType.CAROTID_LI_ANTERIOR,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())))
                .put(SegmentationType.CAROTID_LI_POSTERIOR,
                        new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())))
                .build();

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
