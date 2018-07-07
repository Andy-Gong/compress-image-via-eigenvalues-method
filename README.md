# Includes a lot of transform algorithms
## Fourier transform
### DFT transform
### FFT transform
### Performance results between DFT and FFT
In this test, I use lenna image which is 512x512 and size is 463KB.
DFT and FFT compress the image 64 times.

![alt text](https://github.com/Andy-Gong/transforms/blob/master/src/main/resources/Lenna_color.png)

DFT compression result, which is 64x64 and size is 12KB, it takes 39461 ms.

![alt text](https://github.com/Andy-Gong/transforms/blob/master/DFTCompressLenna.png)

FFT compression result, which is 64x64 and size is 12KB, it takes 1487 ms, it is faster than DFT 26.5 times.

![alt text](https://github.com/Andy-Gong/transforms/blob/master/FFTCompressLenna.png)






