package edu.unicen.project.dicomseg.dbhelper;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import edu.unicen.project.dicomseg.contracts.DicomNoteContract;
import edu.unicen.project.dicomseg.models.Note;

public class DbXmlExporter {

    public static void exportDatabase(DbHelper dbHelper) {
        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            File file = new File(externalStorage + "/dicomsegdb/notes.xml");
            file.createNewFile();
            file.setReadable(true);
            file.setWritable(true);
            FileWriter notes = new FileWriter(file);
            List<Note> allNotes = dbHelper.getAllNotes();
            String notesXml = writeXml(allNotes);
            if (notesXml != null) {
                notes.append(notesXml);
                notes.close();
            }
        } catch (IOException e) {
        }
    }

    protected static String writeXml(List<Note> notes){
        String notesXml = null;
        if (!notes.isEmpty()) {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            try {
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "notes");
                for (Note note : notes) {
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