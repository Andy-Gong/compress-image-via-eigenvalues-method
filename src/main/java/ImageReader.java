import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {

    public int[][] read(String filePath) throws IOException {
        BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource(filePath));
        int row = image.getHeight();
        int column = image.getWidth();
        int[][] pixel = new int[row][column];
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < column; y++) {
                pixel[x][y] = image.getRGB(x, y);
            }
        }
        return pixel;
    }
}
