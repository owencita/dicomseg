package edu.unicen.project.dicomseg.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.contracts.DicomNoteContract;
import edu.unicen.project.dicomseg.dbhelper.NoteReaderDbHelper;
import edu.unicen.project.dicomseg.dicom.DicomUtils;

public class PointNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String fileName = (String) getIntent().getSerializableExtra("fileName");
        final Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        final int x = (int) getIntent().getSerializableExtra("x");
        final int y = (int) getIntent().getSerializableExtra("y");

        final NoteReaderDbHelper mDbHelper = new NoteReaderDbHelper(getBaseContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database will be returned
        String[] projection = {
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT,
        };

        Cursor cursor = db.query(
                DicomNoteContract.PointNoteEntry.TABLE_NAME,  // The table to query
                projection,     // The columns to return
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ? AND" +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_X + " = ? AND" +
                DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y + " = ?",// The columns for the WHERE clause
                new String[] { fileName, DicomUtils.getStudyUID(),
                        DicomUtils.getSeriesUID(),
                        Integer.toString(imageNumber),
                        String.valueOf(x),
                        String.valueOf(y)},     // The values for the WHERE clause
                null,          // don't group the rows
                null,          // don't filter by row groups
                null           // no sort order
        );

        String noteText = "";

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            noteText = cursor.getString(
                    cursor.getColumnIndexOrThrow(DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT)
            );
        }

        final EditText editText = (EditText) findViewById(R.id.noteText);
        editText.setText(noteText);
        final String textOnCreateActiv = editText.getText().toString();

        final Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();

                if ((textOnCreateActiv == null)||(textOnCreateActiv.isEmpty())) {

                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

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
                } else {
                    SQLiteDatabase db = mDbHelper.getReadableDatabase();

                    // New value for one column
                    ContentValues values = new ContentValues();
                    values.put(DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT, editText.getText().toString());

                    // Which row to update, based on the ID
                    String selection = DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME + " = ? AND " +
                            DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID + " = ? AND " +
                            DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID + " = ? AND " +
                            DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER + " = ? AND" +
                            DicomNoteContract.PointNoteEntry.COLUMN_NAME_X + " = ? AND" +
                            DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y + " = ?";
                    String[] selectionArgs = { fileName, DicomUtils.getStudyUID(), DicomUtils.getSeriesUID(),
                            Integer.toString(imageNumber), String.valueOf(x), String.valueOf(y)};
                    int count = db.update(
                            DicomNoteContract.NoteEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);
                }

                finish();
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
