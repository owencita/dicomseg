package edu.unicen.project.dicomseg.segmentation.validators;

import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class ExteriorityValidator extends AbstractCircumferenceValidator {

    @Override
    public Boolean compare(float interpolatedDistance, float distance, SegmentationType segType) {
        if (distance < interpolatedDistance) {
            errors.add(String.format(SegmentationMessages.EXTERIORITY_ERROR, segType.getName()));
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
