package processor.impl;

public class ImageDecomposeLayers {
    private int[][] pixelRGB;
    private int originalRow;
    private int originalColumn;
    private int[][] valueAlpha;
    private int[][] valueRed;
    private int[][] valueGreen;
    private int[][] valueBlue;

    public ImageDecomposeLayers(int[][] pixelRGB, int originalRow, int originalColumn) {
        this.pixelRGB = pixelRGB;
        this.originalRow = originalRow;
        this.originalColumn = originalColumn;
    }

    public int[][] getValueAlpha() {
        return valueAlpha;
    }

    public int[][] getValueRed() {
        return valueRed;
    }

    public int[][] getValueGreen() {
        return valueGreen;
    }

    public int[][] getValueBlue() {
        return valueBlue;
    }

    public ImageDecomposeLayers invoke() {
        valueAlpha = new int[pixelRGB.length][pixelRGB[0].length];
        valueRed = new int[pixelRGB.length][pixelRGB[0].length];
        valueGreen = new int[pixelRGB.length][pixelRGB[0].length];
        valueBlue = new int[pixelRGB.length][pixelRGB[0].length];

        for (int h = 0; h < originalRow; h++) {
            for (int i = 0; i < originalColumn; i++) {
                int pixel = pixelRGB[h][i];
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                valueAlpha[h][i] = alpha;
                valueRed[h][i] = red;
                valueGreen[h][i] = green;
                valueBlue[h][i] = blue;

            }
        }
        return this;
    }
}
