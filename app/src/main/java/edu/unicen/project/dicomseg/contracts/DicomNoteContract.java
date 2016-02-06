package edu.unicen.project.dicomseg.contracts;

import android.provider.BaseColumns;

public class DicomNoteContract {

    public DicomNoteContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NoteEntry implements BaseColumns {

        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_FILE_NAME = "filename";
        public static final String COLUMN_NAME_IMAGE_NUMBER = "imagenumber";
        public static final String COLUMN_NAME_TEXT = "text";
    }

}
