package edu.unicen.project.dicomseg.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.imebra.dicom.DataSet;

import java.util.List;

import edu.unicen.project.dicomseg.segmentation.Segmentation;

/**
 * These classes of these variables do not implement Serializable (a requirement to pass them along activities)
 * The purpose of this class is to allow these variables to be used along activities (turning them into static variables)
 */
public class DicomSegApp extends Application {

    private static DataSet dataSet = null;
    private static List<Segmentation> adjutableSegmentations = null;
    private static Bitmap dicomFrame = null;
    private static Segmentation snake = null;

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
}