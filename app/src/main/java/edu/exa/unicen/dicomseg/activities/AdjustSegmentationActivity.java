package edu.exa.unicen.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.exa.unicen.dicomseg.R;
import edu.exa.unicen.dicomseg.adapters.ListedSegmentationArrayAdapter;
import edu.exa.unicen.dicomseg.app.DicomSegApp;
import edu.exa.unicen.dicomseg.segmentation.Segmentation;
import edu.exa.unicen.dicomseg.segmentation.SegmentationType;
import edu.exa.unicen.dicomseg.snakes.SnakeImage;

public class AdjustSegmentationActivity extends AppCompatActivity {

    public static final String TAG = "AdjustSegmentationActivity";

    private ListedSegmentationArrayAdapter adapter;
    private ListView listView;

    private SnakeImage snake = new SnakeImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_segmentation);

        listView = (ListView) findViewById(android.R.id.list);

        fill(DicomSegApp.getAdjutableSegmentations());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Segmentation segmentation = (Segmentation) listView.getItemAtPosition(position);
                DicomSegApp.setSegmentationToAdjust(segmentation);

                List<Point> snakePoints = snake.snakeImage(DicomSegApp.getDicomFrame(), segmentation.getPoints());

                Segmentation segmentationSnake = new Segmentation();
                segmentationSnake.setType(SegmentationType.SNAKE);
                segmentationSnake.setPoints(snakePoints);
                DicomSegApp.setSnake(segmentationSnake);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("activity", TAG);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void fill(List<Segmentation> segmentations) {
        adapter = new ListedSegmentationArrayAdapter(AdjustSegmentationActivity.this,
                R.layout.content_adjust_segmentation, segmentations);
        listView.setAdapter(adapter);
    }

}
