package fft;

import dft.DFT;
import org.apache.commons.math3.complex.Complex;

public class FFT {

    public Complex[] fft(Complex[] input) {
        int length = input.length;
        Complex[] output = new Complex[length];

        if (length % 2 != 0) {
            return input;
        }
        //even
        Complex[] evenInput = new Complex[length / 2];
        Complex[] oddInput = new Complex[length / 2];

        for (int i = 0; i < length / 2; i++) {
            evenInput[i] = input[2 * i];
            oddInput[i] = input[2 * i + 1];
        }

        Complex[] eventOutput = fft(evenInput);
        Complex[] oddOutput = fft(oddInput);

        for (int i = 0; i < length / 2; i++) {
            double p = -2 * Math.PI * i / length;
            Complex w = new Complex(Math.cos(p), Math.sin(p));
            output[i] = eventOutput[i].add(oddOutput[i].multiply(w));
            output[i + length / 2] = eventOutput[i].subtract(oddOutput[i].multiply(w));
        }
        return output;
    }

    public static void main(String[] args) {
        //4 nodes fft
        Complex[] input =
                new Complex[] {new Complex(4), new Complex(4), new Complex(4), new Complex(4), new Complex(4), new Complex(4), new Complex(4), new Complex(4)};
        FFT fft = new FFT();
        Complex[] output = fft.fft(input);
        for (int i = 0; i < input.length; i++) {
            System.out.println(output[i]);
        }
    }
}
