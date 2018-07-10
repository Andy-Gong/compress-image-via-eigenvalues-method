package processor.impl;

import processor.ImageProcess;
import processor.ImageReaderAndWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class GrayscaleImage implements ImageProcess {

    public int[][] process(int[][] pixelsRGB) {
        int height = pixelsRGB.length;
        int width = pixelsRGB[0].length;
        int[][] grayPixel = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixelsRGB[i][j];
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                int avg = (red + green + blue) / 3;
                grayPixel[i][j] = (alpha << 24) | (avg << 16) | (avg << 8) | avg;
            }
        }
        return grayPixel;
    }

    public static void main(String[] args) throws IOException {
        ImageReaderAndWriter reader = new ImageReaderAndWriter();
        BufferedImage image = reader.getImage("220px-Lenna.png");
        int[][] pixel = reader.getPixels(image);
        ImageProcess processor = new GrayscaleImage();
        reader.write(processor.process(pixel), "png", image.getType(), "/tmp/lennagray.png");
    }
}
