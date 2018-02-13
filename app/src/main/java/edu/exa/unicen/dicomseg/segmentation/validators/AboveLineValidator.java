package edu.exa.unicen.dicomseg.segmentation.validators;

import edu.exa.unicen.dicomseg.segmentation.SegmentationMessages;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

public class AboveLineValidator extends AbstractLineValidator {

    @Override
    public Boolean compare(float interpolatedY, int y, SegmentationType segType) {
        if ((float)y > interpolatedY) {
            errors.put(SegmentationMessages.NOT_ABOVE_ERROR, segType.getName());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
