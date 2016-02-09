package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dicom.DicomUtils;

public class DicomViewActivity extends Activity {

    private static final String TAG = "DicomViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom_view);

        final Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        Bitmap renderBitmap = DicomUtils.getFrame(imageNumber);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(renderBitmap);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NotesActivity.class);
                intent.putExtra("fileName", (String) getIntent().getSerializableExtra("fileName"));
                intent.putExtra("imageNumber", imageNumber);
                view.getContext().startActivity(intent);
            }
        });
    }

}
