package edu.unicen.project.dicomseg.dagger.modules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.unicen.project.dicomseg.dbhelper.exporters.DbXmlExporter;
import edu.unicen.project.dicomseg.dbhelper.exporters.IDbExporter;
import edu.unicen.project.dicomseg.dbhelper.writers.IXmlWriter;
import edu.unicen.project.dicomseg.dbhelper.writers.XmlNoteWriter;
import edu.unicen.project.dicomseg.dbhelper.writers.XmlPointNoteWriter;
import edu.unicen.project.dicomseg.snakes.externalforces.ExternalForcesImpl;
import edu.unicen.project.dicomseg.snakes.externalforces.IExternalForces;

@Module
public class DicomSegModule {

    public DicomSegModule() {
    }

    @Provides
    @Singleton
    IExternalForces provideExternalForces() {
        return new ExternalForcesImpl();
    }

    @Provides
    @Singleton
    IDbExporter provideDbExporter() {
        return new DbXmlExporter();
    }

    @Provides
    @Singleton
    List<IXmlWriter> provideWriters() {
        List<IXmlWriter> writers = new ArrayList<IXmlWriter>();
        IXmlWriter noteWriter = new XmlNoteWriter();
        writers.add(noteWriter);
        IXmlWriter pointNoteWriter = new XmlPointNoteWriter();
        writers.add(pointNoteWriter);
        return writers;
    }

}
