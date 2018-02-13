package edu.exa.unicen.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.List;
import java.util.Map;

import edu.exa.unicen.dicomseg.segmentation.Segmentation;

public interface SegmentationValidator {

    Boolean validate(List<Point> points, Segmentation toCompare, int imageWidth, int imageHeight);

    Map<String, String> errors();

    void resetErrors();
}
