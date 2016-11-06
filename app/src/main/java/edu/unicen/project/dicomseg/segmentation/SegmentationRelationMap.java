package edu.unicen.project.dicomseg.segmentation;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.AboveLineValidator;
import edu.unicen.project.dicomseg.segmentation.validators.BelowLineValidator;
import edu.unicen.project.dicomseg.segmentation.validators.ExteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.InteriorityValidator;
import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

/**
 * Relation between a segmentation type and its validators, given a segmentation type
 */
public class SegmentationRelationMap {

    // IVUS LI
    public static final ImmutableMap IVUS_LI_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.IVUS_MA, new ArrayList<SegmentationValidator>(Arrays.asList(new InteriorityValidator())))
            .build();
    // IVUS MA
    public static final ImmutableMap IVUS_MA_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.IVUS_LI, new ArrayList<SegmentationValidator>(Arrays.asList(new ExteriorityValidator())))
            .build();
    // BRAIN TUMOR NECROSIS
    public static final ImmutableMap BRAIN_TUMOR_NECROSIS_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.BRAIN_TUMOR_BORDER, new ArrayList<SegmentationValidator>(Arrays.asList(new InteriorityValidator())))
            .build();
    // BRAIN TUMOR BORDER
    public static final ImmutableMap BRAIN_TUMOR_BORDER_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.BRAIN_TUMOR_NECROSIS, new ArrayList<SegmentationValidator>(Arrays.asList(new ExteriorityValidator())))
            .build();
    // CAROTID LI ANTERIOR
    public static final ImmutableMap CAROTID_LI_ANTERIOR_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.CAROTID_LI_POSTERIOR, new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())))
            .put(SegmentationType.CAROTID_MA_POSTERIOR, new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())))
            .build();
    // CAROTID LI POSTERIOR
    public static final ImmutableMap CAROTID_LI_POSTERIOR_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.CAROTID_LI_ANTERIOR, new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())))
            .put(SegmentationType.CAROTID_MA_POSTERIOR, new ArrayList<SegmentationValidator>(Arrays.asList(new AboveLineValidator())))
            .build();
    // CAROTID MA POSTERIOR
    public static final ImmutableMap CAROTID_MA_POSTERIOR_RELATED = ImmutableMap.<SegmentationType, List<SegmentationValidator>>builder()
            .put(SegmentationType.CAROTID_LI_ANTERIOR, new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())))
            .put(SegmentationType.CAROTID_LI_POSTERIOR, new ArrayList<SegmentationValidator>(Arrays.asList(new BelowLineValidator())))
            .build();
}
