package edu.unicen.project.dicomseg.app;

import android.app.Application;

import com.imebra.dicom.DataSet;

/**
 * DataSet from Imebra library does not implement Serializable (a requirement to pass it along activities)
 * The purpose of this class is to allow DataSet to be used along activities (turning it into a static variable)
 */
public class DicomSegApp extends Application {

    private static DataSet dataSet = null;

    public static void setDataSet(DataSet dataSet) {
        DicomSegApp.dataSet = dataSet;
    }

    public static DataSet getDataSet() {
        return DicomSegApp.dataSet;
    }

}