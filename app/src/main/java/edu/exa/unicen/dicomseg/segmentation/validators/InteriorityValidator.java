package edu.exa.unicen.dicomseg.segmentation.validators;

import edu.exa.unicen.dicomseg.segmentation.SegmentationMessages;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

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
