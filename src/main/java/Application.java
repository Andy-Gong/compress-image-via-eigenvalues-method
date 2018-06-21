import java.io.IOException;


public class Application {
    public static void main(String[] args) throws IOException {
        ImageReader reader = new ImageReader();
        int[][] pixel = reader.read("lena.png");
        for (int x = 0; x < pixel.length; x++) {
            for (int y = 0; y < pixel[0].length; y++) {
                System.out.println(pixel[x][y]);
            }
        }

    }
}
