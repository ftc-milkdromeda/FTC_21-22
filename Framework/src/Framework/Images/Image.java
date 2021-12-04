package Framework.Images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import Framework.Data.DataError;
import Framework.Error;
import Framework.GeneralError;
import io.nayuki.bmpio.BmpImage;
import io.nayuki.bmpio.BmpReader;
import io.nayuki.bmpio.BmpWriter;
import io.nayuki.bmpio.BufferedRgb888Image;

/**
 * a Class that represents an image as a two-dimensional array of Pixel objects,
 * and also provides methods for manipulating them.
 */
public class Image {
    //column by row, so width, then height/x then y
    private Pixel[][] image;

    /**
     * copy constructor
     * @param other the image to be copied.
     */
    public Image(Pixel[][] other)
    {
        setImage(other);
    }

    /**
     * create an image from an input stream of bytes, using the setImage method()
     * @param in can be anything, like a file.
     */
    public Image(InputStream in)
    {
        setImage(in);
    }

    /**
     * sets this image to the bmp image given by the input stream of bytes.
     * @param in the input stream
     * @return Any Errors that crop up
     */
    public Error setImage(InputStream in)
    {
        BmpImage tempImage;
        try {
            tempImage = BmpReader.read(in);

        }
        catch(Exception e)
        {
            return DataError.FILE_WRITING_ERROR;
        }
        image = new Pixel[tempImage.image.getWidth()][tempImage.image.getHeight()];
        for(int xValue = 0; xValue < image.length; xValue++)
        {
            for(int yValue = 0; yValue < image[xValue].length; yValue++)
            {
                int rgb = tempImage.image.getRgb888Pixel(xValue,yValue);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                image[xValue][yValue] = new Pixel(red, green, blue);

            }
        }


        return GeneralError.NO_ERROR;
    }

    /**
     * sets each pixel in the image to 0,0,0
     * @return Errors, if there are any.
     */
    public Error setImage()
    {
        for(int i = 0; i < image.length; i++)
        {
            for(int j = 0; j < image[i].length; j++)
            {
                image[i][j].setPixel(0,0,0);
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
        //this.image = image;
        this.image = new Pixel[image.length][image[0].length];
        for(int i = 0; i < image.length; i++)
        {
            for(int j = 0; j < image[i].length; j++)
            {
                image[i][j] = new Pixel(image[i][j]);
            }
        }
        return GeneralError.NO_ERROR;
    }


    /**
     * color curve method, sets all pixels in range according to the formula.
     * @param c A colorcurve object that defines the formula that each pixel will be put through.
     * @return any Errors that crop up.
     */
    public Error curveImage(ColorCurves c)
    {
        for(int i = 0; i < image.length; i++)
        {
            for(int j = 0; j < image[i].length; j++)
            {
                c.evaluate(image[i][j]);
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
     public Error cropImage(int firstX, int firstY, int lastX, int lastY)
     {
         Pixel[][] temp = image;
         image = new Pixel[Math.abs(lastX - firstX)][Math.abs(lastY - firstY)];

         if(firstX > lastX)
         {
             int tempX = lastX;
              lastX = firstX;
              firstX = tempX;
         }
         if(firstY > lastY)
         {
             int tempY = lastY;
             lastY = firstY;
             firstY = tempY;
         }
         for(int i = firstX; i < lastX; i++)
         {
             for(int j = firstY; j < lastY; j++)
             {
                 image[i - firstX][j - firstY] = new Pixel(temp[i][j]);
             }
         }
        return GeneralError.NO_ERROR;
     }


    /**
     * Scales the image by a horizontal and vertical factor. This factor must be greater than 0.
     * @param xScale the horizontal scaling factor
     * @param yScale the vertical scaling factor
     * @return
     */
    public Error scaleImage(double xScale, double yScale)
    {
        if(xScale <= 0 || yScale <= 0)
        {
            return ImageError.SCALE_FACTOR_NEGATIVE_ERROR;
        }
        int newX = (int)(xScale * image.length);
        int newY = (int)(yScale * image[0].length);

        xScale = 1/xScale;
        yScale = 1/yScale;
        Coordinate[][] rect0 = new Coordinate[newX][newY];

        double rectX = 0;
        for(int i = 0; i < newX; i++)
        {
            double rectY = 0;
            for(int j = 0; j < newY; j++)
            {
                rect0[i][j] = new Coordinate(rectX, rectY);
                rectY += yScale;
            }
            rectX += xScale;
        }
        Pixel[][] newImage = new Pixel[newX][newY];
        final int pixelPerIter = (int)(Math.ceil(xScale) * Math.ceil(yScale));
        class PixelData
        {
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
                newImage[i][j] = new Pixel((int)averageR,(int)averageG,(int)averageB);
            }
        }

        image = newImage;
        return GeneralError.NO_ERROR;
    }


    /**
     * getter for individual pixels
     * @param x horizontal position of pixel
     * @param y vertical position of pixel
     * @return
     */
    public Pixel getPixel(int x, int y)
    {
        return image[x][y];
    }

    /**
     * another getter, now for the image itself
     * @return the entire image
     */
    public Pixel[][] getImage()
    {
        return image;
    }

    /**
     * Saves the image to a file, using some hacky fiddling.
     * @param filename the name of the file the image is going to be saved to.
     * @return any errors that crop up.
     */
    public Error fileImage(String filename)
    {
        File outFile = new File(filename + ".bmp");
        try
        {
            if(!outFile.createNewFile())
            {
                System.out.println("File not created; Already exists.");
            }
        }
        catch(Exception e)
        {
            return DataError.FILE_ALREADY_EXISTS_ERROR;

        }
        /**
         * have to mess around with formats and types
         */
        BmpImage hackyImage = new BmpImage();

        BufferedRgb888Image writtenImage = new BufferedRgb888Image(image.length,image[0].length);
        hackyImage.image = writtenImage;
        for(int i = 0; i < image.length; i++)
        {
            for(int j = 0; j < image[0].length; j++)
            {
                int color = image[i][j].getRed();
                color = (color << 8) + image[i][j].getGreen();
                color = (color << 8) + image[i][j].getBlue();

                writtenImage.setRgb888Pixel(i,j,color);
            }
        }


        try
        {
            FileOutputStream myWriter = new FileOutputStream(outFile.getPath());
            BmpWriter.write(myWriter,hackyImage);
        }
        catch(Exception e)
        {
            return DataError.FILE_WRITING_ERROR;
        }


        return GeneralError.NO_ERROR;
    }


    /**
     * private class that'll help with downsampling/scaling
     */
    private class Coordinate
    {
        double x, y;
        Coordinate(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

    }
}
