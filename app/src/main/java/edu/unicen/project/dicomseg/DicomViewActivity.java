package edu.unicen.project.dicomseg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.imebra.dicom.ColorTransformsFactory;
import com.imebra.dicom.DataSet;
import com.imebra.dicom.DrawBitmap;
import com.imebra.dicom.Image;
import com.imebra.dicom.ModalityVOILUT;
import com.imebra.dicom.TransformsChain;
import com.imebra.dicom.VOILUT;

import edu.unicen.project.dicomseg.app.DicomSegApp;

public class DicomViewActivity extends Activity {

    private static final String TAG = "DicomViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicom_view);

        DataSet dataSet = DicomSegApp.getDataSet();
        Integer imageNumber = (Integer) getIntent().getSerializableExtra("imageNumber");

        Image image = dataSet.getImage(imageNumber.intValue());

        // Monochrome images may have a modality transform
        if (ColorTransformsFactory.isMonochrome(image.getColorSpace())) {
            ModalityVOILUT modalityVOILUT = new ModalityVOILUT(dataSet);
            if (!modalityVOILUT.isEmpty()) {
                Image modalityImage = modalityVOILUT.allocateOutputImage(image, image.getSizeX(), image.getSizeY());
                modalityVOILUT.runTransform(image, 0, 0, image.getSizeX(), image.getSizeY(), modalityImage, 0, 0);
                image = modalityImage;
            }
        }
        // Just for fun: get the color space and the patient name
        String colorSpace = image.getColorSpace();
        String patientName = dataSet.getString(0x0010, 0, 0x0010, 0);
        String dataType = dataSet.getDataType(0x0010, 0, 0x0010);
        // Allocate a transforms chain: contains all the transforms to execute before displaying
        //  an image
        TransformsChain transformsChain = new TransformsChain();
        // Monochromatic image may require a presentation transform to display interesting data
        if (ColorTransformsFactory.isMonochrome(image.getColorSpace())) {
            VOILUT voilut = new VOILUT(dataSet);
            int voilutId = voilut.getVOILUTId(0);
            if (voilutId != 0) {
                voilut.setVOILUT(voilutId);
            } else {
                // No presentation transform is present: here we calculate the optimal window/width (brightness,
                //  contrast) and we will use that
                voilut.applyOptimalVOI(image, 0, 0, image.getSizeX(), image.getSizeY());
            }
            transformsChain.addTransform(voilut);
        }
        // Let's use a DrawBitmap object to generate a buffer with the pixel data. We will
        // use that buffer to create an Android Bitmap
        DrawBitmap drawBitmap = new DrawBitmap(image, transformsChain);
        int temporaryBuffer[] = new int[1]; // Temporary buffer. Just used to get the needed buffer size
        int bufferSize = drawBitmap.getBitmap(image.getSizeX(), image.getSizeY(), 0, 0, image.getSizeX(), image.getSizeY(), temporaryBuffer, 0);
        int buffer[] = new int[bufferSize]; // Ideally you want to reuse this in subsequent calls to getBitmap()
        // Now fill the buffer with the image data and create a bitmap from it
        drawBitmap.getBitmap(image.getSizeX(), image.getSizeY(), 0, 0, image.getSizeX(), image.getSizeY(), buffer, bufferSize);
        Bitmap renderBitmap = Bitmap.createBitmap(buffer, image.getSizeX(), image.getSizeY(), Bitmap.Config.ARGB_8888);

        // Let's find the ImageView and se the image
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(renderBitmap);
    }

}
