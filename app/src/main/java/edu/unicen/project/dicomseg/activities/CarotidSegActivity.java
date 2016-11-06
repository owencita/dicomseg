package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class CarotidSegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carotid_seg);

        Button anteriorLI = (Button) findViewById(R.id.anteriorLI);
        anteriorLI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.CAROTID_LI_ANTERIOR);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button posteriorLI = (Button) findViewById(R.id.posteriorLI);
        posteriorLI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.CAROTID_LI_POSTERIOR);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button posteriorMA = (Button) findViewById(R.id.posteriorMA);
        posteriorMA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.CAROTID_MA_POSTERIOR);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
