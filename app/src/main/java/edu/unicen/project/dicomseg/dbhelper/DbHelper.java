package edu.unicen.project.dicomseg.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.unicen.project.dicomseg.dbhelper.contracts.DicomNoteContract;
import edu.unicen.project.dicomseg.dbhelper.contracts.DicomSegmentationContract;
import edu.unicen.project.dicomseg.dicom.DicomUtils;
import edu.unicen.project.dicomseg.models.GenericModel;
import edu.unicen.project.dicomseg.models.NoteModel;
import edu.unicen.project.dicomseg.models.PointNoteModel;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Dicomseg.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";

    private static final String GENERAL_NOTE_CREATE_ENTRIES =
            "CREATE TABLE " + DicomNoteContract.NoteEntry.TABLE_NAME + " (" +
                    DicomNoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER + INT_TYPE + COMMA_SEP +
                    DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT + TEXT_TYPE +
                    " )";

    private static final String POINT_NOTE_CREATE_ENTRIES =
            "CREATE TABLE " + DicomNoteContract.PointNoteEntry.TABLE_NAME + " (" +
                    DicomNoteContract.PointNoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID + TEXT_TYPE + COMMA_SEP +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER + INT_TYPE + COMMA_SEP +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_X + INT_TYPE + COMMA_SEP +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y + INT_TYPE + COMMA_SEP +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT + TEXT_TYPE +
                    " )";

    private static final String SEGMENTATION_CREATE_ENTRIES =
            "CREATE TABLE " + DicomSegmentationContract.Segmentation.TABLE_NAME + " (" +
                    DicomSegmentationContract.Segmentation._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME + TEXT_TYPE + COMMA_SEP +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_STUDY_UID + TEXT_TYPE + COMMA_SEP +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_SERIES_UID + TEXT_TYPE + COMMA_SEP +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER + INT_TYPE + COMMA_SEP +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE + TEXT_TYPE + COMMA_SEP +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS + TEXT_TYPE + COMMA_SEP +
                    DicomSegmentationContract.Segmentation.COLUMN_NAME_REFERENCE_POINT + TEXT_TYPE +
                    " )";

    private static final String GENERAL_NOTE_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DicomNoteContract.NoteEntry.TABLE_NAME;

    private static final String POINT_NOTE_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DicomNoteContract.PointNoteEntry.TABLE_NAME;

    private static final String SEGMENTATION_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DicomSegmentationContract.Segmentation.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GENERAL_NOTE_CREATE_ENTRIES);
        db.execSQL(POINT_NOTE_CREATE_ENTRIES);
        db.execSQL(SEGMENTATION_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(GENERAL_NOTE_DELETE_ENTRIES);
        db.execSQL(POINT_NOTE_DELETE_ENTRIES);
        db.execSQL(SEGMENTATION_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public String getGeneralNote(String fileName, Integer imageNumber) {

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database will be returned
        String[] projection = {
            DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT,
        };

        Cursor cursor = db.query(
            // The table to query
            DicomNoteContract.NoteEntry.TABLE_NAME,
            // The columns to return
            projection,
            // The columns for the WHERE clause
            DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
            DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
            DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
            DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ?",
            // The values for the WHERE clause
            new String[]{fileName, DicomUtils.getStudyUID(),
                    DicomUtils.getSeriesUID(),
                    Integer.toString(imageNumber)},
            null, // don't group the rows
            null, // don't filter by row groups
            null  // no sort order
        );

        String noteText = "";

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            noteText = cursor.getString(
                cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT)
            );
        }

        return noteText;
    }

    public List<GenericModel> getAllNotes() {
        List<GenericModel> notes = new ArrayList<GenericModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DicomNoteContract.NoteEntry.TABLE_NAME, null);
        if (cursor.getCount() > 0 ) {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry._ID));
                    String fileName = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME));
                    String studyUID = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID));
                    String seriesUID = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID));
                    Integer imageNumber = cursor.getInt(cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER));
                    String text = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT));
                    NoteModel note = new NoteModel();
                    note.setId(id);
                    note.setFileName(fileName);
                    note.setStudyUID(studyUID);
                    note.setSeriesUID(seriesUID);
                    note.setImageNumber(imageNumber);
                    note.setText(text);
                    notes.add(note);
                    cursor.moveToNext();
                }
            }
        }
        return notes;
    }

    public String getPointNote(String fileName, Integer imageNumber, int x, int y) {

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database will be returned
        String[] projection = {
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT,
        };

        Cursor cursor = db.query(
            // The table to query
            DicomNoteContract.PointNoteEntry.TABLE_NAME,
            // The columns to return
            projection,
            // The columns for the WHERE clause
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ? AND " +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_X + " = ? AND " +
                    DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y + " = ?",
            // The values for the WHERE clause
            new String[]{fileName, DicomUtils.getStudyUID(),
                    DicomUtils.getSeriesUID(),
                    Integer.toString(imageNumber),
                    String.valueOf(x),
                    String.valueOf(y)},
            null, // don't group the rows
            null, // don't filter by row groups
            null  // no sort order
        );

        String noteText = "";

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            noteText = cursor.getString(
                cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT)
            );
        }

        return noteText;
    }

    public List<Point> getAllPointNotes(String fileName, Integer imageNumber) {

        List<Point> points = new ArrayList<Point>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database will be returned
        String[] projection = {
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_X,
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y,
        };

        Cursor cursor = db.query(
            // The table to query
            DicomNoteContract.PointNoteEntry.TABLE_NAME,
            // The columns to return
            projection,
            // The columns for the WHERE clause
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
            DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ?",
            // The values for the WHERE clause
            new String[] { fileName, DicomUtils.getStudyUID(),
                    DicomUtils.getSeriesUID(),
                    Integer.toString(imageNumber)},
            null, // don't group the rows
            null, // don't filter by row groups
            null  // no sort order
        );

        if (cursor.moveToFirst()) {
            do {
                Point point = new Point();
                point.x = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_X)));
                point.y = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y)));
                points.add(point);
            } while (cursor.moveToNext());
        }

        return points;
    }

    public List<GenericModel> getAllPointNotes() {
        List<GenericModel> pointNotes = new ArrayList<GenericModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DicomNoteContract.PointNoteEntry.TABLE_NAME, null);
        if (cursor.getCount() > 0 ) {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    String fileName = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME));
                    String studyUID = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID));
                    String seriesUID = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID));
                    Integer imageNumber = cursor.getInt(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER));
                    Integer x = cursor.getInt(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_X));
                    Integer y = cursor.getInt(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y));
                    String text = cursor.getString(cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT));
                    PointNoteModel pointNote = new PointNoteModel();
                    pointNote.setFileName(fileName);
                    pointNote.setStudyUID(studyUID);
                    pointNote.setSeriesUID(seriesUID);
                    pointNote.setImageNumber(imageNumber);
                    pointNote.setX(x);
                    pointNote.setY(y);
                    pointNote.setText(text);
                    pointNotes.add(pointNote);
                    cursor.moveToNext();
                }
            }
        }
        return pointNotes;
    }

    public List<Point> getSegmentation(String fileName, Integer imageNumber) {

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database will be returned
        String[] projection = {
            DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS,
        };

        Cursor cursor = db.query(
                // The table to query
                DicomSegmentationContract.Segmentation.TABLE_NAME,
                // The columns to return
                projection,
                // The columns for the WHERE clause
                DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME + " = ? AND " +
                        DicomSegmentationContract.Segmentation.COLUMN_NAME_STUDY_UID + " = ? AND " +
                        DicomSegmentationContract.Segmentation.COLUMN_NAME_SERIES_UID + " = ? AND " +
                        DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER + " = ?",
                // The values for the WHERE clause
                new String[]{fileName, DicomUtils.getStudyUID(),
                        DicomUtils.getSeriesUID(),
                        Integer.toString(imageNumber)},
                null, // don't group the rows
                null, // don't filter by row groups
                null  // no sort order
        );

        String jsonPoints = "";
        List<Point> points = null;

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            jsonPoints = cursor.getString(cursor.getColumnIndexOrThrow(DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS));
        }

        if (jsonPoints != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Point>>() {}.getType();
            points = gson.fromJson(jsonPoints, type);
        }

        return points;
    }

    public List<Segmentation> getSegmentations(String fileName, Integer imageNumber) {

        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database will be returned
        String[] projection = {
                DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE,
                DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS,
                DicomSegmentationContract.Segmentation.COLUMN_NAME_REFERENCE_POINT,
        };

        Cursor cursor = db.query(
                // The table to query
                DicomSegmentationContract.Segmentation.TABLE_NAME,
                // The columns to return
                projection,
                // The columns for the WHERE clause
                DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME + " = ? AND " +
                        DicomSegmentationContract.Segmentation.COLUMN_NAME_STUDY_UID + " = ? AND " +
                        DicomSegmentationContract.Segmentation.COLUMN_NAME_SERIES_UID + " = ? AND " +
                        DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER + " = ?",
                // The values for the WHERE clause
                new String[] { fileName, DicomUtils.getStudyUID(),
                        DicomUtils.getSeriesUID(),
                        Integer.toString(imageNumber) },
                null, // don't group the rows
                null, // don't filter by row groups
                null  // no sort order
        );

        List<Segmentation> segmentations = new ArrayList<Segmentation>();

        if (cursor.moveToFirst()) {
            do {
                Segmentation segmentation = new Segmentation();

                segmentation.setType(SegmentationType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE))));

                String jsonPoints = cursor.getString(cursor.getColumnIndexOrThrow(DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS));
                if (jsonPoints != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Point>>() {}.getType();
                    List<Point> points = new ArrayList<Point>();
                    points = gson.fromJson(jsonPoints, type);
                    segmentation.setPoints(points);
                }

                String jsonPole = cursor.getString(cursor.getColumnIndexOrThrow(DicomSegmentationContract.Segmentation.COLUMN_NAME_REFERENCE_POINT));
                if (jsonPole != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<Point>() {}.getType();
                    Point point = new Point();
                    point = gson.fromJson(jsonPole, type);
                    segmentation.setReferencePoint(point);
                }

                segmentations.add(segmentation);
            } while (cursor.moveToNext());
        }

        return segmentations;
    }

    public void insertGeneralNote(String fileName, Integer imageNumber, String text) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME, fileName);
        values.put(DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER, imageNumber);
        values.put(DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID, DicomUtils.getStudyUID());
        values.put(DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID, DicomUtils.getSeriesUID());
        values.put(DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT, text);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DicomNoteContract.NoteEntry.TABLE_NAME,
                null,
                values);
    }

    public void insertPointNote(String fileName, Integer imageNumber, int x, int y, String text) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME, fileName);
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER, imageNumber);
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID, DicomUtils.getStudyUID());
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID, DicomUtils.getSeriesUID());
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_X, x);
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y, y);
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT, text);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DicomNoteContract.PointNoteEntry.TABLE_NAME,
                null,
                values);
    }

    public void insertSegmentation(String fileName, Integer imageNumber, SegmentationType segType, String points, String referencePoint) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME, fileName);
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER, imageNumber);
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_STUDY_UID, DicomUtils.getStudyUID());
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_SERIES_UID, DicomUtils.getSeriesUID());
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE, segType.toString());
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS, points);
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_REFERENCE_POINT, referencePoint);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DicomSegmentationContract.Segmentation.TABLE_NAME,
                null,
                values);
    }

    public void updateGeneralNote(String text, String fileName, Integer imageNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT, text);

        // Which row to update, based on the ID
        String selection = DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
                DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
                DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
                DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ?";
        String[] selectionArgs = { fileName, DicomUtils.getStudyUID(), DicomUtils.getSeriesUID(),
                Integer.toString(imageNumber)};
        int count = db.update(
                DicomNoteContract.NoteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void updatePointNote(String text, String fileName, Integer imageNumber, int x, int y) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT, text);

        // Which row to update, based on the ID
        String selection = DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_X + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y + " = ?";
        String[] selectionArgs = { fileName, DicomUtils.getStudyUID(), DicomUtils.getSeriesUID(),
                Integer.toString(imageNumber), String.valueOf(x), String.valueOf(y)};
        int count = db.update(
                DicomNoteContract.NoteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void updateSegmentation(String fileName, Integer imageNumber, SegmentationType segType, String points) {
        SQLiteDatabase db = this.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS, points);

        // Which row to update, based on the ID
        String selection = DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME + " = ? AND " +
                DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER + " = ? AND " +
                DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE + " = ?";

        String[] selectionArgs = { fileName, Integer.toString(imageNumber), segType.toString() };

        int count = db.update(
                DicomSegmentationContract.Segmentation.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
