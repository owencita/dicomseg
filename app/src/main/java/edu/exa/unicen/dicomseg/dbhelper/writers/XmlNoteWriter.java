package edu.exa.unicen.dicomseg.dbhelper.writers;


import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.List;

import edu.exa.unicen.dicomseg.dbhelper.contracts.DicomNoteContract;
import edu.exa.unicen.dicomseg.dbhelper.DbHelper;
import edu.exa.unicen.dicomseg.models.GenericModel;
import edu.exa.unicen.dicomseg.models.NoteModel;

public class XmlNoteWriter implements IXmlWriter {

    @Override
    public String getXmlFileName() {
        return "notes.xml";
    }

    @Override
    public List<GenericModel> getModels(DbHelper dbHelper) {
        return dbHelper.getAllNotes();
    }

    @Override
    public String writeXml(List<GenericModel> notes) {
        String notesXml = null;
        if (!notes.isEmpty()) {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            try {
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "notes");
                for (GenericModel genericModel : notes) {
                    NoteModel note = (NoteModel)genericModel;
                    serializer.startTag("", DicomNoteContract.NoteEntry.TABLE_NAME);
                    // id
                    serializer.startTag("", DicomNoteContract.NoteEntry._ID);
                    serializer.text(String.valueOf(note.getId()));
                    serializer.endTag("", DicomNoteContract.NoteEntry._ID);
                    // file name
                    serializer.startTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME);
                    serializer.text(note.getFileName());
                    serializer.endTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_FILE_NAME);
                    // study uid
                    serializer.startTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID);
                    serializer.text(note.getStudyUID());
                    serializer.endTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_STUDY_UID);
                    // series uid
                    serializer.startTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID);
                    serializer.text(note.getSeriesUID());
                    serializer.endTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_SERIES_UID);
                    // image number
                    serializer.startTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER);
                    serializer.text(String.valueOf(note.getImageNumber()));
                    serializer.endTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_IMAGE_NUMBER);
                    // note text
                    serializer.startTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT);
                    serializer.text(note.getText());
                    serializer.endTag("", DicomNoteContract.NoteEntry.COLUMN_NAME_TEXT);

                    serializer.endTag("", DicomNoteContract.NoteEntry.TABLE_NAME);
                }
                serializer.endTag("", "notes");
                serializer.endDocument();
                notesXml = writer.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return notesXml;
    }
}
