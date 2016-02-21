package edu.unicen.project.dicomseg.dicom;

import android.graphics.Bitmap;
import android.util.Log;

import com.imebra.dicom.CodecFactory;
import com.imebra.dicom.ColorTransformsFactory;
import com.imebra.dicom.DataSet;
import com.imebra.dicom.DrawBitmap;
import com.imebra.dicom.Image;
import com.imebra.dicom.ModalityVOILUT;
import com.imebra.dicom.Stream;
import com.imebra.dicom.StreamReader;
import com.imebra.dicom.TransformsChain;
import com.imebra.dicom.VOILUT;

import java.io.File;

import edu.unicen.project.dicomseg.app.DicomSegApp;
import edu.unicen.project.dicomseg.models.Patient;

/**
 * Utils class to interact with Imebra Library
 */
public class DicomUtils {

    private static final String TAG = "DicomUtils";

    public static final DataSet getDataSet(File dicomFile) {
        System.loadLibrary("imebra_lib");

        Stream stream = new Stream();
        String fileName = dicomFile.getAbsolutePath();
        stream.openFileRead(fileName);

        // Build an internal representation of the Dicom file. Tags larger than 256 bytes
        //  will be loaded on demand from the file
        return CodecFactory.load(new StreamReader(stream), 256);
    }

    public static Bitmap getFrame(Integer imageNumber) {
        DataSet dataSet = DicomSegApp.getDataSet();

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

        // Allocate a transforms chain: contains all the transforms to execute before displaying an image
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

        return renderBitmap;
    }

    public static Patient getPatient() {
        Patient patient = new Patient();
        DataSet dataSet = DicomSegApp.getDataSet();

        String id = dataSet.getString(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_ID, 0);
        patient.setId(id);

        String name = dataSet.getString(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_NAME, 0);
        patient.setName(name);

        String sex = dataSet.getString(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_SEX, 0);
        patient.setSex(sex);

        String birthDate = dataSet.getString(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_BIRTH_DATE, 0);
        patient.setBirthDate(birthDate);

        String age = dataSet.getString(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_AGE, 0);
        if (!age.isEmpty()) {
            patient.setAge(age);
        }

        Double weight = dataSet.getDouble(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_WEIGHT, 0);
        patient.setWeight(weight);

        String address = dataSet.getString(DicomTags.PATIENT_INFO_GROUP, 0, DicomTags.PATIENT_ADDRESS, 0);
        patient.setAddress(address);

        return patient;
    }

    public static String getStudyUID() {
        DataSet dataSet = DicomSegApp.getDataSet();
        return dataSet.getString(DicomTags.STUDY_INFO_GROUP, 0, DicomTags.STUDY_INSTANCE_UID, 0);
    }

    public static String getSeriesUID() {
        DataSet dataSet = DicomSegApp.getDataSet();
        return dataSet.getString(DicomTags.STUDY_INFO_GROUP, 0, DicomTags.SERIES_INSTANCE_UID, 0);
    }

}
