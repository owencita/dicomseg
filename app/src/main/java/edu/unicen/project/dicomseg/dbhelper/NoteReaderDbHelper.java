package edu.unicen.project.dicomseg.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.unicen.project.dicomseg.contracts.DicomNoteContract;

public class NoteReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NoteReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DicomNoteContract.NoteEntry.TABLE_NAME + " (" +
                    DicomNoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER + INT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DicomNoteContract.NoteEntry.TABLE_NAME;

    public NoteReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
