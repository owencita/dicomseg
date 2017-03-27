package edu.unicen.project.dicomseg.dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import edu.unicen.project.dicomseg.activities.MainActivity;
import edu.unicen.project.dicomseg.dagger.modules.DicomSegModule;

@Singleton
@Component(modules = {DicomSegModule.class})
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);

    static final class Initializer {

        private Initializer() {
        }

        public static MainActivityComponent init() {
            return DaggerMainActivityComponent.builder()
                    .dicomSegModule(new DicomSegModule())
                    .build();
        }
    }
}
