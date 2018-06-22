package processor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class ImageReaderAndWriter {

    public int[][] read(String filePath) throws IOException {
        BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource(filePath));
        int row = image.getHeight();
        int column = image.getWidth();
        int[][] pixels = new int[row][column];
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < column; y++) {
                pixels[x][y] = image.getRGB(x, y);
            }
        }
        return pixels;
    }

    public void write(int[][] pixelsRGB, String outputFile) throws IOException {
        int width = pixelsRGB[0].length;
        int height = pixelsRGB.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixelRGB = pixelsRGB[i][j];
                image.setRGB(i, j, pixelRGB);
            }
        }
        ImageIO.write(image, "png", new File(outputFile));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ImageReaderAndWriter reader = new ImageReaderAndWriter();
        int[][] pixel = reader.read("lenna.png");
        System.out.println(pixel.length);
        System.out.println(pixel[0].length);
        reader.write(pixel, "/tmp/lenabackup.png");
    }
}
