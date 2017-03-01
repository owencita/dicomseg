package edu.unicen.project.dicomseg.activities;

import android.app.ListActivity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.adapters.ListedSegmentationArrayAdapter;
import edu.unicen.project.dicomseg.app.DicomSegApp;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.snakes.SnakeImage;

public class AdjustSegmentationActivity extends ListActivity {

    private ListedSegmentationArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_segmentation);

        final ListView listView = (ListView) findViewById(android.R.id.list);

        fill(DicomSegApp.getAdjutableSegmentations());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Segmentation segmentation = (Segmentation) listView.getItemAtPosition(position);
                List<Point> snake = SnakeImage.snakeImage(DicomSegApp.getDicomFrame(), segmentation.getPoints());
                snake.size();
            }
        });
    }

    private void fill(List<Segmentation> segmentations) {
        adapter = new ListedSegmentationArrayAdapter(AdjustSegmentationActivity.this,
                R.layout.content_adjust_segmentation, segmentations);
        this.setListAdapter(adapter);
    }

}
