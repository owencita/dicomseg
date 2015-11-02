package edu.unicen.project.dicomseg.app;

import android.app.Application;

import com.imebra.dicom.DataSet;

public class DicomSegApp extends Application {

    // DataSet from Imebra library does not implement Serializable (a requirement to pass it along activities)
    private static DataSet dataSet = null;

    public static void setDataSet(DataSet dataSet) {
        DicomSegApp.dataSet = dataSet;
    }

    public static DataSet getDataSet() {
        return DicomSegApp.dataSet;
    }

}