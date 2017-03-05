package edu.unicen.project.dicomseg.dagger.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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

}
