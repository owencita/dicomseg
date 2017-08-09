package edu.exa.unicen.dicomseg.segmentation;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

import edu.exa.unicen.dicomseg.segmentation.validators.SegmentationValidator;

public class SegmentationUtils {

    public static List<Segmentation> getRelatedSegmentations(List<Segmentation> segmentationList, SegmentationType relatedTo) {
        List<Segmentation> relatedSegs = new ArrayList<Segmentation>();
        ImmutableMap<SegmentationType, List<SegmentationValidator>> related = relatedTo.getRelated();
        if (related != null) {
            for (SegmentationType type : related.keySet()) {
                for (Segmentation segmentation : segmentationList) {
                    if (segmentation.getType().equals(type)) {
                        relatedSegs.add(segmentation);
                        break;
                    }
                }
            }
        }
        return relatedSegs;
    }

    public static ArrayList<Segmentation> getAdjustableSegmentations(List<Segmentation> segmentations) {
        ArrayList<Segmentation> adjustableSegs = new ArrayList<Segmentation>();
        for (Segmentation seg: segmentations) {
            if (seg.getType().isAdjustable()) {
                adjustableSegs.add(seg);
            }
        }
        return adjustableSegs;
    }
}
