package edu.exa.unicen.dicomseg.segmentation;

public class SegmentationMessages {

    public static final String CLOSURE_ERROR = "Closure is required for this segmentation";
    public static final String CONTINUITY_ERROR = "Ending or starting point must be matched to continue with segmentation";
    public static final String INTERIORITY_ERROR = "All segmentation points must be inside %s";
    public static final String EXTERIORITY_ERROR = "All segmentation points must be outside %s";
    public static final String EXISTING_SEGMENTATION_ERROR = "A segmentation of this type already exists for this frame";
    public static final String NOT_ABOVE_ERROR = "All segmentation points must be above";
    public static final String NOT_BELOW_ERROR = "All segmentation points must be below";
    public static final String FRAME_NUMBER_OUT_OF_RANGE = "The entered number is out of range";
    public static final String EMPTY_SEGMENTATION_ERROR = "Empty segmentation";
    public static final String POINTS_MINIMUM_QUANTITY_ERROR = "Segmentation does not reach minimum required points quantity";

}
