package edu.exa.unicen.dicomseg.dbhelper.contracts;

import android.provider.BaseColumns;

public class DicomNoteContract {

    public DicomNoteContract() {}

    /* Inner class that defines the tables contents */

    public static abstract class NoteEntry implements BaseColumns {

        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_FILE_NAME = "filename";
        public static final String COLUMN_NAME_STUDY_UID = "studyUID";
        public static final String COLUMN_NAME_SERIES_UID = "seriesUID";
        public static final String COLUMN_NAME_IMAGE_NUMBER = "imagenumber";
        public static final String COLUMN_NAME_TEXT = "text";
    }

    public static abstract class PointNoteEntry implements BaseColumns {

        public static final String TABLE_NAME = "pointnote";
        public static final String COLUMN_NAME_FILE_NAME = "filename";
        public static final String COLUMN_NAME_STUDY_UID = "studyUID";
        public static final String COLUMN_NAME_SERIES_UID = "seriesUID";
        public static final String COLUMN_NAME_IMAGE_NUMBER = "imagenumber";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
        public static final String COLUMN_NAME_TEXT = "text";
    }

}
