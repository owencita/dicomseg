package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.segmentation.SegmentationsConstants;

public class SelectSegmentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_segmentation);

        Button ivus = (Button) findViewById(R.id.ivusSeg);
        ivus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationsConstants.IVUS);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

}