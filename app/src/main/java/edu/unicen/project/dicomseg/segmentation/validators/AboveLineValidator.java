package edu.unicen.project.dicomseg.segmentation.validators;

import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class AboveLineValidator extends AbstractLineValidator {

    @Override
    public Boolean compare(float interpolatedY, int y, SegmentationType segType) {
        if ((float)y > interpolatedY) {
            errors.add(SegmentationMessages.NOT_ABOVE_ERROR + " " + segType.getName());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
