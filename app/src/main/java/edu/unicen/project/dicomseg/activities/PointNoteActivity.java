package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dbhelper.DbHelper;

public class PointNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        final String fileName = (String) getIntent().getSerializableExtra("fileName");
        final Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        final int x = (int) getIntent().getSerializableExtra("x");
        final int y = (int) getIntent().getSerializableExtra("y");

        final DbHelper mDbHelper = new DbHelper(getBaseContext());

        String noteText = mDbHelper.getPointNote(fileName, imageNumber, x, y);

        final EditText editText = (EditText) findViewById(R.id.noteText);
        editText.setText(noteText);
        final String textOnCreateActiv = editText.getText().toString();

        final Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();

                if ((textOnCreateActiv == null)||(textOnCreateActiv.isEmpty())) {
                    mDbHelper.insertPointNote(fileName, imageNumber, x, y, text);
                } else {
                    mDbHelper.updatePointNote(editText.getText().toString(), fileName, imageNumber, x, y);
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("refreshPointNotes", Boolean.TRUE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("refreshPointNotes", Boolean.FALSE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

}
