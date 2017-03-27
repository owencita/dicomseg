package edu.unicen.project.dicomseg.dbhelper.exporters;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import edu.unicen.project.dicomseg.dagger.components.DbExporterComponent;
import edu.unicen.project.dicomseg.dbhelper.DbHelper;
import edu.unicen.project.dicomseg.dbhelper.writers.IXmlWriter;
import edu.unicen.project.dicomseg.models.GenericModel;

public class DbXmlExporter implements IDbExporter {

    @Inject
    public List<IXmlWriter> writers;

    public DbXmlExporter() {
        DbExporterComponent component = DbExporterComponent.Initializer.init();
        component.inject(this);
    }

    public void exportDatabase(DbHelper dbHelper) {
        String externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        for (IXmlWriter writer: writers) {
            try {
                File file = new File(externalStorage + "/dicomsegdb/" + writer.getXmlFileName());
                file.createNewFile();
                file.setReadable(true);
                file.setWritable(true);
                FileWriter notes = new FileWriter(file);
                List<GenericModel> models = writer.getModels(dbHelper);
                String xml = writer.writeXml(models);
                if (xml != null) {
                    notes.append(xml);
                    notes.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
