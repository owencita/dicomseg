package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dbhelper.NoteReaderDbHelper;
import edu.unicen.project.dicomseg.dicom.DicomUtils;
import edu.unicen.project.dicomseg.listeners.GestureListener;
import edu.unicen.project.dicomseg.segmentation.SegmentationUtils;
import edu.unicen.project.dicomseg.segmentation.SegmentationsConstants;

public class DicomViewActivity extends Activity {

    private static final String TAG = "DicomViewActivity";
    private GestureDetector gestureDetector;

    private Path mPath;
    private Paint mPaint;
    private String selectedSegmentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom_view);

        gestureDetector = new GestureDetector(this, new GestureListener());

        final String fileName = (String) getIntent().getSerializableExtra("fileName");

        final Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        final Bitmap dicomFrame = DicomUtils.getFrame(imageNumber);

        final Bitmap mutableBitmap = dicomFrame.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(mutableBitmap);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(mutableBitmap);

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

                final NoteReaderDbHelper dbHelper = new NoteReaderDbHelper(getBaseContext());
                List<Point> pointList = dbHelper.getAllPointNotes(fileName, imageNumber);

                for (Point point : pointList) {
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.rgb(51, 98, 178));
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

        FloatingActionButton segmentImageButton = (FloatingActionButton) findViewById(R.id.menu_segment);
        segmentImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                hideMenu();

                Intent intent = new Intent(view.getContext(), SelectSegmentationActivity.class);
                startActivityForResult(intent, 1);

                mPath = new Path();
                mPaint = SegmentationUtils.getPaint(dicomFrame.getWidth(), dicomFrame.getHeight());

                Button doneButton = (Button) findViewById(R.id.done);
                doneButton.setVisibility(View.VISIBLE);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button doneButton = (Button) findViewById(R.id.done);
                        doneButton.setVisibility(View.GONE);

                        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawPath(mPath, mPaint);

                        imageView.setOnTouchListener(null);
                        // TODO: check for correct segmentation (mPath) according to <selectedSegmentation>
                        // TODO: save mPath
                        showMenu();
                    }
                });

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        float[] coords = getCoordsForCanvas(event, imageView);
                        int x = (int) coords[0];
                        int y = (int) coords[1];

                        SegmentationUtils.setPath(mPath, canvas, view, event, x, y);

                        canvas.drawPath(mPath, mPaint);
                        return true;
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
                selectedSegmentation = data.getStringExtra("result");
            }
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
