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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dbhelper.DbHelper;
import edu.unicen.project.dicomseg.dicom.DicomUtils;
import edu.unicen.project.dicomseg.listeners.GestureListener;
import edu.unicen.project.dicomseg.segmentation.Segmentation;
import edu.unicen.project.dicomseg.segmentation.SegmentationDrawingUtils;
import edu.unicen.project.dicomseg.segmentation.SegmentationMessages;
import edu.unicen.project.dicomseg.segmentation.SegmentationType;
import edu.unicen.project.dicomseg.segmentation.SegmentationUtils;

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

    private Bitmap dicomFrame;
    private Canvas canvas;

    private Point referencePoint;

    private Segmentation segmentation = new Segmentation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom_view);

        gestureDetector = new GestureDetector(this, new GestureListener());
        dbHelper = new DbHelper(getBaseContext());

        fileName = (String) getIntent().getSerializableExtra("fileName");

        imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        dicomFrame = DicomUtils.getFrame(imageNumber);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dicomFrame);

        final Bitmap mutableBitmap = dicomFrame.copy(Bitmap.Config.ARGB_8888, true);
        final ImageView mutableImageView = (ImageView) findViewById(R.id.mutableImageView);
        mutableImageView.setImageBitmap(mutableBitmap);
        canvas = new Canvas(mutableBitmap);

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
                hideMenu();
                TextView textInfo = (TextView) findViewById(R.id.textInfo);
                textInfo.setText(getResources().getString(R.string.pointnote_adding));
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(getResources().getString(R.string.pointnote_tap_to_add));

                refreshPointNote();

                Button okButton = (Button) findViewById(R.id.ok);
                okButton.setVisibility(View.VISIBLE);

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
                            startActivityForResult(intent, 1);

                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button okButton = (Button) findViewById(R.id.ok);
                        okButton.setVisibility(View.GONE);
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        imageView.setOnTouchListener(null);
                        showMenu();
                    }
                });
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

                Button doneButton = (Button) findViewById(R.id.done);
                doneButton.setEnabled(false);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button doneButton = (Button) findViewById(R.id.done);
                        Button clearButton = (Button) findViewById(R.id.clear);
                        TextView textView = (TextView) findViewById(R.id.textView);

                        if (!segmentation.getPoints().isEmpty()) {
                            if (segmentation.isValid()) {
                                // hide done, clear buttons, messages and clear canvas
                                doneButton.setVisibility(View.GONE);
                                clearButton.setVisibility(View.GONE);
                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                imageView.setOnTouchListener(null);
                                // save segmentation
                                saveSegmentation();
                                // show menu
                                showMenu();
                            } else {
                                // show validation error
                                StringBuffer sb = new StringBuffer();
                                for (String error : segmentation.errors()) {
                                    sb.append(error + "\n\r");
                                }
                                textView.setText(sb.toString());
                            }
                        } else {
                            doneButton.setEnabled(false);
                            textView.setText(SegmentationMessages.EMPTY_SEGMENTATION_ERROR);
                        }
                    }
                });

                Button clearButton = (Button) findViewById(R.id.clear);
                clearButton.setOnClickListener(new View.OnClickListener() {
                    // clear canvas from newly drawn segmentation and reset path
                    @Override
                    public void onClick(View view) {
                        inputStart = null;
                        inputEnd = null;
                        previousPathAdded.set(false);
                        accumSegPath = new Path();
                        segPath = new Path();
                        segmentation.clearPoints();

                        TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText("");

                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        drawSegmentationsOnFrame();

                        Button doneButton = (Button) findViewById(R.id.done);
                        doneButton.setEnabled(false);

                        view.invalidate();
                    }
                });

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        Button doneButton = (Button) findViewById(R.id.done);
                        if (!doneButton.isEnabled()) {
                            doneButton.setEnabled(true);
                        }

                        float[] coords = getCoordsForCanvas(event, imageView);
                        int x = (int) coords[0];
                        int y = (int) coords[1];

                        if (inputStart == null) {
                            inputStart = new Point();
                            inputStart.x = x;
                            inputStart.y = y;
                        }

                        if ((inputEnd == null) || SegmentationDrawingUtils.isEnd(inputStart, inputEnd, x, y)) {
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
                                if (!segmentation.getType().allowsRepeats()) {
                                    accumSegPath.addPath(segPath);
                                    segPath = new Path();
                                    Point start = segmentation.getPoints().get(0);
                                    segPath.moveTo(start.x, start.y);
                                    previousPathAdded.set(true);
                                    TextView textView = (TextView) findViewById(R.id.textView);
                                    textView.setText(SegmentationMessages.CONTINUITY_ERROR);
                                } else {
                                    inputStart = null;
                                    inputEnd = null;
                                    saveSegmentation();
                                    segPath = new Path();
                                    SegmentationDrawingUtils.setPathFromTouchEvent(segPath, canvas, view, event, x, y);
                                    segmentation.clearPoints();
                                }
                            }
                        }

                        canvas.drawPath(segPath, segPaint);
                        return true;
                    }
                });
            }
        });

        FloatingActionButton adjustSegmentationButton = (FloatingActionButton) findViewById(R.id.menu_adjust);
        adjustSegmentationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GeneralNoteActivity.class);
                List<Segmentation> segmentations = dbHelper.getSegmentations(fileName, imageNumber);
                ArrayList<Segmentation> adjustableSegmentations = SegmentationUtils.getAdjustableSegmentations(segmentations);
                intent.putParcelableArrayListExtra("existingAdjustableSegmentations", adjustableSegmentations);
                view.getContext().startActivity(intent);
            }
        });

        FloatingActionButton showSegmentationImageButton = (FloatingActionButton) findViewById(R.id.menu_show_segment);
        showSegmentationImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                hideMenu();
                drawSegmentationsOnFrame();

                Button okButton = (Button) findViewById(R.id.ok);
                okButton.setVisibility(View.VISIBLE);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button okButton = (Button) findViewById(R.id.ok);
                        okButton.setVisibility(View.GONE);
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
        switch (requestCode) {
            case 1:
                String activity = (String) data.getSerializableExtra("activity");
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        switch (activity) {
                            case SelectSegmentationActivity.TAG:

                                // Draw existing segmentations in the same frame
                                final List<Segmentation> segmentations = drawSegmentationsOnFrame();

                                SegmentationType segType = (SegmentationType) data.getSerializableExtra("segmentationType");

                                if (segType != null) {

                                    segPaint = SegmentationDrawingUtils.getPaint(dicomFrame.getWidth(), segType.getColor());
                                    segmentation.setType(segType);
                                    List<Segmentation> relatedSegs = SegmentationUtils.getRelatedSegmentations(segmentations, segmentation.getType());
                                    segmentation.setExistingRelatedSegmentations(relatedSegs);
                                    segmentation.clearPoints();

                                    if (segmentation.isContained(segmentations) && !segmentation.getType().allowsRepeats()) {
                                        TextView textInfo = (TextView) findViewById(R.id.textInfo);
                                        textInfo.setText("");
                                        TextView textView = (TextView) findViewById(R.id.textView);
                                        textView.setText(SegmentationMessages.EXISTING_SEGMENTATION_ERROR);

                                        imageView.setOnTouchListener(null);

                                        hideDoneAndClearButtons();

                                        Button okButton = (Button) findViewById(R.id.ok);
                                        okButton.setVisibility(View.VISIBLE);
                                        okButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Button okButton = (Button) findViewById(R.id.ok);
                                                okButton.setVisibility(View.GONE);
                                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                                showMenu();
                                            }
                                        });
                                    } else {

                                        TextView textInfo = (TextView) findViewById(R.id.textInfo);
                                        textInfo.setText("Segmenting: " + segmentation.getType().getName());

                                        if (!segType.isReferencePointSelectable()) {
                                            Point pole = new Point(dicomFrame.getWidth()/2, dicomFrame.getHeight()/2);
                                            referencePoint = null;
                                            segmentation.setReferencePoint(pole);
                                            showDoneAndClearButtons();
                                        } else {
                                            if (relatedSegs.isEmpty()) {
                                                TextView textView = (TextView) findViewById(R.id.textView);
                                                textView.setText(getResources().getString(R.string.dicomview_select_reference_point) + " "
                                                                + segmentation.getType().getReferencePointHint());

                                                imageView.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View view, MotionEvent event) {
                                                        if (gestureDetector.onTouchEvent(event)) {

                                                            float[] coords = getCoordsForCanvas(event, imageView);
                                                            int x = (int) coords[0];
                                                            int y = (int) coords[1];

                                                            referencePoint = new Point(x, y);
                                                            segmentation.setReferencePoint(referencePoint);

                                                            drawPoint(x, y);

                                                            imageView.setOnTouchListener(null);

                                                            Button okButton = (Button) findViewById(R.id.ok);
                                                            okButton.setVisibility(View.VISIBLE);

                                                            return true;
                                                        } else {
                                                            return false;
                                                        }
                                                    }
                                                });

                                                Button okButton = (Button) findViewById(R.id.ok);
                                                okButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        TextView textView = (TextView) findViewById(R.id.textView);
                                                        textView.setText("");

                                                        Button okButton = (Button) findViewById(R.id.ok);
                                                        okButton.setVisibility(View.GONE);
                                                        showDoneAndClearButtons();

                                                        final AtomicBoolean previousPathAdded = new AtomicBoolean(false);

                                                        // TODO: move this listener implementation to a variable if possible
                                                        imageView.setOnTouchListener(new View.OnTouchListener() {
                                                            @Override
                                                            public boolean onTouch(View view, MotionEvent event) {

                                                                Button doneButton = (Button) findViewById(R.id.done);
                                                                if (!doneButton.isEnabled()) {
                                                                    doneButton.setEnabled(true);
                                                                }

                                                                float[] coords = getCoordsForCanvas(event, imageView);
                                                                int x = (int) coords[0];
                                                                int y = (int) coords[1];

                                                                if (inputStart == null) {
                                                                    inputStart = new Point();
                                                                    inputStart.x = x;
                                                                    inputStart.y = y;
                                                                }

                                                                if ((inputEnd == null) || SegmentationDrawingUtils.isEnd(inputStart, inputEnd, x, y)) {
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
                                                                        if (!segmentation.getType().allowsRepeats()) {
                                                                            accumSegPath.addPath(segPath);
                                                                            segPath = new Path();
                                                                            Point start = segmentation.getPoints().get(0);
                                                                            segPath.moveTo(start.x, start.y);
                                                                            previousPathAdded.set(true);
                                                                            TextView textView = (TextView) findViewById(R.id.textView);
                                                                            textView.setText(SegmentationMessages.CONTINUITY_ERROR);
                                                                        } else {
                                                                            inputStart = null;
                                                                            inputEnd = null;
                                                                            saveSegmentation();
                                                                            segPath = new Path();
                                                                            SegmentationDrawingUtils.setPathFromTouchEvent(segPath, canvas, view, event, x, y);
                                                                            segmentation.clearPoints();
                                                                        }
                                                                    }
                                                                }

                                                                canvas.drawPath(segPath, segPaint);
                                                                return true;
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                segmentation.setReferencePoint(relatedSegs);
                                                showDoneAndClearButtons();
                                            }
                                        }
                                    }
                                }
                                break;
                            case PointNoteActivity.TAG:
                                Boolean refreshView = (Boolean) data.getSerializableExtra("refreshPointNotes");
                                if ((refreshView != null) && (refreshView)) {
                                    refreshPointNote();
                                }
                                break;
                        }
                    break;
                    case Activity.RESULT_CANCELED:
                        switch (activity) {
                            case SelectSegmentationActivity.TAG:
                                hideDoneAndClearButtons();
                                showMenu();
                            break;
                        }
                }
            break;
        }
    }

    private void refreshPointNote() {
        List<Point> pointList = dbHelper.getAllPointNotes(fileName, imageNumber);
        for (Point point : pointList) {
            drawPoint(point.x, point.y);
        }
    }

    private void drawPoint(int x, int y) {
        Paint paint = SegmentationDrawingUtils.getPaintForPoint();
        float radius = SegmentationDrawingUtils.getRadiusForPoint(dicomFrame.getWidth());
        canvas.drawCircle(x, y, radius, paint);
        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.invalidate();
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
        TextView textInfo = (TextView) findViewById(R.id.textInfo);
        textInfo.setText(getResources().getString(R.string.dicomview_textinfo_default));
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
        menu.close(true);
        menu.setVisibility(View.VISIBLE);
    }

    private void showDoneAndClearButtons() {
        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setVisibility(View.VISIBLE);
        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setVisibility(View.VISIBLE);
    }

    private void hideDoneAndClearButtons() {
        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setVisibility(View.GONE);
        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setVisibility(View.GONE);
    }

    private List<Segmentation> drawSegmentationsOnFrame() {
        List<Segmentation> segmentations = dbHelper.getSegmentations(fileName, imageNumber);

        if (!segmentations.isEmpty()) {
            for (Segmentation segmentation: segmentations) {
                List<Point> points = segmentation.getPoints();
                segPath = SegmentationDrawingUtils.setPathFromPointList(points, canvas);
                segPaint = SegmentationDrawingUtils.getPaint(dicomFrame.getWidth(), segmentation.getType().getColor());
                canvas.drawPath(segPath, segPaint);
                if (segmentation.getType().isReferencePointDrawable()) {
                    drawPoint(segmentation.getReferencePoint().x, segmentation.getReferencePoint().y);
                }
            }
        }

        if ((segmentation.getType() != null) && (segmentation.getType().isReferencePointDrawable())) {
            if (referencePoint != null) {
                drawPoint(referencePoint.x, referencePoint.y);
            }
        }

        ImageView view = (ImageView) findViewById(R.id.imageView);
        view.invalidate();

        return segmentations;
    }

    private void saveSegmentation() {
        Gson gson = new Gson();
        String pointsString = gson.toJson(segmentation.getPoints());
        String referencePointString = gson.toJson(segmentation.getReferencePoint());
        dbHelper.insertSegmentation(fileName, imageNumber, segmentation.getType(), pointsString, referencePointString);
    }

}