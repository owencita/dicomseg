package edu.unicen.project.dicomseg.snakes;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.List;

public class SnakeImage {

    public static List<Point> snakeImage(Bitmap image, List<Point> segmentation) {

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
        int[][] chanel_gradient = new int[W][H];
        int maxgradient = 0;
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
                chanel_gradient[x + 1][y + 1] = snorm;
                maxgradient = Math.max(maxgradient, snorm);
            }
        }

        int THRESHOLD = 12;

        // Thresholding
        boolean[][] binarygradient = new boolean[W][H];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (chanel_gradient[x][y] > THRESHOLD * maxgradient / 100) {
                    binarygradient[x][y] = true;
                } else {
                    chanel_gradient[x][y] = 0;
                }
            }
        }

        // Distance map to binarized gradient
        int [][] chanel_flow = new int[W][H];
        double[][] cdist = new ChamferDistance(ChamferDistance.chamfer5).compute(binarygradient, W,H);
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                chanel_flow[x][y] = (int) (5 * cdist[x][y]);
            }
        }

        Snake snakeInstance = new Snake(W, H, chanel_gradient, chanel_flow, segmentation);

        return snakeInstance.snake;
    }
}
