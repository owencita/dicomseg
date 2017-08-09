package edu.exa.unicen.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.List;

import edu.exa.unicen.dicomseg.segmentation.Segmentation;

public interface SegmentationValidator {

    Boolean validate(List<Point> points, Segmentation toCompare, int imageWidth, int imageHeight);

    List<String> errors();

    void resetErrors();
}
