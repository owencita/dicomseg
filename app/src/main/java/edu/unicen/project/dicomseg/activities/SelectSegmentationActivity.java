package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class SelectSegmentationActivity extends AppCompatActivity {

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                segmentationType = (SegmentationType) data.getSerializableExtra("segmentationType");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("segmentationType", segmentationType);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

}
