package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.List;

public interface SegmentationValidator {

    Boolean validate(List<Point> points, int imageWidth, int imageHeight);

    String onTouchUp();
}
