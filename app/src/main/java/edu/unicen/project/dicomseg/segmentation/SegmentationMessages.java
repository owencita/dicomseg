package edu.unicen.project.dicomseg.segmentation;

public class SegmentationMessages {

    public static final String CLOSURE_ERROR = "Closure is required for this segmentation";
    public static final String CONTINUITY_ERROR = "Ending or starting point must be matched to continue with segmentation";
    public static final String INTERIORITY_ERROR = "All segmentation points must be inside";
    public static final String EXTERIORITY_ERROR = "All segmentation points must be outside";
    public static final String EXISTING_SEGMENTATION_ERROR = "A segmentation of this type already exists for this frame";
    public static final String NOT_ABOVE_ERROR = "All segmentation points must be above";
    public static final String NOT_BELOW_ERROR = "All segmentation points must be below";

}
