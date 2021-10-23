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
     * @param in
     * @return
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
        image = new Pixel[tempImage.image.getHeight()][tempImage.image.getWidth()];
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
     * scaling images, making them bigger or smaller
    *using a simple nearest neighbor interpolation.
     */
    public Error scaleImage(int outputX, int outputY)
    {
        //first, find the scale factors for the new width and height.
        double scaleX = (double)outputX/image.length;
        double scaleY = (double)outputY/image[0].length;
        //create output image
        Pixel[][] output = new Pixel[outputX][outputY];

        //for each pixel in the output, use the closest(in coordinates) pixel from the input image.
        for(int i = 0; i < outputX; i++)
        {
            for(int j = 0; j < outputY; j++)
            {
                //actually doing the scaling
                output[i][j] = image[(int)(i/scaleX)][(int)(j/scaleY)];
            }
        }
    image = output;
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
     public Error cropImage(int firstX, int lastX, int firstY, int lastY)
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
     * downscaling, average the pixels out, using a box averaging algorithm
     * @param outputX the width of the image after scaling
     * @param outputY the height of the image after scaling
     * @return
     */

    public Error downscaleImage(int outputX, int outputY)
    {

        //output image created
        Pixel[][] output = new Pixel[outputX][outputY];

        //width and height of boxes made
        int boxWidth = (int)((double)image.length/outputX + 1);
        int boxHeight = (int)((double)image[0].length/outputY + 1);

        //for each pixel in output, replace with average of all values in the box corresponding to
        //the output pixel in the input image.
        for(int i = 0; i < outputX; i++)
        {
            for (int j = 0; j < outputY; j++)
            {
            int endX = Math.min(i + boxWidth,image.length - 1);
            int endY = Math.min(j + boxHeight,image[0].length - 1);

            Pixel average = new Pixel();
                for(int k = i; k < endX; i++)
                {
                    for (int l = j; l < endY; j++)
                    {
                        average.addPixel(image[k][l]);

                    }
                }
                int boxSize = (endX - i) * (endY - j);
                average.setPixel(average.getRed()/boxSize, average.getGreen()/boxSize, average.getBlue()/boxSize);
                output[i][j] = new Pixel(average);
            }
        }
        image = output;
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
}
