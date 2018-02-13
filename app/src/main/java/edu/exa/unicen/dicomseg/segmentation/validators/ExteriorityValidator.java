package edu.exa.unicen.dicomseg.segmentation.validators;

import edu.exa.unicen.dicomseg.segmentation.SegmentationMessages;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

public class ExteriorityValidator extends AbstractCircumferenceValidator {

    @Override
    public Boolean compare(float interpolatedDistance, float distance, SegmentationType segType) {
        if (distance < interpolatedDistance) {
            errors.put(SegmentationMessages.EXTERIORITY_ERROR, segType.getName());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
