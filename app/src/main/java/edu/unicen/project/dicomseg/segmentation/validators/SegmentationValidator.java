package edu.unicen.project.dicomseg.segmentation.validators;

import android.graphics.Point;

import java.util.List;

import edu.unicen.project.dicomseg.segmentation.Segmentation;

public interface SegmentationValidator {

    Boolean validate(List<Point> points, Segmentation toCompare, int imageWidth, int imageHeight);

    List<String> errors();

    void resetErrors();
}
