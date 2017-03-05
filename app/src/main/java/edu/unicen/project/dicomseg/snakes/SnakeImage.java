package edu.unicen.project.dicomseg.snakes;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.List;

import javax.inject.Inject;

import edu.unicen.project.dicomseg.dagger.components.DicomSegComponent;
import edu.unicen.project.dicomseg.snakes.externalforces.IExternalForces;

public class SnakeImage {

    @Inject
    public IExternalForces externalForces;

    public SnakeImage() {
        DicomSegComponent component = DicomSegComponent.Initializer.init();
        component.inject(this);
    }

    public List<Point> snakeImage(Bitmap image, List<Point> segmentation) {

        externalForces.setChannels(image);

        int[][] channelGradient = externalForces.getChannelGradient();
        int[][] channelFlow = externalForces.getChannelFlow();

        Snake snakeInstance = new Snake(image.getWidth(), image.getHeight(), channelGradient, channelFlow, segmentation);
        snakeInstance.snake();

        return snakeInstance.snake;
    }
}
