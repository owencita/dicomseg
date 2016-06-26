package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dbhelper.DbHelper;
import edu.unicen.project.dicomseg.dicom.DicomUtils;
import edu.unicen.project.dicomseg.listeners.GestureListener;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationColors;
import edu.unicen.project.dicomseg.segmentation.SegmentationDrawingUtils;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;

public class DicomViewActivity extends Activity {

    private static final String TAG = "DicomViewActivity";
    private GestureDetector gestureDetector;
    private DbHelper dbHelper;

    private Path segPath;
    private Path accumSegPath;
    private Paint segPaint;
    private Point inputStart;
    private Point inputEnd;

    private String fileName;
    private Integer imageNumber;
    private ImageView imageView;

    private Segmentation segmentation = new Segmentation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom_view);

        gestureDetector = new GestureDetector(this, new GestureListener());
        dbHelper = new DbHelper(getBaseContext());

        fileName = (String) getIntent().getSerializableExtra("fileName");

        imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        final Bitmap dicomFrame = DicomUtils.getFrame(imageNumber);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dicomFrame);

        final Bitmap mutableBitmap = dicomFrame.copy(Bitmap.Config.ARGB_8888, true);
        final ImageView mutableImageView = (ImageView) findViewById(R.id.mutableImageView);
        mutableImageView.setImageBitmap(mutableBitmap);
        final Canvas canvas = new Canvas(mutableBitmap);

        mutableImageView.bringToFront();
        imageView.invalidate();

        FloatingActionButton generalNoteButton = (FloatingActionButton) findViewById(R.id.menu_general_note);
        generalNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GeneralNoteActivity.class);
                intent.putExtra("fileName", fileName);
                intent.putExtra("imageNumber", imageNumber);
                view.getContext().startActivity(intent);
            }
        });

        FloatingActionButton pointNoteButton = (FloatingActionButton) findViewById(R.id.menu_point_note);
        pointNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
                menu.hideMenu(true);
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Tap the image point where you want to add a note");

                List<Point> pointList = dbHelper.getAllPointNotes(fileName, imageNumber);

                for (Point point : pointList) {
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(SegmentationColors.BLUE);
                    paint.setStyle(Paint.Style.FILL);
                    // TODO: scale circle accordingly (to the image, which is automatically scaled to fit screen)
                    canvas.drawCircle(point.x, point.y, 8, paint);
                }

                view.invalidate();

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (gestureDetector.onTouchEvent(event)) {

                            float[] coords = getCoordsForCanvas(event, imageView);
                            int x = (int) coords[0];
                            int y = (int) coords[1];

                            Intent intent = new Intent(view.getContext(), PointNoteActivity.class);
                            intent.putExtra("fileName", fileName);
                            intent.putExtra("imageNumber", imageNumber);
                            intent.putExtra("x", x);
                            intent.putExtra("y", y);
                            view.getContext().startActivity(intent);

                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                // TODO: refresh screen to draw point notes after a new point note is created/updated
            }
        });

        final FloatingActionButton segmentImageButton = (FloatingActionButton) findViewById(R.id.menu_segment);
        segmentImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                hideMenu();

                inputStart = null;
                inputEnd = null;
                final AtomicBoolean previousPathAdded = new AtomicBoolean(false);

                Intent intent = new Intent(view.getContext(), SelectSegmentationActivity.class);
                startActivityForResult(intent, 1);

                accumSegPath = new Path();
                segPath = new Path();

                segmentation.setImageWidth(dicomFrame.getWidth());
                segmentation.setImageHeight(dicomFrame.getHeight());

                List<Segmentation> segmentations = dbHelper.getSegmentations(fileName, imageNumber);
                segmentation.setRelatedSegmentations(segmentations);

                if (!segmentations.isEmpty()) {
                    for (Segmentation segmentation: segmentations) {
                        List<Point> points = segmentation.getPoints();
                        Path segPath = SegmentationDrawingUtils.setPathFromPointList(points, canvas);
                        Paint segPaint = SegmentationDrawingUtils.getPaint(dicomFrame.getWidth(), dicomFrame.getHeight(), SegmentationDrawingUtils.getColor());
                        canvas.drawPath(segPath, segPaint);
                        view.invalidate();
                    }
                }

                segPaint = SegmentationDrawingUtils.getPaint(dicomFrame.getWidth(), dicomFrame.getHeight(), SegmentationDrawingUtils.getColor());

                Button doneButton = (Button) findViewById(R.id.done);
                doneButton.setVisibility(View.VISIBLE);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button doneButton = (Button) findViewById(R.id.done);
                        doneButton.setVisibility(View.GONE);

                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText("");

                        if (segmentation.isValid()) {
                            // save segmentation
                            imageView.setOnTouchListener(null);
                            Gson gson = new Gson();
                            String segmentationPointsString = gson.toJson(segmentation.getPoints());
                            dbHelper.insertSegmentation(fileName, imageNumber, segmentation.getType(), segmentationPointsString);
                            textView.setText("");
                            showMenu();
                        } else {
                            StringBuffer sb = new StringBuffer();
                            for (String error: segmentation.errors()) {
                                sb.append(error + "\n\r");
                            }
                            textView.setText(sb.toString());
                        }
                    }
                });

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        float[] coords = getCoordsForCanvas(event, imageView);
                        int x = (int) coords[0];
                        int y = (int) coords[1];

                        if (inputStart == null) {
                            inputStart = new Point();
                            inputStart.x = x;
                            inputStart.y = y;
                        }

                        if ((inputEnd == null)||SegmentationDrawingUtils.isEnd(inputStart, inputEnd, x, y)) {
                            // user never touched up or touched up previously
                            inputEnd = SegmentationDrawingUtils.setPathFromTouchEvent(segPath, canvas, view, event, x, y);
                            Point point = new Point();
                            point.x = x;
                            point.y = y;
                            segmentation.getPoints().add(point);
                            canvas.drawPath(segPath, segPaint);
                            previousPathAdded.set(false);
                        } else {
                            // user touched up
                            if ((inputEnd != null) && !previousPathAdded.get()) {
                                accumSegPath.addPath(segPath);
                                segPath = new Path();
                                Point start = segmentation.getPoints().get(0);
                                segPath.moveTo(start.x, start.y);
                                previousPathAdded.set(true);
                                TextView textView = (TextView) findViewById(R.id.textView);
                                textView.setText(SegmentationMessages.CONTINUITY_ERROR);
                            }
                        }

                        canvas.drawPath(segPath, segPaint);
                        return true;
                    }
                });
            }
        });

        FloatingActionButton showSegmentationImageButton = (FloatingActionButton) findViewById(R.id.menu_show_segment);
        showSegmentationImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                hideMenu();

                List<Segmentation> segmentations = dbHelper.getSegmentations(fileName, imageNumber);

                if (!segmentations.isEmpty()) {
                    for (Segmentation segmentation: segmentations) {
                        List<Point> points = segmentation.getPoints();
                        segPath = SegmentationDrawingUtils.setPathFromPointList(points, canvas);
                        segPaint = SegmentationDrawingUtils.getPaint(dicomFrame.getWidth(), dicomFrame.getHeight(), SegmentationDrawingUtils.getColor());
                        canvas.drawPath(segPath, segPaint);
                        view.invalidate();
                    }
                }

                Button doneButton = (Button) findViewById(R.id.done);
                doneButton.setVisibility(View.VISIBLE);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button doneButton = (Button) findViewById(R.id.done);
                        doneButton.setVisibility(View.GONE);
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        showMenu();
                    }
                });
            }
        });
    }

    private float[] getCoordsForCanvas(MotionEvent event, ImageView imageView) {
        final int index = event.getActionIndex();
        final float[] coords = new float[] { event.getX(index), event.getY(index) };
        Matrix matrix = new Matrix();
        imageView.getImageMatrix().invert(matrix);
        matrix.postTranslate(imageView.getScrollX(), imageView.getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                segmentation.setType((SegmentationType)data.getSerializableExtra("segmentationType"));
            }
        }

        TextView textInfo = (TextView) findViewById(R.id.textInfo);
        textInfo.setText("Segmenting: " + segmentation.getType().getName());

        if (segmentation.isContained(segmentation.getRelatedSegmentations())) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(SegmentationMessages.EXISTING_SEGMENTATION_ERROR);
            imageView.setOnTouchListener(null);
            showMenu();
        }
    }

    private void hideMenu() {
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.hideMenu(true);
        menu.setVisibility(View.GONE);
        FloatingActionButton generalNoteButton = (FloatingActionButton) findViewById(R.id.menu_general_note);
        generalNoteButton.setVisibility(View.GONE);
        FloatingActionButton pointNoteButton = (FloatingActionButton) findViewById(R.id.menu_point_note);
        pointNoteButton.setVisibility(View.GONE);
        FloatingActionButton segmentImageButton = (FloatingActionButton) findViewById(R.id.menu_segment);
        segmentImageButton.setVisibility(View.GONE);
    }

    private void showMenu() {
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.close(true);
        menu.setVisibility(View.VISIBLE);
    }

}
