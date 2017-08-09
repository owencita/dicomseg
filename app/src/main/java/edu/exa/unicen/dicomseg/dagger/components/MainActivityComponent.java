package edu.exa.unicen.dicomseg.dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import edu.exa.unicen.dicomseg.activities.MainActivity;
import edu.exa.unicen.dicomseg.dagger.modules.DicomSegModule;

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
