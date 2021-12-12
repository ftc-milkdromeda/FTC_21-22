package Framework.Images;

import java.awt.image.BufferedImage;
import java.io.*;

import Framework.Error;
import Framework.GeneralError;

import javax.imageio.ImageIO;

/**
 * a Class that represents an image as a two-dimensional array of Pixel objects,
 * and also provides methods for manipulating them.
 */
public class Image {
    /**
     * copy constructor
     * @param other the image to be copied.
     */
    public Image(Pixel[][] other)
    {
        this.setImage(other);
    }
    public Image() {
        this.image = null;
    }

    /**
     * sets this image to the bmp image given by the input stream of bytes.
     * @param filename name of file to read.
     * @return Any Errors that crop up
     */
    public Error readImage(String filename) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(filename));
        }
        catch (FileNotFoundException e) {
            return GeneralError.FILE_NOT_FOUND;
        }
        catch (IOException e) {
            return GeneralError.FILE_IO_ERROR;
        }

        System.out.println(img.getType());
        if(img.getType() != BufferedImage.TYPE_3BYTE_BGR && img.getType() != BufferedImage.TYPE_4BYTE_ABGR)
            return ImageError.IMAGE_INCOMPATIBLE_TYPE;

        image = new Pixel[img.getWidth()][img.getHeight()];
        for(int xValue = 0; xValue < image.length; xValue++) {
            for(int yValue = 0; yValue < image[xValue].length; yValue++) {
                this.image[xValue][yValue] = (Pixel) new RGBPixel();
                Pixel.RGB888ToPixel_converter(img.getRGB(xValue, yValue), this.image[xValue][yValue]);
            }
        }

        return GeneralError.NO_ERROR;
    }
    /**
     * sets each pixel in the image to 0,0,0
     * @return Errors, if there are any.
     */
    public Error clearImage() {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        for (Pixel[] rgbPixels : image) {
            for (Pixel rgbPixel : rgbPixels) {
                rgbPixel.setRed(0);
                rgbPixel.setGreen(0);
                rgbPixel.setBlue(0);
            }
        }

        return GeneralError.NO_ERROR;
    }
    /**
     * copy setImage
     * @param image the image that this image will become a copy of
     * @return any errors that crop up
     */
    public Error setImage(Pixel[][] image) {
        this.image = new Pixel[image.length][];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                this.image[i][j] = image[i][j].copy();
            }
        }

        return GeneralError.NO_ERROR;
    }

    /**
     * /crops images by taking advantage of the array implementation of it.
     * @param firstX start of the range we want to keep, horizontally
     * @param lastX end of the range we want to keep, horizontally
     * @param firstY start of the range we want to keep, vertically
     * @param lastY lastX end of the range we want to keep, vertically
     * @return any Errors that crop up.
     */
     public Error cropImage(int firstX, int firstY, int lastX, int lastY) {
         if(this.image == null)
             return ImageError.IMAGE_NOT_SET;

         if(firstX > lastX) {
             int tempX = lastX;
             lastX = firstX;
             firstX = tempX;
         }
         if(firstY > lastY) {
             int tempY = lastY;
             lastY = firstY;
             firstY = tempY;
         }

         int width = this.image[0] != null ? this.image[0].length : 0;

         firstX = firstX < 0 ? 0 : firstX;
         firstY = firstY < 0 ? 0 : firstY;
         lastX = lastX > this.image.length ? this.image.length : lastX;
         lastY = lastY > width ?  width: lastY;

         Pixel[][] buffer = new Pixel[lastX - firstX][lastY - firstY];

         for(int i = firstX; i < lastX; i++) {
             for(int j = firstY; j < lastY; j++) {
                 buffer[i - firstX][j - firstY] = this.image[i][j].copy();
             }
         }

        this.image = buffer;

        return GeneralError.NO_ERROR;
     }

    /**
     * Scales the image by a horizontal and vertical factor. This factor must be greater than 0.
     * @param xScale the horizontal scaling factor
     * @param yScale the vertical scaling factor
     * @return
     */
    public Error scaleImage(double xScale, double yScale) {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        if(xScale <= 0 || yScale <= 0) {
            return ImageError.SCALE_FACTOR_NEGATIVE_ERROR;
        }
        int newX = (int)(xScale * image.length);
        int newY = (int)(yScale * image[0].length);

        xScale = 1/xScale;
        yScale = 1/yScale;
        Coordinate[][] rect0 = new Coordinate[newX][newY];

        double rectX = 0;
        for(int i = 0; i < newX; i++) {
            double rectY = 0;
            for(int j = 0; j < newY; j++) {
                rect0[i][j] = new Coordinate(rectX, rectY);
                rectY += yScale;
            }
            rectX += xScale;
        }
        Pixel[][] newImage = new Pixel[newX][newY];
        final int pixelPerIter = (int)(Math.ceil(xScale) * Math.ceil(yScale));
        class PixelData {
            Pixel pix;
            double freq;
            PixelData(Pixel pix, double freq)
            {
                this.pix = pix;
                this.freq = freq;
            }
        }
        for(int i = 0; i < newX; i++) {
            for (int j = 0; j < newY; j++)
            {
                double averageR = 0;
                double averageG = 0;
                double averageB = 0;
                for(int x = (int)rect0[i][j].x; x < Math.ceil(rect0[i][j].x + xScale); x++)
                {

                    double pixelXLength =( (x + 1) < (xScale + rect0[i][j].x) ? (x + 1) : (xScale + rect0[i][j].x))-
                        ((x > rect0[i][j].x)? x : rect0[i][j].x);

                    for(int y = (int)rect0[i][j].y; y < Math.ceil(rect0[i][j].y + yScale); y++)
                    {
                        double pixelYLength =( (y + 1) < (yScale + rect0[i][j].y) ? (y + 1) : (yScale + rect0[i][j].y))-
                                ((y > rect0[i][j].y)? y : rect0[i][j].y);
                        averageR += Math.pow(image[x][y].getRed(), 2) * pixelXLength * pixelYLength / (xScale * yScale);
                        averageG += Math.pow(image[x][y].getGreen(), 2) * pixelXLength * pixelYLength / (xScale * yScale);
                        averageB += Math.pow(image[x][y].getBlue(), 2) * pixelXLength * pixelYLength / (xScale * yScale);



                    }

                }
                averageR = Math.pow(averageR,0.5);
                averageG = Math.pow(averageG,0.5);
                averageB = Math.pow(averageB,0.5);
                newImage[i][j] = new RGBPixel(averageR,averageG,averageB);
            }
        }

        image = newImage;
        return GeneralError.NO_ERROR;
    }

    public Error curveAdjAll(Pixel.Curve curve) {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        for(int i = 0 ; i < this.image.length; i++) {
            for(int j = 0; j < this.image[0].length; j++) {
                this.image[i][j].curveAll(curve);
            }
        }

        return GeneralError.NO_ERROR;
    }
    public Error curveAdjR(Pixel.Curve curve) {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        for(int i = 0 ; i < this.image.length; i++) {
            for(int j = 0; j < this.image[0].length; j++) {
                this.image[i][j].curveR(curve);
            }
        }

        return GeneralError.NO_ERROR;

    }
    public Error curveAdjG(Pixel.Curve curve) {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        for(int i = 0 ; i < this.image.length; i++) {
            for(int j = 0; j < this.image[0].length; j++) {
                this.image[i][j].curveG(curve);
            }
        }

        return GeneralError.NO_ERROR;
    }
    public Error curveAdjB(Pixel.Curve curve) {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        for(int i = 0 ; i < this.image.length; i++) {
            for(int j = 0; j < this.image[0].length; j++) {
                this.image[i][j].curveB(curve);
            }
        }

        return GeneralError.NO_ERROR;

    }

    /**
     * getter for individual pixels
     * @param x horizontal position of pixel
     * @param y vertical position of pixel
     * @return
     */
    public Pixel getPixel(int x, int y) {
        if(this.image == null)
            return null;

        return image[x][y];
    }
    /**
     * another getter, now for the image itself
     * @return the entire image
     */
    public Pixel[][] getImage() {
        return image;
    }

    /**
     * Saves the image to a file, using some hacky fiddling.
     * @param filename the name of the file the image is going to be saved to.
     * @return any errors that crop up.
     */
    public Error saveImage(String filename) {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        BufferedImage image = new BufferedImage(this.image.length, this.image[0] != null ? this.image[0].length : 0, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < this.image.length; i++) {
            for(int j = 0; j < this.image[i].length; j++) {
                image.setRGB(i, j, Pixel.PixelToRGB888_converter(this.image[i][j]));
            }
        }

        try {
            FileOutputStream write = new FileOutputStream(filename);
            ImageIO.write(image, "png", write);
        }
        catch(IOException e) {
            return GeneralError.FILE_IO_ERROR;
        }

        return GeneralError.NO_ERROR;
    }

    public Error toGray() {
        if(this.image == null)
            return ImageError.IMAGE_NOT_SET;

        for(int i = 0; i < this.image.length; i++) {
            for(int j = 0; j < this.image[0].length; j++) {
                if(this.image[i][j].isGray())
                    continue;
                this.image[i][j] = ((RGBPixel) this.image[i][j]).toMonochrome();
            }
        }

        return GeneralError.NO_ERROR;
    }

    /**
     * private class that'll help with downsampling/scaling
     */
    private static class Coordinate {
        double x, y;
        Coordinate(double x, double y)
        {
            this.x = x;
            this.y = y;
        }
    }

    //column by row, so width, then height/x then y
    private Pixel[][] image;
}
