package edu.exa.unicen.dicomseg.snakes.externalforces;

import android.graphics.Bitmap;

public interface IExternalForces {

    public void setChannels(Bitmap image);

    public int[][] getChannelGradient();

    public int[][] getChannelFlow();
}
