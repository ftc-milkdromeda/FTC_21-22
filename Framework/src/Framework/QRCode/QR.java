package Framework.QRCode;

import Framework.Error;
import Framework.GeneralError;
import Framework.Images.Image;
import Framework.Images.RGBPixel;
import Framework.Units.AngleUnits;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import javax.sound.sampled.Line;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Deprecated
public class QR {
    public QR(String fileName) {
        byte[] inputBuffer;

        File file;
        InputStream stream;

        try {
            file = new File(fileName);
            stream = new FileInputStream(file);

            inputBuffer = new byte[(int)file.length()];
            stream.read(inputBuffer);
        }
        catch (FileNotFoundException e) {
            this.constructOut = GeneralError.FILE_NOT_FOUND;
            return;
        }
        catch (IOException e) {
            this.constructOut = GeneralError.FILE_IO_ERROR;
            return;
        }

        try {
            stream.close();
        }
        catch (IOException e) {
            this.constructOut = GeneralError.FILE_IO_ERROR;
            return;
        }

        if(inputBuffer.length != inputBuffer[0] * inputBuffer[1] + 34) {
            this.constructOut = QRCodeError.QR_CODE_LENGTH_MISMATCH;
            return;
        }

        /*
        File numbers formatted in big endian

        File Header:
            1 byte x size
            1 byte y size
            32 bytes code SHA256 hash

        Code encoded in raw bytes: >0x00 light pixel; 0x00 dark pixel.
        */

        byte[] qrCode = new byte[inputBuffer[0] * inputBuffer[1]];
        System.arraycopy(inputBuffer, 34, qrCode, 0, inputBuffer[0] * inputBuffer[1]);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(qrCode);

            for(int i = 0; i < 32; i++) {
                if(hash[i] != hash[i + 2]) {
                    this.constructOut = QRCodeError.QR_CODE_CORRUPTED;
                    return;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Issues with software development, algorithm does do exist");
            this.constructOut = GeneralError.STUPIDITY_ERROR;
            return;
        }

        this.qrCode = new Code(qrCode, inputBuffer[0], inputBuffer[1]);
    }
    public QR(Image image, int x, int y) {
        image.curveAdjAll(value -> value < 0.5 ? 0.0 : 1.0);
    }
    protected QR(boolean[] qrCode, int x, int y) {
        if(qrCode.length == x * y) {
            this.constructOut = QRCodeError.QR_CODE_LENGTH_MISMATCH;
            return;
        }

        this.qrCode = new Code(qrCode, x, y);
    }

    public boolean validate() {
        this.checkValid = true;
        return this.constructOut == GeneralError.NO_ERROR;
    }
    public Error constructionOutput() {
        return this.constructOut;
    }

    public boolean equals(QR qrCode) {
        return this.qrCode.equals(qrCode);
    }

    private Code qrCode;

    private static class Code {
        public Code(boolean[] buffer, int x, int y) {
            this.code = new Pixel[x][y];

            for(int i = 0; i < x; i++) {
                for(int j = 0; j < y; j++)
                    this.code[i][j] = buffer[i * y + j] ? Pixel.LIGHT : Pixel.DARK;
            }
        }
        public Code(byte[] buffer, int x, int y) {
            this.code = new Pixel[x][y];

            for(int i = 0; i < x; i++) {
                for(int j = 0; j < y; j++)
                    this.code[i][j] = buffer[i * y + j] != 0x00 ? Pixel.LIGHT : Pixel.DARK;
            }
        }

        public Pixel getPixel(int x, int y) {
            return code[x][y] ;
        }

        public boolean equals(Code code) {
            if(this.code.length != code.code.length || this.code[0].length != code.code[0].length)
                return false;

            for(int i = 0; i < this.code.length; i++) {
                for(int j = 0; j < this.code[i].length; j++) {
                    if (this.code[i][j] != code.code[i][j])
                        return false;
                }
            }

            return true;
        }

        private final Pixel [][] code;
    }

    public enum Pixel {
        LIGHT(false),
        DARK(true);

        Pixel(boolean state) {
            this.state = state;
            this.value = state ? 0xff : 0x00;
        }

        public final boolean state;
        public final int value;
    }

    private static boolean[] qrCode(Image image, int x, int y) {
        return new boolean[0];
    }

    public static Image testQR(Image image) {
        QR.readQR(image);

        return image;
    }

    private static Image readQR(Image image) {
        long startTime = System.currentTimeMillis();

        //scaling images
        final int maxSize = 150;

        if(image.getWidth() > maxSize || image.getHeight() > maxSize)
            image.scaleImage((double)maxSize/Math.max(image.getHeight(), image.getWidth()), (double)maxSize/Math.max(image.getHeight(), image.getWidth()));

        //adjusting image
        double brightest = image.findBrightest().getLuminance();
        double dimmest = image.findDimmest().getLuminance();

        image.toMono();
        image.curveAdjAll(x -> x < (brightest - dimmest) * .55 + dimmest  ? 0.0 : 1.0);

        class Coordinate {
            Coordinate(int x, int y) {
                this.x = x;
                this.y = y;
            }
            int x;
            int y;
        }

        //finding reference points
        class FindPoint {
            public FindPoint(int x, int y, Image image) {
                this.coordinate = new Coordinate(x, y);
                this.image = image;
            }

            public void run() {
                //tests if border pixel
                if(this.image.getPixel(this.coordinate.x, this.coordinate.y).getLuminance() > 0.5)
                    return;

                if(this.coordinate.y > 0 && this.image.getPixel(this.coordinate.x, this.coordinate.y - 1).getLuminance() > 0.5) {
                    if(this.type != BoardType.NONE) {
                        this.type = BoardType.NONE;
                        return;
                    }

                    this.type = BoardType.UP;
                }

                if(this.coordinate.y < this.image.getHeight() - 1 && this.image.getPixel(this.coordinate.x, this.coordinate.y + 1).getLuminance() > 0.5) {
                    if(this.type != BoardType.NONE) {
                        this.type = BoardType.NONE;
                        return;
                    }

                    this.type = BoardType.DOWN;
                }

                if(this.coordinate.x > 0 && this.image.getPixel(this.coordinate.x - 1, this.coordinate.y).getLuminance() > 0.5) {
                    if(this.type != BoardType.NONE) {
                        this.type = BoardType.NONE;
                        return;
                    }

                    this.type = BoardType.RIGHT;
                }

                if(this.coordinate.x < this.image.getWidth() - 1 && this.image.getPixel(this.coordinate.x + 1, this.coordinate.y).getLuminance() > 0.5) {
                    if(this.type != BoardType.NONE) {
                        this.type = BoardType.NONE;
                        return;
                    }

                    this.type = BoardType.LEFT;
                }
            }

            public int sortValue() {
                if(this.type == BoardType.NONE)
                    return 0;
                return this.type == BoardType.UP || this.type == BoardType.DOWN ? this.coordinate.y : this.coordinate.x;
            }


            final Coordinate coordinate;
            final Image image;

            BoardType type = BoardType.NONE;

        }

        class FindPointCompare implements Comparator<FindPoint> {
            @Override
            public int compare(FindPoint findPoint, FindPoint t1) {
                if(findPoint.type != t1.type)
                    return 0;

                return findPoint.sortValue() - t1.sortValue();
            }

        }

        LinkedList<FindPoint> rawdata = new LinkedList<>();

        //running threads
        for(int i = 0; i < image.getWidth(); i++) {
            for(int j = 0; j < image.getHeight(); j++) {
                FindPoint point = new FindPoint(i, j, image);
                point.run();
                rawdata.add(point);

            }
        }

        //wait for process
        ListIterator<FindPoint> iter;// = threadList.listIterator();

        //check process
        iter = rawdata.listIterator();
        while(iter.hasNext()) {
            FindPoint point = iter.next();
            if(point.type == BoardType.NONE)
                iter.remove();
        }

        //sorting by bound type
        LinkedList<FindPoint>[] sortedPoints = new LinkedList[] {
                new LinkedList<FindPoint>(),
                new LinkedList<FindPoint>(),
                new LinkedList<FindPoint>(),
                new LinkedList<FindPoint>()
        };

        for(FindPoint point : rawdata)
            sortedPoints[point.type.getValue()].add(point);

        //sorting into lines
        class Lines {
            public Lines(double m, double y_int) {
                this.a = -m;
                this.b = 1;
                this.c = y_int;
            }
            public Lines(double a, double b, double c) {
                this.a = a;
                this.b = b;
                this.c = c;
            }

            double evaluateX(double x) {
                return -a/b * x + c/b;
            }

            double evaluateY(double y) {
                return -b/a * y + c/a;
            }

            double a;
            double b;
            double c;
        }

        FindPoint[][] array_sortedPoints = new FindPoint[4][];

        for(int i = 0; i < 4; i++) {
            array_sortedPoints[i] = new FindPoint[sortedPoints[i].size()];
            array_sortedPoints[i] = sortedPoints[i].toArray(array_sortedPoints[i]);
        }

        FindPointCompare comparator = new FindPointCompare();

        for(int i = 0; i < 4; i++)
            Arrays.sort(array_sortedPoints[i], comparator);


        //linear regression
        final int lineTolerence = 2;

        Iterator<FindPoint> arrayIterator;
        LinkedList<Lines> lines = new LinkedList<>();
        SimpleRegression regression =  new SimpleRegression();

        for(int i = 0; i < array_sortedPoints.length; i++) {
            arrayIterator = Arrays.stream(array_sortedPoints[i]).iterator();

            while(arrayIterator.hasNext()) {
                FindPoint current = arrayIterator.next();
                LinkedList<Coordinate> data = new LinkedList<>();
                int referencePoint = 0;

                do {
                    regression.addData(current.coordinate.x, current.coordinate.y);
                    data.add(new Coordinate(current.coordinate.x, current.coordinate.y));

                    referencePoint = current != null ? current.sortValue(): -100;
                    current = (arrayIterator.hasNext()) ? arrayIterator.next() : null;
                } while (current != null && Math.abs(current.sortValue() - referencePoint) <= lineTolerence);

                System.out.println("Slope: " + regression.getSlope());

                if(regression.getN() > 15 && regression.getSlope() == Double.NaN) {
                    double average = 0;
                    for(Coordinate value : data)
                        average += value.x / data.size();
                    lines.add(new Lines(1, 0, average));
                }
                else if(regression.getN() > 15 && (BoardType.acceptableSlope(regression.getSlope()))) {
                    lines.add(new Lines(regression.getSlope(), regression.getIntercept()));
                    System.out.println(regression.getRSquare());
                }
                regression.clear();
            }
        }

        //finding all intersection


        //testing portion
        int numOfPoints = 0;
        for(FindPoint point : rawdata) {
            numOfPoints++;
        }
        System.out.println("Num of points: " + numOfPoints);
        System.out.println("ms to complete: " + (System.currentTimeMillis() - startTime));
        System.out.println("Num of lines: " + lines.size());
        for(Lines line : lines) {
            System.out.println("a=" + line.a + ", b=" + line.b + ", c=" + line.c);
        }

        Image newImage = new Image(image);
        newImage.toRGB();
        newImage.curveAdjAll(x -> x < 0.5 ? 0.9 : 1.0);

        /*
        LEFT: red
        DOWN: green
        RIGHT: blue
        UP: magenta
         */
        for(FindPoint point : rawdata) {
            switch (point.type) {
                case LEFT:
                    newImage.setPixel(point.coordinate.x, point.coordinate.y, new RGBPixel(1.0, 0.0, 0.0));
                    break;
                case DOWN:
                    newImage.setPixel(point.coordinate.x, point.coordinate.y, new RGBPixel(0.0, 1.0, 0.0));
                    break;
                case RIGHT:
                    newImage.setPixel(point.coordinate.x, point.coordinate.y, new RGBPixel(0.0, 0.0, 1.0));
                    break;
                case UP:
                    newImage.setPixel(point.coordinate.x, point.coordinate.y, new RGBPixel(1.0, 0.0, 1.0));
                    break;
            }
        }

        //graphing lines
        for(Lines line : lines) {
           for(int x = 0; x < image.getWidth(); x++) {
               int y = (int)Math.round(line.evaluateX(x));
               if(y >= 0 && y < image.getHeight())
                   newImage.curveAdjAll(value -> value * 0.5, x, y);
           }

           for(int y = 0; y < image.getHeight(); y++) {
               int x= (int)Math.round(line.evaluateY(y));
               if(x >= 0 && x < image.getWidth())
                   newImage.curveAdjAll(value -> value * 0.5, x, y);
           }
        }

        newImage.saveImage("pixelmap/PointOutput0-" + (int)(1000*Math.random()) + ".png");

        return image;
    }


    private Error constructOut = GeneralError.NO_ERROR;
    private boolean checkValid = true;

    private enum BoardType {
        //where the white pixel is
        DOWN(0), RIGHT(1), LEFT(2), UP(3), NONE(4);
        BoardType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static boolean acceptableSlope(double value) {
            final double acceptableAngle = AngleUnits.DEG.toBase(10);

            return Math.tan(acceptableAngle) > Math.abs(value) || 1/Math.tan(acceptableAngle) < Math.abs(value);
        }

        private int value;
    }
}