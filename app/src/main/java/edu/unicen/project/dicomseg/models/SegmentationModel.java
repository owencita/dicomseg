package edu.unicen.project.dicomseg.models;

import android.graphics.Point;
import java.util.List;

import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class SegmentationModel extends GenericModel {

    private String fileName;
    private String studyUID;
    private String seriesUID;
    private Integer imageNumber;
    private SegmentationType segmentationType;
    private List<Point> points;
    private Point referencePoint;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStudyUID() {
        return studyUID;
    }

    public void setStudyUID(String studyUID) {
        this.studyUID = studyUID;
    }

    public String getSeriesUID() {
        return seriesUID;
    }

    public void setSeriesUID(String seriesUID) {
        this.seriesUID = seriesUID;
    }

    public Integer getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(Integer imageNumber) {
        this.imageNumber = imageNumber;
    }

    public SegmentationType getSegmentationType() {
        return segmentationType;
    }

    public void setSegmentationType(SegmentationType segmentationType) {
        this.segmentationType = segmentationType;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public Point getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(Point referencePoint) {
        this.referencePoint = referencePoint;
    }
}
