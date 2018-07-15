package ft.processor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class ImageReaderAndWriter {

    public BufferedImage getImage(String filePath) throws IOException {
        return ImageIO.read(getClass().getClassLoader().getResource(filePath));
    }

    public int[][] getPixels(BufferedImage bufferedImage) throws IOException {
        int row = bufferedImage.getHeight();
        int column = bufferedImage.getWidth();
        int[][] pixels = new int[row][column];
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < column; x++) {
                pixels[y][x] = bufferedImage.getRGB(x, y);
            }
        }
        return pixels;
    }

    public void write(int[][] pixelsRGB, String formatName, int type, String outputFile) throws IOException {
        int width = pixelsRGB[0].length;
        int height = pixelsRGB.length;
        BufferedImage image = new BufferedImage(width, height, type);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixelRGB = pixelsRGB[i][j];
                image.setRGB(j, i, pixelRGB);
            }
        }
        ImageIO.write(image, formatName, new File(outputFile));
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ImageReaderAndWriter reader = new ImageReaderAndWriter();
        BufferedImage image = reader.getImage("lenna.png");
        int[][] pixel = reader.getPixels(image);
        reader.write(pixel, "png", image.getType(), "/tmp/lenabackup.png");
    }
}
