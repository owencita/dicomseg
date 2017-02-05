package edu.unicen.project.dicomseg.app;

import android.app.Application;

import com.imebra.dicom.DataSet;

import java.util.List;

import edu.unicen.project.dicomseg.segmentation.Segmentation;

/**
 * DataSet from Imebra library does not implement Serializable (a requirement to pass it along activities)
 * The purpose of this class is to allow DataSet to be used along activities (turning it into a static variable)
 */
public class DicomSegApp extends Application {

    private static DataSet dataSet = null;
    private static List<Segmentation> adjutableSegmentations = null;

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
}