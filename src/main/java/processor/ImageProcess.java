package processor;


import com.jcabi.aspects.Loggable;

public interface ImageProcess {

    @Loggable(Loggable.DEBUG)
    int[][] process(int[][] pixelRGB);
}
