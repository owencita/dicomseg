package edu.exa.unicen.dicomseg.snakes;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.List;

import javax.inject.Inject;

import edu.exa.unicen.dicomseg.dagger.components.DicomSegComponent;
import edu.exa.unicen.dicomseg.snakes.externalforces.IExternalForces;

public class SnakeImage {

    @Inject
    public IExternalForces externalForces;

    public SnakeImage() {
        DicomSegComponent component = DicomSegComponent.Initializer.init();
        component.inject(this);
    }

    public List<Point> snakeImage(Bitmap image, List<Point> segmentation, double[] coefficients) {

        externalForces.setChannels(image);

        int[][] channelGradient = externalForces.getChannelGradient();
        int[][] channelFlow = externalForces.getChannelFlow();

        Snake snakeInstance = new Snake(image.getWidth(), image.getHeight(), channelGradient, channelFlow, segmentation, coefficients);
        snakeInstance.snake();

        return snakeInstance.snake;
    }
}
