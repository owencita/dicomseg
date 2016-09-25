package edu.unicen.project.dicomseg.segmentation;

import java.util.List;

public class SegmentationUtils {

    public static Segmentation getRelatedSegmentation(List<Segmentation> segmentationList, SegmentationType relatedTo) {
        for (Segmentation segmentation: segmentationList) {
            if (segmentation.getType().getRelated().equals(relatedTo)) {
                return segmentation;
            }
        }
        return null;
    }
}
