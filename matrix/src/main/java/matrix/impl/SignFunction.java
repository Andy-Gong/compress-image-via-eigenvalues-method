package matrix.impl;

import matrix.Function;

public class SignFunction implements Function {

    @Override
    public double value(double x) {
        if (x > 0) {
            return 1;
        } else if (x < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
