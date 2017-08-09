package edu.exa.unicen.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

public class IVUSSegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivusseg);

        Button ivusLI = (Button) findViewById(R.id.ivusLI);
        ivusLI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.IVUS_LI);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button ivusMA = (Button) findViewById(R.id.ivusMA);
        ivusMA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", SegmentationType.IVUS_MA);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
