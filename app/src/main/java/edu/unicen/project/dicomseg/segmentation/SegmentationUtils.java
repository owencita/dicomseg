package edu.unicen.project.dicomseg.segmentation;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.validators.SegmentationValidator;

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
}
