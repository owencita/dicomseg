package edu.unicen.project.dicomseg.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import edu.unicen.project.dicomseg.R;
import edu.unicen.project.dicomseg.dicom.DicomUtils;
import edu.unicen.project.dicomseg.listeners.GestureListener;

public class DicomViewActivity extends Activity {

    private static final String TAG = "DicomViewActivity";
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom_view);

        gestureDetector = new GestureDetector(this, new GestureListener());

        final Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");
        Bitmap renderBitmap = DicomUtils.getFrame(imageNumber);

        Bitmap mutableBitmap = renderBitmap.copy(Bitmap.Config.ARGB_8888, true);
        final Canvas canvas = new Canvas(mutableBitmap);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(mutableBitmap);

        FloatingActionButton generalNote = (FloatingActionButton) findViewById(R.id.menu_general_note);
        generalNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GeneralNoteActivity.class);
                intent.putExtra("fileName", (String) getIntent().getSerializableExtra("fileName"));
                intent.putExtra("imageNumber", imageNumber);
                view.getContext().startActivity(intent);
            }
        });

        FloatingActionButton pointNote = (FloatingActionButton) findViewById(R.id.menu_point_note);
        pointNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.menu);
                menu.hideMenu(true);
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Tap the image point where you want to add a note");

                // draw points where there is a note

                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (gestureDetector.onTouchEvent(event)) {
                            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                            paint.setColor(Color.RED);
                            paint.setStyle(Paint.Style.FILL);
                            canvas.drawCircle(event.getX(), event.getY(), 5, paint);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
        });

        //Intent intent = new Intent(view.getContext(), PointNoteActivity.class);
        //view.getContext().startActivity(intent);

    }

}
