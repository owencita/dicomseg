package edu.unicen.project.dicomseg.segmentation;

import android.graphics.Point;

import java.util.List;

public class Segmentation {

    private SegmentationType type;
    private int imageWidth;
    private int imageHeight;
    private List<Point> points;

    public SegmentationType getType() {
        return type;
    }

    public void setType(SegmentationType type) {
        this.type = type;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Boolean isValid() {
        return type.getValidator().validate(points, imageWidth, imageHeight);
    }

    // TODO: maybe not needed for all seg types
    public String onTouchUp() {
        return type.getValidator().onTouchUp();
    }
}
