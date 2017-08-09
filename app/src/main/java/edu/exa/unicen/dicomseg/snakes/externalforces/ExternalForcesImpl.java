package edu.exa.unicen.dicomseg.snakes.externalforces;

import android.graphics.Bitmap;

import edu.exa.unicen.dicomseg.snakes.ChamferDistance;

public class ExternalForcesImpl implements IExternalForces {

    public static int[][] CHANNEL_GRADIENT = null;
    public static int[][] CHANNEL_FLOW = null;

    private int THRESHOLD = 12;

    public void setChannels(Bitmap image) {

        int W = image.getWidth();
        int H = image.getHeight();

        // GrayLevelScale (Luminance)
        int[][] clum = new int[W][H];

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                //int rgb = image.getRGB(x,y);
                int rgb = image.getPixel(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                clum[x][y] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            }
        }

        // Gradient (sobel)
        CHANNEL_GRADIENT = new int[W][H];
        int maxGradient = 0;
        for (int y = 0; y < H-2; y++) {
            for (int x = 0; x < W - 2; x++) {
                int p00 = clum[x + 0][y + 0];
                int p10 = clum[x + 1][y + 0];
                int p20 = clum[x + 2][y + 0];
                int p01 = clum[x + 0][y + 1];
                int p21 = clum[x + 2][y + 1];
                int p02 = clum[x + 0][y + 2];
                int p12 = clum[x + 1][y + 2];
                int p22 = clum[x + 2][y + 2];
                int sx = (p20 + 2 * p21 + p22) - (p00 + 2 * p01 + p02);
                int sy = (p02 + 2 * p12 + p22) - (p00 + 2 * p10 + p10);
                int snorm = (int) Math.sqrt(sx * sx + sy * sy);
                CHANNEL_GRADIENT[x + 1][y + 1] = snorm;
                maxGradient = Math.max(maxGradient, snorm);
            }
        }

        // Thresholding
        boolean[][] binaryGradient = new boolean[W][H];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (CHANNEL_GRADIENT[x][y] > THRESHOLD * maxGradient / 100) {
                    binaryGradient[x][y] = true;
                } else {
                    CHANNEL_GRADIENT[x][y] = 0;
                }
            }
        }

        // Distance map to binarized gradient
        CHANNEL_FLOW = new int[W][H];
        double[][] cdist = new ChamferDistance(ChamferDistance.chamfer5).compute(binaryGradient, W,H);
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                CHANNEL_FLOW[x][y] = (int) (5 * cdist[x][y]);
            }
        }
    }

    public int[][] getChannelGradient() {
        return CHANNEL_GRADIENT;
    }

    public int[][] getChannelFlow() {
        return CHANNEL_FLOW;
    }
}
