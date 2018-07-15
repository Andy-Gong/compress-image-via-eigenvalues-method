package matrix;

import org.junit.Test;

public class MatrixTest {


    @Test
    public void testReverse() {
        double[][] values = new double[][] {{1, 2, 3, 4}, {2, 2, 1, 5}, {3, 4, 3, 6}, {5, 4, 3, 8}};
        Matrix initial = new Matrix(values);
        Matrix result = initial.inverse();
        result.output();
    }
}
