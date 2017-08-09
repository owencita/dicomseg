package edu.exa.unicen.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

public class BrainTumorSegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tumor_seg);

        Button necrosis = (Button) findViewById(R.id.brainTumorNecrosis);
        necrosis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.BRAIN_TUMOR_NECROSIS);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button border = (Button) findViewById(R.id.brainTumorBorder);
        border.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.BRAIN_TUMOR_BORDER);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
