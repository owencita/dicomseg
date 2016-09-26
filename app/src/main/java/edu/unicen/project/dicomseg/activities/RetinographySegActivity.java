package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class RetinographySegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retinography_seg);

        Button opticDisc = (Button) findViewById(R.id.opticDisc);
        opticDisc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.OPTIC_DISC);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button vessels = (Button) findViewById(R.id.vessels);
        vessels.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.VESSELS);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
