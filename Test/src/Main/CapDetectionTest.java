package Main;

import Framework.Drivers.DriverType;
import Framework.Error;
import Framework.GeneralError;
import Framework.Images.Image;
import Framework.Images.Pixel;

public class CapDetectionTest {
    public static void main(String[] args) {
        CapDetection cap = new CapDetection();

        Image[] images = new Image[12];
        CapDetection.Coordinate[] coords = new CapDetection.Coordinate[12];
        for(int i = 0; i < images.length; i++)
            images[i] = new Image();

        System.out.println(images[0].readImage("BlueCarousel1.bmp"));
        System.out.println(images[1].readImage("BlueCarousel2.bmp"));
        System.out.println(images[2].readImage("BlueCarousel3.bmp"));
        System.out.println(images[3].readImage("BlueWarehouse1.bmp"));
        System.out.println(images[4].readImage("BlueWarehouse2.bmp"));
        System.out.println(images[5].readImage("BlueWarehouse3.bmp"));

        System.out.println(images[6].readImage("RedCarousel1.bmp"));
        System.out.println(images[7].readImage("RedCarousel2.bmp"));
        System.out.println(images[8].readImage("RedCarousel3.bmp"));
        System.out.println(images[9].readImage("RedWarehouse1.bmp"));
        System.out.println(images[10].readImage("RedWarehouse2.bmp"));
        System.out.println(images[11].readImage("RedWarehouse3.bmp"));

        coords[0] = new CapDetection.Coordinate(0, 0, 1, 1, 1000000);
        coords[1] = new CapDetection.Coordinate(580,730,750,887, 20812);
        coords[2] = new CapDetection.Coordinate(120,730,399,888, 35310);

        coords[3] = new CapDetection.Coordinate(0, 0, 1, 1, 10000000);
        coords[4] = new CapDetection.Coordinate(629,732,720,884, 11814);
        coords[5] = new CapDetection.Coordinate(169,734,408,893, 27673);

        coords[6] = new CapDetection.Coordinate(490,721,720,882, 33675);
        coords[7] = new CapDetection.Coordinate(0,723,287,890, 37901);
        coords[8] = new CapDetection.Coordinate(0, 0, 1, 1, 100000);

        coords[9] = new CapDetection.Coordinate(499,728,720,890, 30778);
        coords[10] = new CapDetection.Coordinate(0,727,276,882, 33937);
        coords[11] = new CapDetection.Coordinate(0, 0, 1, 1, 10000);

        //test
        /**/
        cap.config = new CapDetection.CapConfiguration(
                coords[0], coords[1], coords[2], CapDetection.Position.POS1
        );
        for(int i = 0; i < 3; i++) {
            System.out.println(i + ": " + cap.process(images[i]));
        }

        cap.config = new CapDetection.CapConfiguration(
                coords[3], coords[4], coords[5], CapDetection.Position.POS1
        );
        for(int i = 3; i < 6; i++) {
            System.out.println(i + ": " + cap.process(images[i]));
        }

        cap.config = new CapDetection.CapConfiguration(
                coords[6], coords[7], coords[8], CapDetection.Position.POS3
        );
        for(int i = 6; i < 9; i++) {
            System.out.println(i + ": " + cap.process(images[i]));
        }

        cap.config = new CapDetection.CapConfiguration(
                coords[9], coords[10], coords[11], CapDetection.Position.POS3
        );
        for(int i = 9; i < 12; i++) {
            System.out.println(i + ": " + cap.process(images[i]));
        }
        /**/

        //calibration
        /*
        for(int i = 1; i < 12; i++)
            System.out.println(i + ": " + cap.process(images[i], coords[i]));
        /**/
    }

