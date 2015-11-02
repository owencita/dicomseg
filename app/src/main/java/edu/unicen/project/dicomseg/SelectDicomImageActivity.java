package edu.unicen.project.dicomseg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.imebra.dicom.CodecFactory;
import com.imebra.dicom.DataSet;
import com.imebra.dicom.Stream;
import com.imebra.dicom.StreamReader;

import java.io.File;

import edu.unicen.project.dicomseg.app.DicomSegApp;

public class SelectDicomImageActivity extends AppCompatActivity {

    private static final String TAG = "SelectDicomImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dicom_image);
        System.loadLibrary("imebra_lib");

        File dicomFile = (File) getIntent().getSerializableExtra("dicom");

        Stream stream = new Stream();
        stream.openFileRead(dicomFile.getAbsolutePath());

        // Build an internal representation of the Dicom file. Tags larger than 256 bytes
        //  will be loaded on demand from the file
        DataSet dataSet = CodecFactory.load(new StreamReader(stream), 256);
        DicomSegApp.setDataSet(dataSet);

        final Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = (EditText) findViewById(R.id.imageNumber);
                String imageNumber = editText.getText().toString();
                Intent intent = new Intent(view.getContext(), DicomViewActivity.class);
                intent.putExtra("imageNumber", new Integer(imageNumber));
                view.getContext().startActivity(intent);
            }
        });

    }

}
