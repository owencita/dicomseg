package edu.unicen.project.dicomseg.dbhelper.writers;


import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.List;

import edu.unicen.project.dicomseg.dbhelper.DbHelper;
import edu.unicen.project.dicomseg.dbhelper.contracts.DicomNoteContract;
import edu.unicen.project.dicomseg.models.GenericModel;
import edu.unicen.project.dicomseg.models.PointNoteModel;

public class XmlPointNoteWriter implements IXmlWriter {

    @Override
    public String getXmlFileName() {
        return "pointNotes.xml";
    }

    @Override
    public List<GenericModel> getModels(DbHelper dbHelper) {
        return dbHelper.getAllPointNotes();
    }

    @Override
    public String writeXml(List<GenericModel> pointNotes) {
        String pointNotesXml = null;
        if (!pointNotes.isEmpty()) {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            try {
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "pointnotes");
                for (GenericModel genericModel : pointNotes) {
                    PointNoteModel note = (PointNoteModel)genericModel;
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.TABLE_NAME);
                    // file name
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME);
                    serializer.text(note.getFileName());
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_FILE_NAME);
                    // study uid
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID);
                    serializer.text(note.getStudyUID());
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_STUDY_UID);
                    // series uid
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID);
                    serializer.text(note.getSeriesUID());
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_SERIES_UID);
                    // image number
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER);
                    serializer.text(String.valueOf(note.getImageNumber()));
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_IMAGE_NUMBER);
                    // x
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_X);
                    serializer.text(String.valueOf(note.getX()));
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_X);
                    // y
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y);
                    serializer.text(String.valueOf(note.getY()));
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_Y);
                    // note text
                    serializer.startTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT);
                    serializer.text(note.getText());
                    serializer.endTag("", DicomNoteContract.PointNoteEntry.COLUMN_NAME_TEXT);

                    serializer.endTag("", DicomNoteContract.PointNoteEntry.TABLE_NAME);
                }
                serializer.endTag("", "pointnotes");
                serializer.endDocument();
                pointNotesXml = writer.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return pointNotesXml;
    }
}
