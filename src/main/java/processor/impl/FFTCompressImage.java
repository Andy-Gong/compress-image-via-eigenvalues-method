package processor.impl;

import fft.FFT;
import org.apache.commons.math3.complex.Complex;
import processor.ImageProcess;
import processor.ImageReaderAndWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Compressing image via Fast Fourier Transform Algorithm.
 * And the compression rate is 64 times: 8x8.
 */
public class FFTCompressImage implements ImageProcess {

    /**
     * Compress a RGB image via Fast Fourier Transform Algorithm.
     * <p>
     * It mainly includes 3 steps:
     * 1. Split RGB data to 4 levels: alpha, red, green, blue.
     * 2. Compress 4 level data 64 times separately.
     * 3. Combine 4 level data to a RGB value.
     *
     * @param pixelRGB, size is N x M
     * @return int[][] compressed pixels values, and size is N/8 X M/8
     */
    public int[][] process(int[][] pixelRGB) {
        int rowRate = 8;
        int columnRate = 8;
        int originalRow = pixelRGB.length;
        int originalColumn = pixelRGB[0].length;
        //split to 8x8 block
        int compressRow = pixelRGB.length / rowRate;
        int compressColumn = pixelRGB[0].length / columnRate;

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
        Complex[] compressAlpha = compress64Ratio(valueAlpha, columnRate, rowRate);
        Complex[] compressRed = compress64Ratio(valueRed, columnRate, rowRate);
        Complex[] compressGreen = compress64Ratio(valueGreen, columnRate, rowRate);
        Complex[] compressBlue = compress64Ratio(valueBlue, columnRate, rowRate);

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
     * Compress image data columnCprRation * rowCprRation times
     * <p>
     * The length of return result should be ((row of valueOf2D / row compression rate) * (row of valueOf2D / row compression rate)).
     * For example,
     * valueOf2D is 512 X 512,
     * columnCprRate = 8,
     * rowCprRate = 8,
     * <p>
     * length = 512/8 * 512/8 = 4096
     *
     * @param valueOf2D     original image data values, like the values of red rate
     * @param columnCprRate compression rate of column
     * @param rowCprRate    compression rate of row
     * @return value of compression result
     */
    public Complex[] compress64Ratio(int[][] valueOf2D, int columnCprRate, int rowCprRate) {
        //split to 8x8 block
        int row = valueOf2D.length / rowCprRate;
        int column = valueOf2D[0].length / columnCprRate;
        List<Complex[]> vectors = new ArrayList<Complex[]>(row * column);

        for (int h = 0; h < row; h++) {
            for (int i = 0; i < column; i++) {
                Complex[] block = new Complex[rowCprRate * columnCprRate];
                for (int j = 0; j < rowCprRate; j++) {
                    for (int k = 0; k < columnCprRate; k++) {
                        block[j * 8 + k] = new Complex(valueOf2D[rowCprRate * h + j][columnCprRate * i + k]);
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
        FFTCompressImage processor = new FFTCompressImage();
        int[][] result = processor.process(pixel);
        reader.write(result, "png", image.getType(), "compressLenna.png");
    }
}
