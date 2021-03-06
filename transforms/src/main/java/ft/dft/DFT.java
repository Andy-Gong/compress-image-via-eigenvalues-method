package ft.dft;

import org.apache.commons.math3.complex.Complex;

public class DFT {

    public static Complex[] dft(Complex[] input) {
        int length = input.length;
        Complex[] output = new Complex[length];
        for (int k = 0; k < length; k++) {
            output[k] = new Complex(0.0, 0.0);
            for (int n = 0; n < length; n++) {
                double p = -(2 * Math.PI * k * n) / length;
                output[k] = output[k].add(new Complex(Math.cos(p), Math.sin(p)).multiply(input[n]));
            }
        }
        return output;
    }

    public static Complex[] inverse(Complex[] input) {
        int length = input.length;
        Complex[] output = new Complex[length];
        for (int k = 0; k < length; k++) {
            output[k] = new Complex(0.0, 0.0);
            for (int n = 0; n < length; n++) {
                double p = (2 * Math.PI * k * n) / length;
                output[k] = output[k].add(new Complex(Math.cos(p), Math.sin(p)).multiply(input[n]));
            }
            output[k] = output[k].divide(length);
        }
        return output;
    }

    public static void main(String[] args) {
        //4 nodes ft.dft.fft
        Complex[] input =
                new Complex[] {new Complex(4), new Complex(2), new Complex(2), new Complex(4),
                        new Complex(2), new Complex(2), new Complex(4), new Complex(2)};
        Complex[] output = DFT.dft(input);
        for (int i = 0; i < input.length; i++) {
            System.out.println(output[i]);
        }

        Complex[] inverseResult = DFT.inverse(output);
        for (int i = 0; i < input.length; i++) {
            System.out.println(inverseResult[i]);
        }
    }
}
