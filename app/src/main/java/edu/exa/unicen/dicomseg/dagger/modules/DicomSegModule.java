package edu.exa.unicen.dicomseg.dagger.modules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.exa.unicen.dicomseg.dbhelper.exporters.DbXmlExporter;
import edu.exa.unicen.dicomseg.dbhelper.exporters.IDbExporter;
import edu.exa.unicen.dicomseg.dbhelper.writers.IXmlWriter;
import edu.exa.unicen.dicomseg.dbhelper.writers.XmlNoteWriter;
import edu.exa.unicen.dicomseg.dbhelper.writers.XmlPointNoteWriter;
import edu.exa.unicen.dicomseg.dbhelper.writers.XmlSegmentationWriter;
import edu.exa.unicen.dicomseg.snakes.externalforces.ExternalForcesImpl;
import edu.exa.unicen.dicomseg.snakes.externalforces.IExternalForces;

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
        // add new writers here
        List<IXmlWriter> writers = new ArrayList<IXmlWriter>();
        XmlNoteWriter noteWriter = new XmlNoteWriter();
        writers.add(noteWriter);
        XmlPointNoteWriter pointNoteWriter = new XmlPointNoteWriter();
        writers.add(pointNoteWriter);
        XmlSegmentationWriter segmentationWriter = new XmlSegmentationWriter();
        writers.add(segmentationWriter);
        return writers;
    }

}
