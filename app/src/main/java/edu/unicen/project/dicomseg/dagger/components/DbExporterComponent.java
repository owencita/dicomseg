package edu.unicen.project.dicomseg.dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import edu.unicen.project.dicomseg.dagger.modules.DicomSegModule;
import edu.unicen.project.dicomseg.dbhelper.exporters.DbXmlExporter;


@Singleton
@Component(modules = {DicomSegModule.class})
public interface DbExporterComponent {

    void inject(DbXmlExporter snakeImage);

    static final class Initializer {

        private Initializer() {
        }

        public static DbExporterComponent init() {
            return DaggerDbExporterComponent.builder()
                    .dicomSegModule(new DicomSegModule())
                    .build();
        }
    }
}
