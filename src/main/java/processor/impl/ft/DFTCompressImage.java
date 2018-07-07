package processor.impl.ft;

import dft.DFT;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.math3.complex.Complex;
import org.apache.log4j.Logger;
import processor.ImageReaderAndWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class DFTCompressImage extends AbstractFTImageProcess {
    private static final Logger LOGGER = Logger.getLogger(FFTCompressImage.class);

    public Complex[] compress64Ratio(int[][] valuesOf2D, int columnCprRatio, int rowCprRatio) {
        List<Complex[]> blocks = split(valuesOf2D, columnCprRatio, rowCprRatio);
        return blocks.stream().map(complexes -> {
            Complex[] tmp = DFT.inverse(complexes);
            for (int i = 1; i < tmp.length; i++) {
                tmp[i] = Complex.ZERO;
            }
            return DFT.dft(tmp)[0];
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
        DFTCompressImage processor = new DFTCompressImage();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int[][] result = processor.process(pixel);
        stopWatch.stop();
        LOGGER.info("Image compression time: " + stopWatch.getTime());
        reader.write(result, "png", image.getType(), "compressLenna1.png");
    }
}
