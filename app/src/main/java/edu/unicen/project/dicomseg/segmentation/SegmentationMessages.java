package edu.unicen.project.dicomseg.segmentation;

public class SegmentationMessages {

    public static final String CLOSURE_ERROR = "Closure is required for this segmentation";
    public static final String CONTINUITY_ERROR = "Ending or starting point must be matched to continue with segmentation";
    public static final String INTERIORITY_ERROR = "All segmentation points must be inside previous segmentation";
    public static final String EXTERIORITY_ERROR = "All segmentation points must be outside previous segmentation";

}
