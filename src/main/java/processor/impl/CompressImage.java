package processor.impl;

import fft.FFT;
import org.apache.commons.math3.complex.Complex;
import processor.ImageProcess;
import processor.ImageReaderAndWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompressImage {

    /**
     * Compress a RGB image.
     * 1. Split RGB data to 4 levels: alpha, red, green, blue.
     * 2. Compress 4 level data 64 times separately.
     * 3. Combine 4 level data to a RGB value.
     *
     * @param pixelRGB
     * @return
     */
    public int[][] process(int[][] pixelRGB) {
        int originalRow = pixelRGB.length;
        int originalColumn = pixelRGB[0].length;
        //split to 8x8 block
        int compressRow = pixelRGB.length / 8;
        int compressColumn = pixelRGB[0].length / 8;

        int[][] valueAlpha = new int[pixelRGB.length][pixelRGB[0].length];
        int[][] valueRed = new int[pixelRGB.length][pixelRGB[0].length];
        int[][] valueGreen = new int[pixelRGB.length][pixelRGB[0].length];
        int[][] valueBlue = new int[pixelRGB.length][pixelRGB[0].length];

        for (int h = 0; h < originalRow; h++) {
            for (int i = 0; i < originalColumn; i++) {
                int pixel = pixelRGB[h][i];
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                valueAlpha[h][i] = alpha;
                valueRed[h][i] = red;
                valueGreen[h][i] = green;
                valueBlue[h][i] = blue;

            }
        }
        Complex[] compressAlpha = compress64Ratio(valueAlpha);
        Complex[] compressRed = compress64Ratio(valueRed);
        Complex[] compressGreen = compress64Ratio(valueGreen);
        Complex[] compressBlue = compress64Ratio(valueBlue);

        // convert to image pixel array
        int[][] keepPixels = new int[compressRow][compressColumn];
        for (int i = 0; i < compressRow; i++) {
            for (int j = 0; j < compressColumn; j++) {
                int alpha = (int) compressAlpha[compressColumn * i + j].getReal();
                int redValue = (int) compressRed[compressColumn * i + j].getReal();
                int greenValue = (int) compressGreen[compressColumn * i + j].getReal();
                int blueValue = (int) compressBlue[compressColumn * i + j].getReal();
                keepPixels[i][j] = (alpha << 24) | (redValue << 16) | (greenValue << 8) | blueValue;
            }
        }
        return keepPixels;
    }

    /**
     * Compress image data 64 times, and compress data block is 8x8
     *
     * @param valueOf2D
     * @return
     */
    public Complex[] compress64Ratio(int[][] valueOf2D) {
        //split to 8x8 block
        int row = valueOf2D.length / 8;
        int column = valueOf2D[0].length / 8;
        List<Complex[]> vectors = new ArrayList<Complex[]>(row * column);

        for (int h = 0; h < row; h++) {
            for (int i = 0; i < column; i++) {
                Complex[] block = new Complex[8 * 8];
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        block[j * 8 + k] = new Complex(valueOf2D[8 * h + j][8 * i + k]);
                    }
                }
                vectors.add(block);
            }
        }

        // fft transform to each block
        List<Complex[]> fftResult = new ArrayList<Complex[]>();
        for (Complex[] block : vectors) {
            fftResult.add(FFT.inverse(block));
        }

        // filter coefficient, and only select first one
        for (Complex[] block : fftResult) {
            for (int i = 0; i < block.length; i++) {
                if (i == 0) {
                    block[i] = block[i];
                } else {
                    block[i] = new Complex(0);
                }
            }
        }

        // fft convert to image pixels and remove other pixel except one
        Complex[] fftPixel = new Complex[row * column];
        for (int i = 0; i < fftResult.size(); i++) {
            Complex[] tmp = FFT.fft(fftResult.get(i));
            fftPixel[i] = tmp[0];
        }
        return fftPixel;
    }

    /**
     * This test compresses image 64 times, input image is 512x512, output image is 64x64
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ImageReaderAndWriter reader = new ImageReaderAndWriter();
        BufferedImage image = reader.getImage("Lenna_color.png");
        int[][] pixel = reader.getPixels(image);
        CompressImage processor = new CompressImage();
        int[][] result = processor.process(pixel);
        reader.write(result, "png", image.getType(), "compressLenna.png");
    }
}
