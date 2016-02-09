package edu.unicen.project.dicomseg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imebra.dicom.DataSet;

import java.io.File;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.app.DicomSegApp;
import edu.unicen.project.dicomseg.dicom.DicomUtils;
import edu.unicen.project.dicomseg.models.Patient;

public class SelectDicomImageActivity extends AppCompatActivity {

    private static final String TAG = "SelectDicomImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dicom_image);

        final File dicomFile = (File) getIntent().getSerializableExtra("dicom");

        DataSet dataSet = DicomUtils.getDataSet(dicomFile);
        DicomSegApp.setDataSet(dataSet);

        Patient patient = DicomUtils.getPatient();

        if (patient.getId() != null) {
            TextView patientId = (TextView) findViewById(R.id.patientId);
            patientId.append(" " + patient.getId().toString());
        }
        if (patient.getName() != null) {
            TextView patientName = (TextView) findViewById(R.id.patientName);
            patientName.append(" " + patient.getName());
        }
        if (patient.getSex() != null) {
            TextView patientSex = (TextView) findViewById(R.id.patientSex);
            patientSex.append(" " + patient.getSex());
        }
        if (patient.getAge() != null) {
            TextView patientAge = (TextView) findViewById(R.id.patientAge);
            patientAge.append(" " + patient.getAge().toString());
        }
        if (patient.getBirthDate() != null) {
            TextView patientBirthday = (TextView) findViewById(R.id.patientBirthday);
            patientBirthday.append(" " + patient.getBirthDate().toString());
        }
        if (patient.getWeight() != null) {
            TextView patientWeight = (TextView) findViewById(R.id.patientWeight);
            patientWeight.append(" " + patient.getWeight().toString());
        }
        if (patient.getAddress() != null) {
            TextView patientAddress = (TextView) findViewById(R.id.patientAddress);
            patientAddress.append(" " + patient.getAddress());
        }

        final Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = (EditText) findViewById(R.id.imageNumber);
                String imageNumber = editText.getText().toString();
                Intent intent = new Intent(view.getContext(), DicomViewActivity.class);
                intent.putExtra("imageNumber", new Integer(imageNumber));
                intent.putExtra("fileName", dicomFile.getAbsolutePath());
                view.getContext().startActivity(intent);
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