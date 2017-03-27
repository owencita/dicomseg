package edu.unicen.project.dicomseg.dbhelper.writers;

import android.graphics.Point;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.List;

import edu.unicen.project.dicomseg.dbhelper.DbHelper;
import edu.unicen.project.dicomseg.dbhelper.contracts.DicomSegmentationContract;
import edu.unicen.project.dicomseg.models.GenericModel;
import edu.unicen.project.dicomseg.models.SegmentationModel;

public class XmlSegmentationWriter implements IXmlWriter {

    @Override
    public String getXmlFileName() {
        return "segmentations.xml";
    }

    @Override
    public List<GenericModel> getModels(DbHelper dbHelper) {
        return dbHelper.getAllSegmentations();
    }

    @Override
    public String writeXml(List<GenericModel> segmentations) {
        String segmentationsXml = null;
        if (!segmentations.isEmpty()) {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            try {
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "segmentations");
                for (GenericModel genericModel : segmentations) {
                    SegmentationModel segmentation = (SegmentationModel)genericModel;
                    serializer.startTag("", DicomSegmentationContract.Segmentation.TABLE_NAME);
                    // file name
                    serializer.startTag("",DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME);
                    serializer.text(segmentation.getFileName());
                    serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_FILE_NAME);
                    // study uid
                    serializer.startTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_STUDY_UID);
                    serializer.text(segmentation.getStudyUID());
                    serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_STUDY_UID);
                    // series uid
                    serializer.startTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_SERIES_UID);
                    serializer.text(segmentation.getSeriesUID());
                    serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_SERIES_UID);
                    // image number
                    serializer.startTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER);
                    serializer.text(String.valueOf(segmentation.getImageNumber()));
                    serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_IMAGE_NUMBER);
                    // segmentation type
                    serializer.startTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE);
                    serializer.text(segmentation.getSegmentationType().getName());
                    serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_SEG_TYPE);
                    // points
                    serializer.startTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS);
                    for (Point point: segmentation.getPoints()) {
                        serializer.startTag("", "point");
                        serializer.startTag("", "x");
                        serializer.text(String.valueOf(point.x));
                        serializer.endTag("", "x");
                        serializer.startTag("", "y");
                        serializer.text(String.valueOf(point.y));
                        serializer.endTag("", "y");
                        serializer.endTag("", "point");
                    }
                    serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_POINTS);
                    // reference points (pole)
                    if (segmentation.getReferencePoint() != null) {
                        serializer.startTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_REFERENCE_POINT);
                        serializer.startTag("", "x");
                        serializer.text(String.valueOf(segmentation.getReferencePoint().x));
                        serializer.endTag("", "x");
                        serializer.startTag("", "y");
                        serializer.text(String.valueOf(segmentation.getReferencePoint().y));
                        serializer.endTag("", "y");
                        serializer.endTag("", DicomSegmentationContract.Segmentation.COLUMN_NAME_REFERENCE_POINT);
                    }
                    serializer.endTag("", DicomSegmentationContract.Segmentation.TABLE_NAME);
                }
                serializer.endTag("", "segmentations");
                serializer.endDocument();
                segmentationsXml = writer.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return segmentationsXml;
    }
}
