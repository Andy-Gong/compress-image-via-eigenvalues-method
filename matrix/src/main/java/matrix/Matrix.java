package matrix;

import com.google.common.base.Preconditions;

import java.util.Arrays;

public class Matrix {
    private double[][] values;

    public Matrix(int n) {
        values = new double[n][n];
    }

    public Matrix(int n, int m) {
        values = new double[n][m];
    }

    public Matrix(double[] values) {
        this.values = new double[values.length][1];
        for (int i = 0; i < values.length; i++) {
            this.values[i][0] = values[i];
        }
    }

    public Matrix(double[][] values) {
        this.values = values;
    }

    public double[][] getValues() {
        return values;
    }

    public void setValue(int i, int j, double value) {
        values[i][j] = value;
    }

    public void output() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                System.out.print(values[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public Matrix transpose() {
        int n = values.length;
        int m = values[0].length;
        Matrix transMatrix = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                transMatrix.getValues()[i][j] = values[j][i];
            }
        }
        return transMatrix;
    }

    public Matrix multiply(Matrix inputMatrix) {
        Matrix result = new Matrix(this.values.length, inputMatrix.getValues()[0].length);
        for (int i = 0; i < this.values.length; i++) {
            for (int j = 0; j < inputMatrix.getValues()[0].length; j++) {
                double temp = 0;
                for (int k = 0; k < this.values[0].length; k++) {
                    temp += this.values[i][k] * inputMatrix.getValues()[k][j];
                }
                result.getValues()[i][j] = temp;
            }
        }
        return result;
    }

    public Matrix multiply(double value) {
        Matrix result = new Matrix(this.values.length, this.values[0].length);
        for (int i = 0; i < this.values.length; i++) {
            for (int j = 0; j < this.values[0].length; j++) {
                result.getValues()[i][j] = this.values[i][j] * value;
            }
        }
        return result;
    }

    public Matrix sub(Matrix inputMatrix) {
        Matrix result = new Matrix(this.values.length, this.values[0].length);
        for (int i = 0; i < this.values.length; i++) {
            for (int j = 0; j < this.values[0].length; j++) {
                result.getValues()[i][j] = this.values[i][j] - inputMatrix.getValues()[i][j];
            }
        }
        return result;
    }

    public Matrix add(Matrix inputMatrix) {
        Matrix result = new Matrix(this.values.length, this.values[0].length);
        for (int i = 0; i < this.values.length; i++) {
            for (int j = 0; j < this.values[0].length; j++) {
                result.getValues()[i][j] = this.values[i][j] + inputMatrix.getValues()[i][j];
            }
        }
        return result;
    }

    public void clear() {
        for (int i = 0; i < this.values.length; i++) {
            for (int j = 0; j < this.values[0].length; j++) {
                this.values[i][j] = 0;
            }
        }
    }

    public Matrix inverse() {
        Preconditions.checkArgument(values.length == values[0].length, "ONLY square values can be inversed.");
        int row = this.values.length;
        int column = row;
        Matrix result = new Matrix(row, column);
        Matrix tmp = new Matrix(row, 2 * column);
        Matrix identity = Matrix.getIdentityMetrix(row);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < 2 * column; j++) {
                if (j < column) {
                    tmp.setValue(i, j, this.values[i][j]);
                } else {
                    tmp.setValue(i, j, identity.values[i][j - column]);
                }
            }
        }
        Matrix eliminatedMatrix = eliminate(tmp);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.values[i][j] = eliminatedMatrix.values[i][column + j];
            }
        }
        return result;
    }

    public Matrix eliminate(Matrix matrix) {
        Matrix matrix1 = (Matrix) matrix.clone();
        int row = matrix1.values.length;
        int column = matrix1.values[0].length;
        for (int i = 0; i < row; i++) {
            //Handle i row values
            double[] iRowVlaues = matrix1.values[i];
            double iiValue = iRowVlaues[i];
            //eliminate (i,i) value to 1
            for (int j = 0; j < column; j++) {
                matrix1.values[i][j] = matrix1.values[i][j] / iiValue;
            }
            //eliminate other row's except i
            for (int j = 0; j < row; j++) {
                if (i != j) {
                    double value = matrix1.values[j][i];
                    for (int k = 0; k < column; k++) {
                        matrix1.values[j][k] = matrix1.values[j][k] - matrix1.values[i][k] * value;
                    }
                }
            }
        }
        return matrix1;
    }

    @Override
    public Object clone() {
        int row = this.values.length;
        int column = this.values[0].length;

        Matrix matrix = new Matrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrix.setValue(i, j, this.values[i][j]);
            }
        }
        return matrix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(values);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix other = (Matrix) obj;
        if (!Arrays.deepEquals(values, other.values))
            return false;
        return true;
    }

    public static Matrix getIdentityMetrix(int n) {
        Matrix identity = new Matrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    identity.getValues()[i][j] = 1;
                } else {
                    identity.getValues()[i][j] = 0;
                }
            }
        }
        return identity;
    }
}
