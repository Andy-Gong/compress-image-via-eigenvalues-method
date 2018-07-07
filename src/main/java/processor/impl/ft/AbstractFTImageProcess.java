package processor.impl.ft;

import com.jcabi.aspects.Loggable;
import org.apache.commons.math3.complex.Complex;
import processor.ImageProcess;
import processor.impl.ImageDecomposeLayers;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFTImageProcess implements ImageProcess {

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

        ImageDecomposeLayers decomposeLayers = new ImageDecomposeLayers(pixelRGB, originalRow, originalColumn).invoke();
        Complex[] compressAlpha = compress64Ratio(decomposeLayers.getValueAlpha(), columnRate, rowRate);
        Complex[] compressRed = compress64Ratio(decomposeLayers.getValueRed(), columnRate, rowRate);
        Complex[] compressGreen = compress64Ratio(decomposeLayers.getValueGreen(), columnRate, rowRate);
        Complex[] compressBlue = compress64Ratio(decomposeLayers.getValueBlue(), columnRate, rowRate);

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

    @Loggable(Loggable.DEBUG)
    abstract Complex[] compress64Ratio(int[][] valueOf2D, int columnCprRate, int rowCprRate);

    @Loggable(Loggable.DEBUG)
    protected List<Complex[]> split(int[][] valuesOf2D, int columnCprRate, int rowCprRate) {
        int columnOfBlock = valuesOf2D.length / columnCprRate;
        int rowOfBlock = valuesOf2D.length / rowCprRate;
        List<Complex[]> blocks = new ArrayList<Complex[]>(rowOfBlock * columnOfBlock);
        for (int h = 0; h < rowOfBlock; h++) {
            for (int i = 0; i < columnOfBlock; i++) {
                Complex[] block = new Complex[rowCprRate * columnCprRate];
                for (int j = 0; j < rowCprRate; j++) {
                    for (int k = 0; k < columnCprRate; k++) {
                        block[j * 8 + k] = new Complex(valuesOf2D[rowCprRate * h + j][columnCprRate * i + k]);
                    }
                }
                blocks.add(block);
            }
        }
        return blocks;
    }
}
