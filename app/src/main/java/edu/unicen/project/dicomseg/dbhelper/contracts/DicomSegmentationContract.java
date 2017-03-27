package edu.unicen.project.dicomseg.dbhelper.contracts;

import android.provider.BaseColumns;

public class DicomSegmentationContract {

    public DicomSegmentationContract() {}

    public static abstract class Segmentation implements BaseColumns {

        public static final String TABLE_NAME = "segmentation";
        public static final String COLUMN_NAME_FILE_NAME = "filename";
        public static final String COLUMN_NAME_STUDY_UID = "studyUID";
        public static final String COLUMN_NAME_SERIES_UID = "seriesUID";
        public static final String COLUMN_NAME_IMAGE_NUMBER = "imagenumber";
        public static final String COLUMN_NAME_SEG_TYPE = "segmentationtype";
        public static final String COLUMN_NAME_POINTS = "points";
        public static final String COLUMN_NAME_REFERENCE_POINT = "referencePoint";
    }
}
