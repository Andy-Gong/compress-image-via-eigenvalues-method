package leastsquares;

import matrix.Matrix;

public class LeastSquares {

    /**
     * calculation first_order_linear equation for least squares.
     * like y=ax+b
     *
     * @param points
     * @return
     */
    public Matrix firstOrderLinearEquation(double[][] points) {
        int length = points.length;
        Matrix b = new Matrix(length, 1);
        for (int i = 0; i < length; i++) {
            b.setValue(i, 0, points[i][1]);
        }
        Matrix a = new Matrix(length, 2);
        for (int i = 0; i < length; i++) {
            a.setValue(i, 0, points[i][0]);
            a.setValue(i, 1, 1);
        }
        return a.transpose().multiply(a).inverse().multiply(a.transpose().multiply(b));
    }

    /**
     * calculation second_order_linear equation for least squares.
     * like y=ax^2+bx+c
     *
     * @param points
     * @return
     */
    public Matrix secondOrderLinearEquation(double[][] points) {
        int length = points.length;
        Matrix b = new Matrix(length, 1);
        for (int i = 0; i < length; i++) {
            b.setValue(i, 0, points[i][1]);
        }
        Matrix a = new Matrix(length, 3);
        for (int i = 0; i < length; i++) {
            a.setValue(i, 0, Math.pow(points[i][0], 2));
            a.setValue(i, 1, points[i][0]);
            a.setValue(i, 2, 1);
        }
        a.output();
        return a.transpose().multiply(a).inverse().multiply(a.transpose().multiply(b));
    }

    public static void main(String[] args) {
        LeastSquares ls = new LeastSquares();
        Matrix result = ls.secondOrderLinearEquation(new double[][] {{1, 1}, {2, 2}, {3, 4}, {4, 6}});
        result.output();
    }
}
