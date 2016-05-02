package edu.unicen.project.dicomseg.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dbhelper.NoteReaderDbHelper;

public class GeneralNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        final String fileName = (String) getIntent().getSerializableExtra("fileName");
        final Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");

        final NoteReaderDbHelper mDbHelper = new NoteReaderDbHelper(getBaseContext());

        String noteText = mDbHelper.getGeneralNote(fileName, imageNumber);

        final EditText editText = (EditText) findViewById(R.id.noteText);
        editText.setText(noteText);
        final String textOnCreateActiv = editText.getText().toString();

        final Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();

                if ((textOnCreateActiv == null)||(textOnCreateActiv.isEmpty())) {
                    mDbHelper.insertGeneralNote(fileName, imageNumber, text);
                } else {
                    mDbHelper.updateGeneralNote(editText.getText().toString(), fileName, imageNumber);
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
