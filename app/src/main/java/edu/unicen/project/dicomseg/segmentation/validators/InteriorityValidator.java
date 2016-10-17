package edu.unicen.project.dicomseg.segmentation.validators;

import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class InteriorityValidator extends AbstractCircumferenceValidator {

    @Override
    public Boolean compare(float interpolatedY, float distance, SegmentationType segType) {
        if (distance > interpolatedY) {
            errors.add(String.format(SegmentationMessages.INTERIORITY_ERROR, segType.getName()));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
