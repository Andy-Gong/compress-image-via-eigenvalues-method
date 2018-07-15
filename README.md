# Includes a lot of transform algorithms

The project includes below transform algorithms:
. DFT and FFT, and example to compress image
. Linearsqaures, includes first order linear equation and second order linear equation.

## Fourier transform
### DFT transform
The discrete Fourier transform transforms a sequence of N complex numbers {Xn} :=x0,x1,...,x(n-1) into another sequence of complex numbers, {Xk} :=X0,X1,...,X(n-1), which is defined by

![equation](https://github.com/Andy-Gong/transforms/blob/master/transforms/src/main/resources/math-equations/DFT-math-equation.png)

The discrete Fourier transform is an invertible, linear transformation. In other words, for any N>0, an N-dimensional complex vector has a DFT and an IDFT which are in turn N-dimensional complex vectors.
The inverse transform is given by:

![equation](https://github.com/Andy-Gong/transforms/blob/master/transforms/src/main/resources/math-equations/IDFT-math-equation.png)

Another way of looking at the DFT is to note that in the above discussion, the DFT can be expressed as the DFT matrix, a Vandermonde matrix, introduced by Sylvester in 1867, DFT matrix is defined by

![equation](https://github.com/Andy-Gong/transforms/blob/master/transforms/src/main/resources/math-equations/DFT-matrix.png)

where 

![equation](https://github.com/Andy-Gong/transforms/blob/master/transforms/src/main/resources/math-equations/w(n).png)



### FFT transform
### Performance results between DFT and FFT
In this test, I use lenna image which is 512x512 and size is 463KB.
DFT and FFT compress the image 64 times.

![alt text](https://github.com/Andy-Gong/transforms/blob/master/transforms/src/main/resources/Lenna_color.png)

DFT compression result, which is 64x64 and size is 12KB, it takes 39461 ms.

![alt text](https://github.com/Andy-Gong/transforms/blob/master/DFTCompressLenna.png)

FFT compression result, which is 64x64 and size is 12KB, it takes 1487 ms, it is faster than DFT 26.5 times.

![alt text](https://github.com/Andy-Gong/transforms/blob/master/FFTCompressLenna.png)






