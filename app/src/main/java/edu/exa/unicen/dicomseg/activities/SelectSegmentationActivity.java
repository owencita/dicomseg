package edu.exa.unicen.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;

public class SelectSegmentationActivity extends AppCompatActivity {

    public static final String TAG = "SelectSegmentationActivity";
    private SegmentationType segmentationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_segmentation);

        Button ivus = (Button) findViewById(R.id.ivusSeg);
        ivus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IVUSSegActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button brainTumor = (Button) findViewById(R.id.brainTumor);
        brainTumor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BrainTumorSegActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button retinography = (Button) findViewById(R.id.retinography);
        retinography.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RetinographySegActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button carotid = (Button) findViewById(R.id.carotid);
        carotid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CarotidSegActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Intent returnIntent = new Intent();
            if (resultCode == Activity.RESULT_OK) {
                segmentationType = (SegmentationType) data.getSerializableExtra("segmentationType");
                returnIntent.putExtra("segmentationType", segmentationType);
                returnIntent.putExtra("activity", TAG);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("activity", TAG);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
