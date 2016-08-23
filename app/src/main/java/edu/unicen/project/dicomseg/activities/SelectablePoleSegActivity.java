package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class SelectablePoleSegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectable_pole_seg);

        Button innerSelectablePole = (Button) findViewById(R.id.innerSelectablePole);
        innerSelectablePole.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.INNER_SELECTABLE_POLE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button outerSelectablePole = (Button) findViewById(R.id.outerSelectablePole);
        outerSelectablePole.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.OUTER_SELECTABLE_POLE);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