    public static class CapDetection {
        public Position process(Image inputImage) {
            Image[] imageSet = new Image[3];
            imageSet[0] = inputImage;

            Error error = imageSet[0].toMono();
            if(error != GeneralError.NO_ERROR) {
                return Position.NO_POS;
            }

            error = imageSet[0].saveImage("Initial");
            if(error != GeneralError.NO_ERROR) {
                return Position.NO_POS;
            }

            for(int i = imageSet.length - 1; i >= 0; i--) {
                imageSet[i] = new Image(imageSet[0]);

                error = imageSet[i].cropImage(
                        this.config.getPos(i).x0,
                        this.config.getPos(i).y0,
                        this.config.getPos(i).x1,
                        this.config.getPos(i).y1
                );
                if(error != GeneralError.NO_ERROR) {
                    return Position.NO_POS;
                }

                error = imageSet[i].saveImage("inter" + i);
                if(error != GeneralError.NO_ERROR) {
                    return Position.NO_POS;
                }

                Pixel brightest = imageSet[i].findBrightest();
                Pixel dimmest = imageSet[i].findDimmest();

                error = imageSet[i].curveAdjAll(
                        (double x) -> this.colorCutoffFactor * (brightest.getLuminance() - dimmest.getLuminance()) + dimmest.getLuminance() > x ? 0 : 1
                );
                if(error != GeneralError.NO_ERROR) {
                    return Position.NO_POS;
                }

                error = imageSet[i].saveImage("Final" + i);
                if(error != GeneralError.NO_ERROR) {
                    return Position.NO_POS;
                }

            }

            double[] imagePixelCount = {
                    ((double) this.countBlackPixel(imageSet[0]) / this.config.getPos(0).countCutoff),
                    ((double) this.countBlackPixel(imageSet[1]) / this.config.getPos(1).countCutoff),
                    ((double) this.countBlackPixel(imageSet[2]) / this.config.getPos(2).countCutoff)
            };

            Position pos = this.config.failurePos;

            for(int i = 0; i < imagePixelCount.length; i++)
                pos = imagePixelCount[i] >=1 && imagePixelCount[pos.getValue()] < imagePixelCount[i] ? Position.getPos(i) : pos;

            return pos;
        }

        public int process(Image inputImage, Coordinate coord) {
            Error error = inputImage.toMono();
            if (error != GeneralError.NO_ERROR) {
                return -1;
            }

            error = inputImage.saveImage("inital");
            if (error != GeneralError.NO_ERROR) {
                return -1;
            }

            error = inputImage.cropImage(
                    coord.x0,
                    coord.y0,
                    coord.x1,
                    coord.y1
            );
            if (error != GeneralError.NO_ERROR) {
                return -1;
            }

            error = inputImage.saveImage("inter");

            Pixel brightest = inputImage.findBrightest();
            Pixel dimmest = inputImage.findDimmest();

            error = inputImage.curveAdjAll(
                    (double x) -> this.colorCutoffFactor * (brightest.getLuminance() - dimmest.getLuminance()) + dimmest.getLuminance() > x ? 0 : 1
            );
            if (error != GeneralError.NO_ERROR) {
                return 0;
            }

            error = inputImage.saveImage("final");
            if (error != GeneralError.NO_ERROR) {
                    return 0;
                }

            int imagePixelCount = this.countBlackPixel(inputImage);

            return imagePixelCount;
        }

        private int countBlackPixel(Image image) {
            int count = 0;

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++)
                    count = image.getPixel(i, j).getLuminance() < 0.5 ? count + 1 : count;
            }

            return count;
        }

        private CapConfiguration config;

        private final double colorCutoffFactor = 0.5;
        private final int countCutoff = 100;

        public static class CapConfiguration {
            public CapConfiguration(Coordinate pos1, Coordinate pos2, Coordinate pos3, Position failurePos) {
                this.pos1 = pos1;
                this.pos2 = pos2;
                this.pos3 = pos3;

                this.failurePos = failurePos;
            }

            public Coordinate getPos(int index) {
                switch (index) {
                    case 0:
                        return this.pos1;
                    case 1:
                        return this.pos2;
                    case 2:
                        return this.pos3;
                    default:
                        return null;
                }
            }

            public final Coordinate pos1;
            public final Coordinate pos2;
            public final Coordinate pos3;

            public final Position failurePos;
        }

        public static class Coordinate {
            public Coordinate(int x0, int y0, int x1, int y1, int countCutoff) {
                this.x0 = x0;
                this.y0 = y0;

                this.x1 = x1;
                this.y1 = y1;

                this.countCutoff = (int) (countCutoff * this.countCutoffPercent);
            }

            public final int x0;
            public final int y0;

            public final int x1;
            public final int y1;

            public final int countCutoff;

            public final double countCutoffPercent = 0.7;
            //todo fix

        }

        public enum Position {
            NO_POS(-1),
            POS1(0),
            POS2(1),
            POS3(2);

            Position(int value) {
                this.value = value;
            }

            public int getValue() {
                return this.value;
            }

            public static Position getPos(int index) {
                switch (index) {
                    case 0:
                        return POS1;
                    case 1:
                        return POS2;
                    case 2:
                        return POS3;
                    default:
                        return NO_POS;
                }
            }

            private final int value;
        }
    }
}

