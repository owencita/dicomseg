package edu.exa.unicen.dicomseg.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.imebra.dicom.DataSet;

import java.util.List;

import edu.exa.unicen.dicomseg.segmentation.Segmentation;

/**
 * The classes of these variables do not implement Serializable (a requirement to pass them along activities)
 * The purpose of this class is to allow these variables to be used along activities (turning them into static variables)
 *
 * This class also allows to keep track of user settings, to be used outside activity classes
 */
public class DicomSegApp extends Application {

    private static DataSet dataSet = null;
    private static List<Segmentation> adjutableSegmentations = null;
    private static Bitmap dicomFrame = null;
    private static Segmentation segmentationToAdjust;
    private static Segmentation snake = null;

    // Variables to make setting available outside activity classes
    private static Integer clousureTolerance = null;

    public static void setDataSet(DataSet dataSet) {
        DicomSegApp.dataSet = dataSet;
    }

    public static DataSet getDataSet() {
        return DicomSegApp.dataSet;
    }

    public static List<Segmentation> getAdjutableSegmentations() {
        return DicomSegApp.adjutableSegmentations;
    }

    public static void setAdjutableSegmentations(List<Segmentation> adjutableSegmentations) {
        DicomSegApp.adjutableSegmentations = adjutableSegmentations;
    }

    public static Bitmap getDicomFrame() {
        return DicomSegApp.dicomFrame;
    }

    public static void setDicomFrame(Bitmap dicomFrame) {
        DicomSegApp.dicomFrame = dicomFrame;
    }

    public static Segmentation getSnake() {
        return DicomSegApp.snake;
    }

    public static void setSnake(Segmentation snake) {
        DicomSegApp.snake = snake;
    }

    public static Segmentation getSegmentationToAdjust() {
        return DicomSegApp.segmentationToAdjust;
    }

    public static void setSegmentationToAdjust(Segmentation segmentationToAdjust) {
        DicomSegApp.segmentationToAdjust = segmentationToAdjust;
    }

    public static Integer getClousureTolerance() {
        return DicomSegApp.clousureTolerance;
    }

    public static void setClousureTolerance(Integer clousureTolerance) {
        DicomSegApp.clousureTolerance = clousureTolerance;
    }
}