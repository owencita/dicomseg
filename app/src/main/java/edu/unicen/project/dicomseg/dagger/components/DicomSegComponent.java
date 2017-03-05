package edu.unicen.project.dicomseg.dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import edu.unicen.project.dicomseg.dagger.modules.DicomSegModule;
import edu.unicen.project.dicomseg.snakes.SnakeImage;

@Singleton
@Component(modules = {DicomSegModule.class})
public interface DicomSegComponent {

    void inject(SnakeImage snakeImage);

    static final class Initializer {

        private Initializer() {
        }

        public static DicomSegComponent init() {
            return DaggerDicomSegComponent.builder()
                    .dicomSegModule(new DicomSegModule())
                    .build();
        }
    }

}
