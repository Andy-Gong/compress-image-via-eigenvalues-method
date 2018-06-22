import java.io.IOException;
import java.net.URISyntaxException;


public class Application {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ImageReaderAndWriter reader = new ImageReaderAndWriter();
        int[][] pixel = reader.read("lena.png");
        System.out.println(pixel.length);
        System.out.println(pixel[0].length);
        reader.write(pixel, "/tmp/lenabackup.png");
    }
}
