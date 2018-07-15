package ft.processor.impl.ft;

import ft.fft.FFT;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.math3.complex.Complex;
import ft.processor.ImageReaderAndWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Compressing image via Fast Fourier Transform Algorithm.
 * And the compression rate is 64 times: 8x8.
 */
public class FFTCompressImage extends AbstractFTImageProcess {
    private static final Logger LOGGER = Logger.getLogger(FFTCompressImage.class);

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
        List<Complex[]> blocks = split(valueOf2D, columnCprRate, rowCprRate);
        return blocks.stream().map(block -> {
            Complex[] tmp = FFT.inverse(block);
            for (int i = 1; i < tmp.length; i++) {
                tmp[i] = Complex.ZERO;
            }
            return FFT.fft(tmp)[0];
        }).toArray(Complex[]::new);
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int[][] result = processor.process(pixel);
        stopWatch.stop();
        LOGGER.info("Image compression time: " + stopWatch.getTime());
        reader.write(result, "png", image.getType(), "FFTCompressLenna.png");
    }
}
